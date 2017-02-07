package dbutils

import static dbutils.DbInit.connect

connect('jdbc:sqlserver://10.0.0.10:1433;databaseName=mydb',
	'user', 'password')

// schema export
def schemaExporter = new SqlSchemaExporter()
schemaExporter.catalog = 'mydb'
schemaExporter.schemaPattern = 'mychema' // or can be left to dbo
schemaExporter.extract('MyTable')

// data export
def dataExporter = new SqlDataExporter()
dataExporter.dumpTablesStdout('MyTable')
