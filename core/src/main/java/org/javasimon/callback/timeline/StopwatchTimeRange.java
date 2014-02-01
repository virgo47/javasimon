package org.javasimon.callback.timeline;

import org.javasimon.utils.SimonUtils;

/**
 * Stopwatch statistics gathered for a specific time range.
 * This class is not intended to be thread safe.
 *
 * @author gerald
 */
public class StopwatchTimeRange extends TimeRange {

	/** Last value. */
	private long last;

	/** Minimum value. */
	private long min = Long.MAX_VALUE;

	/** Maximum value. */
	private long max = Long.MIN_VALUE;

	/** Total sum of all values. */
	private long total;

	/** Sum of squares. */
	private long squareTotal;

	/** Counter - number of values. */
	private long counter;

	/** Main constructor. */
	public StopwatchTimeRange(long startTimestamp, long endTimestamp) {
		super(startTimestamp, endTimestamp);
	}

	/**
	 * Add stopwatch split information.
	 *
	 * @param timestampInMs when the split started, expressed in milliseconds
	 * @param durationInNs how long the split was, expressed in nanoseconds
	 */
	public void addSplit(long timestampInMs, long durationInNs) {
		last = durationInNs;
		total += durationInNs;
		squareTotal += durationInNs * durationInNs;
		if (durationInNs > max) {
			max = durationInNs;
		}
		if (durationInNs < min) {
			min = durationInNs;
		}
		counter++;
		lastTimestamp = timestampInMs;
	}

	public long getLast() {
		return last;
	}

	public long getMin() {
		return min;
	}

	public long getMax() {
		return max;
	}

	public long getTotal() {
		return total;
	}

	public long getCounter() {
		return counter;
	}

	/** Computes mean (average) using total and non-zero counter. */
	private double computeMean() {
		return ((double) total) / counter;
	}

	/**
	 * Compute mean (average) duration.
	 *
	 * @return mean (average) duration.
	 */
	public Double getMean() {
		return counter == 0 ? Double.NaN : computeMean();
	}

	/**
	 * Compute variance.
	 *
	 * @return variance
	 */
	public Double getVariance() {
		if (counter == 0) {
			return Double.NaN;
		} else {
			final double mean = computeMean();
			final double meanSquare = mean * mean;
			final double squareMean = ((double) squareTotal) / counter;
			return squareMean - meanSquare;
		}
	}

	/**
	 * Computes standard deviation.
	 *
	 * @return standard deviation
	 */
	public Double getStandardDeviation() {
		return Math.sqrt(getVariance());
	}

	@Override
	protected StringBuilder toStringBuilder(StringBuilder stringBuilder) {
		return super.toStringBuilder(stringBuilder)
			.append(" counter=").append(counter)
			.append(" total=").append(SimonUtils.presentNanoTime(total))
			.append(" min=").append(SimonUtils.presentNanoTime(min))
			.append(" mean=").append(SimonUtils.presentNanoTime(getMean()))
			.append(" last=").append(SimonUtils.presentNanoTime(getLast()))
			.append(" max=").append(SimonUtils.presentNanoTime(max))
			.append(" stddev=").append(SimonUtils.presentNanoTime(getStandardDeviation()));
	}
}
