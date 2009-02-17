package org.javasimon.jdbc;

import org.javasimon.CompositeFilterCallback;
import org.javasimon.SimonManager;
import org.javasimon.FilterCallback;

import java.sql.SQLException;
import java.sql.DriverPropertyInfo;
import java.sql.DriverManager;
import java.sql.Connection;
import java.util.Properties;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.InputStream;

/**
 * Simon JDBC Proxy Driver.
 * <p>
 * An application should not use this class directly. The application (if standalone)
 * should use {@link java.sql.DriverManager} only. For example:
 * </p>
 * <pre>
 * Connection conn = DriverManager.getConnection("jdbc:simon:oracle:thin:...", "scott", "tiger");</pre>
 * <p/>
 * Simon driver has following format of JDBC connection string:
 * <pre>{@literal
 * jdbc:simon:<real driver conn string>;<param1>=<value1>;...}</pre>
 * Simon driver recognizes two parameters:
 * <ul>
 * <li>
 * <code>SIMON_REAL_DRV</code> - if you don't want or can't register real driver for any
 * reason, you can use this parameter and Simon proxy driver will do the registration
 * for you. You don't need to specify real driver parameter for some well known databases.
 * Simon proxy driver recognize database by first key word after JDBC and register.
 * </li>
 * <li>
 * <code>SIMON_PREFIX</code> - setting this parameter you can choose different prefix
 * for all monitors for this instance of driver. For example, setting
 * <code>SIMON_PREFIX=com.foo</code> will ensure that all proxy related Simons are located
 * under the subtree specified by the prefix, e.g. <code>com.foo.conn</code>, <code>com.foo.stmt</code>,
 * <code>com.foo.select</code>, etc.
 * </li>
 * <li>
 * <code>SIMON_LOGFILE</code> - setting this parameter you can choose different prefix
 * for all monitors for this instance of driver. For example, setting
 * <code>SIMON_LOGFILE=c:\a.log</code> will ensure that all proxy related Simons are located
 * under the subtree specified by the prefix, e.g. <code>com.foo.conn</code>, <code>com.foo.stmt</code>,
 * <code>com.foo.select</code>, etc.
 * </li>
 * </ul>
 * <p/>
 * By default, there is no need to load any driver explicitly, because drivers are loaded automatically
 * (since JDK 1.5) if they are in class path and jar have appropriate
 * meta information (see {@link java.sql.DriverManager}.
 * <p/>
 * If this is not a case for any reason, you need to register Simon proxy driver at least.
 * For real driver Simon proxy driver contains following procedure for find and register it:
 * <ol>
 * <li>Simon proxy driver tries if there is registered driver for driver key word.
 * <li>If not, driver tries if there is real driver parameter in info properties and then registers it.
 * <li>If not, driver tries to find driver by key word within internal list of well known drivers and
 * then registers it. For now, list contains default drivers for Oracle, PostgreSQL, Enterprise DB, H2,
 * MySQL.
 * <li>If not, driver tries to find real driver param within connection string and then registers it.
 * <li>If not, getting new connection fails.
 * </ol>
 * The safest way to get Simon proxy driver work is to load the drivers, the real one (i.e. oracle)
 * and a Simon proxy driver explicitly. This can be done using Class.forName. To load the driver and open a
 * database connection, use following code:
 * <pre>
 * Class.forName("oracle.jdbc.driver.OracleDriver");  // loads real driver
 * Class.forName("org.javasimon.jdbc.Driver");  // loads Simon proxy driver
 * Connection conn = DriverManager.getConnection(
 *      "jdbc:simon:oracle:thin:...", "scott", "tiger");</pre>
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 6.8.2008 23:13:27
 * @see java.sql.DriverManager#getConnection(String)
 * @since 1.0
 */
public final class Driver implements java.sql.Driver {
	/**
	 * Name for the property holding the real driver class value.
	 */
	public static final String REAL_DRIVER = "simon_real_drv";

	/**
	 * Name for the property holding the hierarchy prefix given to JDBC Simons.
	 */
	public static final String PREFIX = "simon_prefix";

	/**
	 * todo javadoc
	 */
	public static final String LOGFILE = "simon_logfile";

	static {
		try {
			DriverManager.registerDriver(new Driver());
		} catch (SQLException e) {
			// don't know what to do yet, maybe throw RuntimeException ???
		}
	}

	private final Properties drivers = new Properties();

	/**
	 * Trieda Url.
	 *
	 * @author Radovan Sninsky
	 * @version $Revision$ $Date$
	 * @created 14.2.2009 18:18:48
	 * @since 2
	 */
	class Url {

		private static final String SIMON_JDBC = "jdbc:simon";

		private static final String DEFAULT_PREFIX = "org.javasimon.jdbc";

		private static final int JDBC_URL_FIXED_PREFIX_LEN = 5;

		private String realUrl;
		private String driverId;
		private String realDriver;
		private String prefix;
		private String logfile;

		public Url(String url) {
			int i = url.indexOf(':', JDBC_URL_FIXED_PREFIX_LEN);
			if (i > -1) {
				driverId = url.substring(JDBC_URL_FIXED_PREFIX_LEN, i - 1);
			}

			StringTokenizer st = new StringTokenizer(url, ";=");
			while (st.hasMoreTokens()) {
				String token = st.nextToken().trim();
				if (token.startsWith("jdbc")) {
					realUrl = token.replaceFirst(SIMON_JDBC, "jdbc");
				} else if (token.equalsIgnoreCase(REAL_DRIVER)) {
					realDriver = st.hasMoreTokens() ? st.nextToken().trim() : null;
				} else if (token.equalsIgnoreCase(PREFIX)) {
					prefix = st.hasMoreTokens() ? st.nextToken().trim() : null;
				} else if (token.equalsIgnoreCase(LOGFILE)) {
					logfile = st.hasMoreTokens() ? st.nextToken().trim() : null;
				} else {
					realUrl += ";" + token + (st.hasMoreTokens() ? st.nextToken().trim() : "");
				}
			}
		}

		public String getRealUrl() {
			return realUrl;
		}

		public String getDriverId() {
			return driverId;
		}

		public String getRealDriver() {
			return realDriver;
		}

		public String getPrefix() {
			return prefix == null ? DEFAULT_PREFIX : prefix;
		}

		public String getLogfile() {
			return logfile;
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
	 * @param simonUrl jdbc connection string (i.e. jdbc:simon:h2:file:test)
	 * @param info properties for connection
	 * @return open connection to database or null if provided url is not accepted by this driver
	 * @throws SQLException if there is no real driver registered/recognized or opening real connection fails
	 * @see org.javasimon.jdbc.Driver
	 */
	public Connection connect(String simonUrl, Properties info) throws SQLException {
		if (!acceptsURL(simonUrl)) {
			return null;
		}

		Url url = new Url(simonUrl);
		java.sql.Driver driver = getRealDriver(url, info);

		if (url.getLogfile() != null) {
			CompositeFilterCallback filter = new CompositeFilterCallback();
			filter.addRule(FilterCallback.Rule.Type.MUST, null, url.getPrefix()+".*");
			filter.addCallback(new JdbcLogCallback(url.getLogfile()));

			SimonManager.installCallback(filter);
		}

		return new org.javasimon.jdbc.SimonConnection(driver.connect(url.getRealUrl(), info), url.getPrefix());
	}

	private java.sql.Driver getRealDriver(Url url, Properties info) throws SQLException {
		java.sql.Driver drv = null;
		try {
			drv = DriverManager.getDriver(url.getRealUrl());
		} catch (SQLException e) {
			// nothing, not an error
		}

		if (drv == null && info != null && info.keySet().contains(REAL_DRIVER)) {
			drv = registerDriver(info.getProperty(REAL_DRIVER));
		}

		if (drv == null && url.getDriverId() != null) {
			drv = registerDriver(drivers.getProperty(url.getDriverId()));
		}

		if (drv == null) {
			if (url.getRealDriver() != null) {
				drv = registerDriver(url.getRealDriver());
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

	/**
	 * {@inheritDoc}
	 */
	public boolean acceptsURL(String url) throws SQLException {
		return url != null && url.toLowerCase().startsWith(Url.SIMON_JDBC);
	}

	/**
	 * {@inheritDoc}
	 */
	public DriverPropertyInfo[] getPropertyInfo(String s, Properties properties) throws SQLException {
		return new DriverPropertyInfo[0];
	}

	/**
	 * {@inheritDoc}
	 */
	public int getMajorVersion() {
		return 1;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getMinorVersion() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean jdbcCompliant() {
		return true;
	}
}
