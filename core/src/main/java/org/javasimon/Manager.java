package org.javasimon;

import org.javasimon.callback.CompositeCallback;
import org.javasimon.clock.Clock;

import java.util.Collection;

/**
 * Manager provides access to Simons and manages them in a tree structure. Any number of Managers
 * can be created. There is also one special Manager (called "default manager") that is accessible
 * via convenient static utility class {@link SimonManager}.
 * <p/>
 * Three different implementations exist:
 * <ul>
 * <li>{@link EnabledManager} represents fully functional manager in the enabled state;</li>
 * <li>{@link DisabledManager} is manager implementation in the disabled state;</li>
 * <li>{@link SwitchingManager} is manager implementation with both enabled and disabled manager in the background,
 * only this implementation supports {@link #enable()} and {@link #disable()} method.</li>
 * </ul>
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public interface Manager extends Clock {

	/** Name of the root Simon. */
	String ROOT_SIMON_NAME = "";

	/** Hierarchy delimiter in Simon name. */
	String HIERARCHY_DELIMITER = ".";

	/**
	 * Returns root Simon. Type of the Simon is unknown at the start but it can be replaced
	 * by the real Simon later. Specific get method with root Simon name constant can be used
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
	 * Returns existing Counter or creates new if necessary. "Anonymous" Counter can
	 * be obtained if null name is specified - then it is not added to the Simon hierarchy.
	 *
	 * @param name name of the Counter
	 * @return counter object
	 */
	Counter getCounter(String name);

	/**
	 * Returns existing Stopwatch or creates new if necessary. "Anonymous" Stopwatch can
	 * be obtained if null name is specified - then it is not added to the Simon hierarchy.
	 *
	 * @param name name of the Stopwatch
	 * @return stopwatch object
	 */
	Stopwatch getStopwatch(String name);

	/**
	 * Returns unmodifiable collection containing names of all existing Simons. Collection is not ordered.
	 *
	 * @return collection of all Simon names
	 * @since 3.1
	 */
	Collection<String> getSimonNames();

	/**
	 * Returns collection containing all existing Simons accepted by specified {@link SimonFilter}.
	 * If {@code null} filter is provided all Simons are returned in an unmodifiable Collection.
	 * Otherwise new collection with matching Simons is returned.
	 *
	 * @param simonFilter filter accepting the Simons to result collection
	 * @return collection of all Simons which pass the filter
	 * @see SimonPattern to find out more about possible patterns
	 * @since 3.2
	 */
	Collection<Simon> getSimons(SimonFilter simonFilter);

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
	 * Accesses default composite callback of this manager. Default callback can't be removed or replaced,
	 * only other callbacks can be added or removed.
	 *
	 * @return Simon callback
	 */
	CompositeCallback callback();

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

	/**
	 * Method propagates message to manager's {@link org.javasimon.callback.Callback}. This allows user to report any
	 * message if they implement {@link org.javasimon.callback.Callback#onManagerMessage(String)}.
	 *
	 * @param message message text
	 */
	void message(String message);

	/**
	 * Method propagates warning to manager's {@link org.javasimon.callback.Callback}. This allows user to report any
	 * warning and/or exception if they implement {@link org.javasimon.callback.Callback#onManagerWarning(String, Exception)}.
	 *
	 * @param warning arbitrary warning message
	 * @param cause exception causing this warning
	 */
	void warning(String warning, Exception cause);
}
