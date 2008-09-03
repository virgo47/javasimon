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
 * Simon JDBC Proxy Driver.
 * <p>
 * An application should not use this class directly. The application (if standalone)
 * should use {@link java.sql.DriverManager} only. For example:
 * </p>
 * <pre>
 * Connection conn = DriverManager.getConnection(&quot;jdbc:simon:oracle:thin:...&quot;, &quot;scott&quot;, &quot;tiger&quot;);
 * </pre>

 * Simon driver has following format of jdbc connection string:
 * <pre>
 * jdbc:simon:&lt;real driver conn string&gt;;&lt;param1&gt;=&lt;value1&gt;;...
 * </pre>
 * Simon driver recognizes two parameters:
 * <ul>
 * <li>
 * <code>SIMON_REAL_DRV</code> - if you don't want or can't register real driver for any
 * reason, you can use this parameter and Simon proxy driver will do the registration
 * for you. You don't need to specify real driver parameter for some well known databases.
 * Simon proxy driver recognize database by first key word after jdbc and register.
 * </li>
 * <li>
 * <code>SIMON_PREFIX</code> - setting this parameter you can choose different prefix
 * for all monitors for this instance of driver. For examlple, setting
 * <code>SIMON_PREXIF=com.foo</code> will ensure that all inferior simons will be based on
 * this hierarchy prexif, <code>com.foo.conn</code>, <code>com.foo.stmt</code>,
 * <code>com.foo.select</code>, etc.
 * </li>
 * </ul>

 * <p>
 * By default, there is no need to load any driver explicitly, becouse (from Java 1.5)
 * drivers are loaded automaticly if they are in class path and jar have apropriate
 * meta information (see {@link java.sql.DriverManager}.<br>
 * If this is not a case for any reason, you need to register Simon proxy driver at least.
 * For real driver Simon proxy driver contains following procedure for find and register it:<br>
 * 1. Simon proxy driver tries if there is registered driver for driver key word. <br>
 * 2. If not, driver tries if there is real driver parameter in info properties and then registers it.<br>
 * 3. If not, driver tries to find driver by key word within internal list of well known drivers and
 * then registers it. For now, list contains default drivers for Oracle, PostgreSQL, Enterprise DB, H2,
 * MySQL.
 * 4. If not, driver tries to find real driver param within connection string and then registers it.
 * 5. If not, getting new connection fails.
 *
 * The safest way to get Simon proxy driver work is to load the drivers, the real one (i.e. oracle)
 * and a simon proxy driver explicitly. This can be done using Class.forName. To load the driver and open a
 * database connection, use following code:
 * </p>
 * <pre>
 * Class.forName(&quot;oracle.jdbc.driver.OracleDriver&quot;);  // loads real driver
 * Class.forName(&quot;org.javasimon.jdbc.Driver&quot;);  // loads simon proxy driver
 * Connection conn = DriverManager.getConnection(
 *      &quot;jdbc:simon:oracle:thin:...&quot;, &quot;scott&quot;, &quot;tiger&quot;);
 * </pre>
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 6.8.2008 23:13:27
 * @since 1.0
 * @see java.sql.DriverManager#getConnection(String)
 */
public final class Driver implements java.sql.Driver {

	public static final String REAL_DRIVER = "simon_real_drv";
	public static final String PREFIX = "simon_prefix";

	private static final String DEFAULT_PREFIX = "org.javasimon.jdbc";
	private static final String SIMON_JDBC = "jdbc:simon";

	private final Properties drivers = new Properties();

	static {
		try {
			DriverManager.registerDriver(new Driver());
		} catch (SQLException e) {
			// don't know what to do yet, maybe throw RuntimeException ???
		}
	}

	/**
	 * Class constructor. It loads well known driver list from resource file drivers.properties.
	 */
	public Driver() {
		try {
			drivers.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("org/javasimon/jdbc/drivers.properties"));
		} catch (IOException e) {
			// log somewhere
		}
	}

	/**
	 * Opens new Simon proxy driver connection associated with real connection to specified database.
	 * 
	 * @param url jdbc connection string (i.e. jdbc:simon:h2:file:test)
	 * @param info properties for connection
	 * @return open connection to database or null if provided url is not accepted by this driver
	 * @throws SQLException if there is no real driver registered/recognized or opening real connection fails
	 * @see org.javasimon.jdbc.Driver
	 */
	public Connection connect(String url, Properties info) throws SQLException {
		if (!acceptsURL(url)) {
			return null;
		}

		String realUrl = url.replaceFirst(SIMON_JDBC, "jdbc");
		java.sql.Driver driver = getRealDriver(realUrl, info);
		// Todo obtain custom jdbc subpackage if exists

		return new org.javasimon.jdbc.Connection(driver.connect(realUrl, info), DEFAULT_PREFIX);
	}

	private java.sql.Driver getRealDriver(String url, Properties info) throws SQLException {
		java.sql.Driver drv = null;
		try {
			drv = DriverManager.getDriver(url);
		} catch (SQLException e) {
			// nothing, not an error
		}

		if (drv == null && info != null && info.keySet().contains(REAL_DRIVER)) {
			drv = registerDriver(info.getProperty(REAL_DRIVER));
		}

		int i = url.indexOf(':', 5);
		if (drv == null && i > -1) {
			drv = registerDriver(drivers.getProperty(url.substring(5, i-1)));
		}

		if (drv == null) {
			// .*real_drv - any characters up to real_drv
			//  [\\s=]* - any number of optional spaces surrounding the equal sign.
			// ([\\w\\.]*) - trying to get a package name which can have any number of characters [a-zA-Z_0-9] and '.'. This value is what is needed
			//  [\\W]? - 0 or 1 characters that aren't in a package name.

			Pattern re = Pattern.compile(".*"+REAL_DRIVER+"[\\s=]*([\\w\\.]*)[\\W]?");
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
		return url != null && url.toLowerCase().startsWith(SIMON_JDBC);
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
