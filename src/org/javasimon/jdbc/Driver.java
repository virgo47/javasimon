package org.javasimon.jdbc;

import java.sql.SQLException;
import java.sql.DriverPropertyInfo;
import java.sql.DriverManager;
import java.sql.Connection;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;

/**
 * Trieda Driver.
 *
 * @author <a href="mailto:radovan.sninsky@siemens.com">Radovan Sninsky</a>
 * @version $Revision$ $Date$
 * @created 6.8.2008 23:13:27
 * @since 1.0
 */
public class Driver implements java.sql.Driver {

	private static final String DEFAULT_NAME = "org.javasimon.jdbc";

	private Properties drivers = new Properties();

	static {
		try {
			DriverManager.registerDriver(new Driver());
		} catch (SQLException e) {
			// don't know what to do yet, maybe throw RuntimeException ???
		}
	}

	public Driver() {
		try {
			drivers.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("org/javasimon/jdbc/drivers.properties"));
		} catch (IOException e) {
			// log somewhere
		}
	}

	public Connection connect(String url, Properties info) throws SQLException {
		if (!acceptsURL(url)) {
			return null;
		}

		String realUrl = url.replaceFirst("jdbc:simon", "jdbc");
		java.sql.Driver driver = getRealDriver(realUrl, info);
		// Todo obtain custom jdbc subpackage if exists
//		return new org.javasimon.jdbc.Connection(driver.connect(realUrl, info), DEFAULT_NAME);
		return driver.connect(realUrl, info);
	}

	private java.sql.Driver getRealDriver(String url, Properties info) throws SQLException {
		java.sql.Driver drv = null;
		try {
			drv = DriverManager.getDriver(url);
		} catch (SQLException e) {
			// nothing, not a error
		}

		if (drv == null && info != null && info.keySet().contains("real_drv")) {
			drv = registerDriver(info.getProperty("real_drv"));
		}

		int i = url.indexOf(':', 5);
		if (drv == null && i > -1) {
			drv = registerDriver(drivers.getProperty(url.substring(5, i-1)));
		}

		if (drv == null) {
			// .*jamonrealdriver - any characters up to real_drv
			//  [\\s=]* - any number of optional spaces surrounding the equal sign.
			// ([\\w\\.]*) - trying to get a package name which can have any number of characters [a-zA-Z_0-9] and '.'. This value is what is needed
			//  [\\W]? - 0 or 1 characters that aren't in a package name.

			Pattern re = Pattern.compile(".*real_drv[\\s=]*([\\w\\.]*)[\\W]?");
			Matcher matcher = re.matcher(url);
			if (matcher.lookingAt()) {
				drv = registerDriver(matcher.group(1).trim());
			}
		}

		if (drv == null) {
			throw new SQLException("Real driver is not registered and can't determine real driver class name for registration.");
		}
		return drv;
	}

	private java.sql.Driver registerDriver(String driverName) throws SQLException {
		try {
			java.sql.Driver d = (java.sql.Driver)Class.forName(driverName).newInstance();
			DriverManager.registerDriver(d);
			return d;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean acceptsURL(String url) throws SQLException {
		return url != null && url.toLowerCase().startsWith("jdbc:simon:");
	}

	public DriverPropertyInfo[] getPropertyInfo(String s, Properties properties) throws SQLException {
		return new DriverPropertyInfo[0];
	}

	public int getMajorVersion() {
		return 1;
	}

	public int getMinorVersion() {
		return 0;
	}

	public boolean jdbcCompliant() {
		return true;
	}
}
