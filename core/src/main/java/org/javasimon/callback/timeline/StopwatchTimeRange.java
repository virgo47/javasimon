package org.javasimon.callback.timeline;

import org.javasimon.utils.SimonUtils;

/**
 * Stopwatch statistics gathered for a specific time range.
 * This class is not intended to be thread safe.
 *
 * @author gerald
 */
public class StopwatchTimeRange extends TimeRange {
	/**
	 * Last value
	 */
	private long last;
	/**
	 * Minimum
	 */
	private long min = Long.MAX_VALUE;
	/**
	 * Minimum
	 */
	private long max = Long.MIN_VALUE;
	/**
	 * Total (Sum of all values)
	 */
	private long total;
	/**
	 * (Sum of square of all values)
	 */
	private long squareTotal;
	/**
	 * Counter (Number of values)
	 */
	private long counter;

	/**
	 * Main constructor.
	 */
	public StopwatchTimeRange(long startTimestamp, long endTimestamp) {
		super(startTimestamp, endTimestamp);
	}

	/**
	 * Add stopwatch split information.
	 *
	 * @param timestampInMs When the split started, expressed in milliseconds
	 * @param durationInNs How long the split was, expressed in nanoseconds
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

	/**
	 * Compute mean/average using total and counter
	 */
	private double computeMean() {
		return ((double) total) / ((double) counter);
	}

	/**
	 * Compute mean/average.
	 *
	 * @return mean/average duration.
	 */
	public Double getMean() {
		return counter == 0 ? null : computeMean();
	}

	/**
	 * Compute variance.
	 *
	 * @return variance
	 */
	public Double getVariance() {
		if (counter == 0) {
			return null;
		} else {
			final double mean = computeMean();
			final double meanSquare = mean * mean;
			final double squareMean = ((double) squareTotal) / ((double) counter);
			return squareMean - meanSquare;
		}
	}

	/**
	 * Compute standard deviation.
	 *
	 * @return Standard deviation
	 */
	public Double getStandardDeviation() {
		final Double variance = getVariance();
		return variance == null ? null : Math.sqrt(variance);
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
