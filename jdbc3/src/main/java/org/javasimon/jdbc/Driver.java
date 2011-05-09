package org.javasimon.jdbc;

import org.javasimon.SimonManager;
import org.javasimon.Callback;
import org.javasimon.jdbc.logging.LoggingCallback;

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
 * <li>
 * {@code SIMON_LOGFILE} - setting this parameter you can enable logging events (JDBC activity)
 * to specified file, for example {@code ...;simon_logfile=c:\jdbc.log} or
 * {@code ...;simon_logfile=/tmp/jdbc.log}.
 * </li>
 * <li>
 * {@code SIMON_LOGGER} - setting this you can enable logging events (JDBC activity) to specified JDK14
 * logger (@{link java.util.logging.Logger}), for example {@code ...;simon_logger=my.package.logger}.
 * </li>
 * <li>
 * {@code SIMON_CONSOLE} - setting this you can enable logging events (JDBC activity) to console, which is
 * ({@code System.err}) using standard {@link java.util.logging.ConsoleHandler}, for example
 * {@code ...;simon_console=y} or {@code ...;simon_console=true}.
 * </li>
 * <li>
 * {@code SIMON_FORMAT} - by this parameter you can set format of log. There are two build-in formats:
 * {@code human} and {@code csv}. Custom format is also possible by specifing clasname. Custom formatter
 * must by derived from {@link org.javasimon.jdbc.logging.SimonFormatter}. For example
 * {@code ...;simon_format=csv} or {@code ...;simon_format=my.package.MyFormmater}
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
 * @version $Revision$ $Date$
 * @see java.sql.DriverManager#getConnection(String)
 * @since 1.0
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

	/**
	 * Name for the driver property enabling JDBC logging to specified file.
	 */
	private static final String LOGFILE = "simon_logfile";

	/**
	 * Name for the driver property enabling JDBC logging to specified JDK14 logger (@{link java.util.logging.Logger}).
	 */
	private static final String LOGGER = "simon_logger";

	/**
	 * Name for the driver property enabling JDBC logging to console ({@code System.err})
	 * using {@link java.util.logging.ConsoleHandler}.
	 */
	private static final String CONSOLE = "simon_console";

	/**
	 * Name for the driver property setting format for logs. There are two build-in formats: {@code human} and
	 * {@code csv}. Custom format is also possible by specifing clasname, must by derived from
	 * {@link org.javasimon.jdbc.logging.SimonFormatter}.
	 */
	private static final String FORMAT = "simon_format";

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
	 * @version $Revision$ $Date$
	 * @since 2
	 */
	static class Url {

		private static final String SIMON_JDBC = "jdbc:simon";

		private static final int JDBC_URL_FIXED_PREFIX_LEN = 5;

		private String realUrl;
		private String driverId;
		private String realDriver;
		private String prefix;
		private String logfile;
		private String logger;
		private String console;
		private String format;

		/**
		 * Class constructor, parses given url and recognizes driver's properties.
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
				} else if (token.equalsIgnoreCase(LOGFILE)) {
					logfile = tokenValue;
				} else if (token.equalsIgnoreCase(LOGGER)) {
					logger = tokenValue;
				} else if (token.equalsIgnoreCase(CONSOLE)) {
					console = tokenValue;
				} else if (token.equalsIgnoreCase(FORMAT)) {
					format = tokenValue;
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

		/**
		 * Returns filename to log events from JDBC Simons if provided.
		 *
		 * @return filename
		 */
		public String getLogfile() {
			return logfile;
		}

		/**
		 * Returns logger name if provided.
		 *
		 * @return logger name
		 */
		public String getLogger() {
			return logger;
		}

		/**
		 * Returns {@code true} if driver's parameter {@code simon_console} is set to yes,
		 * otherwise {@code false}.
		 *
		 * @return {@code true} log to console is enabled, otherwise {@code false}
		 */
		public boolean getConsole() {
			return console != null && (
				console.equalsIgnoreCase("yes") ||
					console.equalsIgnoreCase("y") ||
					console.equalsIgnoreCase("true") ||
					console.equalsIgnoreCase("t") ||
					console.equalsIgnoreCase("1")
			);
		}

		/**
		 * Returns log format (build-in or custom) if specified.
		 *
		 * @return log format
		 */
		public String getFormat() {
			return format;
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
	 * @throws SQLException if there is no real driver registered/recognized or opening real connection fails
	 * @see org.javasimon.jdbc.Driver
	 */
	public Connection connect(String simonUrl, Properties info) throws SQLException {
		if (!acceptsURL(simonUrl)) {
			return null;
		}

		Url url = new Url(simonUrl);
		java.sql.Driver driver = getRealDriver(url, info);

		// if one of three possible way of setting logging is set, than
		// configure Simon JDBC driver logging through JDBC logging callback
		if (url.getLogfile() != null || url.getLogger() != null || url.getConsole()) {
			// check if LoggingCallback is already registerd
			LoggingCallback loggingCallback = null;
			for (Callback c : SimonManager.callback().callbacks()) {
				if (c instanceof LoggingCallback) {
					loggingCallback = (LoggingCallback) c;
					break;
				}
			}
			// if callback is already registered do not register callback again
			if (loggingCallback == null) {
				registerLoggingCallback(url);
			}
		}

		return new org.javasimon.jdbc.SimonConnection(driver.connect(url.getRealUrl(), info), url.getPrefix());
	}

	/**
	 * Registers JDBC logging callback to Simon manager.
	 *
	 * @param url instance of {@link org.javasimon.jdbc.Driver.Url}
	 * @see org.javasimon.SimonManager#callback()
	 * @see org.javasimon.Callback#addCallback(org.javasimon.Callback)
	 */
	private void registerLoggingCallback(Url url) {
		LoggingCallback loggingCallback = new LoggingCallback();
		loggingCallback.setPrefix(url.getPrefix());
		if (url.getLogfile() != null) {
			loggingCallback.setLogFilename(url.getLogfile());
		}
		if (url.getLogger() != null) {
			loggingCallback.setLoggerName(url.getLogger());
		}
		if (url.getConsole()) {
			loggingCallback.setLogToConsole();
		}
		if (url.getFormat() != null) {
			loggingCallback.setLogFormat(url.getFormat());
		}
		SimonManager.callback().addCallback(loggingCallback);
	}

	/**
	 * Tries to determine driver class, instantiate it and register if already not registered.
	 * For more detail look at {@link org.javasimon.jdbc.Driver} class javadoc.
	 *
	 * @param url instance of url object that represents url
	 * @param info parameters from {@link #connect(String, java.util.Properties)} method
	 * @return instance of real driver
	 * @throws SQLException if real driver can't be determined or is not registerd
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
		return 2;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getMinorVersion() {
		return 4;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean jdbcCompliant() {
		return true;
	}
}
