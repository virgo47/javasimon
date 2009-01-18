package org.javasimon;

/**
 * CounterSample.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Jan 7, 2009
 */
public final class CounterSample extends AbstractSample {
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
	public long getCounter() {
		return counter;
	}

	/**
	 * Sets the value of the counter.
	 *
	 * @param counter value of the counter
	 */
	public void setCounter(long counter) {
		this.counter = counter;
	}

	/**
	 * Returns minimal value of counter.
	 *
	 * @return maximal reached value
	 */
	public long getMin() {
		return min;
	}

	/**
	 * Sets the minimal value of the counter.
	 *
	 * @param min the minimal value of the counter.
	 */
	public void setMin(long min) {
		this.min = min;
	}

	/**
	 * Returns maximal value of counter.
	 *
	 * @return maximal reached value
	 */
	public long getMax() {
		return max;
	}

	/**
	 * Sets the maximal value of the counter.
	 *
	 * @param max the maximal value of the counter.
	 */
	public void setMax(long max) {
		this.max = max;
	}

	/**
	 * Returns ms timestamp when the min value was reached.
	 *
	 * @return ms timestamp of the min value decremented
	 */
	public long getMinTimestamp() {
		return minTimestamp;
	}

	/**
	 * Sets ms timestamp when the min value was reached.
	 *
	 * @param minTimestamp ms timestamp when the min value was reached
	 */
	public void setMinTimestamp(long minTimestamp) {
		this.minTimestamp = minTimestamp;
	}

	/**
	 * Returns ms timestamp when the max value was reached.
	 *
	 * @return ms timestamp of the max value incremented
	 */
	public long getMaxTimestamp() {
		return maxTimestamp;
	}

	/**
	 * Sets ms timestamp when the max value was reached.
	 *
	 * @param maxTimestamp ms timestamp when the max value was reached
	 */
	public void setMaxTimestamp(long maxTimestamp) {
		this.maxTimestamp = maxTimestamp;
	}

	/**
	 * Returns the sum of all incremented values. If incremented value was negative, sum
	 * is lowered by this value.
	 *
	 * @return sum of all incremented values
	 */
	public long getIncrementSum() {
		return incrementSum;
	}

	/**
	 * Sets the sum of all incremented values.
	 *
	 * @param incrementSum sum of all incremented values
	 */
	public void setIncrementSum(long incrementSum) {
		this.incrementSum = incrementSum;
	}

	/**
	 * Returns the sum of all decremented values (as a positive number). If decremented value was negative, sum
	 * is lowered by this value.
	 *
	 * @return sum of all decremented values
	 */
	public long getDecrementSum() {
		return decrementSum;
	}

	/**
	 * Sets the sum of all decremented values.
	 *
	 * @param decrementSum sum of all decremented values
	 */
	public void setDecrementSum(long decrementSum) {
		this.decrementSum = decrementSum;
	}
}
