package org.javasimon.jdbcx4;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

/**
 * Wrapper class for real ConnectionPoolDataSource implementation, produces pooled
 * {@link javax.sql.PooledConnection} object.
 * <p/>
 * See the {@link SimonDataSource} for more information.
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 2.4
 */
public final class SimonConnectionPoolDataSource extends AbstractSimonDataSource implements ConnectionPoolDataSource {
	private ConnectionPoolDataSource ds;

	private ConnectionPoolDataSource datasource() throws SQLException {
		if (ds == null) {
			ds = createDataSource(ConnectionPoolDataSource.class);
		}
		return ds;
	}

	@Override
	public PooledConnection getPooledConnection() throws SQLException {
		return new SimonPooledConnection(datasource().getPooledConnection(), getPrefix());
	}

	@Override
	public PooledConnection getPooledConnection(String user, String password) throws SQLException {
		return new SimonPooledConnection(datasource().getPooledConnection(user, password), getPrefix());
	}

	@Override
	protected String doGetRealDataSourceClassName() {
		return configuration.getRealConnectionPoolDataSourceName();
	}

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return ds.getParentLogger();
    }
}
