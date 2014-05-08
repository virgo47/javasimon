package org.javasimon.utils;

import org.javasimon.CounterSample;

/**
 * Object that holds aggregate values from all counters in hierarchy.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 * @since 3.5
 */
public class CounterAggregate {

	private long counter;
	private long incrementSum;
	private long decrementSum;
	private long max = Long.MIN_VALUE;
	private long min = Long.MAX_VALUE;
	private long maxTimestamp;
	private long minTimestamp;

	CounterAggregate() {
	}

	/**
	 * Returns sum of all counters in a hierarchy of simons.
	 *
	 * @return sum of all counters
	 */
	public long getCounter() {
		return counter;
	}

	/**
	 * Returns sum of all increment sums in a hierarchy of simons.
	 *
	 * @return sum of all increment sums
	 */
	public long getIncrementSum() {
		return incrementSum;
	}

	/**
	 * Returns sum of all decrement sums in a hierarchy of simons.
	 *
	 * @return sum of all decrement sums
	 */
	public long getDecrementSum() {
		return decrementSum;
	}

	/**
	 * Returns max value among max values in a hierarchy of simons.
	 *
	 * @return max value among max values
	 */
	public long getMax() {
		return max;
	}

	/**
	 * Returns timestamp in milliseconds when max value was reached.
	 *
	 * @return timestamp in milliseconds when max value was reached
	 */
	public long getMaxTimestamp() {
		return maxTimestamp;
	}

	/**
	 * Returns min value among min values in a hierarchy of simons.
	 *
	 * @return min value among min values
	 */
	public long getMin() {
		return min;
	}

	/**
	 * Returns timestamp in milliseconds when min value was reached.
	 *
	 * @return timestamp in milliseconds when min value was reached
	 */
	public long getMinTimestamp() {
		return minTimestamp;
	}

	void addSample(CounterSample sample) {
		counter += sample.getCounter();
		incrementSum += sample.getIncrementSum();
		decrementSum += sample.getDecrementSum();

		if (min > sample.getMin()) {
			min = sample.getMin();
			minTimestamp = sample.getMinTimestamp();
		}

		if (max < sample.getMax()) {
			max = sample.getMax();
			maxTimestamp = sample.getMaxTimestamp();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CounterAggregate that = (CounterAggregate) o;

		if (counter != that.counter) return false;
		if (decrementSum != that.decrementSum) return false;
		if (incrementSum != that.incrementSum) return false;
		if (max != that.max) return false;
		if (maxTimestamp != that.maxTimestamp) return false;
		if (min != that.min) return false;
		if (minTimestamp != that.minTimestamp) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = (int) (counter ^ (counter >>> 32));
		result = 31 * result + (int) (incrementSum ^ (incrementSum >>> 32));
		result = 31 * result + (int) (decrementSum ^ (decrementSum >>> 32));
		result = 31 * result + (int) (max ^ (max >>> 32));
		result = 31 * result + (int) (min ^ (min >>> 32));
		result = 31 * result + (int) (maxTimestamp ^ (maxTimestamp >>> 32));
		result = 31 * result + (int) (minTimestamp ^ (minTimestamp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "CounterAggregate{" +
			"counter=" + counter +
			", incrementSum=" + incrementSum +
			", decrementSum=" + decrementSum +
			", max=" + SimonUtils.presentMinMaxCount(max) +
			", min=" + SimonUtils.presentMinMaxCount(min) +
			", maxTimestamp=" + SimonUtils.presentTimestamp(maxTimestamp) +
			", minTimestamp=" + SimonUtils.presentTimestamp(minTimestamp) +
			'}';
	}
}
