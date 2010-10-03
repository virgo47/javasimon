package org.javasimon.jdbcx;

import org.javasimon.jdbc.SimonConnection;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Connection;
import java.lang.reflect.Method;

/**
 * Wrapper class for real DataSource implementation, produces standard {@link Connection}
 * object.
 * <p/>
 * To use SimonDataSource, <b>MUST</b> properties are:
 * <ul>
 * <li><code>realDataSourceClassName</code> - full qualified classname of real Datasource
 * implementation</li>
 * <li><code>url</code> - JDBC connection URL (no special Simon added tags are needed)</li>
 * <li><code>user</code> - DB user name</li>
 * <li><code>password</code> - DB user name</li>
 * </ul>
 * <b>MAY</b> properties are:
 * <ul>
 * <li><code>perfix</code> - Simon prefix (default: <code>org.javasimon.jdbcx</code></li>
 * </ul>
 * <p/>
 * As mentioned in package description all <code>getConnection</code> methods
 * just invokes real {@link javax.sql.DataSource} object methods and wraps obtained
 * {@link java.sql.Connection} with {@link org.javasimon.jdbc.SimonConnection} object.
 * <p/>
 * Real {@link javax.sql.DataSource} is obtained in method {@link #datasource()}. It tries
 * to instantiate real datasource object by property <code>realDataSourceClassName</code>
 * (setters and getters for properties are in {@link AbstractSimonDataSource}) and then sets
 * basic properties (<code>url</code>, <code>user</code>, <code>password</code>).
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 14.9.2008 16:30:36
 * @since 1.0
 */
public final class SimonDataSource extends AbstractSimonDataSource implements DataSource {

	private DataSource ds;

	private DataSource datasource() throws SQLException {
		if (ds == null) {
			if (realDataSourceClassName == null || realDataSourceClassName.length() == 0) {
				throw new SQLException("Property realdatasourceclassname is not set");
			}
			Object o;
			try {
				o = Class.forName(realDataSourceClassName).newInstance();
			} catch (Exception e) {
				throw new SQLException(e.getMessage());
			}
			if (o instanceof DataSource) {
				ds = (DataSource) o;
				try {
					for (Method m : ds.getClass().getMethods()) {
						String methodName = m.getName();
						if (methodName.equalsIgnoreCase("setUser")) {
							m.invoke(ds, user);
						} else if (methodName.equalsIgnoreCase("setPassword")) {
							m.invoke(ds, password);
						} else if (methodName.equalsIgnoreCase("setUrl")) {
							m.invoke(ds, url);
						}
					}
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
				ds.setLogWriter(logWriter);
				ds.setLoginTimeout(loginTimeout);
			} else {
				throw new SQLException("Class in realdatasourceclassname is not a DataSource");
			}
		}
		return ds;
	}

	/**
	 * <p>Attempts to establish a connection with the data source that
	 * this <code>DataSource</code> object represents.
	 *
	 * @return a connection to the data source
	 * @throws SQLException if a database access error occurs
	 */
	public Connection getConnection() throws SQLException {
		return new SimonConnection(datasource().getConnection(), prefix);
	}

	/**
	 * <p>Attempts to establish a connection with the data source that
	 * this <code>DataSource</code> object represents.
	 *
	 * @param user the database user on whose behalf the connection is being made
	 * @param password the user's password
	 * @return a connection to the data source
	 * @throws SQLException if a database access error occurs
	 */
	public Connection getConnection(String user, String password) throws SQLException {
		return new SimonConnection(datasource().getConnection(user, password), prefix);
	}
}
