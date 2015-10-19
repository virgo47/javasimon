package dbutils

import groovy.transform.Field

import static dbutils.DbInit.*

// names we want to escape
@Field List specialNames = ['order', 'action', 'default', 'group', 'user', 'sign', 'from', 'to', 'key']

@Field def sqlOutput = System.out // stream or writer, default is stdout

@Field String dir = "test-support/src/main/resources/h2/"

connect("jdbc:some:url", "user", "password")

def userAdminId = find('User', [name: 'Administrator']).id
dumpTables('populate-base-data.sql',
	'User where id = $userAdminId',
	'Role where id < 0',
	'User_Role where user_id = $userAdminId',
	'OtherTable')

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
	String strValue = value.toString().replace("'", "''")
	if (value instanceof Date && strValue.endsWith(" 00:00:00.0")) {
		strValue -= " 00:00:00.0"
	}
	return "'$strValue'"
}