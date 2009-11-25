package org.javasimon.jdbcx;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.javasimon.jdbc.SimonConnection;

/**
 * WrappingSimonDataSource allows to wrap existing datasource instead of providing
 * the Driver and URL information. For example - it can be used with Spring easily:
 * <pre>
&lt;bean id="dataSource" class="org.javasimon.jdbcx.WrappingSimonDataSource">
	&lt;property name="dataSource" ref="pooledDataSource"/>
	&lt;property name="prefix" value="sky.batchpricer.skydb"/>
&lt;/bean>

&lt;bean id="pooledDataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
	&lt;property name="driverClassName" value="my.driver.class.Driver"/>
	&lt;property name="url" value="${mydb.url}"/>
	&lt;property name="initialSize" value="0"/>
	&lt;property name="maxActive" value="5"/>
	&lt;property name="maxIdle" value="2"/>
	&lt;property name="validationQuery" value="SELECT 1"/>
&lt;/bean></pre>
 *
 * @author diroussel
 * @version $Revision$ $Date$
 */
public class WrappingSimonDataSource extends AbstractSimonDataSource implements DataSource {
	private DataSource ds;

	public DataSource getDataSource() {
		return ds;
	}

	public void setDataSource(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * <p>Attempts to establish a connection with the data source that
	 * this <code>DataSource</code> object represents.
	 *
	 * @return  a connection to the data source
	 * @exception SQLException if a database access error occurs
	 */
	public Connection getConnection() throws SQLException {
		return new SimonConnection(getDataSource().getConnection(), prefix);
	}

	/**
	 * <p>Attempts to establish a connection with the data source that
	 * this <code>DataSource</code> object represents.
	 *
	 * @param user the database user on whose behalf the connection is being made
	 * @param password the user's password
	 * @return  a connection to the data source
	 * @exception SQLException if a database access error occurs
	 */
	public Connection getConnection(String user, String password) throws SQLException {
		return new SimonConnection(getDataSource().getConnection(user, password), prefix);
	}
}