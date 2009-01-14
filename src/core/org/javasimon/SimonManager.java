package org.javasimon;

import java.util.*;

/**
 * SimonManager is the central-point of the API. Manager provides access to all available Simons
 * and is also responsible for creating them when necessary. However - if you need to separate
 * your Simon hierarchies across different Java EE applications you should create your own Manager
 * in the application itself.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public final class SimonManager {
	private static SwitchingManager manager = new SwitchingManager();

	static {
//		try {
			// TODO: Not used/active yet. Configuration is not working yet.
//			SimonConfigManager.init();
//		} catch (IOException e) {
//			Logger.getLogger(SimonConfigManager.class.getName()).log(Level.SEVERE, "Simon config couldn't be processed correctly", e);
//		}
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
	 * Returns existing UnknownSimon or creates new if necessary.
	 *
	 * @param name name of the Simon
	 * @return stopwatch object
	 */
	static Simon getUnknown(String name) {
		return manager.getUnknown(name);
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
}
