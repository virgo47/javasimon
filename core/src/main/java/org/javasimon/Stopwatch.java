package org.javasimon;

/**
 * Stopwatch Simon measures time spans and holds related statistics.
 * Methods {@link #start} creates new {@link org.javasimon.Split} object.
 * On this object you can call {@link org.javasimon.Split#stop()} - this demarcates measured interval.
 * Alternatively method {@link #addTime} can be used to add split time to the stopwatch. Both ways effectively
 * updates usage times, increase usage counter by one and updates total time of the stopwatch.
 * Split object enables multiple time-splits to be measured in parallel.
 * <p/>
 * Example:
 * <pre>
 * Split split = SimonManager.getStopwatch("com.my.stopwatch").start();
 * //... here goes the measured code
 * split.stop();
 * System.out.println("Result: " + split.getStopwatch()); // print will be probably somewhere else</pre>
 * This can be used for simple micro-benchmarking, critical section monitoring, in web
 * filter to measure request times, etc.
 *
 * {@link org.javasimon.SimonManager} should always be used to get the stopwatch before using it,
 * because otherwise the code will not reflect enable/disable of the whole API.
 *
 * <h3>Disable/enable considerations</h3>
 *
 * While Counter's usage is atomic, Stopwatch measures splits and every measurement involves two
 * calls (start/stop) over a period of time. It's important to know how various management actions
 * affect measurement:
 * <ul>
 * <li>If start OR stop is called with disabled Simon, nothing is measured. That means that if
 * stopwatch is disabled or enabled between these calls, stop always returns 0 and totals are
 * not updated.
 * <li>If stopwatch is obtained from enabled Manager (API is enabled) and Manager is later
 * disabled before stop, this split is measured because real stopwatch instance was obtained
 * (considering that the Stopwatch itself is enabled).
 * If it's other way around then the split is not measured because obtained instance is "null"
 * Simon.
 * </ul>
 *
 * While API disable causes that the code works with "null" Simons, state of the real Simon is
 * perfectly preserved. Disabling particular Simon on the other hand resets some of its state.
 * When the stopwatch is disabled, its active count is set to 0 and before it is enabled again both
 * its thread-local split map and keyed-object split map is cleared. Of course, all totals/counts
 * are preserved.
 * <h3>Other usages</h3>
 * Reset of the stopwatch resets all stats except usages that are rather management related and
 * should not be reset. Reset is used often for various sampling purposes which requires that only
 * cumulative stats are reset, but all running splits are preserved. Running splits will be added
 * to the stopwatch after reset when respective stop methods are called.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public interface Stopwatch extends Simon {
	/**
	 * Adds split time in nanoseconds to total time of the stopwatch.
	 *
	 * @param ns split time
	 * @return this stopwatch
	 */
	Stopwatch addTime(long ns);

	/**
	 * Starts the new split for this stopwatch. This action does not hold any resources and
	 * if {@link Split} object is collected, no leak occurs. However, active count is increased
	 * and without stopping the split active count stays increased which may render that
	 * information useless.
	 *
	 * @return split object
	 * @see org.javasimon.Split#stop()
	 */
	Split start();

	/**
	 * Returns total sum of all split times in nanoseconds.
	 *
	 * @return total time of the stopwatch in nanoseconds
	 */
	long getTotal();

	/**
	 * Returns value of the last added split - wheter it was added directly or with stop method.
	 *
	 * @return value of the last added split
	 */
	long getLast();

	/**
	 * Returns usage count of the stopwatch. Counter is increased by {@code addTime} and
	 * {@code stop} - that means that it's updated every time the next time split is added.
	 *
	 * @return count of time splits
	 */
	long getCounter();

	/**
	 * Returns maximal time split value in nanoseconds.
	 *
	 * @return maximal time split in nanoseconds
	 */
	long getMax();

	/**
	 * Returns minimal time split value in nanoseconds.
	 *
	 * @return minimal time split in nanoseconds
	 */
	long getMin();

	/**
	 * Returns ms timestamp when the max value was measured.
	 *
	 * @return ms timestamp of the max value measurement
	 */
	long getMaxTimestamp();

	/**
	 * Returns ms timestamp when the min value was measured.
	 *
	 * @return ms timestamp of the min value measurement
	 */
	long getMinTimestamp();

	/**
	 * Resets the Simon - clears total time, min, max, usage stats, etc. Split times that
	 * started before reset will be counted when appropriate stop is called, so no split
	 * time is ignored by the stopwatch. Active count is not reset.
	 *
	 * @return returns this
	 */
	Stopwatch reset();

	/**
	 * Returns current number of measured splits (concurrently running).
	 *
	 * @return current number of active splits
	 */
	long getActive();

	/**
	 * Returns peek value of active concurrent splits.
	 *
	 * @return maximum reached value of active splits
	 */
	long getMaxActive();

	/**
	 * Retruns ms timestamp when the last peek of the active split count occured.
	 *
	 * @return ms timestamp of the last peek of the active split count
	 */
	long getMaxActiveTimestamp();

	/**
	 * Returns mean value (average) of all measured values.
	 *
	 * @return mean value
	 */
	double getMean();

	/**
	 * Returns standard deviation for all measured values.
	 *
	 * @return standard deviation
	 */
	double getStandardDeviation();

	/**
	 * Returns unbiased estimate of the population variance.
	 *
	 * @return unbiased estimated variance
	 */
	double getVariance();

	/**
	 * Returns variance value of all measured values (entire population).
	 *
	 * @return entire population variance
	 */
	double getVarianceN();
}
