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
	/**
	 * An internal counter.
	 */
	private long counter;

	/**
	 * A maximum tracker.
	 */
	private long max = Long.MIN_VALUE;

	/**
	 * A minimum tracker - only negative values.
	 */
	private long min = Long.MAX_VALUE;

	public CounterImpl(String name) {
		super(name);
	}

	public synchronized Counter set(long val) {
		counter = val;
		if (counter > max) {
			max = counter;
		} else if (counter < min) {
			min = counter;
		}
		return this;
	}

	public synchronized Counter increment() {
		counter++;
		if (counter > max) {
			max = counter;
		}
		return this;
	}

	public synchronized Counter decrement() {
		counter--;
		if (counter < min) {
			min = counter;
		}
		return this;
	}

	public synchronized Counter increment(long inc) {
		counter += inc;
		if (counter > max) {
			max = counter;
		} else if (counter < min) {
			min = counter;
		}
		return this;
	}

	public synchronized Counter decrement(long dec) {
		counter -= dec;
		if (counter > max) {
			max = counter;
		} else if (counter < min) {
			min = counter;
		}
		return this;
	}

	public synchronized Counter reset() {
		counter = 0;
		max = Long.MIN_VALUE;
		min = Long.MAX_VALUE;
		return this;
	}

	@Override
	public Map<String, String> sample(boolean reset) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("counter", String.valueOf(counter));
		map.put("min", String.valueOf(min));
		map.put("max", String.valueOf(max));
		map.putAll(getStatProcessor().sample(reset));
		if (reset) {
			reset();
		}
		return map;
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

	public String toString() {
		return "Simon Counter: " + super.toString() + " counter=" + counter;
	}
}
