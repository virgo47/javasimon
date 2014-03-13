package org.javasimon.callback.quantiles;

/**
 * {@link Bucket} sample.
 *
 * @author gquintana
 * @since 3.3
 */
public final class BucketSample {

	/** Minimal value. */
	private final long min;
	/** Maximal value. */
	private final long max;
	/** Number of values in the range min-max. */
	private final int count;

	/**
	 * Constructor with min/max value specified.
	 *
	 * @param min min value
	 * @param max max value
	 */
	public BucketSample(long min, long max, int count) {
		this.min = min;
		this.max = max;
		this.count = count;
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
}
