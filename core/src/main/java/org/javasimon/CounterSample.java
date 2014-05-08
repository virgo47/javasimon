package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * Object holds all relevant data from {@link Counter} Simon. Whenever it is important to get more values
 * in a synchronous manner, {@link org.javasimon.Counter#sample()} (or {@link Stopwatch#sampleIncrement(Object)}
 * should be used to obtain this Java Bean object.
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
	 * Returns the total sum of increments and decrements as a formatted string (+inc/-dec).
	 *
	 * @return total sum of increments and decrements as string
	 */
	public final String getTotalAsString() {
		return "+" + SimonUtils.presentMinMaxCount(incrementSum) + "/-" + SimonUtils.presentMinMaxCount(decrementSum);
	}

	/**
	 * Returns readable representation of object.
	 *
	 * @return string with readable representation of object
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("CounterSample{");
		if (getName() != null) {
			sb.append("name=").append(getName()).append(", ");
		}
		sb.append("counter=").append(counter);
		sb.append(", max=").append(SimonUtils.presentMinMaxCount(max));
		sb.append(", min=").append(SimonUtils.presentMinMaxCount(min));
		sb.append(", maxTimestamp=").append(SimonUtils.presentTimestamp(maxTimestamp));
		sb.append(", minTimestamp=").append(SimonUtils.presentTimestamp(minTimestamp));
		sb.append(", incrementSum=").append(incrementSum);
		sb.append(", decrementSum=").append(decrementSum);
		toStringCommon(sb);
		return sb.toString();
	}

	/** Equivalent to {@link org.javasimon.CounterImpl#toString()} without state. */
	public synchronized String simonToString() {
		return "Simon Counter: counter=" + counter +
			", max=" + SimonUtils.presentMinMaxCount(max) +
			", min=" + SimonUtils.presentMinMaxCount(min) +
			simonToStringCommon();
	}
}
