/**
 * Simon DataSources for JavaEE JDBC monitoring.
 * <p>
 * Basic principle behind Java Simon implementation of DataSources for JDBC monitoring inside JavaEE
 * compliant application servers is mostly similar like in case of JavaSE JDBC monitorig. With same
 * principle, each datasource delegates all its calls to real datasource implementation. Retrieving
 * real datasource is made by JavaSimon datasource, which in this case acts similar like application server.
 * Aplication server creates datasource form configuration holded in its JNDI. First, application server
 * creates Java Simon datasource instance and then it pushs all properties from JNDI into datasource
 * instance. Then, when datasource within application is called, Java Simon datasource creates real
 * datasource instance and pushes all retrieved properties form application server into new real datasource
 * instance.
 * <p>
 * Takze na spravne nastavenie je JavaSimon datasource je potrebne vediet datasource type (normal,
 * connection pool or xa), implementation class of real datasource and real datasource properties of course.
 * By datasource type there are three different Java Simon datasource implementation for each type:
 * <ul>
 * <li>{@link SimonDataSource} for ordinary or non-XA datasource (implements {@link javax.sql.DataSource}),</li>
 * <li>{@link SimonConnectionPoolDataSource} for connection pool datasource with pooling connections feature
 * (implements {@link javax.sql.ConnectionPoolDataSource}),</li>
 * <li> and {@link SimonDataSource} for XA datasource with two-phase commits feature (implements
 * {@link javax.sql.XADataSource}).</li>
 * </ul>
 * Implementation class of real datasource is then important for creating right datasource instance by Java Simon
 * datasource, property <code>realDataSourceClassName</code>.
 * <p>
 * In fact, ... impl not for all properties
 */
package org.javasimon.jdbcx;