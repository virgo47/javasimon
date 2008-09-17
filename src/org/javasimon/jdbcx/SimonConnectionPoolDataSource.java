package org.javasimon.jdbcx;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import javax.naming.NamingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.SQLException;

/**
 * Trieda SimonConnectionPoolDataSource.
 *
 * @author Radovan Sninsky
 * @version $ $
 * @created 17.9.2008 21:55:01
 * @since 1.0
 */
public final class SimonConnectionPoolDataSource extends SimonDataSource
	implements ConnectionPoolDataSource
{

	private ConnectionPoolDataSource realDS;

	private ConnectionPoolDataSource getRealDS() throws NamingException {
		if (realDS == null) {
			Context ctx = null;
			try {
				ctx = new InitialContext();
				realDS = (ConnectionPoolDataSource) ctx.lookup(realDataSource);
			} finally {
				if (ctx != null) { ctx.close(); }
			}
		}
		return realDS;
	}

	public PooledConnection getPooledConnection() throws SQLException {
		try {
			return new SimonPooledConnection(getRealDS().getPooledConnection(), prefix);
		} catch (NamingException e) {
			throw new SQLException(e);
		}
	}

	public PooledConnection getPooledConnection(String user, String password) throws SQLException {
		try {
			return new SimonPooledConnection(getRealDS().getPooledConnection(user, password), prefix);
		} catch (NamingException e) {
			throw new SQLException(e);
		}
	}
}
