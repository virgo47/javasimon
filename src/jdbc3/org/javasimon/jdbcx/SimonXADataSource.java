package org.javasimon.jdbcx;

import javax.sql.XADataSource;
import javax.sql.XAConnection;
import java.sql.SQLException;
import java.lang.reflect.Method;

/**
 * Wrapper class for real XADataSource implementation, produces xa
 * {@link javax.sql.XAConnection} object.
 * <p>
 * See the {@link org.javasimon.jdbcx.SimonDataSource} for more information.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 17.9.2008 22:27:53
 * @since 1.0
 */
public final class SimonXADataSource extends SimonCommonDataSource implements XADataSource {

	private XADataSource ds;

	private XADataSource datasource() throws SQLException {
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
			if (o instanceof XADataSource) {
				ds = (XADataSource)o;
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
				throw new SQLException("Class in realdatasourceclassname is not a XADataSource");
			}
		}
		return ds;
	}

	public XAConnection getXAConnection() throws SQLException {
		return new SimonXAConnection(datasource().getXAConnection(), prefix);
	}

	public XAConnection getXAConnection(String user, String password) throws SQLException {
		return new SimonXAConnection(datasource().getXAConnection(user, password), prefix);
	}
}
