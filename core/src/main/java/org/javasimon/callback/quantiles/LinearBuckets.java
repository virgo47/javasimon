package org.javasimon.callback.quantiles;

/**
 * Linearly organized {@link Buckets}.
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
 *
 * @author Gérald Quintana
 * @author Alexej Vlasov
 */
public class LinearBuckets extends Buckets {
	/**
	 * Constructor
	 *
	 * @param min Duration min (lower bound of all buckets)
	 * @param max Duration max (upper bound of all buckets)
	 * @param bucketNb Number of buckets between min and max
	 */
	public LinearBuckets(long min, long max, int bucketNb) {
		super(min, max, bucketNb);
		long width = (max - min) / bucketNb;
		long currentMin, currentMax = min;
		for (int i = 1; i <= bucketNb; i++) {
			currentMin = currentMax;
			currentMax = currentMin + width;
			buckets[i] = new Bucket(currentMin, currentMax);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Override the base method making it generally faster thanks to linear regression.
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
				if (value > bucket.getMax()) {
					bucketIndex++;
					bucket = buckets[bucketIndex];
				}
			}
		}
		return bucket;
	}
}
