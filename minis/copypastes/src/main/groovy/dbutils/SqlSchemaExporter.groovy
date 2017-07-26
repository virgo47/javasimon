package dbutils

import groovy.transform.ToString

import java.sql.DatabaseMetaData
import java.sql.ResultSet

import static dbutils.DbInit.sql

@ToString(includeNames = true, excludes = "tableDefs,metaData,sqlOutput,password")
class SqlSchemaExporter {

	// INPUT PROPERTIES
	def catalog = System.getProperty("jdbc.catalog", "catalog")
	def schemaPattern = 'dbo' // default for SQL Server
	DatabaseMetaData metaData

	def sqlOutput

	/**
	 * LIST of tables, order is important, later can depend on sooner, but not other way around
	 * keys in the resulting map are all lower-case.
	 */
	Map<String, TableDef> tableDefs
	List<String> sequences = []

	SqlSchemaExporter(String filename) {
		this(new FileWriter(filename))
		println "\nPreparing $filename"
	}

	SqlSchemaExporter(sqlOutput = System.out) {
		this.sqlOutput = sqlOutput
	}

	/** Call this with the list of tables*/
	void extract(String... tables) {
		prepareTableDefs(tables)

		metaData = sql.connection.metaData
		int jdbcMajorVersion = metaData.getJDBCMajorVersion()
		int jdbcMinorVersion = metaData.getJDBCMinorVersion()
		println "-- Driver ${metaData.getDriverName()}, JDBC version ${jdbcMajorVersion}.${jdbcMinorVersion}"

		investigateColumns(metaData.getColumns(catalog, schemaPattern, null, null))
		tableDefs.keySet().each {
			investigatePrimaryKeys(metaData.getPrimaryKeys(catalog, schemaPattern, it))
			investigateForeignKeys(metaData.getImportedKeys(catalog, schemaPattern, it))
			investigateIndexes(metaData.getIndexInfo(catalog, schemaPattern, it, false, false))
		}
		investigateOtherDefinitions()
		dumpSchema()
	}

	private void investigateColumns(ResultSet rs) {
		processResultSet(rs, 'TABLE_NAME') { tableDef ->
			def columnDef = new ColumnDef()
			def columnName = rs.getString('COLUMN_NAME')
			columnDef.name = columnName
			columnDef.sqlType = rs.getInt('DATA_TYPE')
			columnDef.size = rs.getInt('COLUMN_SIZE')
			columnDef.decimalDigits = rs.getInt('DECIMAL_DIGITS')
			columnDef.nullable = yesNo(rs, 'IS_NULLABLE')
			columnDef.autoIncrement = yesNo(rs, 'IS_AUTOINCREMENT')
			columnDef.defaultValue =
				processDefaultOrComputedValue(rs.getString('COLUMN_DEF'), columnDef.sqlType)

			tableDef.columns.put(columnName.toLowerCase(), columnDef)
		}
	}

	public static final String NEXT_VAL_SEQ_PREFIX = 'NEXT VALUE FOR '

	String processDefaultOrComputedValue(String defaultValue, int sqlType) {
		if (defaultValue == null) {
			return null
		}

		if (isMicrosoftSqlDatabase()) {
			defaultValue = replaceMicrosoftSqlFunctions(defaultValue, sqlType)
		}

		if (defaultValue.toUpperCase().startsWith(NEXT_VAL_SEQ_PREFIX)) {
			def seqName = defaultValue.substring(NEXT_VAL_SEQ_PREFIX.length())
			def schemaSeparatorIndex = seqName.indexOf('.')
			if (schemaSeparatorIndex >= 0) {
				seqName = seqName.substring(schemaSeparatorIndex + 1)
			}
			sequences.add(seqName)
			defaultValue = NEXT_VAL_SEQ_PREFIX + seqName
		}

		defaultValue
	}

	private static String replaceMicrosoftSqlFunctions(String definition, int sqlType) {
		processSqlServerColumnsInDefinitions(stripOuterParenthesis(definition))
			.replace('getdate()', currentDateFunction(sqlType))
			.replace('newid()', 'RANDOM_UUID()')
			.replaceAll('CONVERT\\(.*,(.*),.*\\)', '$1')
			.replaceAll('datepart\\(year,(.*)\\)', 'YEAR($1)')
	}

	@SuppressWarnings("GrEqualsBetweenInconvertibleTypes")
	static String currentDateFunction(int sqlType) {
		sqlType == 91 ? 'CURRENT_DATE()'
			: sqlType == 93 || sqlType == 2014 ? 'CURRENT_TIMESTAMP()'
			: 'CURRENT_TIME()'
	}

	private void investigatePrimaryKeys(ResultSet rs) {
		processResultSet(rs, 'TABLE_NAME') { TableDef tableDef ->
			def primaryKey = tableDef.primaryKey
			if (primaryKey == null) {
				primaryKey = new PrimaryKey(rs.getString('PK_NAME'))
				tableDef.primaryKey = primaryKey
			}
			primaryKey.columnNames.add(rs.getString('COLUMN_NAME'))
		}
	}

	def investigateForeignKeys(ResultSet rs) {
		processResultSet(rs, 'FKTABLE_NAME') { TableDef tableDef ->
			String fkName = rs.getString('FK_NAME')
			def foreignKey = tableDef.foreignKeys.computeIfAbsent(fkName, {
				new ForeignKey(fkName)
			})
			/* For JDK before 8 (without computeIfAbsent):
			def foreignKey = tableDef.foreignKeys[fkName]
			if (!foreignKey) {
				foreignKey = new ForeignKey(fkName)
				tableDef.foreignKeys[fkName] = foreignKey
			}
			*/
			foreignKey.columnNames.add(rs.getString('FKCOLUMN_NAME'))
			foreignKey.refColumnNames.add(rs.getString('PKCOLUMN_NAME'))
			foreignKey.refTableName = rs.getString('PKTABLE_NAME')
		}
	}

	def investigateIndexes(ResultSet rs) {
		processResultSet(rs, 'TABLE_NAME') { TableDef tableDef ->
			def columnName = rs.getString('COLUMN_NAME')
			String indexName = rs.getString('INDEX_NAME')
			if (columnName != null && !indexName.startsWith('PK_')) {
				def index = tableDef.indexes.computeIfAbsent(indexName, {
					new Index(indexName, tableDef.tableName)
				})
				/* For JDK before 8 (without computeIfAbsent):
				def index = tableDef.indexes[indexName]
				if (!index) {
					index = new Index(indexName, tableDef.tableName)
					tableDef.indexes[indexName] = index
				}
				*/
				index.columnNames.add(columnName)
				index.unique = !rs.getBoolean('NON_UNIQUE')
				// TODO: do we want ASC/DESC? For H2 it probably doesn't matter that much
			}
		}
	}

	private void processResultSet(ResultSet rs, String tableColName, Closure processColumn) {
		while (rs.next()) { // ++rs should work, but at least on JDK 7 + Groovy 2.4 it does not
			def tableName = rs.getString(tableColName)
			def tableDef = getTableDef(tableName)
			if (tableDef == null) {
				continue
			}
// we fill in the table name "lazily" from metadata,
// so the casing is from DB, not from our list
			if (tableDef.tableName == null) {
				tableDef.tableName = tableName
			}

			processColumn tableDef
		}
		rs.close()
	}

	def dumpSchema() {
		sqlOutput.println(
			"""-- Generated by SchemaExporter.groovy, just run it and copy the content of generated file,
-- or set working directory to where create-schema.sql is and you don't have to copy anything.
-- Only TABLEs are created automatically as of now, VIEWs and the rest must be handled separately""")
		sequences.each {
			sqlOutput.println "\nCREATE SEQUENCE $it;"
		}
		tableDefs.each { k, v ->
			if (v.tableName != null) {
				sqlOutput.println "\n-----------------------------------------------------------\n$v"
			} else {
				System.err.println("Table $k NOT FOUND!")
			}
		}
		sqlOutput.flush()
	}

	static boolean yesNo(ResultSet resultSet, String columnName) {
		"YES" == resultSet.getString(columnName)
	}

	class TableDef {
		String tableName
		Map<String, ColumnDef> columns = new LinkedHashMap<>() // keys are lower-case
		PrimaryKey primaryKey
		Map<String, ForeignKey> foreignKeys = new LinkedHashMap<>()
		Map<String, Index> indexes = new LinkedHashMap<>()
		List otherConstraints = new ArrayList<>()

		@Override
		@SuppressWarnings("GroovyAssignabilityCheck")
		String toString() {
			assert tableName != null

			def afterColumnList = new ArrayList()
			if (primaryKey != null) {
				afterColumnList.add(primaryKey)
			}
			afterColumnList.addAll(foreignKeys.values())
			def alterList = indexes.values() + otherConstraints

			return """CREATE TABLE $tableName (
${columns.values().join(',\n\t')}${
				afterColumnList.isEmpty() ? '' : ',\n\n\t' + afterColumnList.join(',\n\t')
			}
)${alterList.isEmpty() ? '' : ';\n\n' + alterList.join(';\n')};"""
		}
	}

	class ColumnDef {
		String name
		int sqlType
		int size
		int decimalDigits
		boolean nullable
		boolean autoIncrement
		String computedDefinition
		String defaultValue

		String sqlTypeString() {
			switch (sqlType) {
				case -7: return 'BIT' // BIT
				case -6: return 'TINYINT' // TINYINT
				case 5: return 'SMALLINT' // SMALLINT
				case 4: return 'INT' // INTEGER
				case -5: return 'BIGINT' // BIGINT
				case 6: return 'FLOAT' // FLOAT
				case 7: return 'REAL' // REAL
				case 8: return 'DOUBLE' // DOUBLE
				case 2: return withSizeAndPrecision('NUMERIC') // NUMERIC
				case 3: return withSizeAndPrecision('DECIMAL') // DECIMAL
				case 1: return withSize('CHAR') // CHAR
				case 12: return withSize('VARCHAR') // VARCHAR
				case -1: return withSize('LONGVARCHAR') // LONGVARCHAR
				case 91: return 'DATE' // DATE
				case 92: return 'TIME' // TIME
				case 93: return 'TIMESTAMP' // TIMESTAMP
				case -2: return implyDefault('UUID', 'RANDOM_UUID()') // BINARY
				case -3: return 'VARBINARY' // VARBINARY
				case -4: return 'LONGVARBINARY' // LONGVARBINARY
				case 0: return 'NULL' // NULL
				case 1111: return 'OTHER' // OTHER
				case 2000: return 'JAVA_OBJECT' // JAVA_OBJECT
				case 2001: return 'DISTINCT' // DISTINCT
				case 2002: return 'STRUCT' // STRUCT
				case 2003: return 'ARRAY' // ARRAY
				case 2004: return 'BLOB' // BLOB
				case 2005: return 'CLOB' // CLOB
				case 2006: return 'REF' // REF
				case 70: return 'DATALINK' // DATALINK
				case 16: return 'BOOLEAN' // BOOLEAN
				case -8: return 'ROWID' // ROWID
				case -15: return withSize('NCHAR') // NCHAR
				case -9: return withSize('NVARCHAR') // NVARCHAR
				case -16: return withSize('LONGNVARCHAR') // LONGNVARCHAR
				case 2011: return 'NCLOB' // NCLOB
				case 2009: return 'SQLXML' // SQLXML
				case 2012: return 'REF_CURSOR' // REF_CURSOR
				case 2013: return 'TIME_WITH_TIMEZONE' // TIME_WITH_TIMEZONE
				case 2014: return 'TIMESTAMP_WITH_TIMEZONE' // TIMESTAMP_WITH_TIMEZONE
				default: throw new RuntimeException("Unknown sqlType $sqlType")
			}
		}

		String withSize(String type) {
			"$type($size)"
		}

		String withSizeAndPrecision(String type) {
			"$type($size, $decimalDigits)"
		}

		String implyDefault(String type, String defaultValue) {
			this.defaultValue = defaultValue
			return type
		}

		@Override
		String toString() {
			StringBuilder sb = new StringBuilder(SpecialColumns.escape(name))
				.append(' ')
				.append(sqlTypeString())
			if (autoIncrement) {
				sb.append(' AUTO_INCREMENT')
				// IDENTITY implies PK, which may not be always true, PK are also covered by constraints
			}
			if (!nullable) {
				sb.append(' NOT NULL')
			}
			if (defaultValue != null) {
				sb.append(" DEFAULT ").append(defaultValue)
			}
			if (computedDefinition != null) {
				sb.append(" AS ").append(computedDefinition)
			}
			return sb
		}

	}

	class PrimaryKey {
		final String constraintName
		List<String> columnNames = new ArrayList<>()

		PrimaryKey(String constraintName) {
			this.constraintName = constraintName
		}

		@Override
		String toString() {
			return "CONSTRAINT $constraintName PRIMARY KEY " +
				"(${SpecialColumns.escapeAll(columnNames).join(', ')})"
		}
	}

	class ForeignKey {
		final String constraintName
		List<String> columnNames = new ArrayList<>()
		String refTableName
		List<String> refColumnNames = new ArrayList<>()

		ForeignKey(String constraintName) {
			this.constraintName = constraintName
		}

		@Override
		String toString() {
			return "CONSTRAINT $constraintName FOREIGN KEY" +
				" (${SpecialColumns.escapeAll(columnNames).join(', ')})" + " REFERENCES" +
				" $refTableName (${SpecialColumns.escapeAll(refColumnNames).join(', ')})"
		}
	}

	class Index {
		final String indexName
		final String tableName
		List<String> columnNames = new ArrayList<>()
		boolean unique

		Index(String indexName, String tableName) {
			this.indexName = indexName
			this.tableName = tableName
		}

		@Override
		String toString() {
			return "CREATE ${unique ? 'UNIQUE ' : ''}INDEX $indexName ON $tableName" +
				" (${SpecialColumns.escapeAll(columnNames).join(', ')})"
		}
	}

	class CheckConstraint {
		final String constraintName
		final String tableName
		String checkDefinition

		CheckConstraint(String constraintName, String tableName) {
			this.constraintName = constraintName
			this.tableName = tableName
		}

		@Override
		String toString() {
			return "ALTER TABLE $tableName ADD CONSTRAINT $constraintName CHECK $checkDefinition"
		}
	}

	void prepareTableDefs(String... tableNames) {
		tableDefs = new LinkedHashMap<>()
		tableNames.each {
			tableDefs.put(it.toLowerCase(), new TableDef())
		}
	}

	TableDef getTableDef(String tableName) {
		tableDefs.get(tableName.toLowerCase())
	}

	def investigateOtherDefinitions() {
		if (isMicrosoftSqlDatabase()) {
			investigateSqlServerCheckConstraints()
			investigateSqlServerComputedColumns()
		}
	}

	private boolean isMicrosoftSqlDatabase() {
		metaData.getDatabaseProductName() == "Microsoft SQL Server"
	}

	@SuppressWarnings(["SqlDialectInspection", "SqlNoDataSourceInspection"])
	private void investigateSqlServerCheckConstraints() {
		ResultSet rs = sql.connection.createStatement().executeQuery(
			'select cs.name as CONSTRAINT_NAME, cs.definition as DEFINITION, t.name as TABLE_NAME ' +
				'from sys.check_constraints cs, sys.tables t ' +
				'where cs.parent_object_id = t.object_id and cs.type = \'C\';')
		processResultSet(rs, 'TABLE_NAME', { TableDef tableDef ->
			def constraintName = rs.getString('CONSTRAINT_NAME')
			def constraint = new CheckConstraint(constraintName, tableDef.tableName)
			constraint.checkDefinition = processSqlServerCheckDefinition(rs.getString('DEFINITION'))
			tableDef.otherConstraints.add(constraint)
		})
	}

	@SuppressWarnings(["SqlDialectInspection", "SqlNoDataSourceInspection"])
	private void investigateSqlServerComputedColumns() {
		//select * from sys.computed_columns
		ResultSet rs = sql.connection.createStatement().executeQuery(
			'select cc.name as NAME, cc.definition as DEFINITION, t.name as TABLE_NAME ' +
				'from sys.computed_columns cc, sys.tables t ' +
				'where cc.object_id = t.object_id;')
		processResultSet(rs, 'TABLE_NAME', { TableDef tableDef ->
			def columnName = rs.getString('NAME')
			def columnDef = tableDef.columns[columnName]
			if (columnDef) {
				columnDef.computedDefinition = processDefaultOrComputedValue(
					rs.getString('DEFINITION'), columnDef.sqlType)
			} else {
				// this doesn't seem critical, at least not for "as null" columns that are not used
				// throw new RuntimeException(
				//      "Undefined column $columnName in table ${tableDef.tableName}")
			}
		})
	}

	private static String processSqlServerCheckDefinition(String definition) {
		stripOuterParenthesis(processSqlServerColumnsInDefinitions(definition))
	}

	private static String processSqlServerColumnsInDefinitions(String definition) {
		definition
			.replaceAll("\\[($SpecialColumns.specialNamesRegex)\\]", '"$1"')
			.replaceAll('\\[(.*?)]', '$1')
	}

	static String stripOuterParenthesis(String input) {
		input?.replaceAll(/\((.*)\)/, '$1')
	}

	static class SpecialColumns {
		static List specialNames = [
			'action', 'default', 'from', 'group', 'key', 'order', 'sign', 'to', 'user']
		static String specialNamesRegex = specialNames.join('|')

		static String escape(String columnName) {
			columnName.replaceAll("^($specialNamesRegex)\$", '"$1"')
		}

		static List<String> escapeAll(List<String> columnNames) {
			columnNames.collect { escape(it) }
		}
	}
}