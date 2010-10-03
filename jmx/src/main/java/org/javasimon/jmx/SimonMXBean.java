package org.javasimon.jmx;

import org.javasimon.StopwatchSample;

/**
 * Interface of Simon management bean (MXBean).
 * <p>
 * It provides general management tasks over all Simon infrastructure (means hierarchy
 * of Simons) plus some usefull util functions.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 15.7.2008 22:43:49
 * @since 2
 */
public interface SimonMXBean {

	/**
	 * Enables the Simon Manager, enable monitoring application by Simons.
	 *
	 * @see org.javasimon.Manager#enable()
	 */
	void enable();

	/**
	 * Disables the Simon Manager, disable monitoring application by Simons.
	 *
	 * @see org.javasimon.Manager#disable()
	 */
	void disable();

	/**
	 * Returns true if the Simon Manager is enabled, if monitoring is enabled.
	 *
	 * @return true if the Simon Manager is enabled
	 * @see org.javasimon.Manager#isEnabled()
	 */
	boolean isEnabled();

	/**
	 * Returns array containing full hierarchical names of all existing Simons.
	 *
	 * @return array of all Simon names
	 * @see org.javasimon.Manager#simonNames()
	 */
	String[] getSimonNames();

	/**
	 * Returns type of Simon, either COUNTER, STOPWATCH or UNKNOWN.
	 *
	 * @param name name of Simon
	 * @return string COUNTER if Counter Simon, STOPWATCH if Stopwatch Simon
	 * or UKNOWN if there is no Simon just undefined hierarchy node
	 */
	String getType(String name);

	/**
	 * Returns array containing names and types of all existing Simons.
	 *
	 * @return array of {@link SimonInfo} object
	 */
	SimonInfo[] getSimonInfos();

	/**
	 * Clears the Manager (ignored if manager is disabled). All Simons are lost,
	 * but the configuration is preserved.
	 *
	 * @see org.javasimon.Manager#clear()
	 */
	void clear();

	/**
	 * Enables particular Simon only.
	 *
	 * @param name name of the Simon
	 * @see org.javasimon.Simon#setState(org.javasimon.SimonState, boolean)
	 */
	void enableSimon(String name);

	/**
	 * Disables particular Simon only.
	 *
	 * @param name name of the Simon
	 * @see org.javasimon.Simon#setState(org.javasimon.SimonState, boolean)
	 */
	void disableSimon(String name);

	/**
	 * Lets the Simon to inherit its enable/disable state from its parent.
	 *
	 * @param name name of the Simon
	 */
	void inheritState(String name);

	/**
	 * Retrieves sample data object for a particular Counter.
	 *
	 * @param name name of the Simon
	 * @return sample object or null if Simon with entered name doesn't exist
	 * @see org.javasimon.CounterSample
	 */
	CounterSample getCounterSample(String name);

	/**
	 * Retrieves sample data object for a particular Stopwatch.
	 *
	 * @param name name of the Simon
	 * @return sample object or null if Simon with entered name doesn't exist
	 * @see org.javasimon.StopwatchSample
	 */
	StopwatchSample getStopwatchSample(String name);

	/**
	 * Retrieves sample data object for a particular Counter and resets it.
	 *
	 * @param name name of the Simon
	 * @return sample object or null if Simon with entered name doesn't exist
	 * @see org.javasimon.CounterSample
	 */
	CounterSample getCounterSampleAndReset(String name);

	/**
	 * Retrieves sample data object for a particular Stopwatch and resets it.
	 *
	 * @param name name of the Simon
	 * @return sample object or null if Simon with entered name doesn't exist
	 * @see org.javasimon.StopwatchSample
	 */
	StopwatchSample getStopwatchSampleAndReset(String name);

	/**
	 * Resets the particular Simon by its name.
	 *
	 * @param name name of the Simon
	 */
	void reset(String name);

	/**
	 * Prints multi-line string containing Simon tree starting with the specified Simon to standard output.
	 *
	 * @see org.javasimon.utils.SimonUtils#simonTreeString(org.javasimon.Simon)
	 */
	void printSimonTree();
}
