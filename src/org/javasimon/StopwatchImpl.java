package org.javasimon;

import org.javasimon.utils.SimonUtils;

import java.util.*;

/**
 * Stopwatch default implementation - it is thread-safe, more split times can be
 * measured in parallel - but start and stop must be called within the same thread.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
final class StopwatchImpl extends AbstractSimon implements Stopwatch {
	private long total;

	private long counter;

	private long active;

	private long max;

	private long maxTimestamp;

	private long maxActive;

	private long maxActiveTimestamp;

	private long min = Long.MAX_VALUE;

	private long minTimestamp;

	private long last;

	private ThreadLocal<Long> threadBasedSplitMap = new ThreadLocal<Long>();

	private Map<Object, Long> splitMap = new HashMap<Object, Long>();

	private long firstUsageNanos;

	private long currentNanos;

	StopwatchImpl(String name) {
		super(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Stopwatch addTime(long ns) {
		if (enabled) {
			updateUsages();
			addSplit(ns);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Stopwatch start() {
		if (enabled) {
			if (threadBasedSplitMap.get() != null) {
				throw new SimonException("Illegal start - there is another split running in the current thread: " + Thread.currentThread().getId());
			}
			updateUsages();
			activeStart();
			threadBasedSplitMap.set(currentNanos);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized long stop() {
		if (enabled) {
			Long start = threadBasedSplitMap.get();
			if (start == null) {
				throw new SimonException("Illegal stop - there is no split running in the current thread: " + Thread.currentThread().getId());
			}
			active--;
			threadBasedSplitMap.remove();
			updateUsages();
			return addSplit(currentNanos - start);
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Stopwatch start(Object key) {
		if (enabled) {
			if (splitMap.containsKey(key)) {
				throw new SimonException("Illegal start - there is another split running for the specified key: " + key);
			}
			updateUsages();
			activeStart();
			splitMap.put(key, currentNanos);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized long stop(Object key) {
		if (enabled) {
			Long start = splitMap.remove(key);
			if (start == null) {
				throw new SimonException("Illegal stop - there is no split running for the specified key: " + key);
			}
			active--;
			updateUsages();
			return addSplit(currentNanos - start);
		}
		return 0;
	}

	// Uses last usage, hence it must be placed after usages update
	private void activeStart() {
		active++;
		if (active >= maxActive) {
			maxActive = active;
			maxActiveTimestamp = getLastUsage();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Stopwatch reset() {
		total = 0;
		counter = 0;
		max = 0;
		min = Long.MAX_VALUE;
		maxTimestamp = 0;
		minTimestamp = 0;
		// active is not reset, because
		maxActive = 0;
		maxActiveTimestamp = 0;
		getStatProcessor().reset();
		return this;
	}

	private long addSplit(long split) {
		last = split;
		total += split;
		counter++;
		if (split > max) {
			max = split;
			maxTimestamp = getLastUsage();
		}
		if (split < min) {
			min = split;
			minTimestamp = getLastUsage();
		}
		if (getStatProcessor() != null) {
			getStatProcessor().process(split);
		}
		return split;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized long getTotal() {
		return total;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getLast() {
		return last;
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
	public synchronized long getMax() {
		return max;
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
	public synchronized long getMaxTimestamp() {
		return maxTimestamp;
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
	public long getActive() {
		return active;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getMaxActive() {
		return maxActive;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getMaxActiveTimestamp() {
		return maxActiveTimestamp;
	}

	@Override
	protected void enabledObserver() {
		threadBasedSplitMap = new ThreadLocal<Long>();
	}

	@Override
	public void setStatProcessor(StatProcessor statProcessor) {
		super.setStatProcessor(statProcessor);
		statProcessor.setInterpreter(StatProcessor.NanoInterpreter.INSTANCE);
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * Updates usage statistics.
	 */
	protected void updateUsages() {
		currentNanos = System.nanoTime();
		if (firstUsage == 0) {
			firstUsage = System.currentTimeMillis();
			firstUsageNanos = currentNanos;
		}
		lastUsage = firstUsage + (currentNanos - firstUsageNanos) / SimonUtils.NANOS_IN_MILLIS;
	}

	@Override
	public String toString() {
		return "Simon Stopwatch: " + super.toString() +
			" total " + SimonUtils.presentNanoTime(total) +
			", counter " + counter +
			", max " + SimonUtils.presentNanoTime(max) +
			", min " + SimonUtils.presentNanoTime(min) +
			(getNote() != null && getNote().length() != 0 ? ", note '" + getNote() + "'" : "");
	}
}
