package dbutils

import groovy.transform.Field

import java.sql.ResultSet

import static dbutils.DbInit.connect
import static dbutils.DbInit.getSql

// names we want to escape
@Field List specialNames = ['order', 'action', 'default', 'group', 'user', 'sign', 'from', 'to', 'key']

@Field def sqlOutput = System.out // stream or writer, default is stdout

@Field String dir = "copypastes/src/main/resources/h2/"

@Field Map<String, TableDef> tableDefs = emptyTableDefs(
	'GuiUsers', 'BankTransactions')

// INPUT PROPERTIES
def url = System.getProperty("jdbc.url", "jdbc:some:url")
def user = System.getProperty("jdbc.user", "user")
def password = System.getProperty("jdbc.password", "password")
def catalog = System.getProperty("jdbc.catalog", "catalog")

connect(url, user, password)

def metaData = sql.connection.metaData

investigateColumns(metaData.getColumns(catalog, null, null, null))
tableDefs.keySet().each {
	investigatePrimaryKeys(metaData.getPrimaryKeys(catalog, null, it))
	investigateConstraints(metaData.getImportedKeys(catalog, null, it))
}
printSchema(tableDefs)

private void investigateColumns(ResultSet rs) {
	processResultSet(rs, 'TABLE_NAME') { tableDef ->
		def columnDef = new ColumnDef()
		columnDef.name = rs.getString('COLUMN_NAME')
		columnDef.sqlType = rs.getInt('DATA_TYPE')
		columnDef.size = rs.getInt('COLUMN_SIZE')
		columnDef.decimalDigits = rs.getInt('DECIMAL_DIGITS')
		columnDef.nullable = yesNo(rs, 'IS_NULLABLE')
		columnDef.autoIncrement = yesNo(rs, 'IS_AUTOINCREMENT')

		tableDef.columns.add(columnDef)
	}
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

def investigateConstraints(ResultSet rs) {
	processResultSet(rs, 'FKTABLE_NAME') { TableDef tableDef ->
		String fkName = rs.getString('FK_NAME')
		def foreignKey = tableDef.foreignKeys.computeIfAbsent(fkName, { new ForeignKey(fkName)})
		foreignKey.columnNames.add(rs.getString('FKCOLUMN_NAME'))
		foreignKey.refColumnNames.add(rs.getString('PKCOLUMN_NAME'))
		foreignKey.refTableName = rs.getString('PKTABLE_NAME')
	}
}

private void processResultSet(ResultSet rs, String tableColName, Closure processColumn) {
	while (rs.next()) {
		def tableDef = getTableDef(rs.getString(tableColName))
		if (tableDef == null) {
			continue
		};

		processColumn tableDef
	}
	rs.close()
}

private Map<String, TableDef> printSchema(Map<String, TableDef> tableDefs) {
	tableDefs.each { k, v ->
		sqlOutput.println v
	}
}

// OLD from SqlExportAsInsert
def dumpTables(String outFileName, String... tableSpecs) {
	println "\nPreparing $outFileName"
	sqlOutput = new FileWriter(dir + outFileName)
	tableSpecs.each {
		dumpTable(it)
	}
	sqlOutput.flush()
}

boolean yesNo(ResultSet resultSet, String columnName) {
	"YES".equals(resultSet.getString(columnName))
}

class TableDef {
	final String tableName
	List<ColumnDef> columns = new ArrayList<>()
	PrimaryKey primaryKey;
	Map<String, ForeignKey> foreignKeys = new LinkedHashMap<>()

	TableDef(String name) {
		tableName = name
	}

	@Override
	String toString() {
		return """CREATE TABLE $tableName (
	${columns.join(",\n\t")},

	${primaryKey}${foreignKeys.isEmpty() ? '' : ',\n\t' + foreignKeys.values().join(",\n\t")}
);
"""
	}
}

class ColumnDef {
	String name
	int sqlType
	int size
	int decimalDigits
	boolean nullable

	boolean autoIncrement

	String sqlTypeString() {
		switch (sqlType) {
			case -7: return "BIT"; // BIT
			case -6: return "TINYINT"; // TINYINT
			case 5: return "SMALLINT"; // SMALLINT
			case 4: return "INT"; // INTEGER
			case -5: return "BIGINT"; // BIGINT
			case 6: return "FLOAT"; // FLOAT
			case 7: return "REAL"; // REAL
			case 8: return "DOUBLE"; // DOUBLE
			case 2: return withSizeAndPrecision("NUMERIC"); // NUMERIC
			case 3: return withSizeAndPrecision("DECIMAL"); // DECIMAL
			case 1: return withSize("CHAR"); // CHAR
			case 12: return withSize("VARCHAR"); // VARCHAR
			case -1: return withSize("LONGVARCHAR"); // LONGVARCHAR
			case 91: return "DATE"; // DATE
			case 92: return "TIME"; // TIME
			case 93: return "TIMESTAMP"; // TIMESTAMP
			case -2: return "BINARY"; // BINARY
			case -3: return "VARBINARY"; // VARBINARY
			case -4: return "LONGVARBINARY"; // LONGVARBINARY
			case 0: return "NULL"; // NULL
			case 1111: return "OTHER"; // OTHER
			case 2000: return "JAVA_OBJECT"; // JAVA_OBJECT
			case 2001: return "DISTINCT"; // DISTINCT
			case 2002: return "STRUCT"; // STRUCT
			case 2003: return "ARRAY"; // ARRAY
			case 2004: return "BLOB"; // BLOB
			case 2005: return "CLOB"; // CLOB
			case 2006: return "REF"; // REF
			case 70: return "DATALINK"; // DATALINK
			case 16: return "BOOLEAN"; // BOOLEAN
			case -8: return "ROWID"; // ROWID
			case -15: return withSize("NCHAR"); // NCHAR
			case -9: return withSize("NVARCHAR"); // NVARCHAR
			case -16: return withSize("LONGNVARCHAR"); // LONGNVARCHAR
			case 2011: return "NCLOB"; // NCLOB
			case 2009: return "SQLXML"; // SQLXML
			case 2012: return "REF_CURSOR"; // REF_CURSOR
			case 2013: return "TIME_WITH_TIMEZONE"; // TIME_WITH_TIMEZONE
			case 2014: return "TIMESTAMP_WITH_TIMEZONE"; // TIMESTAMP_WITH_TIMEZONE
			default: throw new RuntimeException("Unknown sqlType $sqlType")
		}
	}

	String withSize(String type) {
		"$type($size)"
	}

	String withSizeAndPrecision(String type) {
		"$type($size, $decimalDigits)"
	}

	@Override
	String toString() {
		StringBuilder sb = new StringBuilder(name).append(' ').append(sqlTypeString())
		if (autoIncrement) {
			sb.append(" IDENTITY")
		}
		if (!nullable) {
			sb.append(" NOT NULL")
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
		return "CONSTRAINT $constraintName PRIMARY KEY (${columnNames.join(", ")})"
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
		return "CONSTRAINT $constraintName FOREIGN KEY (${columnNames.join(", ")}) REFERENCES $refTableName (${refColumnNames.join(", ")})"
	}
}

Map<String, TableDef> emptyTableDefs(String... tableNames) {
	def map = new LinkedHashMap<>()
	tableNames.each {
		map.put(it.toLowerCase(), new TableDef(it))
	}
	map
}

TableDef getTableDef(String tableName) {
	tableDefs.get(tableName.toLowerCase())
}
