package org.javasimon.jdbcx4;

import javax.sql.StatementEventListener;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import java.sql.SQLException;

/**
 * Simon implementation of <code>XAConnection</code>, needed for
 * Simon XADataSource implementation.
 * <p/>
 * All method invokes its real implementation.
 * <p/>
 * See the {@link org.javasimon.jdbcx package description} for more
 * information.
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @version $Revision: $ $Date: $
 * @created 3.10.2010
 * @since 2.4
 */
public final class SimonXAConnection extends SimonPooledConnection implements XAConnection {
	private final XAConnection realConn;

	/**
	 * Class constructor.
	 *
	 * @param connection real xa connection
	 * @param prefix Simon prefix
	 */
	public SimonXAConnection(XAConnection connection, String prefix) {
		super(connection, prefix);

		this.realConn = connection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public XAResource getXAResource() throws SQLException {
		return realConn.getXAResource();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addStatementEventListener(StatementEventListener listener) {
		realConn.addStatementEventListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeStatementEventListener(StatementEventListener listener) {
		realConn.removeStatementEventListener(listener);
	}
}
