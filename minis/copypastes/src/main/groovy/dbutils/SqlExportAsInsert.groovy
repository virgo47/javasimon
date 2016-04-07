package dbutils

import groovy.transform.Field

import java.sql.Clob
import static dbutils.DbInit.*

@Field String dir = "copypastes/src/main/resources/h2/"

// names we want to escape
@Field List specialNames = ['order', 'action', 'default', 'group', 'user', 'sign', 'from', 'to', 'key']
@Field def sqlOutput = System.out // stream or writer, default is stdout


// INPUT PROPERTIES
def url = System.getProperty("jdbc.url", "jdbc:some:url")
def user = System.getProperty("jdbc.user", "user")
def password = System.getProperty("jdbc.password", "password")

connect(url, user, password)

// just a demo code, here comes custom part of the script with dumpTables calls
def userAdminId = find('User', [name: 'Administrator']).id
dumpTables('populate-base-data.sql',
	"User where id = $userAdminId",
	'Role where id < 0',
	'User_Role where user_id = $userAdminId',
	'OtherTable')

def dumpTables(String outFileName, String... tableSpecs) {
	println "\nPreparing $outFileName"
	sqlOutput = new FileWriter(dir + outFileName)
	sqlOutput.println "-- noinspection SqlResolveForFile"
	sqlOutput.println "-- Generated with SqlExporterAsInsert.groovy"
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