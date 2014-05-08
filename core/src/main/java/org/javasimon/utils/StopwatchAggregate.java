package org.javasimon.utils;

import org.javasimon.StopwatchSample;

/**
 * Object that holds aggregate values from all stopwatches in hierarchy.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 * @since 3.5
 */
public class StopwatchAggregate {

	private long total;
	private long counter;
	private long min = Long.MAX_VALUE;
	private long max;
	private long minTimestamp;
	private long maxTimestamp;
	private long active;
	private long maxActive;
	private long maxActiveTimestamp;

	StopwatchAggregate() {
	}

	/**
	 * Returns total sum of all splits of all stopwatches in hierarchy in milliseconds.
	 *
	 * @return total time of all stopwatches in nanoseconds
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * Returns total sum of usage counts of all stopwatches in hierarchy in milliseconds.
	 *
	 * @return total sum of usage counts of all stopwatches
	 */
	public long getCounter() {
		return counter;
	}

	/**
	 * Returns min time split among all stopwatches in hierarchy in milliseconds.
	 *
	 * @return min time split among all stopwatches
	 */
	public long getMin() {
		return min;
	}

	/**
	 * Returns max time split among all stopwatches in hierarchy in milliseconds
	 *
	 * @return max time split among all stopwatches
	 */
	public long getMax() {
		return max;
	}

	/**
	 * Returns timestamp in milliseconds when min value was measured among all stopwatches.
	 *
	 * @return timestamp with min value was measured in milliseconds
	 */
	public long getMinTimestamp() {
		return minTimestamp;
	}

	/**
	 * Returns timestamp in milliseconds when max value was measured among all stopwatches.
	 *
	 * @return timestamp with max value was measured in milliseconds
	 */
	public long getMaxTimestamp() {
		return maxTimestamp;
	}

	/**
	 * Returns total number of active splits in all stopwatches in hierarchy.
	 *
	 * @return total number of active splits
	 */
	public long getActive() {
		return active;
	}

	/**
	 * Returns peek value of active concurrent splits among all stopwatches in hierarchy.
	 *
	 * @return peek value of active concurrent splits.
	 */
	public long getMaxActive() {
		return maxActive;
	}

	/**
	 * Returns timestamp in millisecond when the last peek of active split counter occurred among all stopwatches in hierarchy.
	 *
	 * @return timestamp im milliseconds of the last peek of the active split counter
	 */
	public long getMaxActiveTimestamp() {
		return maxActiveTimestamp;
	}

	/**
	 * Add stopwatch sample to current statistics aggregate.
	 *
	 * @param sample - stopwatch sample that will be added to statistics aggregate
	 */
	void addSample(StopwatchSample sample) {
		total += sample.getTotal();
		counter += sample.getCounter();

		if (min > sample.getMin()) {
			min = sample.getMin();
			minTimestamp = sample.getMinTimestamp();
		}

		if (max < sample.getMax()) {
			max = sample.getMax();
			maxTimestamp = sample.getMaxTimestamp();
		}

		active += sample.getActive();

		if (maxActive < sample.getMaxActive()) {
			maxActive = sample.getMaxActive();
			maxActiveTimestamp = sample.getMaxActiveTimestamp();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		StopwatchAggregate aggregate = (StopwatchAggregate) o;

		if (active != aggregate.active) return false;
		if (counter != aggregate.counter) return false;
		if (max != aggregate.max) return false;
		if (maxActive != aggregate.maxActive) return false;
		if (maxActiveTimestamp != aggregate.maxActiveTimestamp) return false;
		if (maxTimestamp != aggregate.maxTimestamp) return false;
		if (min != aggregate.min) return false;
		if (minTimestamp != aggregate.minTimestamp) return false;
		if (total != aggregate.total) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = (int) (total ^ (total >>> 32));
		result = 31 * result + (int) (counter ^ (counter >>> 32));
		result = 31 * result + (int) (min ^ (min >>> 32));
		result = 31 * result + (int) (max ^ (max >>> 32));
		result = 31 * result + (int) (minTimestamp ^ (minTimestamp >>> 32));
		result = 31 * result + (int) (maxTimestamp ^ (maxTimestamp >>> 32));
		result = 31 * result + (int) (active ^ (active >>> 32));
		result = 31 * result + (int) (maxActive ^ (maxActive >>> 32));
		result = 31 * result + (int) (maxActiveTimestamp ^ (maxActiveTimestamp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "StopwatchAggregate{total= " + SimonUtils.presentNanoTime(total) +
			", counter=" + counter +
			", min=" + SimonUtils.presentNanoTime(min) +
			", max=" + SimonUtils.presentNanoTime(max) +
			", minTimestamp=" + SimonUtils.presentTimestamp(minTimestamp) +
			", maxTimestamp=" + SimonUtils.presentTimestamp(maxTimestamp) +
			", active=" + active +
			", maxActive=" + maxActive +
			", maxActiveTimestamp=" + SimonUtils.presentTimestamp(maxActiveTimestamp) + '}';
	}
}
