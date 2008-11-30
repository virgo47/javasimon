package org.javasimon.jdbcx;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.sql.SQLException;
import java.lang.reflect.Method;

/**
 * Trieda SimonConnectionPoolDataSource.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$ 
 * @created 17.9.2008 21:55:01
 * @since 1.0
 */
public final class SimonConnectionPoolDataSource extends SimonCommonDataSource implements ConnectionPoolDataSource {

	private ConnectionPoolDataSource ds;

	private ConnectionPoolDataSource datasource() throws SQLException{
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
			if (o instanceof ConnectionPoolDataSource) {
				ds = (ConnectionPoolDataSource)o;
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
				ds.setLoginTimeout(loginTimeout);
			} else {
				throw new SQLException("Class in realdatasourceclassname is not a ConnectionPoolDataSource");
			}
		}
		return ds;
	}

	public PooledConnection getPooledConnection() throws SQLException {
		return new SimonPooledConnection(datasource().getPooledConnection(), prefix);
	}

	public PooledConnection getPooledConnection(String user, String password) throws SQLException {
		return new SimonPooledConnection(datasource().getPooledConnection(user, password), prefix);
	}
}
