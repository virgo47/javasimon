package org.javasimon.jdbcx;

import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * SimonCommonDataSource is parent for all three datasource implementation classes.
 * <p/>
 * It contains getters and setters for basic properties which all three datasource types
 * needs to impelement.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 29.9.2008 22:39:06
 * @since 1.0
 */
public abstract class AbstractSimonDataSource {

	protected transient PrintWriter logWriter;

	protected String url;
	protected String user;
	protected String password;
	protected int loginTimeout;

	protected String realDataSourceClassName;
	protected String prefix = "org.javasimon.jdbcx";

	/**
	 * <p>Retrieves the log writer for this <code>DataSource</code> object.
	 *
	 * @return the log writer for this data source or null if logging is disabled
	 * @throws SQLException if a database access error occurs
	 * @see javax.sql.DataSource#getLogWriter()
	 * @see #setLogWriter(java.io.PrintWriter)
	 */
	public final PrintWriter getLogWriter() throws SQLException {
		return logWriter;
	}

	/**
	 * <p>Sets the log writer for this <code>DataSource</code>
	 * object to the given <code>java.io.PrintWriter</code> object.
	 *
	 * @param out the new log writer; to disable logging, set to null
	 * @throws SQLException if a database access error occurs
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
		return url;
	}

	/**
	 * Setter for URL property.
	 *
	 * @param url JDBC connection URL
	 */
	public final void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Returns database user to autenticate connection.
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
	 * Returns database password to autenticate connection.
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
	 * <p>Sets the maximum time in seconds that this data source will wait
	 * while attempting to connect to a database.  A value of zero
	 * specifies that the timeout is the default system timeout
	 * if there is one; otherwise, it specifies that there is no timeout.
	 * When a <code>DataSource</code> object is created, the login timeout is
	 * initially zero.
	 *
	 * @param seconds the data source login time limit
	 * @throws SQLException if a database access error occurs.
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
	 * When a <code>DataSource</code> object is created, the login timeout is
	 * initially zero.
	 *
	 * @return the data source login time limit
	 * @throws SQLException if a database access error occurs.
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
	 * Returns Simon prefix for constructing names of Simons.
	 *
	 * @return Simon prefix
	 */
	public final String getPrefix() {
		return prefix;
	}

	/**
	 * Setter for prefix property.
	 *
	 * @param prefix Simon prefix
	 */
	public final void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
