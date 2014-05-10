package org.javasimon.callback.quantiles;

import org.javasimon.Simon;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.clock.ClockUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Callback which stores data in buckets to compute quantiles.
 * Quantiles can only be obtained after warmup period, after which buckets are
 * initialized.
 * For each Simon the following lifecycle occurs:<ol>
 * <li><em>Warm up</em>:<ul>
 * <li>Buckets do not exist</li>
 * <li>Quantiles can not be computed</li>
 * <li>Splits are kept</li>
 * </ul>
 * <li><em>Trigger</em>: splits count >= warmup count threshold<ul>
 * <li>Buckets are created, configuration (min, max) is determined from kept splits, bucket number is constant</li>
 * <li>Buckets are filled with previously stored splits</li>
 * <li>Retained splits are removed</li>
 * <li>From now on, quantiles can not be computed and splits are not kept anymore</li>
 * </ul>
 * <li><em>Normal</em>: <ul>
 * <li>Buckets are filled/updated with new splits as they come</li>
 * <li>Quantiles can be computed (provided there is enough splits and buckets are properly configured)</li>
 * </ul>
 * </li></ol>
 *
 * @author gquintana
 * @see Buckets
 * @since 3.2
 */
@SuppressWarnings("UnusedDeclaration")
public class AutoQuantilesCallback extends QuantilesCallback {

	/** Simon attribute name of the list of split values stored in Simons before warmup time. */
	public static final String ATTR_NAME_BUCKETS_VALUES = "bucketsValues";

	/**
	 * Number of splits before buckets are initialized.
	 * Default 10
	 */
	private final long warmupCounter;

	/** Number of buckets of data for each Simon. */
	private final int bucketNb;

	/** Default constructor. */
	public AutoQuantilesCallback() {
		this.warmupCounter = 10;
		this.bucketNb = 8;
	}

	/** Constructor with warmup counter and number of linear buckets for each Simon. */
	public AutoQuantilesCallback(long warmupCounter, int bucketNb) {
		this.warmupCounter = warmupCounter;
		this.bucketNb = bucketNb;
	}

	/**
	 * Constructor with all configuration.
	 *
	 * @param bucketsType Linear or exponential
	 * @param warmupCounter Number of splits before init
	 * @param bucketNb Bucket number
	 */
	public AutoQuantilesCallback(BucketsType bucketsType, long warmupCounter, int bucketNb) {
		super(bucketsType);
		this.warmupCounter = warmupCounter;
		this.bucketNb = bucketNb;
	}

	/** Get the bucket values attribute or create it if it does not exist. */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	private List<Long> getOrCreateBucketsValues(final Stopwatch stopwatch) {
		synchronized (stopwatch) {
			List<Long> values = getBucketsValues(stopwatch);
			if (values == null) {
				values = new ArrayList<>((int) warmupCounter);
				stopwatch.setAttribute(ATTR_NAME_BUCKETS_VALUES, values);
			}
			return values;
		}
	}

	/** Get the bucket values attribute. */
	@SuppressWarnings("unchecked")
	private List<Long> getBucketsValues(final Stopwatch stopwatch) {
		return (List<Long>) stopwatch.getAttribute(ATTR_NAME_BUCKETS_VALUES);
	}

	/** Remove the bucket values attribute (after warmup). */
	private void removeBucketsValues(final Stopwatch stopwatch) {
		stopwatch.removeAttribute(ATTR_NAME_BUCKETS_VALUES);
	}

	/**
	 * Create the buckets after warmup time.
	 * Can be overridden to customize buckets configuration.
	 * By default buckets are create with:<ul>
	 * <li>Min: stopwatch min-10% rounded to inferior millisecond</li>
	 * <li>Max: stopwatch max+10 rounded to superior millisecond</li>
	 * <li>Nb buckets: {@link #bucketNb}
	 *
	 * @param stopwatch Stopwatch (containing configuration)
	 * @return new Buckets objects
	 */
	protected Buckets createBucketsAfterWarmup(Stopwatch stopwatch) {
		// Compute min
		long min = stopwatch.getMin() * 90L / 100L; // min -10%
		min = Math.max(0, min); // no negative mins
		min = (min / ClockUtils.NANOS_IN_MILLIS) * ClockUtils.NANOS_IN_MILLIS; // round to lower millisecond
		// Compute max
		long max = (stopwatch.getMax() * 110L) / 100L; // max +10%
		max = (max / ClockUtils.NANOS_IN_MILLIS + 1) * ClockUtils.NANOS_IN_MILLIS; // round to upper millisecond
		return createBuckets(stopwatch, min, max, bucketNb);
	}

	/** When warmup ends, buckets are create and retained splits are sorted in the buckets. */
	protected final Buckets createBuckets(Stopwatch stopwatch) {
		if (stopwatch.getCounter() > warmupCounter) {
			Buckets buckets = createBucketsAfterWarmup(stopwatch);
			// Add retained splits to buckets
			buckets.addValues(getBucketsValues(stopwatch));
			removeBucketsValues(stopwatch);
			return buckets;
		} else {
			return null;
		}
	}

	/** When simon is created, the list containing Split values is added to stopwatch attributes. */
	@Override
	public void onSimonCreated(Simon simon) {
		if (simon instanceof Stopwatch) {
			Stopwatch stopwatch = (Stopwatch) simon;
			getOrCreateBucketsValues(stopwatch);
		}
	}

	/**
	 * Called when there is a new split on a Stopwatch, either
	 * {@link #onStopwatchStop} or {@link #onStopwatchAdd}.
	 * If buckets have been initialized, the value is added to appropriate bucket.
	 * Else if stopwatch is warming up value is added to value list.
	 */
	@Override
	protected void onStopwatchSplit(Stopwatch stopwatch, Split split) {
		Buckets buckets = getOrCreateBuckets(stopwatch);
		long value = split.runningFor();
		if (buckets == null) {
			// Warming up
			getOrCreateBucketsValues(stopwatch).add(value);
		} else {
			// Warm
			buckets.addValue(value);
			buckets.log(split);
		}
	}
}
