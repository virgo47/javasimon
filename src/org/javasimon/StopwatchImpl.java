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

	private ThreadLocal<Long> threadBasedSplitMap = new ThreadLocal<Long>();

	private Map<Object, Long> splitMap = new HashMap<Object, Long>();

	StopwatchImpl(String name) {
		super(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Stopwatch addTime(long ns) {
		if (enabled) {
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
			threadBasedSplitMap.set(System.nanoTime());
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
			return addSplit(System.nanoTime() - start);
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
			splitMap.put(key, System.nanoTime());
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
			return addSplit(System.nanoTime() - start);
		}
		return 0;
	}

	private void activeStart() {
		active++;
		if (active >= maxActive) {
			maxActive = active;
			maxActiveTimestamp = System.currentTimeMillis();
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
		resetCommon();
		return this;
	}

	private long addSplit(long split) {
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

	/**
	 * {@inheritDoc}
	 */
	public synchronized long getTotal() {
		return total;
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
