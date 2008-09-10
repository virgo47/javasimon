package org.javasimon.jdbc;

import java.sql.SQLException;
import java.sql.DriverPropertyInfo;
import java.sql.DriverManager;
import java.sql.Connection;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;
import java.io.InputStream;

/**
 * Simon JDBC Proxy Driver.
 * <p>
 * An application should not use this class directly. The application (if standalone)
 * should use {@link java.sql.DriverManager} only. For example:
 * </p>
 * <pre>
 * Connection conn = DriverManager.getConnection(&quot;jdbc:simon:oracle:thin:...&quot;, &quot;scott&quot;, &quot;tiger&quot;);
 * </pre>
 * <p/>
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
 * <p/>
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
 * <p/>
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
 * @see java.sql.DriverManager#getConnection(String)
 * @since 1.0
 */
public final class Driver implements java.sql.Driver {

	public static final String REAL_DRIVER = "simon_real_drv";
	public static final String PREFIX = "simon_prefix";

	private static final String DEFAULT_PREFIX = "org.javasimon.jdbc";
	private static final String SIMON_JDBC = "jdbc:simon";
	private static final Pattern REAL_DRIVER_PATTERN = Pattern.compile(REAL_DRIVER + "\\s*=\\s*([\\w\\.]+)");
	private static final Pattern PREFIX_PATTERN = Pattern.compile(PREFIX + "\\s*=\\s*([\\w\\.]+)");

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
			InputStream stream = null;
			try {
				stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("org/javasimon/jdbc/drivers.properties");
				drivers.load(stream);
			} finally {
				if (stream != null) {
					stream.close();
				}
			}
		} catch (IOException e) {
			// log somewhere
		}
	}

	/**
	 * Opens new Simon proxy driver connection associated with real connection to specified database.
	 *
	 * @param url	jdbc connection string (i.e. jdbc:simon:h2:file:test)
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

		String prefix = null;
		Matcher matcher = PREFIX_PATTERN.matcher(url);
		if (matcher.find()) {
			prefix = matcher.group(1);
		}
		if (prefix == null) {
			prefix = DEFAULT_PREFIX;
		}

		return new org.javasimon.jdbc.Connection(driver.connect(realUrl, info), prefix);
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
			drv = registerDriver(drivers.getProperty(url.substring(5, i - 1)));
		}

		if (drv == null) {
			Matcher matcher = REAL_DRIVER_PATTERN.matcher(url);
			if (matcher.find()) {
				drv = registerDriver(matcher.group(1));
			}
		}

		if (drv == null) {
			throw new SQLException("Real driver is not registered and can't determine real driver class name for registration.");
		}
		return drv;
	}

	/**
	 * Registers real driver through {@link java.sql.DriverManager}. 
	 *
	 * @param name real driver class name
	 * @return instance of registered real driver
	 * @throws SQLException if registration fails
	 */
	private java.sql.Driver registerDriver(String name) throws SQLException {
		try {
			java.sql.Driver d = (java.sql.Driver) Class.forName(name).newInstance();
			DriverManager.registerDriver(d);
			return d;
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			return null;
		}
	}

	/** {@inheritDoc} */
	public boolean acceptsURL(String url) throws SQLException {
		return url != null && url.toLowerCase().startsWith(SIMON_JDBC);
	}

	/** {@inheritDoc} */
	public DriverPropertyInfo[] getPropertyInfo(String s, Properties properties) throws SQLException {
		return new DriverPropertyInfo[0];
	}

	/** {@inheritDoc} */
	public int getMajorVersion() {
		return 1;
	}

	/** {@inheritDoc} */
	public int getMinorVersion() {
		return 0;
	}

	/** {@inheritDoc} */
	public boolean jdbcCompliant() {
		return true;
	}
}
