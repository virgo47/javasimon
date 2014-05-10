package org.javasimon.jmx;

import org.javasimon.StopwatchSample;

import java.util.List;

/**
 * Interface of Simon management bean (MXBean) representing single point of access to a particular Simon
 * {@link org.javasimon.Manager}.
 * <p/>
 * It provides general management tasks over all Simons for a single Manager (hierarchy of Simons)
 * plus some useful util functions.
 *
 * @author Radovan Sninsky
 * @since 2.0
 */
@SuppressWarnings("UnusedDeclaration")
public interface SimonManagerMXBean {

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
	 * @see org.javasimon.Manager#getSimonNames()
	 */
	String[] getSimonNames();

	/**
	 * Returns array containing full hierarchical names of all existing Simons in natural String order.
	 *
	 * @return array of all Simon names (ordered)
	 * @see org.javasimon.Manager#getSimonNames()
	 */
	String[] getSimonNamesOrdered();

	/**
	 * Returns type of Simon, either COUNTER, STOPWATCH or UNKNOWN.
	 *
	 * @param name name of Simon
	 * @return string COUNTER if Counter Simon, STOPWATCH if Stopwatch Simon
	 * or UNKNOWN if there is no Simon just undefined hierarchy node
	 */
	String getType(String name);

	/**
	 * Returns array containing names and types of all existing Simons ordered naturally by name.
	 *
	 * @return array of {@link SimonInfo} objects
	 */
	SimonInfo[] getSimonInfos();

	/**
	 * Clears the Manager (ignored if manager is disabled). All Simons are lost,
	 * but the configuration is preserved.
	 *
	 * @see org.javasimon.Manager#clear()
	 */
	void clearManager();

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
	 * Samples increment in Counter values since the previous call of this method with the
	 * same key. When the method is called the first time for the key, current values
	 * are returned (same like from {@link #getCounterSample(String)} }. Any subsequent calls with the key
	 * provide increments.
	 * <p/>
	 * Clients should use a unique key (GUID, host name, etc.), to avoid interference
	 * with other clients.
	 *
	 * @param name name of the stopwatch
	 * @param key name of an incremental sample
	 * @return sample collected for a specified key if it was created before
	 */
	CounterSample getIncrementCounterSample(String name, String key);

	/**
	 * Samples increment in Stopwatch values since the previous call of this method with the
	 * same key. When the method is called the first time for the key, current values
	 * are returned (same like from {@link #getCounterSample(String)} }. Any subsequent calls with the key
	 * provide increments.
	 * <p/>
	 * Clients should use a unique key (GUID, host name, etc.), to avoid interference
	 * with other clients.
	 *
	 * @param name name of the stopwatch
	 * @param key name of an incremental sample
	 * @return sample collected for a specified key if it was created before
	 */
	org.javasimon.jmx.StopwatchSample getIncrementStopwatchSample(String name, String key);

	/**
	 * Prints multi-line string containing Simon tree starting with the specified Simon to standard output.
	 *
	 * @see org.javasimon.utils.SimonUtils#simonTreeString(org.javasimon.Simon)
	 */
	void printSimonTree();

	/**
	 * Sample all Counters
	 *
	 * @return One Sample for each Counter
	 */
	List<CounterSample> getCounterSamples();

	/**
	 * Increment sample all Counters.
	 *
	 * @param key name of an incremental sample
	 * @return one Sample for each Counter
	 * @see #getIncrementCounterSample(String, String)
	 */
	List<CounterSample> getIncrementCounterSamples(String key);

	/**
	 * Sample all Counters whose name matches given pattern
	 *
	 * @param namePattern Name pattern, null means all Counters
	 * @return One Sample for each Counter
	 */
	List<CounterSample> getCounterSamples(String namePattern);

	/**
	 * Incremental sample for all Counters whose name matches given pattern.     *
	 *
	 * @param namePattern name pattern ({@link org.javasimon.SimonPattern}), {@code null} means all Counters
	 * @param key name of an incremental sample
	 * @return one Sample for each Counter whose name matches given pattern
	 * @see #getIncrementCounterSample(String, String)
	 */
	List<CounterSample> getIncrementCounterSamples(String namePattern, String key);

	/**
	 * Sample all Stopwatches whose name matches given pattern.
	 *
	 * @param namePattern name pattern ({@link org.javasimon.SimonPattern}), {@code null} means all Stopwatches
	 * @return one Sample for each Stopwatch
	 */
	List<org.javasimon.jmx.StopwatchSample> getStopwatchSamples(String namePattern);

	/**
	 * Increment sample all Stopwatches whose name matches given pattern.
	 *
	 * @param namePattern name pattern ({@link org.javasimon.SimonPattern}), {@code null} means all Stopwatches
	 * @param key name of an incremental sample
	 * @return one Sample for each Stopwatch whose name matches given pattern
	 * @see #getIncrementStopwatchSample(String, String)
	 */
	List<org.javasimon.jmx.StopwatchSample> getIncrementStopwatchSamples(String namePattern, String key);

	/**
	 * Sample all Stopwatches.
	 *
	 * @return one Sample for each Stopwatch
	 */
	List<org.javasimon.jmx.StopwatchSample> getStopwatchSamples();

	/**
	 * Increment sample all Stopwatches.
	 *
	 * @param key name of an incremental sample
	 * @return one Sample for each Stopwatch
	 * @see #getIncrementStopwatchSample(String, String)
	 */
	List<org.javasimon.jmx.StopwatchSample> getIncrementStopwatchSamples(String key);
}
