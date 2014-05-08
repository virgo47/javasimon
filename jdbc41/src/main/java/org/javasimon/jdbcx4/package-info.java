/**
 * Simon DataSources for JavaEE JDBC monitoring.
 * <p>
 * Basic principle behind Java Simon implementation of DataSources for JDBC monitoring inside JavaEE
 * compliant application servers is mostly similar like in case of JavaSE JDBC monitoring. With same
 * principle in mind, each datasource delegates all its calls to real datasource implementation. Retrieving
 * real datasource is made by JavaSimon datasource, which in this case acts similar like application server.
 * Application server creates datasource form configuration held in its JNDI. First, application server
 * creates Java Simon datasource instance and then it pushes all properties from JNDI into datasource
 * instance. Then, when datasource within application is called, Java Simon datasource creates real
 * datasource instance and pushes all retrieved properties form application server into new real datasource
 * instance.
 * <p>
 * So, for correct settings of JavaSimon datasource, it is need to know wanted datasource type (normal,
 * connection pool or XA), implementation class of real datasource and real datasource properties of course.
 * By datasource type, there are three different Java Simon datasource implementation for each type:
 * <ul>
 * <li>{@link org.javasimon.jdbcx4.SimonDataSource} for ordinary or non-XA datasource (implements
 * {@link javax.sql.DataSource}),</li>
 * <li>{@link org.javasimon.jdbcx4.SimonConnectionPoolDataSource} for connection pool datasource with pooling
 * connections feature (implements {@link javax.sql.ConnectionPoolDataSource}),</li>
 * <li> and {@link org.javasimon.jdbcx4.SimonDataSource} for XA datasource with two-phase commits feature (implements
 * {@link javax.sql.XADataSource}).</li>
 * </ul>
 * Implementation class of real datasource is then important for creating right datasource instance by Java Simon
 * datasource, property <code>realDataSourceClassName</code>.
 * <p>
 * Not all datasource types are supported with all their possible properties. At the moment only following
 * properties are supported: <i>url</i>, <i>user</i>, <i>password</i>. These should still be enough for
 * almost all important datasource implementations (Oracle thin, PostgreSQL, H2, MySQL, ...). Other datasource
 * types (e.g. Oracle OCI case) will be added in the future as needed/requested.
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 2.4
 */
package org.javasimon.jdbcx4;