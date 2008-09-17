package org.javasimon.jdbc;

import javax.naming.*;
import java.sql.*;
import java.sql.Connection;
import java.io.PrintWriter;

/**
 * Trieda DataSource.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 14.9.2008 16:30:36
 * @since 1.0
 */
public final class SimonDataSource implements Referenceable, javax.sql.DataSource {

	private transient PrintWriter logWriter;
	private int loginTimeout;

	private String realDataSource;
	private String prefix;

	public String getRealDataSource() {
		return realDataSource;
	}

	public void setRealDataSource(String realDataSource) {
		this.realDataSource = realDataSource;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Reference getReference() throws NamingException {
		Reference ref = new Reference(getClass().getName(), SimonDataSourceFactory.class.getName(), null);
		ref.add(new StringRefAddr("realDataSource", realDataSource));
		ref.add(new StringRefAddr("prefix", prefix));
		return ref;
	}

	public Connection getConnection() throws SQLException {
		Context ctx = null;
		try {
			try {
				ctx = new InitialContext();
				SimonDataSource ds = (SimonDataSource) ctx.lookup(realDataSource);
				return new SimonConnection(ds.getConnection(), prefix);
			} finally {
				if (ctx != null) { ctx.close(); }
			}
		} catch (NamingException e) {
			throw new SQLException(e);
		}
	}

	public Connection getConnection(String user, String password) throws SQLException {
		Context ctx = null;
		try {
			try {
				ctx = new InitialContext();
				SimonDataSource ds = (SimonDataSource) ctx.lookup(realDataSource);
				return new SimonConnection(ds.getConnection(user, password), prefix);
			} finally {
				if (ctx != null) { ctx.close(); }
			}
		} catch (NamingException e) {
			throw new SQLException(e);
		}
	}

	public void setLogWriter(PrintWriter printWriter) throws SQLException {
		this.logWriter = printWriter;
	}

	public PrintWriter getLogWriter() throws SQLException {
		return logWriter;
	}

	public void setLoginTimeout(int i) throws SQLException {
		this.loginTimeout = i;
	}

	public int getLoginTimeout() throws SQLException {
		return loginTimeout;
	}

	public <T> T unwrap(Class<T> tClass) throws SQLException {
		throw new SQLException("not implemented");
	}

	public boolean isWrapperFor(Class<?> aClass) throws SQLException {
		throw new SQLException("not implemented");
	}
}
