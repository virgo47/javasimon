package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * CounterSample.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public class CounterSample extends Sample {
	private long counter;
	private long min;
	private long max;
	private long minTimestamp;
	private long maxTimestamp;
	private long incrementSum;
	private long decrementSum;

	/**
	 * Returns the value of the counter.
	 *
	 * @return counter value
	 */
	public final long getCounter() {
		return counter;
	}

	/**
	 * Sets the value of the counter.
	 *
	 * @param counter value of the counter
	 */
	public final void setCounter(long counter) {
		this.counter = counter;
	}

	/**
	 * Returns minimal value of counter.
	 *
	 * @return maximal reached value
	 */
	public final long getMin() {
		return min;
	}

	/**
	 * Sets the minimal value of the counter.
	 *
	 * @param min the minimal value of the counter.
	 */
	public final void setMin(long min) {
		this.min = min;
	}

	/**
	 * Returns maximal value of counter.
	 *
	 * @return maximal reached value
	 */
	public final long getMax() {
		return max;
	}

	/**
	 * Sets the maximal value of the counter.
	 *
	 * @param max the maximal value of the counter.
	 */
	public final void setMax(long max) {
		this.max = max;
	}

	/**
	 * Returns ms timestamp when the min value was reached.
	 *
	 * @return ms timestamp of the min value decremented
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
	 * Sets ms timestamp when the min value was reached.
	 *
	 * @param minTimestamp ms timestamp when the min value was reached
	 */
	public final void setMinTimestamp(long minTimestamp) {
		this.minTimestamp = minTimestamp;
	}

	/**
	 * Returns ms timestamp when the max value was reached.
	 *
	 * @return ms timestamp of the max value incremented
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
	 * Sets ms timestamp when the max value was reached.
	 *
	 * @param maxTimestamp ms timestamp when the max value was reached
	 */
	public final void setMaxTimestamp(long maxTimestamp) {
		this.maxTimestamp = maxTimestamp;
	}

	/**
	 * Returns the sum of all incremented values. If incremented value was negative, sum
	 * is lowered by this value.
	 *
	 * @return sum of all incremented values
	 */
	public final long getIncrementSum() {
		return incrementSum;
	}

	/**
	 * Sets the sum of all incremented values.
	 *
	 * @param incrementSum sum of all incremented values
	 */
	public final void setIncrementSum(long incrementSum) {
		this.incrementSum = incrementSum;
	}

	/**
	 * Returns the sum of all decremented values (as a positive number). If decremented value was negative, sum
	 * is lowered by this value.
	 *
	 * @return sum of all decremented values
	 */
	public final long getDecrementSum() {
		return decrementSum;
	}

	/**
	 * Sets the sum of all decremented values.
	 *
	 * @param decrementSum sum of all decremented values
	 */
	public final void setDecrementSum(long decrementSum) {
		this.decrementSum = decrementSum;
	}

	/**
	 * Returns readable representation of object.
	 *
	 * @return string with readable representation of object
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("CounterSample");
		sb.append("{counter=").append(counter);
		sb.append(", min=").append(SimonUtils.presentMinMaxCount(min));
		sb.append(", max=").append(SimonUtils.presentMinMaxCount(max));
		sb.append(", minTimestamp=").append(SimonUtils.presentTimestamp(minTimestamp));
		sb.append(", maxTimestamp=").append(SimonUtils.presentTimestamp(maxTimestamp));
		sb.append(", incrementSum=").append(incrementSum);
		sb.append(", decrementSum=").append(decrementSum);
		sb.append(", note=").append(getNote());
		sb.append(", firstUsage=").append(getFirstUsage());
		sb.append(", lastUsage=").append(getLastUsage());
		sb.append(", lastReset=").append(getLastReset());
		sb.append("}");
		return sb.toString();
	}
}
