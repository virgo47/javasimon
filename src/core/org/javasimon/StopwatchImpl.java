package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * Class implements {@link org.javasimon.Stopwatch} interface - see there for how to use Stopwatch.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 * @see org.javasimon.Stopwatch
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

	private long firstUsageNanos;

	private long currentNanos;

	StopwatchImpl(String name, Manager manager) {
		super(name, manager);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Stopwatch addTime(long ns) {
		if (enabled) {
			updateUsages();
			addSplit(ns);
			manager.callback().stopwatchAdd(this, ns);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Split start() {
		if (enabled) {
			Split split;
			updateUsages();
			activeStart();
			split = new Split(this, currentNanos);
			manager.callback().stopwatchStart(split);
			return split;
		}
		return new Split(null, 0);
	}

	/**
	 * Protected method doing the stop work based on provided start nano-time.
	 *
	 * @param split Split object that has been stopped
	 * @param start start nano-time of the split @return split time in ns
	 * @return duration of the split in nanoseconds
	 */
	synchronized long stop(Split split, long start) {
		try {
			active--;
			updateUsages();
			return addSplit(currentNanos - start);
		} finally {
			manager.callback().stopwatchStop(split);
		}
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
		// active is not reset, because active Splits do not know about this reset
		maxActive = active;
		maxActiveTimestamp = 0;
		saveResetTimestamp();
		getStatProcessor().reset();
		manager.callback().reset(this);
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
	public void setStatProcessor(StatProcessor statProcessor) {
		super.setStatProcessor(statProcessor);
		statProcessor.setInterpreter(StatProcessor.NanoInterpreter.INSTANCE);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized StopwatchSample sampleAndReset() {
		StopwatchSample sample = sample();
		reset();
		return sample;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized StopwatchSample sample() {
		StopwatchSample sample = new StopwatchSample();
		sample.setTotal(total);
		sample.setCounter(counter);
		sample.setMin(min);
		sample.setMax(max);
		sample.setMinTimestamp(minTimestamp);
		sample.setMaxTimestamp(maxTimestamp);
		sample.setActive(active);
		sample.setMaxActive(maxActive);
		sample.setMaxActiveTimestamp(maxActiveTimestamp);
		sample.setFromStatProcessor(getStatProcessor());
		sample.setNote(getNote());
		return sample;
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
	public synchronized String toString() {
		return "Simon Stopwatch: " + super.toString() +
			" total " + SimonUtils.presentNanoTime(total) +
			", counter " + counter +
			", max " + SimonUtils.presentNanoTime(max) +
			", min " + SimonUtils.presentNanoTime(min) +
			(getNote() != null && getNote().length() != 0 ? ", note '" + getNote() + "'" : "");
	}
}
