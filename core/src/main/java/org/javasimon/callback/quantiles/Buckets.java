package org.javasimon.callback.quantiles;

import static org.javasimon.callback.logging.LogTemplates.disabled;
import static org.javasimon.utils.SimonUtils.presentNanoTime;

import org.javasimon.Split;
import org.javasimon.callback.logging.LogMessageSource;
import org.javasimon.callback.logging.LogTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * List of buckets and quantiles computer.
 * Samples are not kept in buckets only the counter indicates their presence.
 * <br/>
 * Some details impact quantiles computation precision:
 * <ul><li><em>Not enough samples</em>: The more samples you have, the more precise interpolation are</li>
 * <li><em>Not enough buckets</em>: the more buckets are used, the more regular the distribution is and the more memory you'll need as well!</li>
 * <li><em>All samples in one bucket</em>: samples should be evenly distributed on buckets. If all samples go into the same bucket, you should consider changing the min/max/number settings</li>
 * </ul>
 *
 * @author Gerald Quintana
 * @since 3.2
 */
public abstract class Buckets implements LogMessageSource<Split> {

	/**
	 * Array of buckets, sorted by ranges.
	 * The first and last buckets are special:
	 * The first bucket is range -infinity to min,
	 * The last bucket is range max to +infinity.
	 * Other buckets are regular ones with constant width
	 */
	protected final Bucket[] buckets;

	/** Number of real buckets (=buckets.length-2). */
	protected final int bucketNb;
	/** Lower bound of all real buckets. */
	protected final long min;
	/** Upper bound of all real buckets. */
	protected final long max;
	/** Log template used to log quantiles. */
	private LogTemplate<Split> logTemplate = disabled();

	/**
	 * Constructor, initializes buckets.
	 *
	 * @param min Min of all values
	 * @param max Max of all values
	 * @param bucketNb Number of buckets
	 */
	public Buckets(long min, long max, int bucketNb) {
		// Check arguments
		if (bucketNb < 3) {
			throw new IllegalArgumentException("Expected at least 3 buckets: " + bucketNb);
		}
		if (min >= max) {
			throw new IllegalArgumentException("Expected min<max: " + min + "/" + max);
		}
		// Initialize attributes
		this.min = min;
		this.max = max;
		this.bucketNb = bucketNb;
		// Initialize bucket array
		this.buckets = new Bucket[bucketNb + 2];
		buckets[0] = new Bucket(Long.MIN_VALUE, min);
		buckets[bucketNb + 1] = new Bucket(max, Long.MAX_VALUE);
	}

	/** Computes expected count and check used buckets number. */
	private int checkAndGetTotalCount() throws IllegalStateException {
		int usedBuckets = 0;
		int totalCount = buckets[0].getCount();
		for (int i = 1; i <= bucketNb; i++) {
			int bucketCount = buckets[i].getCount();
			totalCount += bucketCount;
			if (bucketCount > 0) {
				usedBuckets++;
			}
		}
		totalCount += buckets[bucketNb + 1].getCount();
		if (usedBuckets < 3) {
			throw new IllegalStateException("Only " + usedBuckets + " buckets used, not enough for interpolation, consider reconfiguring min/max/nb");
		}
		return totalCount;
	}

	/**
	 * Computes given quantile.
	 *
	 * @param ration Nth quantile: 0.5 is median
	 * @param totalCount Total count over all buckets
	 * @return Quantile
	 * @throws IllegalStateException Buckets are poorly configured and
	 * quantile can not be computed
	 * @throws IllegalArgumentException
	 */
	private double computeQuantile(double ration, int totalCount) throws IllegalStateException, IllegalArgumentException {
		if (ration <= 0.0D || ration >= 1.0D) {
			throw new IllegalArgumentException("Expected ratio between 0 and 1 excluded: " + ration);
		}
		final double expectedCount = ration * totalCount;
		// Search bucket corresponding to expected count
		double lastCount = 0D, newCount;
		int bucketIndex = 0;
		for (int i = 0; i < buckets.length; i++) {
			newCount = lastCount + buckets[i].getCount();
			if (expectedCount >= lastCount && expectedCount < newCount) {
				bucketIndex = i;
				break;
			}
			lastCount = newCount;
		}
		// Check that bucket index is in bounds
		if (bucketIndex == 0) {
			throw new IllegalStateException("Quantile out of bounds: decrease min");
		} else if (bucketIndex == bucketNb + 1) {
			throw new IllegalStateException("Quantile out of bounds: increase max");
		}
		// Interpolation of value
		final Bucket bucket = buckets[bucketIndex];
		return estimateQuantile(bucket, expectedCount, lastCount);
	}

	/**
	 * Interpolate quantile located in given Bucket using linear regression.
	 * <ul>
	 * <li>Quantile is between {@link Bucket#min} and {@link Bucket#max}</li>
	 * <li>Expected count is between last count and last count+{@link Bucket#count}</li>
	 * </ul>
	 *
	 * @param bucket Current bucket containing the quantile
	 * @param expectedCount Searched value
	 * @param lastCount Value of the bucket lower bound
	 * @return Compute quantile
	 */
	protected double estimateQuantile(Bucket bucket, double expectedCount, double lastCount) {
		return bucket.getMin() + (expectedCount - lastCount) * (bucket.getMax() - bucket.getMin()) / bucket.getCount();
	}

	/**
	 * Get the bucket containing the given value.
	 * Bucket should be sorted, the bucket whose min/max bounds are around the value is returned.
	 *
	 * @param value Value
	 * @return Bucket containing given value
	 */
	protected Bucket getBucketForValue(long value) {
		for (Bucket bucket : buckets) {
			if (bucket.contains(value)) {
				return bucket;
			}
		}
		throw new IllegalStateException("Non continuous buckets.");
	}

	/** Searches the appropriate bucket and add the value in it. */
	public void addValue(long value) {
		synchronized (buckets) {
			getBucketForValue(value).incrementCount();
		}
	}

	/** For each value, search the appropriate bucket and add the value in it. */
	public void addValues(Collection<Long> values) {
		synchronized (buckets) {
			for (Long value : values) {
				addValue(value);
			}
		}
	}

	/**
	 * Computes quantile.
	 *
	 * @param ratio Nth quantile, 0.5 is median. Expects values between 0 and 1.
	 * @return quantile
	 */
	public double getQuantile(double ratio) {
		synchronized (buckets) {
			int totalCount = checkAndGetTotalCount();
			return computeQuantile(ratio, totalCount);
		}
	}

	/**
	 * Computes median.
	 *
	 * @return Median
	 */
	public double getMedian() {
		return getQuantile(0.5D);
	}

	/** Computes first (=0.25), second (=median=0.5) and third (=0.75) quartiles. */
	public Double[] getQuartiles() {
		return getQuantiles(0.25D, 0.50D, 0.75D);
	}

	/**
	 * Computes many quantiles.
	 *
	 * @param ratios Nth quantiles, 0.5 is median. Expects values between 0 and 1.
	 * @return quantiles or {@code null}, if computation failed
	 */
	@SuppressWarnings("EmptyCatchBlock")
	public Double[] getQuantiles(double... ratios) {
		synchronized (buckets) {
			final Double[] quantiles = new Double[ratios.length];
			try {
				final int totalCount = checkAndGetTotalCount();
				for (int i = 0; i < ratios.length; i++) {
					try {
						quantiles[i] = computeQuantile(ratios[i], totalCount);
					} catch (IllegalStateException e) {
					}
				}
			} catch (IllegalStateException e) {
			}
			return quantiles;
		}
	}

	public LogTemplate<Split> getLogTemplate() {
		return logTemplate;
	}

	public void setLogTemplate(LogTemplate<Split> logTemplate) {
		this.logTemplate = logTemplate;
	}

	/** Sample buckets and quantiles state. */
	public BucketsSample sample() {
		synchronized (buckets) {
			BucketSample[] bucketSamples = new BucketSample[buckets.length];
			for (int i = 0; i < buckets.length; i++) {
				bucketSamples[i] = buckets[i].sample();
			}
			Double[] quantiles = getQuantiles(0.50D, 0.90D);
			return new BucketsSample(bucketSamples, quantiles[0], quantiles[1]);
		}
	}

	/**
	 * String containing: min/max/number configuration and 50%, 75% and 90% quantiles if available.
	 * Warning this method can be expensive as it is performing computation.
	 */
	@Override
	public String toString() {
		return toString(false);
	}

	private String toString(boolean bars) {
		StringBuilder stringBuilder = new StringBuilder("Buckets[");
		stringBuilder.append("min=").append(presentNanoTime(min))
			.append(",max=").append(presentNanoTime(max))
			.append(",nb=").append(bucketNb)
//			.append(",width=").append(presentNanoTime(width)) // i don't know how important this information in that String.
			.append("] Quantiles[");
		final String eol = System.getProperty("line.separator");
		final String eoc = "\t";
		BucketsSample bucketsSample = sample();
		if (bucketsSample.getMedian() != null) {
			stringBuilder.append("median=").append(presentNanoTime(bucketsSample.getMedian()));
		}
		if (bucketsSample.getPercentile90() != null) {
			stringBuilder.append(",90%=").append(presentNanoTime(bucketsSample.getPercentile90()));
		}
		stringBuilder.append("]");
		if (bars) {
			stringBuilder.append(eol);
			int maxCount = 0;
			final int barMax = 10;
			for (BucketSample bucketSample : bucketsSample.getBuckets()) {
				maxCount = Math.max(maxCount, bucketSample.getCount());
			}
			for (BucketSample bucketSample : bucketsSample.getBuckets()) {
				if (bucketSample.getMin() != Long.MIN_VALUE) {
					stringBuilder.append(presentNanoTime(bucketSample.getMin()));
				}
				stringBuilder.append(eoc);
				if (bucketSample.getMax() != Long.MAX_VALUE) {
					stringBuilder.append(presentNanoTime(bucketSample.getMax()));
				}
				stringBuilder.append(eoc)
					.append(bucketSample.getCount()).append(eoc);
				if (maxCount > 0) {
					final int barSize = bucketSample.getCount() * barMax / maxCount;
					for (int i = 0; i < barSize; i++) {
						stringBuilder.append('#');
					}
				}
				stringBuilder.append(eol);
			}
		}
		return stringBuilder.toString();
	}

	/** Clears all buckets. */
	public void clear() {
		synchronized (buckets) {
			for (Bucket bucket : buckets) {
				bucket.clear();
			}
		}
	}

	/**
	 * Returns the bucket list.
	 *
	 * @return list of buckets
	 */
	public List<Bucket> getBuckets() {
		return Collections.unmodifiableList(Arrays.asList(buckets));
	}

	/** Transforms buckets and quantiles into a loggable message. */
	public String getLogMessage(Split lastSplit) {
		return lastSplit.getStopwatch().getName() + " " + toString(true);
	}

	/** Logs eventually buckets config and quantiles. */
	public void log(Split lastSplit) {
		logTemplate.log(lastSplit, this);
	}

	public int getBucketNb() {
		return bucketNb;
	}

	public long getMin() {
		return min;
	}

	public long getMax() {
		return max;
	}
}
