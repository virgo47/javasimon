package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * Class implements {@link org.javasimon.Stopwatch} interface - see there for how to use Stopwatch.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
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

	private double mean; // used to calculate statistics
	private double mean2; // used to calculate statistics

	/**
	 * Construts Stopwatch Simon with a specified name and for the specified manager.
	 *
	 * @param name Simon's name
	 * @param manager owning manager
	 */
	StopwatchImpl(String name, Manager manager) {
		super(name, manager);
	}

	/**
	 * {@inheritDoc}
	 */
	public Stopwatch addTime(long ns) {
		long nowNanos = System.nanoTime();
		synchronized (this) {
			if (enabled) {
				updateUsages(nowNanos);
				addSplit(ns);
				manager.callback().stopwatchAdd(this, ns);
			}
			return this;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Split start() {
		long nowNanos = System.nanoTime();
		synchronized (this) {
			if (enabled) {
				Split split;
				updateUsages(nowNanos);
				activeStart();
				split = new Split(this, nowNanos);
				manager.callback().stopwatchStart(split);
				return split;
			}
			return new Split(this, 0);
		}
	}

	/**
	 * Protected method doing the stop work based on provided start nano-time.
	 *
	 * @param split Split object that has been stopped
	 * @param start start nano-time of the split @return split time in ns
	 * @return duration of the split in nanoseconds
	 */
	long stop(Split split, long start) {
		long nowNanos = System.nanoTime();
		synchronized (this) {
			try {
				active--;
				updateUsages(nowNanos);
				return addSplit(nowNanos - start);
			} finally {
				manager.callback().stopwatchStop(split);
			}
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
		mean = 0;
		mean2 = 0;
		saveResetTimestamp();
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
		// statistics processing
		double delta = split - mean;
		mean = ((double) total) / counter;
		mean2 += delta * (split - mean);

		return split;
	}

	private synchronized double getMean() {
		return mean;
	}

	private synchronized double getVarianceN() {
		if (counter == 0) {
			return 0;
		}
		return mean2 / counter;
	}

	private synchronized double getVariance() {
		if (counter == 0) {
			return 0;
		}
		long countMinusOne = counter - 1;
		if (counter < 2) {
			countMinusOne = 1;
		}
		return mean2 / countMinusOne;
	}

	private synchronized double getStandardDeviation() {
		return Math.sqrt(getVarianceN());
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
		sample.setMean(getMean());
		sample.setVariance(getVariance());
		sample.setVarianceN(getVarianceN());
		sample.setStandardDeviation(getStandardDeviation());
		sample.setLast(getLast());
		sampleCommon(sample);
		return sample;
	}

	/**
	 * Updates usage statistics.
	 *
	 * @param nowNanos current value of nano timer
	 */
	private void updateUsages(long nowNanos) {
		if (firstUsage == 0) {
			firstUsage = System.currentTimeMillis();
			firstUsageNanos = nowNanos;
		}
		lastUsage = firstUsage + (nowNanos - firstUsageNanos) / SimonUtils.NANOS_IN_MILLIS;
	}

	/**
	 * Returns Simon basic information, total time, counter, max value and min value as a human readable string.
	 *
	 * @return basic information, total time, counter, max and min values
	 * @see AbstractSimon#toString()
	 */
	@Override
	public synchronized String toString() {
		return "Simon Stopwatch: " +
			" total " + SimonUtils.presentNanoTime(total) +
			", counter " + counter +
			", max " + SimonUtils.presentNanoTime(max) +
			", min " + SimonUtils.presentNanoTime(min) +
			", mean " + SimonUtils.presentNanoTime((long) mean) +
			super.toString() +
			(getNote() != null && getNote().length() != 0 ? ", note '" + getNote() + "'" : "");
	}
}
