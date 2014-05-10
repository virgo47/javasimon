package org.javasimon.callback.lastsplits;

import static org.javasimon.callback.logging.LogTemplates.disabled;
import static org.javasimon.utils.SimonUtils.presentNanoTime;

import org.javasimon.Split;
import org.javasimon.callback.logging.LogMessageSource;
import org.javasimon.callback.logging.LogTemplate;

/**
 * Object stored among Stopwatch's attributes in charge of <ul>
 * <li>Managing concurrent access to the inner ring buffer through synchronized blocks</li>
 * <li>Computing som statistics (min, max, mean, trend) based on retained values</li>
 * <li>Log retained values and statistics
 * </ul>
 *
 * @author gquintana
 * @since 3.2
 */
public class LastSplits implements LogMessageSource<Split> {
	/** Ring buffer containing splits. */
	private final CircularList<Split> splits;

	/** Log template used to log this list of splits. */
	private LogTemplate<Split> logTemplate = disabled();

	/**
	 * Constructor with ring buffer size.
	 *
	 * @param capacity Buffer size
	 */
	public LastSplits(int capacity) {
		this.splits = new CircularList<>(capacity);
	}

	/**
	 * Adds split to the buffer.
	 *
	 * @param split Split
	 */
	public void add(Split split) {
		synchronized (splits) {
			splits.add(split);
		}
	}

	/** Removes all splits from buffer. */
	public void clear() {
		synchronized (splits) {
			splits.clear();
		}
	}

	public LogTemplate<Split> getLogTemplate() {
		return logTemplate;
	}

	public void setLogTemplate(LogTemplate<Split> logTemplate) {
		this.logTemplate = logTemplate;
	}

	/**
	 * Gets number of splits in the buffer.
	 *
	 * @return Split number
	 */
	public int getCount() {
		synchronized (splits) {
			return splits.size();
		}
	}

	/**
	 * Evaluate a function over the list of splits.
	 *
	 * @param <T> Function result type
	 * @param function Function to evaluate
	 * @return Function result, null if no splits
	 */
	private <T> T processFunction(SplitFunction<T> function) {
		synchronized (splits) {
			if (splits.isEmpty()) {
				return null;
			}
			for (Split split : splits) {
				function.evaluate(split);
			}
			return function.result();
		}
	}

	/**
	 * Function.
	 *
	 * @param <T> Result type
	 */
	private static interface SplitFunction<T> {
		/**
		 * Called for each split.
		 *
		 * @param split Current split
		 */
		void evaluate(Split split);

		/**
		 * Called after all splits.
		 *
		 * @return Function result
		 */
		T result();
	}

	/**
	 * Base implementation of functions.
	 *
	 * @param <T> Function return type
	 */
	private static abstract class AbstractSplitFunction<T> implements SplitFunction<T> {
		/** Function result. */
		protected T result;

		/** Initial function result. */
		public AbstractSplitFunction(T result) {
			this.result = result;
		}

		/**
		 * Running for duration of the split.
		 *
		 * @param runningFor Running for
		 */
		public abstract void evaluate(long runningFor);

		/**
		 * Calls evaluate with split running for duration.
		 *
		 * @param split Current split
		 */
		public final void evaluate(Split split) {
			evaluate(split.runningFor());
		}

		/** Final result. */
		public T result() {
			return result;
		}
	}

	/**
	 * Compute mean duration of splits in the buffer
	 *
	 * @return Mean or average
	 */
	public Double getMean() {
		return processFunction(new AbstractSplitFunction<Double>(0.0D) {
			@Override
			public void evaluate(long runningFor) {
				result += (double) runningFor;
			}

			@Override
			public Double result() {
				return result / (double) splits.size();
			}

		});
	}

	/**
	 * Compute the smallest duration of splits in the buffer
	 *
	 * @return Minimum
	 */
	public Long getMin() {
		return processFunction(new AbstractSplitFunction<Long>(Long.MAX_VALUE) {
			@Override
			public void evaluate(long runningFor) {
				if (runningFor < result) {
					result = runningFor;
				}
			}
		});
	}

	/**
	 * Compute the longest duration of splits in the buffer
	 *
	 * @return Maximum
	 */
	public Long getMax() {
		return processFunction(new AbstractSplitFunction<Long>(Long.MIN_VALUE) {
			@Override
			public void evaluate(long runningFor) {
				if (runningFor > result) {
					result = runningFor;
				}
			}
		});
	}

	/**
	 * Compute a trend of duration: the average delta of splits between
	 * 2 splits spaced of at least 1 ms.
	 * Sum(splits(t[n])-splits(t[n-1])/SizeOf(splits)
	 *
	 * @return Trend, average delta of splits
	 */
	public Double getTrend() {
		return getTrend(1000);
	}

	/**
	 * Compute a trend of duration: the average delta of splits between
	 * 2 split spaced of at least the given threshold.
	 * The threshold is only here to avoid computing a delta between 2 splits
	 * occurring at the same time by 2 different threads.
	 * Sum(splits(t[n])-splits(t[n-1])/SizeOf(splits)
	 *
	 * @param timeDeltaThreshold Accepted splits space
	 * @return Trend, average delta of splits
	 */
	public Double getTrend(final long timeDeltaThreshold) {
		return processFunction(new SplitFunction<Double>() {
			Split lastSplit;
			long result;
			int count;

			public void evaluate(Split split) {
				if (lastSplit == null) {
					lastSplit = split;
				} else {
					long timeDelta = split.getStart() - lastSplit.getStart();
					if (timeDelta > timeDeltaThreshold) {
						long durationDelta = split.runningFor() - lastSplit.runningFor();
						result += durationDelta;
						count++;
						lastSplit = split;
					}
				}
			}

			public Double result() {
				return count > 0 ? (result / ((double) count)) : null;
			}
		});
	}

	/**
	 * Transforms split values into a String
	 *
	 * @return Splits presented in a String
	 */
	private String getSplitsAsString() {
		return processFunction(new AbstractSplitFunction<StringBuilder>(new StringBuilder()) {
			private boolean first = true;

			@Override
			public void evaluate(long runningFor) {
				if (first) {
					first = false;
				} else {
					result.append(',');
				}
				result.append(presentNanoTime(runningFor));
			}
		}).toString();
	}

	/**
	 * String containing: count, min, mean, max and trend(1ms).
	 * This method can be expensive, because many computations are done.
	 *
	 * @return String
	 */
	@Override
	public String toString() {
		int count;
		long min = 0, mean = 0, max = 0, trend = 0;
		String values = null;
		// First extract data
		synchronized (splits) {
			count = getCount();
			if (count > 0) {
				min = getMin();
				mean = getMean().longValue();
				max = getMax();
				values = getSplitsAsString();
				if (count > 1) {
					trend = getTrend().longValue();
				}
			}
		}
		// Then free lock, and format data
		StringBuilder stringBuilder = new StringBuilder("LastSplits[size=");
		stringBuilder.append(count);
		if (count > 0) {
			stringBuilder.append(",values=[").append(values).append("]")
				.append(",min=").append(presentNanoTime(min))
				.append(",mean=").append(presentNanoTime(mean))
				.append(",max=").append(presentNanoTime(max));
			if (count > 1) {
				stringBuilder.append(",trend=").append(presentNanoTime(trend));
			}
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	/**
	 * Transforms this list of splits into a loggable message.
	 */
	public String getLogMessage(Split lastSplit) {
		return lastSplit.getStopwatch().getName() + " " + toString();
	}

	/**
	 * Log eventually this list of splits into log template
	 */
	public void log(Split lastSplit) {
		logTemplate.log(lastSplit, this);
	}
}
