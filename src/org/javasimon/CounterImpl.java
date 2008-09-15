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

	/** A maximum tracker. */
	private long max = Long.MIN_VALUE;

	private long maxTimestamp;

	/** A minimum tracker - only negative values. */
	private long min = Long.MAX_VALUE;

	private long minTimestamp;

	CounterImpl(String name) {
		super(name);
	}

	@Override
	public synchronized Counter set(long val) {
		counter = val;
		if (counter > max) {
			max = counter;
			maxTimestamp = System.currentTimeMillis();
		} else if (counter < min) {
			min = counter;
			minTimestamp = System.currentTimeMillis();
		}
		updateUsages();
		return this;
	}

	@Override
	public synchronized Counter increment() {
		counter++;
		if (counter > max) {
			max = counter;
			maxTimestamp = System.currentTimeMillis();
		}
		updateUsages();
		return this;
	}

	@Override
	public synchronized Counter decrement() {
		counter--;
		if (counter < min) {
			min = counter;
			minTimestamp = System.currentTimeMillis();
		}
		updateUsages();
		return this;
	}

	@Override
	public synchronized Counter increment(long inc) {
		return set(counter + inc);
	}

	@Override
	public synchronized Counter decrement(long dec) {
		return set(counter - dec);
	}

	@Override
	public synchronized Counter reset() {
		counter = 0;
		max = Long.MIN_VALUE;
		maxTimestamp = System.currentTimeMillis();
		min = Long.MAX_VALUE;
		minTimestamp = System.currentTimeMillis();
		resetCommon();
		return this;
	}

	@Override
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

	@Override
	public synchronized long getCounter() {
		return counter;
	}

	@Override
	public synchronized long getMin() {
		return min;
	}

	@Override
	public synchronized long getMinTimestamp() {
		return minTimestamp;
	}

	@Override
	public synchronized long getMax() {
		return max;
	}

	@Override
	public synchronized long getMaxTimestamp() {
		return maxTimestamp;
	}

	@Override
	public String toString() {
		return "Simon Counter: " + super.toString() +
			" counter=" + counter +
			", max=" + max +
			", min=" + min;
	}
}
