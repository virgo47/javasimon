package org.javasimon.callback.quantiles;

/**
 * @author Alexej Vlasov
 */
public class ExponentialBuckets extends Buckets {

	private double power;
	private double logMin;

	public ExponentialBuckets(long min, long max, int bucketNb) {
		super(min, max, bucketNb);
	}

	@Override
	protected void makeRealBuckets() {
		logMin = Math.log(this.min);
		power = (Math.log(this.max) - logMin) / bucketNb;
		long start = this.min;
		long end = Math.round(Math.exp(power)) * this.min;
		for (int i = 1; i <= bucketNb; i++) {
			buckets[i] = new Bucket(start, end);
			start = end;
			end = Math.round(Math.exp(power * (i + 1)) * this.min);
		}
	}

	/**
	 * Returns bucket where value should be sorted, the bucket whose min/max bounds are around the value.
	 * <p/>
	 * Override base method. It should be faster in most cases
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

	protected double estimateQuantile(Bucket bucket, double expectedCount, double lastCount) {
		return bucket.getMin() + (bucket.getMax() - bucket.getMin()) * Math.exp(Math.log(expectedCount - lastCount) / Math.log(bucket.getCount()));
	}
}
