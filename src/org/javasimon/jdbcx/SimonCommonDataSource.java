package org.javasimon.jdbcx;

import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Trieda SimonCommonDataSource.
 *
 * @author Radovan Sninsky
 * @version $ $
 * @created 29.9.2008 22:39:06
 * @since 1.0
 */
public abstract class SimonCommonDataSource {

	private transient PrintWriter logWriter;

	protected String url;
	protected String user;
	protected String password;
	protected int loginTimeout;

	protected String realDataSourceClassName;
	protected String prefix = "org.javasimon.jdbcx";

	public PrintWriter getLogWriter() throws SQLException {
		return logWriter;
	}

	public void setLogWriter(PrintWriter printWriter) throws SQLException {
		this.logWriter = printWriter;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setLoginTimeout(int i) throws SQLException {
		this.loginTimeout = i;
	}

	public int getLoginTimeout() throws SQLException {
		return loginTimeout;
	}

	public String getRealDataSourceClassName() {
		return realDataSourceClassName;
	}

	public void setRealDataSourceClassName(String className) {
		this.realDataSourceClassName = className;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
