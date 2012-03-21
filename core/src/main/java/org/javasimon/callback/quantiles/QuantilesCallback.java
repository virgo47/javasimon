package org.javasimon.callback.quantiles;

import java.util.ArrayList;
import java.util.List;

import org.javasimon.Simon;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.callback.logging.LogTemplate;
import static org.javasimon.callback.logging.LogTemplates.*;
/**
 * Callback which stores data to compute quantiles.
 * Quantiles can only be obtained after warmup period, after which buckets are
 * initialized. Buckets are stored among simon attributes.
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
 * <li>Quantiles can be computed (provided there is enough splits and buckets are propertly configured)</li>
 * </ul>
 * </li></ol>
 * See {@see Buckets} for buckets configuration.
 * @author gquintana
 * @since 3.2.0
 */
public class QuantilesCallback extends CallbackSkeleton {
	/**
	 * Simon attribute name of the buckets stored
	 * in Simons after warmup time
	 */
	public static final String ATTR_NAME_BUCKETS = "buckets";

	/**
	 * Simon attribute name of the list of split values stored in Simons
	 * before warmup time
	 */
	public static final String ATTR_NAME_BUCKETS_VALUES = "bucketsValues";

	/**
	 * Number of splits before buckets are initialized
	 * Default 10
	 */
	private final long warmupCounter;

	/**
	 * Number of buckets of data for each Simon
	 */
	private final int bucketNb;

	/**
	 * Global flag indicating whether last splits should be logged once in
	 * a while
	 */
	private boolean logEnabled=false;
	/**
	 * SLF4J log template shared by all stopwatches
	 */
	private final LogTemplate<Split> enabledStopwatchLogTemplate=toSLF4J(getClass().getName(),"debug");
	/**
	 * Default constructor
	 */
	public QuantilesCallback() {
		this.warmupCounter = 10;
		this.bucketNb = 8;
	}

	/**
	 * Constructor
	 *
	 * @param warmupCounter
	 * @param bucketNb
	 */
	public QuantilesCallback(long warmupCounter, int bucketNb) {
		this.warmupCounter = warmupCounter;
		this.bucketNb = bucketNb;
	}

	/**
	 * Initialize the bucket values attribute
	 */
	private List<Long> initBucketsValues(final Stopwatch stopwatch) {
		List<Long> values = new ArrayList<Long>((int) warmupCounter);
		stopwatch.setAttribute(ATTR_NAME_BUCKETS_VALUES, values);
		return values;
	}

	/**
	 * Get the bucket values attribute
	 */
	private List<Long> getBucketsValues(final Stopwatch stopwatch) {
		return (List<Long>) stopwatch.getAttribute(ATTR_NAME_BUCKETS_VALUES);
	}

	/**
	 * Remove the bucket values attribute (after warmup)
	 */
	private void removeBucketsValues(final Stopwatch stopwatch) {
		stopwatch.removeAttribute(ATTR_NAME_BUCKETS_VALUES);
	}

	public boolean isLogEnabled() {
		return logEnabled;
	}

	public void setLogEnabled(boolean logEnabled) {
		this.logEnabled = logEnabled;
	}
	
	/**
	 * Create log template for given stopwatch.
	 * This method can be overriden to tune loging strategy.
	 * By default, when enabled, quantiles are logged at most once per minute
	 * @param stopwatch Stopwatch
	 * @return Logger
	 */
	protected LogTemplate<Split> createLogTemplate(Stopwatch stopwatch) {
		LogTemplate<Split> logTemplate;
		if (logEnabled) {
			logTemplate=everyNSeconds(enabledStopwatchLogTemplate, 60);
		} else {
			logTemplate=disabled();
		}
		return logTemplate;
	}
	
	/**
	 * Create the buckets after warmup time.
	 * Can be overriden to customize buckets configuration.
	 * By default buckets are create with:<ul>
	 * <li>Min: stopwatch min-10% rounded to 0</li>
	 * <li>Max: stopwatch max+10</li>
	 * <li>Nb buckets: {@link #bucketNb}
	 *
	 * @param stopwatch Stopwatch (containing configuration)
	 * @return new Buckets objects
	 */
	protected Buckets createBuckets(Stopwatch stopwatch) {
		if (stopwatch.getCounter()>warmupCounter) {
			long max=(stopwatch.getMax()*115L)/100L;
			long min=Math.max(0, (stopwatch.getMin())*90L/100L);
			Buckets buckets=new Buckets(min, max, bucketNb);
			buckets.setLogTemplate(createLogTemplate(stopwatch));
			return buckets;
		} else {
			return null;
		}
	}

	/**
	 * Initialize the buckets attribute
	 */
	private Buckets initBuckets(Stopwatch stopwatch) {
		Buckets buckets = createBuckets(stopwatch);
		if (buckets != null) {
			buckets.addValues(getBucketsValues(stopwatch));
			removeBucketsValues(stopwatch);
			stopwatch.setAttribute(ATTR_NAME_BUCKETS, buckets);
		}
		return buckets;
	}

	/**
	 * Get the buckets attribute
	 */
	private Buckets getBuckets(Stopwatch stopwatch) {
		return (Buckets) stopwatch.getAttribute(ATTR_NAME_BUCKETS);
	}

	/**
	 * When simon is create, en list containing Split values is added to
	 * stopwatch attributes
	 */
	@Override
	public void onSimonCreated(Simon simon) {
		if (simon instanceof Stopwatch) {
			Stopwatch stopwatch = (Stopwatch) simon;
			initBucketsValues(stopwatch);
		}
	}

	/**
	 * When a Splits is stopped, if buckets has been initialized value
	 * is added to appropriate bucket. Else if stopwatch is warming up
	 * value is added to value list.
	 */
	@Override
	public void onStopwatchStop(Split split) {
		final Stopwatch stopwatch = split.getStopwatch();
		Buckets buckets = getBuckets(stopwatch);
		if (buckets == null) {
			buckets = initBuckets(stopwatch);
		}
		long value = split.runningFor();
		if (buckets == null) {
			getBucketsValues(stopwatch).add(value);
		} else {
			buckets.addValue(value);
			buckets.log(split);
		}
	}


	/**
	 * When the Stopwatch is reseted, so are the buckets
	 */
	@Override
	public void onSimonReset(Simon simon) {
		if (simon instanceof Stopwatch) {
			Stopwatch stopwatch = (Stopwatch) simon;
			List<Long> values = getBucketsValues(stopwatch);
			if (values != null) {
				values.clear();
			}
			Buckets buckets = getBuckets(stopwatch);
			if (buckets != null) {
				buckets.clear();
			}
			initBucketsValues(stopwatch);
		}
	}
}
