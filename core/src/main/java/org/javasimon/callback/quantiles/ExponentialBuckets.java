package org.javasimon.callback.quantiles;

/**
 * Exponentially organized {@link Buckets}.
 *
 * @author Alexej Vlasov
 */
public class ExponentialBuckets extends Buckets {
	/**
	 * Power between buckets.
	 */
	private final double power;

	/**
	 * Logarithm of the lower bound.
	 */
	private final double logMin;

	/**
	 * Constructor.
	 *
	 * @param min Duration min (lower bound of all buckets)
	 * @param max Duration max (upper bound of all buckets)
	 * @param bucketNb Number of buckets between min and max
	 */
	public ExponentialBuckets(long min, long max, int bucketNb) {
		super(min, max, bucketNb);
		logMin = Math.log(this.min);
		power = (Math.log(this.max) - logMin) / bucketNb;
		long currentMin, currentMax = this.min;
		for (int i = 1; i <= bucketNb; i++) {
			currentMin = currentMax;
			currentMax = Math.round(Math.exp(power * i)) * this.min;
			buckets[i] = new Bucket(currentMin, currentMax);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Override the base method making it faster thanks to exponential regression.
	 */
	protected Bucket getBucketForValue(long value) {
		Bucket bucket;
		if (value >= max) {
			bucket = buckets[bucketNb + 1];
		} else {
			if (value < min) {
				bucket = buckets[0];
			} else {
				int idx = (int) Math.floor((Math.log(value) - logMin) / power) + 1;
				bucket = buckets[idx];
				if (value >= bucket.getMax()) {
					bucket = buckets[idx + 1];
				}
			}
		}
		return bucket;
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Used during quantiles computation to do exponential regression over one bucket.
	 */
	protected double estimateQuantile(Bucket bucket, double expectedCount, double lastCount) {
		return bucket.getMin() + (bucket.getMax() - bucket.getMin()) * Math.exp(Math.log(expectedCount - lastCount) / Math.log(bucket.getCount()));
	}
}
