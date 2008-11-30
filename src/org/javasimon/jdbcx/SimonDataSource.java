package org.javasimon.jdbcx;

import org.javasimon.jdbc.SimonConnection;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Connection;
import java.lang.reflect.Method;

/**
 * Trieda SimonDataSource.
 *
 * <p>A factory for connections to the physical data source that this
 * <code>DataSource</code> object represents.  An alternative to the
 * <code>DriverManager</code> facility, a <code>DataSource</code> object
 * is the preferred means of getting a connection. An object that implements
 * the <code>DataSource</code> interface will typically be
 * registered with a naming service based on the
 * Java<sup><font size=-2>TM</font></sup> Naming and Directory (JNDI) API.
 * <P>
 * The <code>DataSource</code> interface is implemented by a driver vendor.
 * There are three types of implementations:
 * <OL>
 *   <LI>Basic implementation -- produces a standard <code>Connection</code>
 *       object
 *   <LI>Connection pooling implementation -- produces a <code>Connection</code>
 *       object that will automatically participate in connection pooling.  This
 *       implementation works with a middle-tier connection pooling manager.
 *   <LI>Distributed transaction implementation -- produces a
 *       <code>Connection</code> object that may be used for distributed
 *       transactions and almost always participates in connection pooling.
 *       This implementation works with a middle-tier
 *       transaction manager and almost always with a connection
 *       pooling manager.
 * </OL>
 * <P>
 * A <code>DataSource</code> object has properties that can be modified
 * when necessary.  For example, if the data source is moved to a different
 * server, the property for the server can be changed.  The benefit is that
 * because the data source's properties can be changed, any code accessing
 * that data source does not need to be changed.
 * <P>
 * A driver that is accessed via a <code>DataSource</code> object does not
 * register itself with the <code>DriverManager</code>.  Rather, a
 * <code>DataSource</code> object is retrieved though a lookup operation
 * and then used to create a <code>Connection</code> object.  With a basic
 * implementation, the connection obtained through a <code>DataSource</code>
 * object is identical to a connection obtained through the
 * <code>DriverManager</code> facility.
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
	 * @return  a connection to the data source
	 * @exception SQLException if a database access error occurs
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
	 * @return  a connection to the data source
	 * @exception SQLException if a database access error occurs
	 */
	public Connection getConnection(String user, String password) throws SQLException {
		return new SimonConnection(datasource().getConnection(user, password), prefix);
	}
}
