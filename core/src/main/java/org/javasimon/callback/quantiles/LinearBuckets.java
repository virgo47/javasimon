package org.javasimon.callback.quantiles;

/**
 * @author Alexej Vlasov
 */
public class LinearBuckets extends Buckets {

	public LinearBuckets(long min, long max, int bucketNb) {
		super(min, max, bucketNb);
	}

	protected void makeRealBuckets() {
		long currentMin = min;
		long currentMax;
		long width = (max - min) / bucketNb;
		for (int i = 1; i <= bucketNb; i++) {
			currentMax = currentMin + width;
			buckets[i] = new Bucket(currentMin, currentMax);
			currentMin = currentMax;
		}
	}

	/**
	 * override default implementation. It should be faster in most cases.
	 */
	protected Bucket getBucketForValue(long value) {
		Bucket bucket;
		if (value < min) {
			bucket = buckets[0];
		} else {
			if (value >= max) {
				bucket = buckets[bucketNb + 1];
			} else {
				// Linear interpolation
				int bucketIndex = 1 + (int) ((value - min) * (bucketNb - 1) / (max - min));
				bucket = buckets[bucketIndex];
				// As the division above was round at floor, bucket may be the next one
				if (!bucket.contains(value)) {
					bucketIndex++;
					bucket = buckets[bucketIndex];
				}
			}
		}
		return bucket;
	}
}
