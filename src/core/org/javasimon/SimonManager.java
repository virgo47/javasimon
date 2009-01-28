package org.javasimon;

import java.util.*;
import java.io.*;

/**
 * SimonManager is the central-point of the API. Manager provides access to all available Simons
 * and is also responsible for creating them when necessary. It is possible to create separate
 * Manager, but it cannot be accessed via this convenient utility-like class. This option may be
 * usefull in Java EE environmant when it's required to separate Simon trees accross different
 * applications.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public final class SimonManager {
	/**
	 * Property name for the Simon configuration file.
	 */
	public static final String PROPERTY_CONFIG_FILE_NAME = "javasimon.config.file";

	/**
	 * Property name for the Simon configuration resource.
	 */
	public static final String PROPERTY_CONFIG_RESOURCE_NAME = "javasimon.config.resource";

	private static SwitchingManager manager = new SwitchingManager();

	/**
	 * Calls {@link #init()}.
	 */
	static {
		init();
	}

	/**
	 * Initilizes the configuration facility for the default Simon Manager. Fetches exception
	 * if configuration resource or file is not found. This method does NOT clear the manager
	 * itself, only the configuration is reloaded.
	 */
	public static void init() {
		try {
			manager.configuration().clear();
			String fileName = System.getProperty(PROPERTY_CONFIG_FILE_NAME);
			if (fileName != null) {
				manager.configuration().readConfig(new FileReader(fileName));
			}
			String resourceName = System.getProperty(PROPERTY_CONFIG_RESOURCE_NAME);
			if (resourceName != null) {
				manager.configuration().readConfig(new InputStreamReader(SimonManager.class.getClassLoader().getResourceAsStream(resourceName)));
			}
		} catch (Exception e) {
			manager.callback().warning("SimonManager initialization error", e);
		}
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
	 * by the real Simon later. Specific get method with root simon name constant can be used
	 * in that case.
	 *
	 * @return root Simon
	 */
	public static Simon getRootSimon() {
		return manager.getRootSimon();
	}

	/**
	 * Returns collection containing names of all existing Simons.
	 *
	 * @return collection of all Simon names
	 */
	public static Collection<String> simonNames() {
		return manager.simonNames();
	}

	/**
	 * Clears the SimonManager (ignored if manager is disabled). All Simons are lost,
	 * but configuration is preserved.
	 */
	public static void clear() {
		manager.clear();
	}

	/**
	 * Sets up the Simon callback that allows extending the Java Simon API functionality.
	 * Predefined callback does nothing.
	 *
	 * @param callback new Java Simon API callback
	 */
	public static void installCallback(Callback callback) {
		manager.installCallback(callback);
	}

	/**
	 * Accesses Simon callback.
	 *
	 * @return Simon callback
	 */
	public static Callback callback() {
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
}
