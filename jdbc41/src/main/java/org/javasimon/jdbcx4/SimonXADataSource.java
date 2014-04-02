package org.javasimon.jdbcx4;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.XAConnection;
import javax.sql.XADataSource;

/**
 * Wrapper class for real XADataSource implementation, produces xa
 * {@link javax.sql.XAConnection} object.
 * <p/>
 * See the {@link SimonDataSource} for more information.
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 2.4
 */
public final class SimonXADataSource extends AbstractSimonDataSource implements XADataSource {
	private XADataSource ds;

	private XADataSource datasource() throws SQLException {
		if (ds == null) {
			ds = createDataSource(XADataSource.class);
		}
		return ds;
	}

	@Override
	public XAConnection getXAConnection() throws SQLException {
		return new SimonXAConnection(datasource().getXAConnection(), getPrefix());
	}

	@Override
	public XAConnection getXAConnection(String user, String password) throws SQLException {
		return new SimonXAConnection(datasource().getXAConnection(user, password), getPrefix());
	}

	@Override
	protected String doGetRealDataSourceClassName() {
		return this.configuration.getRealXADataSourceName();
	}

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return ds.getParentLogger();
    }
}
