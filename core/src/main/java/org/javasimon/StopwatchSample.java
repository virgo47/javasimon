package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * Object holds all relevant data from Stopwatch Simon. Whenever it is important to get more values
 * in a synchronous manner, {@link org.javasimon.Stopwatch#sample()} (or {@link Stopwatch#sampleAndReset()}
 * should be used to obtain this Java Bean object.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public class StopwatchSample extends Sample {
	private long total;
	private long counter;
	private long min;
	private long max;
	private long minTimestamp;
	private long maxTimestamp;
	private long active;
	private long maxActive;
	private long maxActiveTimestamp;
	private long last;
	private double mean;
	private double standardDeviation;
	private double variance;
	private double varianceN;

	/**
	 * Returns the total sum of all split times in nanoseconds.
	 *
	 * @return total time of the stopwatch in nanoseconds
	 */
	public final long getTotal() {
		return total;
	}

	/**
	 * Returns the total sum of all split times in nanoseconds as a formatted string.
	 *
	 * @return total time of the stopwatch in nanoseconds as string
	 */
	public final String getTotalAsString() {
		return SimonUtils.presentNanoTime(total);
	}

	/**
	 * Sets the total sum of all split times in nanoseconds.
	 *
	 * @param total total time of the stopwatch in nanoseconds
	 */
	public final void setTotal(long total) {
		this.total = total;
	}

	/**
	 * Returns usage count of the stopwatch. Counter is increased by {@code addTime} and
	 * {@code stop} - that means that it's updated every time the next time split is added.
	 *
	 * @return count of time splits
	 */
	public final long getCounter() {
		return counter;
	}

	/**
	 * Sets the usage count of the stopwatch.
	 *
	 * @param counter count of time splits
	 */
	public final void setCounter(long counter) {
		this.counter = counter;
	}

	/**
	 * Returns minimal time split value in nanoseconds.
	 *
	 * @return minimal time split in nanoseconds
	 */
	public final long getMin() {
		return min;
	}

	/**
	 * Returns minimal time split value in nanoseconds as a formatted string.
	 *
	 * @return minimal time split in nanoseconds as string
	 */
	public final String getMinAsString() {
		return SimonUtils.presentMinMaxSplit(min);
	}

	/**
	 * Sets the minimal time split value in nanoseconds.
	 *
	 * @param min minimal time split in nanoseconds
	 */
	public final void setMin(long min) {
		this.min = min;
	}

	/**
	 * Returns maximal time split value in nanoseconds.
	 *
	 * @return maximal time split in nanoseconds
	 */
	public final long getMax() {
		return max;
	}

	/**
	 * Returns maximal time split value in nanoseconds as a formatted string.
	 *
	 * @return maximal time split in nanoseconds as string
	 */
	public final String getMaxAsString() {
		return SimonUtils.presentMinMaxSplit(max);
	}

	/**
	 * Sets the maximal time split value in nanoseconds.
	 *
	 * @param max maximal time split in nanoseconds
	 */
	public final void setMax(long max) {
		this.max = max;
	}

	/**
	 * Returns ms timestamp when the min value was measured.
	 *
	 * @return ms timestamp of the min value measurement
	 */
	public final long getMinTimestamp() {
		return minTimestamp;
	}

	/**
	 * Returns ms timestamp when the min value was measured as a formatted string.
	 *
	 * @return ms timestamp of the min value measurement as string
	 */
	public final String getMinTimestampAsString() {
		return SimonUtils.presentTimestamp(minTimestamp);
	}

	/**
	 * Sets the ms timestamp when the min value was measured.
	 *
	 * @param minTimestamp ms timestamp of the min value measurement
	 */
	public final void setMinTimestamp(long minTimestamp) {
		this.minTimestamp = minTimestamp;
	}

	/**
	 * Returns ms timestamp when the max value was measured.
	 *
	 * @return ms timestamp of the max value measurement
	 */
	public final long getMaxTimestamp() {
		return maxTimestamp;
	}

	/**
	 * Returns ms timestamp when the max value was measured as a formatted string.
	 *
	 * @return ms timestamp of the max value measurement as string
	 */
	public final String getMaxTimestampAsString() {
		return SimonUtils.presentTimestamp(maxTimestamp);
	}

	/**
	 * Sets the ms timestamp when the max value was measured.
	 *
	 * @param maxTimestamp ms timestamp of the max value measurement
	 */
	public final void setMaxTimestamp(long maxTimestamp) {
		this.maxTimestamp = maxTimestamp;
	}

	/**
	 * Returns current number of measured splits (concurrently running).
	 *
	 * @return current number of active splits
	 */
	public final long getActive() {
		return active;
	}

	/**
	 * Sets the current number of measured splits (concurrently running).
	 *
	 * @param active current number of active splits
	 */
	public final void setActive(long active) {
		this.active = active;
	}

	/**
	 * Returns peek value of active concurrent splits.
	 *
	 * @return maximum reached value of active splits
	 */
	public final long getMaxActive() {
		return maxActive;
	}

	/**
	 * Sets the peek value of active concurrent splits.
	 *
	 * @param maxActive maximum reached value of active splits
	 */
	public final void setMaxActive(long maxActive) {
		this.maxActive = maxActive;
	}

	/**
	 * Returns ms timestamp when the last peek of the active split count occured.
	 *
	 * @return ms timestamp of the last peek of the active split count
	 */
	public final long getMaxActiveTimestamp() {
		return maxActiveTimestamp;
	}

	/**
	 * Returns ms timestamp when the last peek of the active split count occured as a formatted string.
	 *
	 * @return ms timestamp of the last peek of the active split count as string
	 */
	public final String getMaxActiveTimestampAsString() {
		return SimonUtils.presentTimestamp(maxActiveTimestamp);
	}

	/**
	 * Sets the ms timestamp when the last peek of the active split count occured.
	 *
	 * @param maxActiveTimestamp ms timestamp of the last peek of the active split count
	 */
	public final void setMaxActiveTimestamp(long maxActiveTimestamp) {
		this.maxActiveTimestamp = maxActiveTimestamp;
	}

	/**
	 * Returns the value of the last measured split in ns.
	 *
	 * @return last measured split in ns
	 */
	public final long getLast() {
		return last;
	}

	/**
	 * Returns the value of the last measured split in ns as a formatted string.
	 *
	 * @return last measured split in ns as string
	 */
	public final String getLastAsString() {
		return SimonUtils.presentNanoTime(last);
	}

	/**
	 * Sets the value of the last measured split in ns.
	 *
	 * @param last last measured split in ns
	 */
	public final void setLast(long last) {
		this.last = last;
	}

	/**
	 * Returns mean value (average) of all measured values.
	 *
	 * @return mean value
	 */
	public final double getMean() {
		return mean;
	}

	/**
	 * Returns mean value (average) of all measured values as a formatted string (ns).
	 *
	 * @return mean value as string
	 */
	public final String getMeanAsString() {
		return SimonUtils.presentNanoTime((long) mean);
	}

	/**
	 * Sets the mean value (average) of all measured values.
	 *
	 * @param mean mean value
	 */
	public final void setMean(double mean) {
		this.mean = mean;
	}

	/**
	 * Returns standard deviation for all measured values.
	 *
	 * @return standard deviation
	 */
	public final double getStandardDeviation() {
		return standardDeviation;
	}

	/**
	 * Sets the standard deviation for all measured values.
	 *
	 * @param standardDeviation standard deviation
	 */
	public final void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	/**
	 * Returns unbiased estimate of the population variance.
	 *
	 * @return unbiased estimated variance
	 */
	public final double getVariance() {
		return variance;
	}

	/**
	 * Sets the unbiased estimate of the population variance.
	 *
	 * @param variance unbiased estimated variance
	 */
	public final void setVariance(double variance) {
		this.variance = variance;
	}

	/**
	 * Returns variance value of all measured values (entire population).
	 *
	 * @return entire population variance
	 */
	public final double getVarianceN() {
		return varianceN;
	}

	/**
	 * Sets the variance value of all measured values (entire population).
	 *
	 * @param varianceN entire population variance
	 */
	public final void setVarianceN(double varianceN) {
		this.varianceN = varianceN;
	}

	/**
	 * Returns readable representation of object.
	 *
	 * @return string with readable representation of object
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("StopwatchSample");
		sb.append("{total=").append(SimonUtils.presentNanoTime(total));
		sb.append(", counter=").append(counter);
		sb.append(", min=").append(SimonUtils.presentMinMaxSplit(min));
		sb.append(", max=").append(SimonUtils.presentMinMaxSplit(max));
		sb.append(", minTimestamp=").append(SimonUtils.presentTimestamp(minTimestamp));
		sb.append(", maxTimestamp=").append(SimonUtils.presentTimestamp(maxTimestamp));
		sb.append(", active=").append(active);
		sb.append(", maxActive=").append(maxActive);
		sb.append(", maxActiveTimestamp=").append(SimonUtils.presentTimestamp(maxActiveTimestamp));
		sb.append(", last=").append(SimonUtils.presentNanoTime(last));
		sb.append(", mean=").append(SimonUtils.presentNanoTime((long) getMean()));
		sb.append(", standardDeviation=").append(SimonUtils.presentNanoTime((long) getStandardDeviation()));
		sb.append(", variance=").append(getVariance());
		sb.append(", varianceN=").append(getVarianceN());
		sb.append(", note=").append(getNote());
		sb.append(", firstUsage=").append(getFirstUsage());
		sb.append(", lastUsage=").append(getLastUsage());
		sb.append(", lastReset=").append(getLastReset());
		sb.append("}");
		return sb.toString();
	}
}
