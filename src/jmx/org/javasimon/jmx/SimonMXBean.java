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
	 * @see org.javasimon.SimonManager#enable()
	 */
	void enable();

	/**
	 * Disables the Simon Manager.
	 *
	 * @see org.javasimon.SimonManager#disable() 
	 */
	void disable();

	/**
	 * Returns true if the Simon Manager is enabled.
	 *
	 * @return true if the Simon Manager is enabled
	 * @see org.javasimon.SimonManager#isEnabled() 
	 */
	boolean isEnabled();

	/**
	 * Returns array containing names of all existing Simons.
	 *
	 * @return array of all Simon names
	 * @see org.javasimon.SimonManager#simonNames() 
	 */
	String[] getSimonNames();

	/**
	 * Returns type of simon, either COUNTER or STOPWATCH.
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
	 * Clears the SimonManager (ignored if manager is disabled). All Simons are lost,
	 * but configuration is preserved.
	 *
	 * @see org.javasimon.SimonManager#clear()  
	 */
	void clear();

	/**
	 * Enables particular simon only.
	 *
	 * @param name simon hierarchical name
	 * @see org.javasimon.Simon#setState(org.javasimon.SimonState, boolean)
	 */
	void enableSimon(String name);

	/**
	 * Disables particular simon only.
	 * @param name simon hierarchical name
	 * @see org.javasimon.Simon#setState(org.javasimon.SimonState, boolean) 
	 */
	void disableSimon(String name);

	/**
	 * Retrieves so called sample (data objects) from Counter simon.
	 *
	 * @param name name of simon
	 * @return sample object or null if simon with entered name doesn't exist
	 * @see org.javasimon.CounterSample
	 */
	CounterSample getCounterSample(String name);

	/**
	 * Retrieves so called sample (data objects) from Stopwatch simon.
	 *
	 * @param name name of simon
	 * @return sample object or null if simon with entered name doesn't exist
	 * @see org.javasimon.StopwatchSample
	 */
	StopwatchSample getStopwatchSample(String name);

	/**
	 * Prints multi-line string containing Simon tree starting with the specified Simon to standard output.
	 * @see org.javasimon.utils.SimonUtils#simonTreeString(org.javasimon.Simon) 
	 */
	void printSimonTree();
}
