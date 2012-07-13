package org.javasimon.callback.timeline;

import org.javasimon.Split;
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
	 */
	public synchronized void addSplit(long timestampInMs, long durationInNs) {
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

	/* TODO: review, but I'd drop them
	 * Add stopwatch split.
	 *
	 * @param split Split
	void addSplit(Split split, long timestampMs) {
		addSplit(timestampMs, split.runningFor());
	}

	 * TODO: this is broken (I fixed it in org.javasimon.callback.timeline.StopwatchTimeline.addSplit already), nanos can't be converted to timestamp this way
	 * Add stopwatch split.
	 *
	 * @param split Split
	public void addSplit(Split split) {
		addSplit(split.getStart() / SimonUtils.NANOS_IN_MILLIS, split.runningFor());
	}
	 */

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

	private double computeMean() {
		return ((double) total) / ((double) counter);
	}

	public Double getMean() {
		return counter == 0 ? null : computeMean();
	}

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

	public Double getStandardDeviation() {
		final Double variance = getVariance();
		return variance == null ? null : Math.sqrt(variance);
	}

	@Override
	protected StringBuilder toStringBuilder(StringBuilder stringBuilder) {
		return super.toStringBuilder(stringBuilder)
			.append(" counter=").append(counter)
			.append(" min=").append(SimonUtils.presentNanoTime(min))
			.append(" mean=").append(SimonUtils.presentNanoTime(getMean()))
			.append(" last=").append(SimonUtils.presentNanoTime(getLast()))
			.append(" max=").append(SimonUtils.presentNanoTime(max))
			.append(" stddev=").append(SimonUtils.presentNanoTime(getStandardDeviation()));
	}

}
