package dbutils

import groovy.transform.Field

import java.sql.Clob
import java.sql.ResultSet

import static dbutils.DbInit.*

// names we want to escape
@Field List specialNames = ['order', 'action', 'default', 'group', 'user', 'sign', 'from', 'to', 'key']

@Field def sqlOutput = System.out // stream or writer, default is stdout

@Field String dir = "copypastes/src/main/resources/h2/"

// INPUT PROPERTIES
def url = System.getProperty("jdbc.url", "jdbc:some:url")
def user = System.getProperty("jdbc.user", "user")
def password = System.getProperty("jdbc.password", "password")
def catalog = System.getProperty("jdbc.catalog", "catalog")

connect(url, user, password)

def metaData = sql.connection.metaData
ResultSet rs = metaData.getColumns(catalog, null, "GuiUsers", null)
while (rs.next()) {
	def columnDef = new ColumnDef()
	columnDef.name = rs.getString("COLUMN_NAME")
	columnDef.type = sqlTypeAsString(rs.getInt("DATA_TYPE"))
	columnDef.size = rs.getInt("COLUMN_SIZE")
	columnDef.decimalDigits = rs.getInt("DECIMAL_DIGITS")
	columnDef.nullable = yesNo(rs, "IS_NULLABLE")
	columnDef.autoIncrement = yesNo(rs, "IS_AUTOINCREMENT")

	println "columnDef = $columnDef"
}

return

// OLD from SqlExportAsInsert
def dumpTables(String outFileName, String... tableSpecs) {
	println "\nPreparing $outFileName"
	sqlOutput = new FileWriter(dir + outFileName)
	tableSpecs.each {
		dumpTable(it)
	}
	sqlOutput.flush()
}

def dumpTable(String tableSpec) {
	println "Dumping: $tableSpec"
	def tableName = tableSpec.split(/\s+/)[0]

	def list = list(tableSpec)
	list.each {
		sqlOutput.print "INSERT INTO $tableName ("
		sqlOutput.print it.keySet()
			.collect { columnName((String) it) }
			.join(", ")
		sqlOutput.print(it.size() > 5 ? ')\n' : ') ')
		sqlOutput.print 'VALUES ('
		sqlOutput.print it.values()
			.collect { processValue(it) }
			.join(", ")
		sqlOutput.println ');'
	}
}

def columnName(String column) {
	return specialNames.contains(column) ? "\"$column\"" : column
}

def processValue(Object value) {
	if (value == null) {
		return "NULL"
	}
	if (value instanceof Number) {
		return value
	}
	if (value instanceof Clob) {
		value = value.getSubString(1L, (int) value.length())
	}
	String strValue = value.toString().replace("'", "''")
	if (value instanceof Date && strValue.endsWith(" 00:00:00.0")) {
		strValue -= " 00:00:00.0"
	}
	return "'$strValue'"
}

boolean yesNo(ResultSet resultSet, String columnName) {
	"YES".equals(resultSet.getString(columnName))
}

String sqlTypeAsString(int sqlType) {
	switch (sqlType) {
		case -7: return "BIT"; // BIT
		case -6: return "TINYINT"; // TINYINT
		case 5: return "SMALLINT"; // SMALLINT
		case 4: return "INTEGER"; // INTEGER
		case -5: return "BIGINT"; // BIGINT
		case 6: return "FLOAT"; // FLOAT
		case 7: return "REAL"; // REAL
		case 8: return "DOUBLE"; // DOUBLE
		case 2: return "NUMERIC"; // NUMERIC
		case 3: return "DECIMAL"; // DECIMAL
		case 1: return "CHAR"; // CHAR
		case 12: return "VARCHAR"; // VARCHAR
		case -1: return "LONGVARCHAR"; // LONGVARCHAR
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
		case -15: return "NCHAR"; // NCHAR
		case -9: return "NVARCHAR"; // NVARCHAR
		case -16: return "LONGNVARCHAR"; // LONGNVARCHAR
		case 2011: return "NCLOB"; // NCLOB
		case 2009: return "SQLXML"; // SQLXML
		case 2012: return "REF_CURSOR"; // REF_CURSOR
		case 2013: return "TIME_WITH_TIMEZONE"; // TIME_WITH_TIMEZONE
		case 2014: return "TIMESTAMP_WITH_TIMEZONE"; // TIMESTAMP_WITH_TIMEZONE
		default: throw new RuntimeException("Unknown sqlType $sqlType")
	}
}

class ColumnDef {
	String name
	String type
	int size
	int decimalDigits
	boolean nullable
	boolean autoIncrement

	@Override
	String toString() {
		StringBuilder sb = new StringBuilder(name).append(' ').append(type)
		return sb
	}
}
