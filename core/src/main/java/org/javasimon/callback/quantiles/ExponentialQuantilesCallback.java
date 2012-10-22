package org.javasimon.callback.quantiles;

import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;

/**
 * @author Alexej Vlasov
 */
public class ExponentialQuantilesCallback extends QuantilesCallback {

	private final long min;

	/**
	 * Buckets upper bound in milliseconds.
	 */
	private final long max;

	/**
	 * Number of buckets.
	 */
	private final int bucketNb;

	public ExponentialQuantilesCallback(long min, long max, int bucketNb) {
		this.max = max;
		this.min = min;
		this.bucketNb = bucketNb;
	}

	/**
	 * Create buckets using callback attributes.
	 */
	@Override
	protected Buckets createBuckets(Stopwatch stopwatch) {
		return new ExponentialBuckets(min * SimonUtils.NANOS_IN_MILLIS, max * SimonUtils.NANOS_IN_MILLIS, bucketNb);
	}
}
