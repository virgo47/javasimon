package org.javasimon.jdbc4;

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
 * {@code SIMON_REAL_DRV} - if you don't want or can't register real driver for any
 * reason, you can use this parameter and Simon proxy driver will do the registration
 * for you. You don't need to specify real driver parameter for some well known databases.
 * Simon proxy driver recognize database by first key word after JDBC and register.
 * </li>
 * <li>
 * {@code SIMON_PREFIX} - setting this parameter you can choose different prefix
 * for all monitors for one instance of driver. For example, setting
 * {@code SIMON_PREFIX=com.foo} will ensure that all proxy related Simons are located
 * under the subtree specified by the prefix, e.g. {@code com.foo.conn}, <code>com.foo.stmt</code>,
 * <code>com.foo.select</code>, etc. If no prefix is set, default {@code org.javasimon.jdbc} prefix
 * is used.
 * </li>
 * </ul>                                `
 * <p/>
 * By default, there is no need to load any driver explicitly, because drivers are loaded automatically
 * (since JDK 1.5) if they are in class path and jar have appropriate
 * meta information (see {@link java.sql.DriverManager}).
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
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @version $Revision: $ $Date: $
 * @created 3.10.2010
 * @see java.sql.DriverManager#getConnection(String)
 * @since 2.4
 */
public final class Driver implements java.sql.Driver {
	/**
	 * Name for the property holding the real driver class value.
	 */
	public static final String REAL_DRIVER = "simon_real_drv";

	/**
	 * Default hierarchy prefix for Simon JDBC driver. All Simons created by Simon JDBC
	 * driver without explicitly specified prefix are started with default prefix.
	 */
	public static final String DEFAULT_PREFIX = "org.javasimon.jdbc";

	/**
	 * Name for the driver property holding the hierarchy prefix given to JDBC Simons.
	 */
	public static final String PREFIX = "simon_prefix";

	static {
		try {
			DriverManager.registerDriver(new Driver());
		} catch (SQLException e) {
			// don't know what to do yet, maybe throw RuntimeException ???
		}
	}

	private final Properties drivers = new Properties();

	/**
	 * Class Url represents Simon JDBC url. It parses given url and than provides getters for
	 * driver's propreties if provided or default values.
	 *
	 * @author Radovan Sninsky
	 * @version $Revision: 272 $ $Date: 2010-02-08 16:07:46 +0100 (Mon, 08 Feb 2010) $
	 * @created 14.2.2009 18:18:48
	 * @since 2
	 */
	static class Url {

		private static final String SIMON_JDBC = "jdbc:simon";

		private static final int JDBC_URL_FIXED_PREFIX_LEN = 5;

		private String realUrl;
		private String driverId;
		private String realDriver;
		private String prefix;

		/**
		 * Class constructor, parses given URL and recognizes driver's properties.
		 *
		 * @param url given JDBC URL
		 */
		Url(String url) {
			int i = url.indexOf(':', JDBC_URL_FIXED_PREFIX_LEN);
			if (i > -1) {
				driverId = url.substring(JDBC_URL_FIXED_PREFIX_LEN, i - 1);
			}

			StringTokenizer st = new StringTokenizer(url, ";");
			while (st.hasMoreTokens()) {
				String tokenPairStr = st.nextToken().trim();
				String[] tokenPair = tokenPairStr.split("=", 2);
				String token = tokenPair[0];
				String tokenValue = tokenPair.length == 2 ? tokenPair[1].trim() : null;

				if (tokenPairStr.startsWith("jdbc")) {
					realUrl = tokenPairStr.replaceFirst(SIMON_JDBC, "jdbc");
				} else if (token.equalsIgnoreCase(REAL_DRIVER)) {
					realDriver = tokenValue;
				} else if (token.equalsIgnoreCase(PREFIX)) {
					prefix = tokenValue;
				} else {
					realUrl += ";" + tokenPairStr;
				}
			}
		}

		/**
		 * Returns orignal JDBC URL without any Simon stuff.
		 *
		 * @return original JDBC URL
		 */
		public String getRealUrl() {
			return realUrl;
		}

		/**
		 * Returns driver identifier (eg. oracle, postgres, mysql, h2, etc.).
		 *
		 * @return driver identifier
		 */
		public String getDriverId() {
			return driverId;
		}

		/**
		 * Return real driver fully classname.
		 *
		 * @return driver classname
		 */
		public String getRealDriver() {
			return realDriver;
		}

		/**
		 * Returns prefix for hierarchy of JDBC related Simons.
		 *
		 * @return prefix for JDBC Simons
		 */
		public String getPrefix() {
			return prefix == null ? DEFAULT_PREFIX : prefix;
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
	 * @param simonUrl JDBC connection string (i.e. jdbc:simon:h2:file:test)
	 * @param info properties for connection
	 * @return open connection to database or null if provided url is not accepted by this driver
	 * @throws java.sql.SQLException if there is no real driver registered/recognized or opening real connection fails
	 * @see org.javasimon.jdbc4.Driver
	 */
	@Override
	public Connection connect(String simonUrl, Properties info) throws SQLException {
		if (!acceptsURL(simonUrl)) {
			return null;
		}

		Url url = new Url(simonUrl);
		java.sql.Driver driver = getRealDriver(url, info);

		return new SimonConnection(driver.connect(url.getRealUrl(), info), url.getPrefix());
	}

	/**
	 * Tries to determine driver class, instantiate it and register if already not registered.
	 * For more detail look at {@link org.javasimon.jdbc4.Driver} class javadoc.
	 *
	 * @param url instance of url object that represents url
	 * @param info parameters from {@link #connect(String, java.util.Properties)} method
	 * @return instance of real driver
	 * @throws java.sql.SQLException if real driver can't be determined or is not registerd
	 */
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
	 * @throws java.sql.SQLException if registration fails
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
	@Override
	public boolean acceptsURL(String url) throws SQLException {
		return url != null && url.toLowerCase().startsWith(Url.SIMON_JDBC);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DriverPropertyInfo[] getPropertyInfo(String s, Properties properties) throws SQLException {
		return new DriverPropertyInfo[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMajorVersion() {
		return 2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMinorVersion() {
		return 4;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean jdbcCompliant() {
		return true;
	}
}
