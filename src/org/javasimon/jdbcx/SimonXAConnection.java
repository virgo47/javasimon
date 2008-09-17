package org.javasimon.jdbcx;

import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import java.sql.SQLException;

/**
 * Trieda SimonXAConnection.
 *
 * @author Radovan Sninsky
 * @version $ $
 * @created 17.9.2008 22:32:53
 * @since 1.0
 */
public final class SimonXAConnection extends SimonPooledConnection implements XAConnection {

	private final XAConnection realConn;

	public SimonXAConnection(XAConnection connection, String prefix) {
		super(connection, prefix);

		this.realConn = connection;
	}

	public XAResource getXAResource() throws SQLException {
		return realConn.getXAResource();
	}
}
