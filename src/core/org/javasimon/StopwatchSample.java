package org.javasimon;

/**
 * StopwatchSample.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Jan 7, 2009
 */
public final class StopwatchSample extends AbstractSample {
	private final long total;
	private final long counter;
	private final long min;
	private final long max;
	private final long minTimestamp;
	private final long maxTimestamp;
	private final long active;
	private final long maxActive;
	private final long maxActiveTimestamp;

	StopwatchSample(long total, long counter, long min, long max, long minTimestamp, long maxTimestamp,
		long active, long maxActive, long maxActiveTimestamp, StatProcessor statProcessor) {
		super(statProcessor);
		this.total = total;
		this.counter = counter;
		this.min = min;
		this.max = max;
		this.minTimestamp = minTimestamp;
		this.maxTimestamp = maxTimestamp;
		this.active = active;
		this.maxActive = maxActive;
		this.maxActiveTimestamp = maxActiveTimestamp;
	}

	public long getTotal() {
		return total;
	}

	public long getCounter() {
		return counter;
	}

	public long getMin() {
		return min;
	}

	public long getMax() {
		return max;
	}

	public long getMinTimestamp() {
		return minTimestamp;
	}

	public long getMaxTimestamp() {
		return maxTimestamp;
	}

	public long getActive() {
		return active;
	}

	public long getMaxActive() {
		return maxActive;
	}

	public long getMaxActiveTimestamp() {
		return maxActiveTimestamp;
	}
}
