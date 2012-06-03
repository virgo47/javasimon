package org.javasimon.callback.quantiles;

/**
 *
 * @author gquintana
 */
public class BucketsSample {
	private final BucketSample[] buckets;
	private final Double median;
	private final Double percentile90;

	public BucketsSample(BucketSample[] buckets, Double median, Double quantile90) {
		this.buckets = buckets;
		this.median = median;
		this.percentile90 = quantile90;
	}

	public BucketSample[] getBuckets() {
		return buckets;
	}

	public Double getMedian() {
		return median;
	}

	public Double getPercentile90() {
		return percentile90;
	}
}