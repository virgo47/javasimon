package org.javasimon.jdbcx4;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

import org.javasimon.jdbc4.SimonConnection;
import org.javasimon.jdbc4.WrapperSupport;

/**
 * WrappingSimonDataSource allows to wrap existing datasource instead of providing
 * the Driver and URL information. For example - it can be used with Spring easily:
 * <pre>{@literal
 * <bean id="dataSource" class="org.javasimon.jdbcx.WrappingSimonDataSource">
 *     <property name="dataSource" ref="pooledDataSource"/>
 *     <property name="prefix" value="sky.batchpricer.skydb"/>
 * </bean>
 *
 * <bean id="pooledDataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
 *     <property name="driverClassName" value="my.driver.class.Driver"/>
 *     <property name="url" value="${mydb.url}"/>
 *     <property name="initialSize" value="0"/>
 *     <property name="maxActive" value="5"/>
 *     <property name="maxIdle" value="2"/>
 *     <property name="validationQuery" value="SELECT 1"/>
 * </bean>}</pre>
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 2.4
 */
public class WrappingSimonDataSource extends AbstractSimonDataSource implements DataSource {
	private DataSource ds;
	private WrapperSupport<DataSource> wrapperSupport;

	public DataSource getDataSource() {
		return ds;
	}

	public void setDataSource(DataSource ds) {
		this.ds = ds;
		this.wrapperSupport = new WrapperSupport<>(ds, DataSource.class);
	}

	/**
	 * Attempts to establish a connection with the data source that this {@code DataSource} object represents.
	 *
	 * @return a connection to the data source
	 * @throws SQLException if a database access error occurs
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return new SimonConnection(getDataSource().getConnection(), getPrefix());
	}

	/**
	 * Attempts to establish a connection with the data source that this {@code DataSource} object represents.
	 *
	 * @param user the database user on whose behalf the connection is being made
	 * @param password the user's password
	 * @return a connection to the data source
	 * @throws SQLException if a database access error occurs
	 */
	@Override
	public Connection getConnection(String user, String password) throws SQLException {
		return new SimonConnection(getDataSource().getConnection(user, password), getPrefix());
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