package org.javasimon.jdbcx4;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import javax.sql.StatementEventListener;

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
	private final PooledConnection pooledConn;
	private final String prefix;

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
		pooledConn.addConnectionEventListener(listener);
	}

	@Override
	public final void removeConnectionEventListener(ConnectionEventListener listener) {
		pooledConn.removeConnectionEventListener(listener);
	}

	@Override
	public void addStatementEventListener(StatementEventListener listener) {
		pooledConn.addStatementEventListener(listener);
	}

	@Override
	public void removeStatementEventListener(StatementEventListener listener) {
		pooledConn.removeStatementEventListener(listener);
	}
}
