package org.javasimon.jdbcx4;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Properties;

import org.javasimon.jdbc4.SimonConnectionConfiguration;

/**
 * SimonCommonDataSource is parent for all three datasource implementation classes.
 * <p/>
 * It contains getters and setters for basic properties which all three datasource types
 * needs to implement.
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 2.4
 */
public abstract class AbstractSimonDataSource {
	protected transient PrintWriter logWriter;
	protected SimonConnectionConfiguration configuration;
	private String user;
	private String password;
	private int loginTimeout;

	private String realDataSourceClassName;
	private String prefix;
	/**
	 * Properties specific to the real datasource
	 */
	private Properties properties;

	/**
	 * Retrieves the log writer for this <code>DataSource</code> object.
	 *
	 * @return the log writer for this data source or null if logging is disabled
	 * @throws java.sql.SQLException if a database access error occurs
	 * @see javax.sql.DataSource#getLogWriter()
	 * @see #setLogWriter(java.io.PrintWriter)
	 */
	public final PrintWriter getLogWriter() throws SQLException {
		return logWriter;
	}

	/**
	 * Sets the log writer for this <code>DataSource</code> object to the given <code>java.io.PrintWriter</code> object.
	 *
	 * @param out the new log writer; to disable logging, set to null
	 * @throws java.sql.SQLException if a database access error occurs
	 * @see javax.sql.DataSource#setLogWriter(java.io.PrintWriter)
	 * @see #getLogWriter
	 */
	public final void setLogWriter(PrintWriter out) throws SQLException {
		this.logWriter = out;
	}

	/**
	 * Returns JDBC connection URL.
	 *
	 * @return JDBC connection URL
	 */
	public final String getUrl() {
		return configuration == null ? null : configuration.getSimonUrl();
	}

	/**
	 * Setter for URL property.
	 *
	 * @param url JDBC connection URL
	 */
	public final void setUrl(String url) {
		this.configuration = new SimonConnectionConfiguration(url);
	}

	/**
	 * Returns real JDBC connection URL.
	 *
	 * @return real JDBC connection URL
	 */
	public final String getRealUrl() {
		return configuration == null ? null : configuration.getRealUrl();
	}

	/**
	 * Returns database user to authenticate connection.
	 *
	 * @return database user
	 */
	public final String getUser() {
		return user;
	}

	/**
	 * Setter for user property.
	 *
	 * @param user database user
	 */
	public final void setUser(String user) {
		this.user = user;
	}

	/**
	 * Returns database password to authenticate connection.
	 *
	 * @return database password
	 */
	public final String getPassword() {
		return password;
	}

	/**
	 * Setter for password property.
	 *
	 * @param password database password
	 */
	public final void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Sets the maximum time in seconds that this data source will wait
	 * while attempting to connect to a database.  A value of zero
	 * specifies that the timeout is the default system timeout
	 * if there is one; otherwise, it specifies that there is no timeout.
	 * When a {@code DataSource} object is created, the login timeout is
	 * initially zero.
	 *
	 * @param seconds the data source login time limit
	 * @throws java.sql.SQLException if a database access error occurs.
	 * @see #getLoginTimeout
	 */
	public final void setLoginTimeout(int seconds) throws SQLException {
		this.loginTimeout = seconds;
	}

	/**
	 * Gets the maximum time in seconds that this data source can wait
	 * while attempting to connect to a database.  A value of zero
	 * means that the timeout is the default system timeout
	 * if there is one; otherwise, it means that there is no timeout.
	 * When a {@code DataSource} object is created, the login timeout is
	 * initially zero.
	 *
	 * @return the data source login time limit
	 * @throws java.sql.SQLException if a database access error occurs.
	 * @see #setLoginTimeout
	 */
	public final int getLoginTimeout() throws SQLException {
		return loginTimeout;
	}

	/**
	 * Returns real datasource class name.
	 *
	 * @return real datasource class name
	 */
	public final String getRealDataSourceClassName() {
		if ((realDataSourceClassName == null || realDataSourceClassName.isEmpty()) && (configuration != null)) {
			realDataSourceClassName = doGetRealDataSourceClassName();
		}
		return realDataSourceClassName;
	}

	/**
	 * Setter for realDataSourceClassName property.
	 *
	 * @param className class name of real datasource
	 */
	public final void setRealDataSourceClassName(String className) {
		this.realDataSourceClassName = className;
	}

	/**
	 * Instantiates the DataSource.
	 *
	 * @param dataSourceClass Expected DataSource class
	 * @param <T> DataSource type
	 * @return Create DataSource
	 * @throws SQLException
	 */
	protected final <T> T createDataSource(Class<T> dataSourceClass) throws SQLException {
		if (getRealDataSourceClassName() == null) {
			throw new SQLException("Property realDataSourceClassName is not set");
		}
		try {
			T ds = dataSourceClass.cast(Class.forName(realDataSourceClassName).newInstance());
			for (Method m : ds.getClass().getMethods()) {
				String methodName = m.getName();
				if (methodName.startsWith("set") && m.getParameterTypes().length == 1) {
					final Object propertyValue;
					if (methodName.equals("setUser")) {
						propertyValue = getUser();
					} else if (methodName.equals("setPassword")) {
						propertyValue = getPassword();
					} else if (methodName.equalsIgnoreCase("setUrl")) {
						propertyValue = getRealUrl();
					} else if (methodName.equals("setLogWriter")) {
						propertyValue = getLogWriter();
					} else if (methodName.equals("setLoginTimeout")) {
						propertyValue = getLoginTimeout();
					} else {
						String propertyName = methodName.substring(3, 4).toLowerCase();
						if (methodName.length() > 4) {
							propertyName += methodName.substring(4);
						}
						final Class<?> propertyType = m.getParameterTypes()[0];
						propertyValue = getPropertyAs(propertyName, propertyType);
					}
					if (propertyValue != null) {
						m.invoke(ds, propertyValue);
					}
				}
			}
			return ds;

		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	/**
	 * Reads DataSource class name from configuration.
	 */
	protected abstract String doGetRealDataSourceClassName();

	/**
	 * Returns Simon prefix for constructing names of Simons.
	 *
	 * @return Simon prefix
	 */
	public final String getPrefix() {
		if ((prefix == null || prefix.isEmpty()) && (configuration != null)) {
			prefix = configuration.getPrefix();
		}
		return prefix;
	}

	/**
	 * Sets Simon prefix for constructing names of Simons.
	 *
	 * @param prefix Simon prefix
	 */
	public final void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Get properties specific to the real datasource.
	 *
	 * @return Properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Set properties specific to the real datasource.
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * Returns property from {@link #properties} and convert it to given type.
	 *
	 * @param <T> property type
	 * @param propertyName property name
	 * @param propertyType property type
	 * @return property value
	 */
	@SuppressWarnings("unchecked")
	private <T> T getPropertyAs(String propertyName, Class<T> propertyType) {
		final String sValue = properties == null ? null : properties.getProperty(propertyName);
		final T value;
		if (sValue == null) {
			value = null;
		} else if (propertyType.equals(String.class)) {
			value = propertyType.cast(sValue);
		} else if (propertyType.equals(Integer.class)) {
			value = propertyType.cast(Integer.valueOf(sValue));
		} else if (propertyType.equals(int.class)) {
			value = (T) Integer.valueOf(sValue);
		} else if (propertyType.equals(Long.class)) {
			value = propertyType.cast(Long.valueOf(sValue));
		} else if (propertyType.equals(long.class)) {
			value = (T) Long.valueOf(sValue);
		} else if (propertyType.equals(Boolean.class)) {
			value = propertyType.cast(Boolean.valueOf(sValue));
		} else if (propertyType.equals(boolean.class)) {
			value = (T) Boolean.valueOf(sValue);
		} else {
			value = null;
		}
		return value;
	}
}
