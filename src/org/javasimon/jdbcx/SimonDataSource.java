package org.javasimon.jdbcx;

import org.javasimon.jdbc.SimonConnection;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Connection;
import java.lang.reflect.Method;

/**
 * Trieda SimonDataSource.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 14.9.2008 16:30:36
 * @since 1.0
 */
public final class SimonDataSource extends SimonCommonDataSource implements DataSource {

	private DataSource ds;

	private DataSource datasource() throws SQLException {
		if (ds == null) {
			Object o;
			try {
				o = Class.forName(realDataSourceClassName).newInstance();
			} catch (Exception e) {
				throw new SQLException(e.getMessage());
			}
			if (o instanceof DataSource) {
				ds = (DataSource)o;
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
				throw new SQLException("Class in realdatasourceclassname is not a DataSource");
			}
		}
		return ds;
	}

	public Connection getConnection() throws SQLException {
		return new SimonConnection(datasource().getConnection(), prefix);
	}

	public Connection getConnection(String user, String password) throws SQLException {
		return new SimonConnection(datasource().getConnection(user, password), prefix);
	}
}
