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
	private long total;

	private long counter;

	private long max;

	private long maxTimestamp;

	private long min = Long.MAX_VALUE;

	private long minTimestamp;

	AbstractStopwatch(String name) {
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
		maxTimestamp = 0;
		minTimestamp = 0;
		resetCommon();
		return this;
	}

	synchronized long addSplit(long split) {
		updateUsages();
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
	public synchronized long getTotal() {
		return total;
	}

	@Override
	public synchronized long getCounter() {
		return counter;
	}

	@Override
	public synchronized long getMax() {
		return max;
	}

	@Override
	public synchronized long getMin() {
		return min;
	}

	@Override
	public synchronized long getMaxTimestamp() {
		return maxTimestamp;
	}

	@Override
	public synchronized long getMinTimestamp() {
		return minTimestamp;
	}

	@Override
	public void setStatProcessor(StatProcessor statProcessor) {
		super.setStatProcessor(statProcessor);
		statProcessor.setInterpreter(StatProcessor.NanoInterpreter.INSTANCE);
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
		map.putAll(getStatProcessor().sample(false)); // reset is done via Simon's reset method
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
