package org.javasimon.callback.quantiles;

import static org.javasimon.callback.logging.LogTemplates.disabled;
import static org.javasimon.callback.logging.LogTemplates.everyNSeconds;
import static org.javasimon.callback.logging.LogTemplates.toSLF4J;

import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.callback.logging.LogTemplate;

/**
 * Callback which stores data in buckets to compute quantiles.
 * The {@link #createBuckets(org.javasimon.Stopwatch)} should be
 * implemented to configure the width and resolution of buckets.
 * Then {@link Buckets} are stored among Simon attributes.
 * There are 2 implementations:
 * <ul>
 * <li>{@link AutoQuantilesCallback} tries to determine the best configuration for each Stopwatch.</li>
 * <li>{@link FixedQuantilesCallback} uses a fixed configuration for all Stopwatches.</li>
 * </ul>
 *
 * @author gquintana
 * @see Buckets
 * @since 3.2
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class QuantilesCallback extends CallbackSkeleton {

	/** Simon attribute name of the buckets stored in Simons after warmup time. */
	public static final String ATTR_NAME_BUCKETS = "buckets";

	/** SLF4J log template shared by all stopwatches. */
	private final LogTemplate<Split> enabledStopwatchLogTemplate = toSLF4J(getClass().getName(), "debug");

	/** Global flag indicating whether last splits should be logged once in a while. */
	private boolean logEnabled = false;
	/** Type of the buckets: linear or exponential. */
	private BucketsType bucketsType;

	/** Default constructor. */
	protected QuantilesCallback() {
		bucketsType = BucketsType.LINEAR;
	}

	/**
	 * Constructor with buckets type.
	 *
	 * @param bucketsType Type of buckets
	 */
	protected QuantilesCallback(BucketsType bucketsType) {
		this.bucketsType = bucketsType;
	}

	/**
	 * Returns buckets type.
	 *
	 * @return Buckets type
	 */
	public BucketsType getBucketsType() {
		return bucketsType;
	}

	public boolean isLogEnabled() {
		return logEnabled;
	}

	public void setLogEnabled(boolean logEnabled) {
		this.logEnabled = logEnabled;
	}

	/**
	 * Create log template for given stopwatch.
	 * This method can be overridden to tune logging strategy.
	 * By default, when enabled, quantiles are logged at most once per minute
	 *
	 * @param stopwatch Stopwatch
	 * @return Logger
	 */
	@SuppressWarnings("UnusedParameters")
	protected LogTemplate<Split> createLogTemplate(Stopwatch stopwatch) {
		LogTemplate<Split> logTemplate;
		if (logEnabled) {
			logTemplate = everyNSeconds(enabledStopwatchLogTemplate, 60);
		} else {
			logTemplate = disabled();
		}
		return logTemplate;
	}

	/** Returns the buckets attribute. */
	public static Buckets getBuckets(Stopwatch stopwatch) {
		return (Buckets) stopwatch.getAttribute(ATTR_NAME_BUCKETS);
	}

	/**
	 * Factory method to create a Buckets object using given configuration.
	 *
	 * @param stopwatch Target Stopwatch
	 * @param min Min bound
	 * @param max Max bound
	 * @param bucketNb Number of buckets between min and max
	 * @return Buckets
	 */
	protected final Buckets createBuckets(Stopwatch stopwatch, long min, long max, int bucketNb) {
		Buckets buckets = bucketsType.createBuckets(stopwatch, min, max, bucketNb);
		buckets.setLogTemplate(createLogTemplate(stopwatch));
		return buckets;
	}

	/**
	 * Create Buckets for given stopwatch.
	 * Call {@link #createBuckets(org.javasimon.Stopwatch, long, long, int)} to create a new buckets object.
	 *
	 * @param stopwatch Stopwatch
	 * @return Buckets
	 */
	protected abstract Buckets createBuckets(Stopwatch stopwatch);

	/** Returns the buckets attribute or create it if it does not exist. */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	protected final Buckets getOrCreateBuckets(Stopwatch stopwatch) {
		synchronized (stopwatch) {
			Buckets buckets = getBuckets(stopwatch);
			if (buckets == null) {
				buckets = createBuckets(stopwatch);
				stopwatch.setAttribute(ATTR_NAME_BUCKETS, buckets);
			}
			return buckets;
		}
	}

	/** Returns the buckets attribute and sample them. */
	public static BucketsSample sampleBuckets(Stopwatch stopwatch) {
		final Buckets buckets = getBuckets(stopwatch);
		return buckets == null ? null : buckets.sample();
	}

	/**
	 * Called when there is a new split on a Stopwatch, either
	 * {@link #onStopwatchStop} or {@link #onStopwatchAdd}.
	 * If buckets have been initialized, the value is added to appropriate bucket.
	 */
	protected void onStopwatchSplit(Stopwatch stopwatch, Split split) {
		Buckets buckets = getOrCreateBuckets(stopwatch);
		if (buckets != null) {
			buckets.addValue(split.runningFor());
			buckets.log(split);
		}
	}

	/**
	 * When a split is stopped, if buckets have been initialized, the value
	 * is added to appropriate bucket.
	 */
	@Override
	public void onStopwatchStop(Split split, StopwatchSample sample) {
		onStopwatchSplit(split.getStopwatch(), split);
	}

	/** When a split is added, if buckets have been initialized, the value is added to appropriate bucket. */
	@Override
	public void onStopwatchAdd(Stopwatch stopwatch, Split split, StopwatchSample sample) {
		onStopwatchSplit(split.getStopwatch(), split);
	}
}
