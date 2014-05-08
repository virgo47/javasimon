package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * Object holds all relevant data from {@link Stopwatch} Simon. Whenever it is important to get more values
 * in a synchronous manner, {@link org.javasimon.Stopwatch#sample()} (or {@link Stopwatch#sampleIncrement(Object)}
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
	 * Returns ms timestamp when the last peek of the active split count occurred.
	 *
	 * @return ms timestamp of the last peek of the active split count
	 */
	public final long getMaxActiveTimestamp() {
		return maxActiveTimestamp;
	}

	/**
	 * Sets the ms timestamp when the last peek of the active split count occurred.
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
	 * Sets the mean value (average) of all measured values.
	 *
	 * @param mean mean value
	 */
	public final void setMean(double mean) {
		this.mean = mean;
	}

	/**
	 * Returns unbiased estimate of standard deviation.
	 *
	 * @return unbiased estimate of standard deviation
	 */
	public final double getStandardDeviation() {
		return standardDeviation;
	}

	/**
	 * Sets unbiased estimate of standard deviation.
	 *
	 * @param standardDeviation unbiased estimate of standard deviation
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
		sb.append("StopwatchSample{");
		if (getName() != null) {
			sb.append("name=").append(getName()).append(", ");
		}
		sb.append("total=").append(SimonUtils.presentNanoTime(total));
		sb.append(", counter=").append(counter);
		sb.append(", max=").append(SimonUtils.presentNanoTime(max));
		sb.append(", min=").append(SimonUtils.presentNanoTime(min));
		sb.append(", maxTimestamp=").append(SimonUtils.presentTimestamp(maxTimestamp));
		sb.append(", minTimestamp=").append(SimonUtils.presentTimestamp(minTimestamp));
		sb.append(", active=").append(active);
		sb.append(", maxActive=").append(maxActive);
		sb.append(", maxActiveTimestamp=").append(SimonUtils.presentTimestamp(maxActiveTimestamp));
		sb.append(", last=").append(SimonUtils.presentNanoTime(last));
		sb.append(", mean=").append(SimonUtils.presentNanoTime((long) getMean()));
		sb.append(", standardDeviation=").append(SimonUtils.presentNanoTime((long) getStandardDeviation()));
		sb.append(", variance=").append(getVariance());
		sb.append(", varianceN=").append(getVarianceN());
		toStringCommon(sb);
		return sb.toString();
	}

	/** Equivalent to {@link org.javasimon.StopwatchImpl#toString()} without state. */
	public String simonToString() {
		return "Simon Stopwatch: total " + SimonUtils.presentNanoTime(total) +
			", counter " + counter +
			", max " + SimonUtils.presentNanoTime(max) +
			", min " + SimonUtils.presentNanoTime(min) +
			", mean " + SimonUtils.presentNanoTime((long) mean) +
			simonToStringCommon();
	}
}
