package org.javasimon.callback.quantiles;

/**
 * Bucket count the number of samples in the range min-max.
 *
 * @author gquintana
 * @since 3.2
 */
public final class Bucket {

	/** Minimal value. */
	private final long min;
	/** Maximal value. */
	private final long max;
	/** Number of values in the range min-max. */
	private int count;

	/**
	 * Constructor with min/max value specified.
	 *
	 * @param min min value
	 * @param max max value
	 */
	public Bucket(long min, long max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * Get number of values in the range.
	 *
	 * @return number of value in the range
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Get upper bound of the range.
	 *
	 * @return max value
	 */
	public long getMax() {
		return max;
	}

	/**
	 * Get lower bound of the range.
	 *
	 * @return min value
	 */
	public long getMin() {
		return min;
	}

	/**
	 * Check whether value is in the range.
	 *
	 * @param value Value
	 * @return true if in range
	 */
	public boolean contains(long value) {
		return (value >= min) && (value <= max);
	}

	/**
	 * Increment value number
	 */
	public void incrementCount() {
		count++;
	}

	/**
	 * Check if value is in range and increment value number.
	 *
	 * @param value added value
	 * @return true if value is in bucket range (count was increased)
	 */
	public boolean addValue(long value) {
		if (contains(value)) {
			incrementCount();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Resets value number.
	 */
	public void clear() {
		count = 0;
	}

	/**
	 * Get sample from this bucket
	 *
	 * @return Sample
	 */
	public BucketSample sample() {
		return new BucketSample(min, max, count);
	}
}
