package org.javasimon.jmx;

import org.javasimon.StopwatchSample;

/**
 * Simon MXBean interface for general management tasks. This is central point for
 * management tasks over all Simon infrastructure plus some usefull util functions.
 *
 * @author Radovan Sninsky
 * @version $Revision: 269 $ $Date: 2008-10-31 11:02:02 +0100 (Pi, 31 okt 2008) $
 * @created 15.7.2008 22:43:49
 * @since 2
 */
public interface SimonMXBean {

	/**
	 * Enables the Simon Manager.
	 *
	 * @see org.javasimon.Manager#enable()
	 */
	void enable();

	/**
	 * Disables the Simon Manager.
	 *
	 * @see org.javasimon.Manager#disable()
	 */
	void disable();

	/**
	 * Returns true if the Simon Manager is enabled.
	 *
	 * @return true if the Simon Manager is enabled
	 * @see org.javasimon.Manager#isEnabled()
	 */
	boolean isEnabled();

	/**
	 * Returns array containing names of all existing Simons.
	 *
	 * @return array of all Simon names
	 * @see org.javasimon.Manager#simonNames()
	 */
	String[] getSimonNames();

	/**
	 * Returns type of Simon, either COUNTER or STOPWATCH.
	 *
	 * @param name name of simon
	 * @return string COUNTER if Counter simon or STOPWATCH if Stopwatch simon
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
	 * Prints multi-line string containing Simon tree starting with the specified Simon to standard output.
	 *
	 * @see org.javasimon.utils.SimonUtils#simonTreeString(org.javasimon.Simon)
	 */
	void printSimonTree();
}
