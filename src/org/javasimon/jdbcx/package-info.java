/**
 * Simon DataSources for JavaEE JDBC monitoring.
 * <p>
 * Basic principle behind Java Simon implementation of DataSources for JDBC monitoring inside JavaEE
 * compliant application servers is mostly similar like in case of JavaSE JDBC monitorig. With same
 * principle in mind, each datasource delegates all its calls to real datasource implementation. Retrieving
 * real datasource is made by JavaSimon datasource, which in this case acts similar like application server.
 * Aplication server creates datasource form configuration holded in its JNDI. First, application server
 * creates Java Simon datasource instance and then it pushs all properties from JNDI into datasource
 * instance. Then, when datasource within application is called, Java Simon datasource creates real
 * datasource instance and pushes all retrieved properties form application server into new real datasource
 * instance.
 * <p>
 * So, for correct settings of JavaSimon datasource, it is need to know wanted datasource type (normal,
 * connection pool or xa), implementation class of real datasource and real datasource properties of course.
 * By datasource type, there are three different Java Simon datasource implementation for each type:
 * <ul>
 * <li>{@link org.javasimon.jdbcx.SimonDataSource} for ordinary or non-XA datasource (implements
 * {@link javax.sql.DataSource}),</li>
 * <li>{@link org.javasimon.jdbcx.SimonConnectionPoolDataSource} for connection pool datasource with pooling
 * connections feature (implements {@link javax.sql.ConnectionPoolDataSource}),</li>
 * <li> and {@link org.javasimon.jdbcx.SimonDataSource} for XA datasource with two-phase commits feature (implements
 * {@link javax.sql.XADataSource}).</li>
 * </ul>
 * Implementation class of real datasource is then important for creating right datasource instance by Java Simon
 * datasource, property <code>realDataSourceClassName</code>.
 * <p>
 * In fact, we are not providing implementation for all types of different datasources and their properties. At the
 * moment we just choose (minuly cas) very few of them: <i>url</i>, <i>user</i>, <i>password</i>. Those three are
 * very basic properties for almost all datasource implementations (Oracle thin, PostgreSQL, H2, MySQL, ...). If
 * there are some important and yet not implemented (Oracle oci case), they can be very easily. The fact why we do
 * not provide them is only because we didn't need them yet, and we leave it to be managed by needs.
 */
package org.javasimon.jdbcx;