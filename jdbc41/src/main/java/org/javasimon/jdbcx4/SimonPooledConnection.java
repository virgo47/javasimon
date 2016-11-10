package org.javasimon.jdbcx4;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.*;

import org.javasimon.jdbc4.SimonConnection;

/**
 * Simon implementation of <code>PooledConnection</code>, needed for
 * Simon ConnectionPollDataSource implementation.
 * <p/>
 * All method invokes its real implementation.
 * <p/>
 * See the {@link org.javasimon.jdbcx4 package description} for more
 * information.
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 2.4
 */
public class SimonPooledConnection implements PooledConnection {

	private class SimonConnectionEventListener implements ConnectionEventListener {

		private final ConnectionEventListener originalListener;

		SimonConnectionEventListener(ConnectionEventListener originalListener) {
			this.originalListener = originalListener;
		}

		@Override
		public void connectionClosed(ConnectionEvent event) {
			originalListener.connectionClosed(new ConnectionEvent(SimonPooledConnection.this, event.getSQLException()));
		}

		@Override
		public void connectionErrorOccurred(ConnectionEvent event) {
			originalListener.connectionErrorOccurred(new ConnectionEvent(SimonPooledConnection.this, event.getSQLException()));
		}
	}

	private class SimonStatementEventListener implements StatementEventListener {

		private final StatementEventListener originalListener;

		SimonStatementEventListener(StatementEventListener originalListener) {
			this.originalListener = originalListener;
		}

		@Override
		public void statementClosed(StatementEvent event) {
			originalListener.statementClosed(new StatementEvent(SimonPooledConnection.this, event.getStatement()));
		}

		@Override
		public void statementErrorOccurred(StatementEvent event) {
			originalListener.statementErrorOccurred(new StatementEvent(SimonPooledConnection.this, event.getStatement(), event.getSQLException()));
		}
	}

	private final PooledConnection pooledConn;
	private final String prefix;
	private Map<ConnectionEventListener, SimonConnectionEventListener> connListeners = new HashMap<>();
	private Map<StatementEventListener, SimonStatementEventListener> stmtListeners = new HashMap<>();

	/**
	 * Class constructor.
	 *
	 * @param connection real pooled connection
	 * @param prefix Simon prefix
	 */
	public SimonPooledConnection(PooledConnection connection, String prefix) {
		this.pooledConn = connection;
		this.prefix = prefix;
	}

	@Override
	public final Connection getConnection() throws SQLException {
		return new SimonConnection(pooledConn.getConnection(), prefix);
	}

	@Override
	public final void close() throws SQLException {
		pooledConn.close();
	}

	@Override
	public final void addConnectionEventListener(ConnectionEventListener listener) {
		connListeners.put(listener, new SimonConnectionEventListener(listener));
		pooledConn.addConnectionEventListener(connListeners.get(listener));
	}

	@Override
	public final void removeConnectionEventListener(ConnectionEventListener listener) {
		pooledConn.removeConnectionEventListener(connListeners.get(listener));
		connListeners.remove(listener);
	}

	@Override
	public void addStatementEventListener(StatementEventListener listener) {
		stmtListeners.put(listener, new SimonStatementEventListener(listener));
		pooledConn.addStatementEventListener(stmtListeners.get(listener));
	}

	@Override
	public void removeStatementEventListener(StatementEventListener listener) {
		pooledConn.removeStatementEventListener(stmtListeners.get(listener));
		stmtListeners.remove(listener);
	}
}
