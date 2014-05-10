package org.javasimon.jmx;

/**
 * Interface for MX Bean representing a particular {@link org.javasimon.Stopwatch}. It is not created
 * by default when JMX is activated - it must be created explicitly.
 * {@link JmxRegisterCallback} can be used to automate this.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public interface StopwatchMXBean extends SimonSuperMXBean {

	/**
	 * Returns total sum of all split times in nanoseconds.
	 *
	 * @return total time of the stopwatch in nanoseconds
	 * @see org.javasimon.Stopwatch#getTotal()
	 * @since 3.3
	 */
	long getTotal();

	/**
	 * Returns value of the last added split - whether it was added directly or with stop method.
	 *
	 * @return value of the last added split
	 * @see org.javasimon.Stopwatch#getLast()
	 */
	long getLast();

	/**
	 * Returns value of the last added split as formatted string.
	 *
	 * @return value of the last added split as string
	 */
	String getLastAsString();

	/**
	 * Returns usage count of the stopwatch. Counter is increased by {@code addTime} and
	 * {@code stop} - that means that it's updated every time the next time split is added.
	 *
	 * @return count of time splits
	 * @see org.javasimon.Stopwatch#getCounter()
	 * @since 3.3
	 */
	long getCounter();

	/**
	 * Returns maximal time split value in nanoseconds.
	 *
	 * @return maximal time split in nanoseconds
	 * @see org.javasimon.Stopwatch#getMax()
	 * @since 3.3
	 */
	long getMax();

	/**
	 * Returns minimal time split value in nanoseconds.
	 *
	 * @return minimal time split in nanoseconds
	 * @see org.javasimon.Stopwatch#getMin()
	 * @since 3.3
	 */
	long getMin();

	/**
	 * Returns ms timestamp when the max value was measured.
	 *
	 * @return ms timestamp of the max value measurement
	 * @see org.javasimon.Stopwatch#getMaxTimestamp()
	 * @since 3.3
	 */
	long getMaxTimestamp();

	/**
	 * Returns ms timestamp when the min value was measured.
	 *
	 * @return ms timestamp of the min value measurement
	 * @see org.javasimon.Stopwatch#getMinTimestamp()
	 * @since 3.3
	 */
	long getMinTimestamp();

	/**
	 * Returns current number of measured splits (concurrently running).
	 *
	 * @return current number of active splits
	 * @see org.javasimon.Stopwatch#getActive()
	 * @since 3.3
	 */
	long getActive();

	/**
	 * Returns peek value of active concurrent splits.
	 *
	 * @return maximum reached value of active splits
	 * @see org.javasimon.Stopwatch#getMaxActive()
	 * @since 3.3
	 */
	long getMaxActive();

	/**
	 * Returns ms timestamp when the last peek of the active split count occurred.
	 *
	 * @return ms timestamp of the last peek of the active split count
	 * @see org.javasimon.Stopwatch#getMaxActiveTimestamp()
	 * @since 3.3
	 */
	long getMaxActiveTimestamp();

	/**
	 * Returns mean value (average) of all measured values.
	 *
	 * @return mean value
	 * @see org.javasimon.Stopwatch#getMean()
	 * @since 3.3
	 */
	double getMean();

	/**
	 * Returns standard deviation for all measured values.
	 *
	 * @return standard deviation
	 * @see org.javasimon.Stopwatch#getStandardDeviation()
	 * @since 3.3
	 */
	double getStandardDeviation();

	/**
	 * Returns unbiased estimate of the population variance.
	 *
	 * @return unbiased estimated variance
	 * @see org.javasimon.Stopwatch#getVariance()
	 * @since 3.3
	 */
	double getVariance();

	/**
	 * Returns variance value of all measured values (entire population).
	 *
	 * @return entire population variance
	 * @see org.javasimon.Stopwatch#getVarianceN()
	 * @since 3.3
	 */
	double getVarianceN();

	@Override
	StopwatchSample sample();

	@Override
	StopwatchSample sampleIncrement(String key);
}
