package org.javasimon.callback.quantiles;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.javasimon.Split;
import org.javasimon.callback.logging.LogMessageSource;
import org.javasimon.callback.logging.LogTemplate;
import static org.javasimon.callback.logging.LogTemplates.disabled;
import static org.javasimon.utils.SimonUtils.presentNanoTime;

/**
 * List of buckets and quantiles computer.
 * For 100-600 range and 5 bucket count, the following buckets are created:
 * <table>
 * <tr>
 * <th>Index</th>
 * <th>Min</th><th>Max</th>
 * <th>Samples</th>
 * <th>Counter</th>
 * </tr>
 * <tr>
 * <td>0</td>
 * <td>-&infin;</td><td>100</td>
 * <td>53</td>
 * <td># (1)</td>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>100</td><td>200</td>
 * <td>128,136</td>
 * <td>## (2)</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>200</td><td>300</td>
 * <td>245,231,264,287,275</td>
 * <td>###### (5)</td>
 * </tr>
 * <tr>
 * <td>3</td>
 * <td>300</td><td>400</td>
 * <td>356,341</td>
 * <td>## (2)</td>
 * </tr>
 * <tr>
 * <td>4</td>
 * <td>400</td><td>500</td>
 * <td>461</td>
 * <td># (1)</td>
 * </tr>
 * <tr>
 * <td>5</td>
 * <td>500</td><td>600</td>
 * <td>801</td>
 * <td># (1)</td>
 * </tr>
 * <tr>
 * <td>6</td>
 * <td>600</td><td>+&infin;</td>
 * <td></td>
 * <td>(0)</td>
 * </tr>
 * </table>
 * For a total of 12 splits in this example, we can deduce that
 * <ul><li>Median (6th sample) is in bucket #2
 * <li>Third quartile (9th sample) is in bucket #3</li>
 * <li>90% percentile (10,8th sample) is in bucket #4 or #5 (but assume #4).</li>
 * </ul>
 * Samples are not kept in buckets only the counter indicates their presence.
 * <br/>
 * Some details impact quantiles computation precision:
 * <ul><li><em>Not enough samples</em>: The more samples you have, the more precise interpolation are</li>
 * <li><em>Not enough buckets</em>: the more buckets are used, the more regular the distribution is and the more memory you'll need as well!</li>
 * <li><em>All samples in one bucket</em>: samples should be evenly distributed on buckets. If all samples go into the same bucket, you should consider changing the min/max/number settings</li>
 * </ul>
 *
 * @author gquintana
 * @since 3.2.0
 */
public class Buckets implements LogMessageSource<Split> {
	/**
	 * Array of buckets, sorted by ranges.
	 * The first and last buckets are special:
	 * The first bucket is range -infinity to min,
	 * The last bucket is range max to +infinity.
	 * Other buckets are regular ones with constant width
	 */
	private final Bucket[] buckets;

	/**
	 * Number of real buckets (=buckets.length-2)
	 */
	private final int bucketNb;

	/**
	 * Lower bound of all buckets
	 */
	private final long min;

	/**
	 * Upper bound of all buckets
	 */
	private final long max;

	/**
	 * Width of all buckets
	 */
	private final long width;

	/**
	 * Log template used to log quantiles
	 */
	private LogTemplate<Split> logTemplate=disabled();
	/**
	 * Constructor, initializes buckets
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
			throw new IllegalArgumentException("Expected min>max: " + min + "/" + max);
		}
		// Initialize attributes
		this.buckets = new Bucket[bucketNb + 2];
		this.width = (max - min) / bucketNb;
		this.min = min;
		this.max = max;
		this.bucketNb = bucketNb;
		// Initialize buckets with their bounds
		this.buckets[0] = new Bucket(Long.MIN_VALUE, min);
		long currentMin = min;
		long currentMax;
		for (int i = 1; i <= bucketNb; i++) {
			currentMax = currentMin + width;
			buckets[i] = new Bucket(currentMin, currentMax);
			currentMin = currentMax;
		}
		buckets[bucketNb + 1] = new Bucket(max, Long.MAX_VALUE);
	}

	/**
	 * Compute expected count and check used buckets number
	 */
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
	 * Compute given quantile
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
			if (expectedCount >= lastCount && expectedCount <= newCount) {
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
		// Linear interpolation of value
		final Bucket bucket = buckets[bucketIndex];
		return bucket.getMin() + (expectedCount - lastCount) * (bucket.getMax() - bucket.getMin()) / bucket.getCount();
	}

	/**
	 * Search bucket where value should be sorted, the bucket whose
	 * min/max bounds are around the value.
	 *
	 * @param value Value
	 * @return Bucket
	 */
	private Bucket getBucketForValue(long value) {
		Bucket bucket;
		if (value < min) {
			bucket = buckets[0];
		} else {
			if (value > max) {
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

	/**
	 * Search the appropriate bucket and add the value in it
	 *
	 * @param value Value
	 */
	public void addValue(long value) {
		synchronized (buckets) {
			getBucketForValue(value).incrementCount();
		}
	}

	/**
	 * For each value, search the appropriate bucket and add the value in it.
	 *
	 * @param values Values
	 */
	public void addValues(Collection<Long> values) {
		synchronized (buckets) {
			for (Long value : values) {
				addValue(value);
			}
		}
	}

	/**
	 * Compute quantile.
	 *
	 * @param ratio Nth quantile, 0.5 is median. Expects values between 0 and 1.
	 * @return Quantile
	 */
	public double getQuantile(double ratio) {
		synchronized (buckets) {
			int totalCount = checkAndGetTotalCount();
			return computeQuantile(ratio, totalCount);
		}
	}

	/**
	 * Compute median
	 *
	 * @return Median
	 */
	public double getMedian() {
		return getQuantile(0.5D);
	}

	/**
	 * Computes first (=0.25), second (=median=0.5) and third (=0.75) quartiles
	 *
	 * @return
	 */
	public Double[] getQuartiles() {
		return getQuantiles(0.25D, 0.50D, 0.75D);
	}

	/**
	 * Compute many quantiles.
	 *
	 * @param ratios Nth quantiles, 0.5 is median. Expects values between 0 and 1.
	 * @return Quantiles or null, if computation failed
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
	
	/**
	 * String containing: min/max/number configuration and 50%, 75% and 90% quantiles
	 * if available.
	 * Warning this method can be expensive as it's doing some computation.
	 *
	 * @return
	 */
	@Override
	public String toString() {
		Double[] quantiles = getQuantiles(0.50D, 0.75D, 0.90D);
		StringBuilder stringBuilder = new StringBuilder("Buckets[");
		stringBuilder.append("min=").append(presentNanoTime(min))
			.append(",max=").append(presentNanoTime(max))
			.append(",nb=").append(bucketNb)
			.append(",width=").append(presentNanoTime(width))
			.append("] Quantiles[");
		
		if (quantiles[0] != null) {
			stringBuilder.append("median=").append(presentNanoTime(quantiles[0]));
		}
		if (quantiles[1] != null) {
			stringBuilder.append(",75%=").append(presentNanoTime(quantiles[1]));
		}
		if (quantiles[2] != null) {
			stringBuilder.append(",90%=").append(presentNanoTime(quantiles[2]));
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	/**
	 * Clear all buckets
	 */
	public void clear() {
		synchronized (buckets) {
			for (Bucket bucket : buckets) {
				bucket.clear();
			}
		}
	}

	/**
	 * Get the bucket list
	 *
	 * @return Buckets
	 */
	public List<Bucket> getBuckets() {
		return Collections.unmodifiableList(Arrays.asList(buckets));
	}
	
	/**
	 * Transform buckets and quantiles into a loggable message
	 */
	public String getLogMessage(Split lastSplit) {
		return lastSplit.getStopwatch().getName()+" "+toString();
	}
	/**
	 * Log eventually buckets config and quantiles
	 */
	public void log(Split lastSplit) {
		logTemplate.log(lastSplit, this);
	}
}
