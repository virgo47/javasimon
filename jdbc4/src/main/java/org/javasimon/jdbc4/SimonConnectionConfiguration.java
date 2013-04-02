package org.javasimon.jdbc4;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JDBC configuration contains Simon JDBC URL, Real JDBC URL, Simon properties.
 * <p/>
 * It parses given url and than provides getters for
 * driver's propreties if provided or default values.
 *
 * @author Radovan Sninsky
 * @since 2.4
 */
public class SimonConnectionConfiguration {
	/**
	 * Default hierarchy prefix for Simon JDBC driver. All Simons created by Simon JDBC
	 * driver without explicitly specified prefix are started with default prefix.
	 */
	public static final String DEFAULT_PREFIX = "org.javasimon.jdbc";
	/**
	 * Prefix used in JDBC URLs
	 */
	public static final String URL_PREFIX = "jdbc:simon";
	/**
	 * Regex used to parse JDBC URL
	 */
	private static final Pattern URL_PATTERN = Pattern.compile(URL_PREFIX + ":([\\w]*):.*");
	/**
	 * Name for the property holding the real driver class value.
	 */
	public static final String REAL_DRIVER = "simon_real_drv";
	/**
	 * Name for the driver property holding the hierarchy prefix given to JDBC Simons.
	 */
	public static final String PREFIX = "simon_prefix";

	private static final Properties PROPERTIES = initProperties();
	/**
	 * JDBC URL with Simon prefix
	 */
	private final String simonUrl;
	/**
	 * JDBC URL without Simon prefix
	 */
	private final String realUrl;
	/**
	 * Driver Id
	 */
	private final String driverId;
	/**
	 * Real Driver class name
	 */
	private final String realDriver;
	/**
	 * Simon hierarchy prefix
	 */
	private final String prefix;

	/**
	 * Load driver.properties file
	 */
	private static Properties initProperties() {
		InputStream stream = null;
		try {
			// TODO: limited to known drivers, better find driver later based on JDBC URL without "simon" word
			Properties properties = new Properties();
			stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("org/javasimon/jdbc4/drivers.properties");
			properties.load(stream);
			return properties;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// Doesn't matter
				}
			}
		}
	}

	/**
	 * Class constructor, parses given URL and recognizes driver's properties.
	 *
	 * @param url given JDBC URL
	 */
	public SimonConnectionConfiguration(String url) {
		simonUrl = url;
		Matcher m = URL_PATTERN.matcher(url);
		if (m.matches()) {
			driverId = m.group(1);
		} else {
			throw new IllegalArgumentException(url + " is not a Simon JDBC URL");
		}

		StringTokenizer st = new StringTokenizer(url, ";");
		String lRealUrl = null,
				lRealDriver = getProperty(driverId, "driver"),
				lPrefix = DEFAULT_PREFIX;
		while (st.hasMoreTokens()) {
			String tokenPairStr = st.nextToken().trim();
			String[] tokenPair = tokenPairStr.split("=", 2);
			String token = tokenPair[0];
			String tokenValue = tokenPair.length == 2 ? tokenPair[1].trim() : null;

			if (tokenPairStr.startsWith("jdbc")) {
				lRealUrl = tokenPairStr.replaceFirst(URL_PREFIX, "jdbc");
			} else if (token.equalsIgnoreCase(REAL_DRIVER)) {
				lRealDriver = tokenValue;
			} else if (token.equalsIgnoreCase(PREFIX)) {
				lPrefix = tokenValue;
			} else {
				lRealUrl += ";" + tokenPairStr;
			}
		}
		realUrl = lRealUrl;
		realDriver = lRealDriver;
		prefix = lPrefix;
	}

	/**
	 * Get property
	 * @param driverId Driver Id
	 * @param propertyName Property name
	 * @return Property value or null
	 */
	private static String getProperty(String driverId, String propertyName) {
		String propertyValue=PROPERTIES.getProperty(DEFAULT_PREFIX + "." + driverId + "." + propertyName);
		if (propertyValue!=null) {
			propertyValue=propertyValue.trim();
			if (propertyValue.isEmpty()) {
				propertyValue=null;
			}
		}
		return propertyValue;
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
	 * Returns fully qualified class name of the real driver.
	 *
	 * @return driver class FQN
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
		return prefix;
	}

	/**
	 * Simon JDBC URL prefixed with jdbc:simon:
	 *
	 * @return
	 */
	public String getSimonUrl() {
		return simonUrl;
	}

	/**
	 * Test whether url is a Simon JDBC URL
	 */
	public static boolean isSimonUrl(String url) {
		return url != null && url.toLowerCase().startsWith(SimonConnectionConfiguration.URL_PREFIX);
	}

	/**
	 * Get name of class implementing {@link javax.sql.ConnectionPoolDataSource}
	 */
	public String getRealConnectionPoolDataSourceName() {
		return getProperty(driverId, "cpdatasource");
	}

	/**
	 * Get name of class implementing {@link javax.sql.DataSource}
	 */
	public String getRealDataSourceName() {
		return getProperty(driverId, "datasource");
	}

	/**
	 * Get name of class implementing {@link javax.sql.XADataSource}
	 */
	public String getRealXADataSourceName() {
		return getProperty(driverId, "xadatasource");
	}
}
