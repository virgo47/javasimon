package org.javasimon.jdbcx4;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

import org.javasimon.jdbc4.SimonConnection;
import org.javasimon.jdbc4.WrapperSupport;

/**
 * Wrapper class for real DataSource implementation, produces standard {@link java.sql.Connection}
 * object.
 * <p/>
 * To use SimonDataSource, <b>MUST</b> properties are:
 * <ul>
 * <li><code>realDataSourceClassName</code> - full qualified classname of real Datasource
 * implementation</li>
 * <li><code>url</code> - JDBC connection URL (no special Simon added tags are needed)</li>
 * <li><code>user</code> - DB user name</li>
 * <li><code>password</code> - DB user name</li>
 * </ul>
 * <b>MAY</b> properties are:
 * <ul>
 * <li><code>prefix</code> - Simon prefix (default: <code>org.javasimon.jdbcx4</code></li>
 * </ul>
 * <p/>
 * As mentioned in package description all <code>getConnection</code> methods
 * just invokes real {@link javax.sql.DataSource} object methods and wraps obtained
 * {@link java.sql.Connection} with {@link org.javasimon.jdbc4.SimonConnection} object.
 * <p/>
 * Real {@link javax.sql.DataSource} is obtained in method {@link #datasource()}. It tries
 * to instantiate real datasource object by property <code>realDataSourceClassName</code>
 * (setters and getters for properties are in {@link AbstractSimonDataSource}) and then sets
 * basic properties (<code>url</code>, <code>user</code>, <code>password</code>).
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 2.4
 */
public final class SimonDataSource extends AbstractSimonDataSource implements DataSource {
	private DataSource ds;
	private WrapperSupport<DataSource> wrapperSupport;

	DataSource datasource() throws SQLException {
		if (ds == null) {
			ds = createDataSource(DataSource.class);
			ds.setLogWriter(logWriter);
			wrapperSupport = new WrapperSupport<>(ds, DataSource.class);
		}
		return ds;
	}

	/**
	 * Attempts to establish a connection with the data source that this {@code DataSource} object represents.
	 *
	 * @return a connection to the data source
	 * @throws java.sql.SQLException if a database access error occurs
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return new SimonConnection(datasource().getConnection(), getPrefix());
	}

	/**
	 * Attempts to establish a connection with the data source that this {@code DataSource} object represents.
	 *
	 * @param user the database user on whose behalf the connection is being made
	 * @param password the user's password
	 * @return a connection to the data source
	 * @throws java.sql.SQLException if a database access error occurs
	 */
	@Override
	public Connection getConnection(String user, String password) throws SQLException {
		return new SimonConnection(datasource().getConnection(user, password), getPrefix());
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return wrapperSupport.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return wrapperSupport.isWrapperFor(iface);
	}

	@Override
	protected String doGetRealDataSourceClassName() {
		return this.configuration.getRealDataSourceName();
	}

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return ds.getParentLogger();
    }
}
