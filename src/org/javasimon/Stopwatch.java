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

	/**
	 * Stopwatch implementation type. Because stopwatch measures time (sums splits) and the
	 * time measuring involves two methods (start/stop) there are various possibilities how
	 * to do that in a thread safe environment. Default implementation is multithreaded,
	 * can measure more splits in parallel, but start and stop must occur in the same
	 * thread. Simple implementation is very simple, stop adds split time since last start.
	 * Any previous start calls are ignored as are any stops without start.
	 */
	public enum Type {
		/**
		 * Thread-safe implementation where start and stop for one split must be used from
		 * within the same thread.
		 */
		DEFAULT(StopwatchImpl.class),

		/**
		 * Simple implementation that cannot measure more splits in parallel. It is thread
		 * safe, stop always adds the split measured from the last start.
		 */
		SIMPLE(StopwatchSimpleImpl.class);

		private Class<? extends AbstractSimon> stopwatchClass;

		Type(Class<? extends AbstractSimon> stopwatchClass) {
			this.stopwatchClass = stopwatchClass;
		}

		Class<? extends AbstractSimon> getStopwatchClass() {
			return stopwatchClass;
		}
	}
}
