package org.javasimon;

/**
 * Simon stopwatch for time span measuring. You can use {@code start}/{@code stop} or
 * {@code addTime} to add split times to the stopwatch. Both ways effectively updates
 * usage times, increase counter by one and updates total time of the stopwatch.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
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
	 * Starts this stopwatch.
	 *
	 * @return this stopwatch
	 */
	Stopwatch start();

	/**
	 * Stops this stopwatch and adds split time (stop-start) to total time of the stopwatch.
	 *
	 * @return split time in nanoseconds
	 */
	long stop();

	/**
	 * Returns total sum of all split times in nanoseconds.
	 *
	 * @return total time of the stopwatch in nanoseconds
	 */
	long getTotal();

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
	 * time is ignored by the stopwatch.
	 *
	 * @return returns this
	 */
	Stopwatch reset();
}
