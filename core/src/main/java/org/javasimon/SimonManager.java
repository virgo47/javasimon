package org.javasimon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

import org.javasimon.callback.Callback;
import org.javasimon.callback.CompositeCallback;
import org.javasimon.utils.SimonUtils;
import org.javasimon.utils.SystemDebugCallback;

/**
 * SimonManager is static utility class providing so called "default {@link org.javasimon.Manager}.
 * It is possible to create separate Manager, but it cannot be accessed via this convenient
 * utility-like class. This option may be usefull in Java EE environmant when it's required to
 * separate Simon trees accross different applications. For majority of Java SE applications this
 * class is recommended.
 * <p/>
 * SimonManager also provides configuration initialization via properties. To initialize configuration
 * with the configuration file following option can be added to JVM executable:
 * <pre>-Djavasimon.config.file=some-path/simon.config.xml</pre>
 * To configure the SimonManager via resource that can be found on classpath:
 * <pre>-Djavasimon.config.resource=org/javasimon/example/wannabe-config.xml</pre>
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class SimonManager {
	/**
	 * Property name for the Simon configuration file is "javasimon.config.file".
	 */
	public static final String PROPERTY_CONFIG_FILE_NAME = "javasimon.config.file";

	/**
	 * Property name for the Simon configuration resource is "javasimon.config.resource".
	 */
	public static final String PROPERTY_CONFIG_RESOURCE_NAME = "javasimon.config.resource";

	private static final long INIT_NANOS;

	private static final long INIT_MILLIS;

	private static Manager manager = new SwitchingManager();

	/**
	 * Calls {@link #init()}.
	 */
	static {
		init();
		// for conversion between nano and millis - see method millisForNano(long)
		INIT_NANOS = System.nanoTime();
		INIT_MILLIS = System.currentTimeMillis();
	}

	/**
	 * Initilizes the configuration facility for the default Simon Manager. Fetches exception
	 * if configuration resource or file is not found. This method does NOT clear the manager
	 * itself, only the configuration is reloaded. Method also preserves Callback setup.
	 */
	static void init() {
		Callback tempraryCallback = new SystemDebugCallback();
		manager.callback().addCallback(tempraryCallback); // just for reporting warnings, will be removed
		try {
			manager.configuration().clear();
			String fileName = System.getProperty(PROPERTY_CONFIG_FILE_NAME);
			if (fileName != null) {
				manager.configuration().readConfig(new FileReader(fileName));
			}
			String resourceName = System.getProperty(PROPERTY_CONFIG_RESOURCE_NAME);
			if (resourceName != null) {
				InputStream is = SimonManager.class.getClassLoader().getResourceAsStream(resourceName);
				if (is == null) {
					throw new FileNotFoundException(resourceName);
				}
				manager.configuration().readConfig(new InputStreamReader(is));
			}
		} catch (Exception e) {
			manager.callback().onManagerWarning("SimonManager initialization error", e);
		}
		manager.callback().removeCallback(tempraryCallback);
	}

	private SimonManager() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns Simon by its name if it exists.
	 *
	 * @param name name of the Simon
	 * @return Simon object
	 */
	public static Simon getSimon(String name) {
		return manager.getSimon(name);
	}

	/**
	 * Destroys Simon or replaces it with UnknownSimon if it's necessary to preserve the hierarchy.
	 *
	 * @param name name of the Simon
	 */
	public static void destroySimon(String name) {
		manager.destroySimon(name);
	}

	/**
	 * Returns existing Counter or creates new if necessary.
	 *
	 * @param name name of the Counter
	 * @return counter object
	 */
	public static Counter getCounter(String name) {
		return manager.getCounter(name);
	}

	/**
	 * Returns existing Stopwatch or creates new if necessary.
	 *
	 * @param name name of the Stopwatch
	 * @return stopwatch object
	 */
	public static Stopwatch getStopwatch(String name) {
		return manager.getStopwatch(name);
	}

	/**
	 * Enables the Simon Manager. Enabled manager provides real Simons.
	 */
	public static void enable() {
		manager.enable();
	}

	/**
	 * Disables the Simon Manager. Disabled manager provides null Simons that actually do nothing.
	 */
	public static void disable() {
		manager.disable();
	}

	/**
	 * Returns true if the Simon Manager is enabled.
	 *
	 * @return true if the Simon Manager is enabled
	 */
	public static boolean isEnabled() {
		return manager.isEnabled();
	}

	/**
	 * Returns root Simon. Type of the Simon is unknown at the start but it can be replaced
	 * by the real Simon later. Specific get method with root Simon name constant can be used
	 * in that case.
	 *
	 * @return root Simon
	 */
	public static Simon getRootSimon() {
		return manager.getRootSimon();
	}

	/**
	 * Returns unmodifiable collection containing names of all existing Simons.
	 *
	 * @return collection of all Simon names
	 * @since 3.1
	 */
	public static Collection<String> getSimonNames() {
		return manager.getSimonNames();
	}

	/**
	 * Returns collection containing all existing Simons matching the pattern (can be {@code null}).
	 * Collection is unmodifiable if {@code null} pattern is provided and all Simons are returned,
	 * otherwise new collection with matching Simons is returned.
	 *
	 * @param pattern Simon name pattern (see {@link SimonPattern}
	 * @return collection of all Simons matching the pattern
	 * @see SimonPattern to find out more about possible patterns
	 * @since 3.1
	 */
	public static Collection<Simon> getSimons(SimonPattern pattern) {
		return manager.getSimons(pattern);
	}

	/**
	 * Clears the SimonManager (ignored if manager is disabled). All Simons are lost,
	 * but configuration is preserved.
	 */
	public static void clear() {
		manager.clear();
	}

	/**
	 * Accesses Simon callback.
	 *
	 * @return Simon callback
	 */
	public static CompositeCallback callback() {
		return manager.callback();
	}

	/**
	 * Accesses configuration of this manager.
	 *
	 * @return configuration of this manager
	 */
	public static ManagerConfiguration configuration() {
		return manager.configuration();
	}

	/**
	 * Accesses default Simon Manager which is the switching manager.
	 *
	 * @return default Simon Manager
	 */
	public static Manager manager() {
		return manager;
	}

	/**
	 * Method propagates message to manager's {@link org.javasimon.callback.Callback}. This allows user to report any
	 * message if they implement {@link org.javasimon.callback.Callback#onManagerMessage(String)}.
	 *
	 * @param message message text
	 */
	public static void message(String message) {
		manager.message(message);
	}

	/**
	 * Method propagates warning to manager's {@link org.javasimon.callback.Callback}. This allows user to report any
	 * warning and/or exception if they implement {@link org.javasimon.callback.Callback#onManagerWarning(String, Exception)}.
	 *
	 * @param warning arbitrary warning message
	 * @param cause exception causing this warning
	 */
	public static void warning(String warning, Exception cause) {
		manager.warning(warning, cause);
	}

	/**
	 * Converts nano timer value into millis timestamp compatible with {@link System#currentTimeMillis()}.
	 *
	 * @param nanos nano timer value
	 * @return ms timestamp
	 * @since 3.1
	 */
	public static long millisForNano(long nanos) {
		return INIT_MILLIS + (nanos - INIT_NANOS) / SimonUtils.NANOS_IN_MILLIS;
	}
}
