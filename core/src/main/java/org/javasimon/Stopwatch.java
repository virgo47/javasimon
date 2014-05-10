package org.javasimon;

/**
 * Stopwatch Simon measures time spans and holds related statistics.
 * Methods {@link #start} creates new {@link org.javasimon.Split} object.
 * On this object you can call {@link org.javasimon.Split#stop()} - this demarcates measured interval.
 * Alternatively method {@link #addSplit(Split)} can be used to add split to the stopwatch ({@link Split#create(long)}
 * can be used to create finished Split for any nanos value). Both ways effectively
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
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public interface Stopwatch extends Simon {
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
	 * Adds {@link Split} to the stopwatch which is useful for aggregation of splits created for other stopwatch.
	 * Split object should be stopped. Main difference is the callback method called as
	 * {@link org.javasimon.callback.Callback#onStopwatchAdd(Stopwatch, Split, StopwatchSample)} provides split object to the callback.
	 * <p/>
	 * Usage examples:
	 * <pre>Split split = Split.start(); // no stopwatch needed
	 * ...
	 * someStopwatch.addSplit(split.stop()); // you may omit stop(), if you does not use the split after this point</pre>
	 *
	 * @param split split object (should be stopped)
	 * @return this stopwatch
	 * @since 3.1
	 */
	Stopwatch addSplit(Split split);

	/**
	 * Returns total sum of all split times in nanoseconds.
	 *
	 * @return total time of the stopwatch in nanoseconds
	 */
	long getTotal();

	/**
	 * Returns value of the last added split - whether it was added directly or with stop method.
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
	 * Returns current number of measured splits (concurrently running). This counter can show more
	 * splits than is measured at any moment if some splits were "forgotten" (not stopped and garbage
	 * collected). This does not imply any resource leak, just bad practice of not stopping Splits somewhere
	 * in the client code.
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
	 * Returns ms timestamp when the last peek of the active split count occurred.
	 *
	 * @return ms timestamp of the last peek of the active split count
	 */
	long getMaxActiveTimestamp();

	/**
	 * Returns mean value (average) of all measured values.
	 * If {@link #getCounter()} is 0 it should return {@code Double.NaN}, but for practical reasons returns 0.
	 *
	 * @return mean value
	 */
	double getMean();

	/**
	 * Returns unbiased estimate of standard deviation. If {@link #getCounter()} is 0 returns {@code Double.NaN}.
	 * http://en.wikipedia.org/wiki/Unbiased_estimation_of_standard_deviation
	 *
	 * @return unbiased estimate of standard deviation
	 */
	double getStandardDeviation();

	/**
	 * Returns unbiased estimate of the population variance. If {@link #getCounter()} is 0 returns {@code Double.NaN}.
	 * http://en.wikipedia.org/wiki/Variance#Population_variance_and_sample_variance
	 *
	 * @return unbiased estimated variance
	 */
	double getVariance();

	/**
	 * Returns variance value of all measured values (entire population).
	 * If {@link #getCounter()} is 0 returns {@code Double.NaN}.
	 * http://en.wikipedia.org/wiki/Variance#Population_variance_and_sample_variance
	 *
	 * @return entire population variance
	 */
	double getVarianceN();

	@Override
	StopwatchSample sample();

	StopwatchSample sampleIncrement(Object key);
}
