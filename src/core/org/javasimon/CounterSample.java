package org.javasimon;

/**
 * CounterSample.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Jan 7, 2009
 */
public final class CounterSample extends AbstractSample {
	private final long counter;
	private final long min;
	private final long max;
	private final long minTimestamp;
	private final long maxTimestamp;
	private final long incrementSum;
	private final long decrementSum;

	public CounterSample(long counter, long min, long max, long minTimestamp, long maxTimestamp, long incrementSum, long decrementSum, StatProcessor statProcessor) {
		super(statProcessor);
		this.counter = counter;
		this.min = min;
		this.max = max;
		this.minTimestamp = minTimestamp;
		this.maxTimestamp = maxTimestamp;
		this.incrementSum = incrementSum;
		this.decrementSum = decrementSum;
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

	public long getIncrementSum() {
		return incrementSum;
	}

	public long getDecrementSum() {
		return decrementSum;
	}
}
