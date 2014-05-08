package org.javasimon.callback.quantiles;

import org.javasimon.Stopwatch;
import org.javasimon.clock.ClockUtils;

/**
 * Callback which stores data in buckets to compute quantiles.
 * Buckets are create using constant configuration.
 *
 * @author gquintana
 */
public class FixedQuantilesCallback extends QuantilesCallback {

	/** Buckets lower bound in milliseconds. */
	private final long min;
	/** Buckets upper bound in milliseconds. */
	private final long max;
	/** Number of buckets. */
	private final int bucketNb;

	/** Main constructor. */
	public FixedQuantilesCallback(long min, long max, int bucketNb) {
		this.min = min;
		this.max = max;
		this.bucketNb = bucketNb;
	}

	/**
	 * Constructor with all configuration
	 *
	 * @param bucketsType Linear or exponential
	 * @param min Min
	 * @param max Max
	 * @param bucketNb Number of buckets
	 */
	public FixedQuantilesCallback(BucketsType bucketsType, long min, long max, int bucketNb) {
		super(bucketsType);
		this.min = min;
		this.max = max;
		this.bucketNb = bucketNb;
	}

	/**
	 * Create buckets using callback attributes
	 *
	 * @param stopwatch Target stopwatch
	 * @return Created buckets
	 */
	@Override
	protected Buckets createBuckets(Stopwatch stopwatch) {
		return createBuckets(stopwatch, min * ClockUtils.NANOS_IN_MILLIS, max * ClockUtils.NANOS_IN_MILLIS, bucketNb);
	}

	public long getMin() {
		return min;
	}

	public long getMax() {
		return max;
	}

	public int getBucketNb() {
		return bucketNb;
	}
}
