package org.javasimon.jdbcx;

import org.javasimon.jdbc.SimonConnection;

import javax.sql.PooledConnection;
import javax.sql.ConnectionEventListener;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Trieda SimonPooledConnection.
 *
 * @author Radovan Sninsky
 * @version $ $
 * @created 17.9.2008 21:57:55
 * @since 1.0
 */
public class SimonPooledConnection implements PooledConnection {

	private final PooledConnection pooledConn;
	private final String prefix;

	public SimonPooledConnection(PooledConnection connection, String prefix) {
		this.pooledConn = connection;
		this.prefix = prefix;
	}

	public Connection getConnection() throws SQLException {
		return new SimonConnection(pooledConn.getConnection(), prefix);
	}

	public void close() throws SQLException {
		pooledConn.close();
	}

	public void addConnectionEventListener(ConnectionEventListener listener) {
		pooledConn.addConnectionEventListener(listener);
	}

	public void removeConnectionEventListener(ConnectionEventListener listener) {
		pooledConn.removeConnectionEventListener(listener);
	}

/*
	public void addStatementEventListener(StatementEventListener listener) {
		pooledConn.addStatementEventListener(listener);
	}

	public void removeStatementEventListener(StatementEventListener listener) {
		pooledConn.removeStatementEventListener(listener);
	}
*/
}
