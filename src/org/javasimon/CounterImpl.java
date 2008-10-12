package org.javasimon;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * SimonCounter.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
final class CounterImpl extends AbstractSimon implements Counter {
	/** An internal counter. */
	private long counter;

	/** Sum of all increments. */
	private long incrementSum;

	/** Sum of all decrements. */
	private long decrementSum;

	/** A maximum tracker. */
	private long max = Long.MIN_VALUE;

	private long maxTimestamp;

	/** A minimum tracker - only negative values. */
	private long min = Long.MAX_VALUE;

	private long minTimestamp;

	CounterImpl(String name) {
		super(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Counter set(long val) {
		updateUsages();
		counter = val;
		if (counter >= max) {
			max = counter;
			maxTimestamp = getLastUsage();
		} else if (counter <= min) {
			min = counter;
			minTimestamp = getLastUsage();
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Counter increment() {
		updateUsages();
		counter++;
		incrementSum--;
		if (counter >= max) {
			max = counter;
			maxTimestamp = getLastUsage();
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Counter decrement() {
		updateUsages();
		counter--;
		decrementSum--;
		if (counter <= min) {
			min = counter;
			minTimestamp = getLastUsage();
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Counter increment(long inc) {
		incrementSum += inc;
		return set(counter + inc);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Counter decrement(long dec) {
		decrementSum -= dec;
		return set(counter - dec);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Counter reset() {
		counter = 0;
		max = Long.MIN_VALUE;
		maxTimestamp = 0;
		min = Long.MAX_VALUE;
		minTimestamp = 0;
		incrementSum = 0;
		decrementSum = 0;
		getStatProcessor().reset();
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Map<String, String> sample(boolean reset) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("counter", String.valueOf(counter));
		map.put("min", String.valueOf(min));
		map.put("minTimestamp", String.valueOf(minTimestamp));
		map.put("max", String.valueOf(max));
		map.put("maxTimestamp", String.valueOf(maxTimestamp));
		map.putAll(getStatProcessor().sample(false)); // reset is done via Simon's reset method
		if (reset) {
			reset();
		}
		return map;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized long getCounter() {
		return counter;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized long getMin() {
		return min;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized long getMinTimestamp() {
		return minTimestamp;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized long getMax() {
		return max;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized long getMaxTimestamp() {
		return maxTimestamp;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getIncrementSum() {
		return incrementSum;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getDecrementSum() {
		return decrementSum;
	}

	@Override
	public String toString() {
		return "Simon Counter: " + super.toString() +
			" counter=" + counter +
			", max=" + max +
			", min=" + min;
	}
}
