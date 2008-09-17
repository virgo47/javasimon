package org.javasimon.jdbcx;

import org.javasimon.jdbc.SimonConnection;

import javax.naming.*;
import javax.sql.DataSource;
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
public class SimonDataSource implements Referenceable, DataSource {

	protected String realDataSource;
	protected String prefix;

	private DataSource realDS;

	private transient PrintWriter logWriter;
	private int loginTimeout;

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
		ref.add(new StringRefAddr(SimonDataSourceFactory.ATT_REAL_DS, realDataSource));
		ref.add(new StringRefAddr(SimonDataSourceFactory.ATT_PREFIX, prefix));
		return ref;
	}

	private DataSource getRealDS() throws NamingException {
		if (realDS == null) {
			Context ctx = null;
			try {
				ctx = new InitialContext();
				realDS = (DataSource) ctx.lookup(realDataSource);
			} finally {
				if (ctx != null) { ctx.close(); }
			}
		}
		return realDS;
	}

	public Connection getConnection() throws SQLException {
		try {
			return new SimonConnection(getRealDS().getConnection(), prefix);
		} catch (NamingException e) {
			throw new SQLException(e);
		}
	}

	public Connection getConnection(String user, String password) throws SQLException {
		try {
			return new SimonConnection(getRealDS().getConnection(user, password), prefix);
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
