package org.javasimon;

import org.javasimon.utils.SimonUtils;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Common super-class for stopwatch implementations.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Sep 11, 2008
 */
abstract class AbstractStopwatch extends AbstractSimon implements Stopwatch {
	private long total = 0;

	private long counter = 0;

	private long max = 0;

	private long maxTimestamp;

	private long min = Long.MAX_VALUE;

	private long minTimestamp;

	public AbstractStopwatch(String name) {
		super(name);
	}

	@Override
	public synchronized Stopwatch addTime(long ns) {
		if (enabled) {
			addSplit(ns);
		}
		return this;
	}

	@Override
	public synchronized Stopwatch reset() {
		total = 0;
		counter = 0;
		max = 0;
		min = Long.MAX_VALUE;
		resetUsages();
		return this;
	}

	synchronized long addSplit(long split) {
		recordUsages();
		total += split;
		counter++;
		if (split > max) {
			max = split;
			maxTimestamp = System.currentTimeMillis();
		}
		if (split < min) {
			min = split;
			minTimestamp = System.currentTimeMillis();
		}
		if (getStatProcessor() != null) {
			getStatProcessor().process(split);
		}
		return split;
	}

	@Override
	public long getTotal() {
		return total;
	}

	@Override
	public long getCounter() {
		return counter;
	}

	@Override
	public long getMax() {
		return max;
	}

	@Override
	public long getMin() {
		return min;
	}

	@Override
	public long getMaxTimestamp() {
		return maxTimestamp;
	}

	@Override
	public long getMinTimestamp() {
		return minTimestamp;
	}

	@Override
	public synchronized Map<String, String> sample(boolean reset) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("total", String.valueOf(total));
		map.put("counter", String.valueOf(counter));
		map.put("min", String.valueOf(min));
		map.put("max", String.valueOf(max));
		map.put("minTimestamp", String.valueOf(minTimestamp));
		map.put("maxTimestamp", String.valueOf(maxTimestamp));
		map.putAll(getStatProcessor().sample(reset));
		if (reset) {
			reset();
		}
		return map;
	}

	@Override
	public String toString() {
		return "Simon Stopwatch: " + super.toString() +
			" total " + SimonUtils.presentNanoTime(total) +
			", counter " + counter +
			", max " + SimonUtils.presentNanoTime(max) +
			", min " + SimonUtils.presentNanoTime(min) +
			(getNote() != null && !getNote().isEmpty() ? ", note '" + getNote() + "'" : "");
	}
}
