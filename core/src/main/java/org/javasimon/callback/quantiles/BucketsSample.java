package org.javasimon.callback.quantiles;

/**
 * Snapshot of {@link Buckets}
 *
 * @author gquintana
 */
public class BucketsSample {
	private final BucketSample[] buckets;
	/**
	 * Median (50% percentile)
	 */
	private final Double median;
	/**
	 * 90% percentile
	 */
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

	private boolean hasNoBuckets() {
		return buckets == null || buckets.length == 0;
	}

	public Integer getMaxCount() {
		Integer maxCount;
		if (hasNoBuckets()) {
			return null;
		} else {
			maxCount = 0;
			for (BucketSample bucket : buckets) {
				maxCount = Math.max(bucket.getCount(), maxCount);
			}
		}
		return maxCount;
	}

	public Integer getTotalCount() {
		Integer totalCount;
		if (hasNoBuckets()) {
			return null;
		} else {
			totalCount = 0;
			for (BucketSample bucket : buckets) {
				totalCount += bucket.getCount();
			}
		}
		return totalCount;
	}
}