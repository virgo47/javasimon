package dbutils

import java.sql.Clob

import static dbutils.DbInit.list

class SqlDataExporter {

	String dir = "copypastes/src/main/resources/h2/"

	/** Names we want to escape. */
	List specialNames = ['order', 'action', 'default', 'group', 'user', 'sign', 'from', 'to', 'key']

	def sqlOutput // stateful!

	def dumpTables(String outFileName, String... tableSpecs) {
		println "\nPreparing $outFileName"
		sqlOutput = new FileWriter(dir + outFileName)
		sqlOutput.println "-- noinspection SqlResolveForFile"
		sqlOutput.println "-- Generated with SqlExporterAsInsert.groovy"
		tableSpecs.each {
			dumpTable(it)
		}
		sqlOutput.close()
	}

	def dumpTablesStdout(String... tableSpecs) {
		sqlOutput = System.out
		sqlOutput.println "-- EXPORTED DATA (stdout)"
		tableSpecs.each {
			dumpTable(it)
		}
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
}