package org.javasimon.jdbcx;

import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.naming.Context;
import java.sql.SQLException;

/**
 * Trieda SimonXADataSource.
 *
 * @author Radovan Sninsky
 * @version $ $
 * @created 17.9.2008 22:27:53
 * @since 1.0
 */
public final class SimonXADataSource extends SimonDataSource implements XADataSource {

	private XADataSource realDS;

	private XADataSource getRealDS() throws NamingException {
		if (realDS == null) {
			Context ctx = null;
			try {
				ctx = new InitialContext();
				realDS = (XADataSource) ctx.lookup(realDataSource);
			} finally {
				if (ctx != null) { ctx.close(); }
			}
		}
		return realDS;
	}

	public XAConnection getXAConnection() throws SQLException {
		try {
			return new SimonXAConnection(getRealDS().getXAConnection(), prefix);
		} catch (NamingException e) {
			throw new SQLException(e);
		}
	}

	public XAConnection getXAConnection(String user, String password) throws SQLException {
		try {
			return new SimonXAConnection(getRealDS().getXAConnection(user, password), prefix);
		} catch (NamingException e) {
			throw new SQLException(e);
		}
	}
}
