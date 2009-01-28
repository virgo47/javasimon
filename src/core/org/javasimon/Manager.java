package org.javasimon;

import java.util.Collection;

/**
 * Manager functions.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 11, 2008
 */
public interface Manager {
	/**
	 * Name of the root Simon.
	 */
	String ROOT_SIMON_NAME = "";
	/**
	 * Hierarchy delimiter in Simon name.
	 */
	String HIERARCHY_DELIMITER = ".";

	/**
	 * Returns root Simon. Type of the Simon is unknown at the start but it can be replaced
	 * by the real Simon later. Specific get method with root simon name constant can be used
	 * in that case.
	 *
	 * @return root Simon
	 */
	Simon getRootSimon();

	/**
	 * Returns Simon by its name if it exists.
	 *
	 * @param name name of the Simon
	 * @return Simon object
	 */
	Simon getSimon(String name);

	/**
	 * Returns existing Counter or creates new if necessary.
	 *
	 * @param name name of the Counter
	 * @return counter object
	 */
	Counter getCounter(String name);

	/**
	 * Returns existing Stopwatch or creates new if necessary. Stopwatch implementation
	 * type can be specified.
	 *
	 * @param name name of the Stopwatch
	 * @return stopwatch object
	 */
	Stopwatch getStopwatch(String name);

	/**
	 * Returns collection containing names of all existing Simons.
	 *
	 * @return collection of all Simon names
	 */
	Collection<String> simonNames();

	/**
	 * Removes Simon from the Manager. If Simon has some children it will be replaced
	 * by UnknownSimon.
	 *
	 * @param name name of the Simon
	 */
	void destroySimon(String name);

	/**
	 * Clears the whole manager and starts again with a single newly created Root Simon.
	 */
	void clear();

	/**
	 * Sets up the Simon callback that allows extending the Java Simon API functionality.
	 * Predefined callback does nothing.
	 *
	 * @param callback new Java Simon API callback
	 */
	void installCallback(Callback callback);

	/**
	 * Accesses Simon callback.
	 *
	 * @return Simon callback
	 */
	Callback callback();

	/**
	 * Accesses configuration of this manager.
	 *
	 * @return configuration of this manager
	 */
	ManagerConfiguration configuration();

	/**
	 * Enables the Simon Manager. Enabled manager provides real Simons.
	 * Only {@link org.javasimon.SwitchingManager} supports this operation.
	 */
	void enable();

	/**
	 * Disables the Simon Manager. Disabled manager provides null Simons that actually do nothing.
	 * Only {@link org.javasimon.SwitchingManager} supports this operation.
	 */
	void disable();

	/**
	 * Returns true if the Java Simon API is enabled.
	 *
	 * @return true if the API is enabled
	 */
	boolean isEnabled();
}
