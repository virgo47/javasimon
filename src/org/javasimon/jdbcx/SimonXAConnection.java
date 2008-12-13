package org.javasimon.jdbcx;

import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import java.sql.SQLException;

/**
 * Simon implementation of <code>XAConnection</code>, needed for
 * simon XADataSource implementation.
 * <p>
 * All method invokes its real implementation.
 * <p>
 * See the {@link org.javasimon.jdbcx package description} for more
 * information.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 17.9.2008 22:32:53
 * @since 1.0
 */
public final class SimonXAConnection extends SimonPooledConnection implements XAConnection {

	private final XAConnection realConn;

	/**
	 * Class constructor.
	 *
	 * @param connection real xa connection
	 * @param prefix simon prefix
	 */
	public SimonXAConnection(XAConnection connection, String prefix) {
		super(connection, prefix);

		this.realConn = connection;
	}

	/** {@inheritDoc} */
	public XAResource getXAResource() throws SQLException {
		return realConn.getXAResource();
	}
}
