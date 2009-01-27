package org.javasimon;

/**
 * Object holds all relevant data from Stopwatch Simon. Whenever it is important to get more values
 * in a synchronous manner, {@link org.javasimon.Stopwatch#sample()} (or {@link Stopwatch#sampleAndReset()}
 * should be used to obtain this Java Bean object.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Jan 7, 2009
 */
public class StopwatchSample extends AbstractSample {
	private long total;
	private long counter;
	private long min;
	private long max;
	private long minTimestamp;
	private long maxTimestamp;
	private long active;
	private long maxActive;
	private long maxActiveTimestamp;

	/**
	 * Returns the total sum of all split times in nanoseconds.
	 *
	 * @return total time of the stopwatch in nanoseconds
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * Sets the total sum of all split times in nanoseconds.
	 *
	 * @param total total time of the stopwatch in nanoseconds
	 */
	public void setTotal(long total) {
		this.total = total;
	}

	/**
	 * Returns usage count of the stopwatch. Counter is increased by {@code addTime} and
	 * {@code stop} - that means that it's updated every time the next time split is added.
	 *
	 * @return count of time splits
	 */
	public long getCounter() {
		return counter;
	}

	/**
	 * Sets the usage count of the stopwatch.
	 *
	 * @param counter count of time splits
	 */
	public void setCounter(long counter) {
		this.counter = counter;
	}

	/**
	 * Returns minimal time split value in nanoseconds.
	 *
	 * @return minimal time split in nanoseconds
	 */
	public long getMin() {
		return min;
	}

	/**
	 * Sets the minimal time split value in nanoseconds.
	 *
	 * @param min minimal time split in nanoseconds
	 */
	public void setMin(long min) {
		this.min = min;
	}

	/**
	 * Returns maximal time split value in nanoseconds.
	 *
	 * @return maximal time split in nanoseconds
	 */
	public long getMax() {
		return max;
	}

	/**
	 * Sets the maximal time split value in nanoseconds.
	 *
	 * @param max maximal time split in nanoseconds
	 */
	public void setMax(long max) {
		this.max = max;
	}

	/**
	 * Returns ms timestamp when the min value was measured.
	 *
	 * @return ms timestamp of the min value measurement
	 */
	public long getMinTimestamp() {
		return minTimestamp;
	}

	/**
	 * Sets the ms timestamp when the min value was measured.
	 *
	 * @param minTimestamp ms timestamp of the min value measurement
	 */
	public void setMinTimestamp(long minTimestamp) {
		this.minTimestamp = minTimestamp;
	}

	/**
	 * Returns ms timestamp when the max value was measured.
	 *
	 * @return ms timestamp of the max value measurement
	 */
	public long getMaxTimestamp() {
		return maxTimestamp;
	}

	/**
	 * Sets the ms timestamp when the max value was measured.
	 *
	 * @param maxTimestamp ms timestamp of the max value measurement
	 */
	public void setMaxTimestamp(long maxTimestamp) {
		this.maxTimestamp = maxTimestamp;
	}

	/**
	 * Returns current number of measured splits (concurrently running).
	 *
	 * @return current number of active splits
	 */
	public long getActive() {
		return active;
	}

	/**
	 * Sets the current number of measured splits (concurrently running).
	 *
	 * @param active current number of active splits
	 */
	public void setActive(long active) {
		this.active = active;
	}

	/**
	 * Returns peek value of active concurrent splits.
	 *
	 * @return maximum reached value of active splits
	 */
	public long getMaxActive() {
		return maxActive;
	}

	/**
	 * Sets the peek value of active concurrent splits.
	 *
	 * @param maxActive maximum reached value of active splits
	 */
	public void setMaxActive(long maxActive) {
		this.maxActive = maxActive;
	}

	/**
	 * Retruns ms timestamp when the last peek of the active split count occured.
	 *
	 * @return ms timestamp of the last peek of the active split count
	 */
	public long getMaxActiveTimestamp() {
		return maxActiveTimestamp;
	}

	/**
	 * Sets the ms timestamp when the last peek of the active split count occured.
	 *
	 * @param maxActiveTimestamp ms timestamp of the last peek of the active split count
	 */
	public void setMaxActiveTimestamp(long maxActiveTimestamp) {
		this.maxActiveTimestamp = maxActiveTimestamp;
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
		sb.append("{total=").append(total);
		sb.append(", counter=").append(counter);
		sb.append(", min=").append(min);
		sb.append(", max=").append(max);
		sb.append(", minTimestamp=").append(minTimestamp);
		sb.append(", maxTimestamp=").append(maxTimestamp);
		sb.append(", active=").append(active);
		sb.append(", maxActive=").append(maxActive);
		sb.append(", maxActiveTimestamp=").append(maxActiveTimestamp);
		sb.append(" [count=").append(getCount());
		sb.append(", mean=").append(getMean());
		sb.append(", standardDeviation=").append(getStandardDeviation());
		sb.append(", sum=").append(getSum());
		sb.append(", variance=").append(getVariance());
		sb.append(", varianceN=").append(getVarianceN());
		sb.append("]}");
		return sb.toString();
	}
}
