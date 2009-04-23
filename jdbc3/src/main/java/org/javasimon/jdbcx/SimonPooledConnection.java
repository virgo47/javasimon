package org.javasimon.jdbcx;

import org.javasimon.jdbc.SimonConnection;

import javax.sql.PooledConnection;
import javax.sql.ConnectionEventListener;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Simon implementation of <code>PooledConnection</code>, needed for
 * simon ConnectionPollDataSource implementation.
 * <p>
 * All method invokes its real implementation.
 * <p>
 * See the {@link org.javasimon.jdbcx package description} for more
 * information.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 17.9.2008 21:57:55
 * @since 1.0
 */
public class SimonPooledConnection implements PooledConnection {

	private final PooledConnection pooledConn;
	private final String prefix;

	/**
	 * Class constructor.
	 *
	 * @param connection real pooled connection
	 * @param prefix simon prefix
	 */
	public SimonPooledConnection(PooledConnection connection, String prefix) {
		this.pooledConn = connection;
		this.prefix = prefix;
	}

	/** {@inheritDoc} */
	public final Connection getConnection() throws SQLException {
		return new SimonConnection(pooledConn.getConnection(), prefix);
	}

	/** {@inheritDoc} */
	public final void close() throws SQLException {
		pooledConn.close();
	}

	/** {@inheritDoc} */
	public final void addConnectionEventListener(ConnectionEventListener listener) {
		pooledConn.addConnectionEventListener(listener);
	}

	/** {@inheritDoc} */
	public final void removeConnectionEventListener(ConnectionEventListener listener) {
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
