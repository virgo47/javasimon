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
	@Override
	public Stopwatch addTime(long ns) {
		StopwatchSample sample;
		synchronized (this) {
			if (!enabled) {
				return this;
			}
			updateUsages();
			addSplit(ns);
			sample = sample();
		}
		manager.callback().onStopwatchAdd(this, ns, sample);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stopwatch addSplit(Split split) {
		long splitNs = split.runningFor();
		StopwatchSample sample;
		synchronized (this) {
			if (!enabled) {
				return this;
			}
			// using parameter version saves one currentTimeMillis call
			updateUsages(split.getStart() + splitNs);
			addSplit(splitNs);
			sample = sample();
		}
		manager.callback().onStopwatchAdd(this, split, sample);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Split start() {
		long nowNanos = System.nanoTime();
		Split split;
		StopwatchSample sample;
		synchronized (this) {
			if (!enabled) {
				return new Split(this);
			}
			updateUsages(nowNanos);
			activeStart();
			split = new Split(this, nowNanos);
			sample = sample();
		}
		manager.callback().onStopwatchStart(split, sample);
		return split;
	}

	/**
	 * Protected method doing the stop work based on provided start nano-time.
	 *
	 * @param split Split object that has been stopped
	 * @param start start nano-time of the split @return split time in ns
	 * @param nowNanos current nano time
	 */
	void stop(Split split, long start, long nowNanos) {
		StopwatchSample sample;
		synchronized (this) {
			active--;
			updateUsages(nowNanos);
			addSplit(nowNanos - start);
			sample = sample();
		}
		manager.callback().onStopwatchStop(split, sample);
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
	@Override
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
		resetCommon();
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized double getMean() {
		return mean;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized double getVarianceN() {
		if (counter == 0) {
			return 0;
		}
		return mean2 / counter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized double getVariance() {
		if (counter == 0) {
			return 0;
		}
		long countMinusOne = counter - 1;
		if (counter < 2) {
			countMinusOne = 1;
		}
		return mean2 / countMinusOne;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized double getStandardDeviation() {
		return Math.sqrt(getVarianceN());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized long getTotal() {
		return total;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLast() {
		return last;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized long getCounter() {
		return counter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized long getMax() {
		return max;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized long getMin() {
		return min;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized long getMaxTimestamp() {
		return maxTimestamp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized long getMinTimestamp() {
		return minTimestamp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getActive() {
		return active;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMaxActive() {
		return maxActive;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMaxActiveTimestamp() {
		return maxActiveTimestamp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized StopwatchSample sampleAndReset() {
		StopwatchSample sample = sample();
		reset();
		return sample;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
	 * Updates usage statistics without using {@link System#currentTimeMillis()} if client code already has
	 * current nano timer value.
	 *
	 * @param nowNanos current value of nano timer
	 */
	private void updateUsages(long nowNanos) {
		lastUsage = SimonManager.millisForNano(nowNanos);
		if (firstUsage == 0) {
			firstUsage = lastUsage;
		}
	}

	/**
	 * Updates usage statistics. If current nano timer value is available use {@link #updateUsages(long)} instead.
	 */
	private void updateUsages() {
		lastUsage = System.currentTimeMillis();
		if (firstUsage == 0) {
			firstUsage = lastUsage;
		}
	}

	/**
	 * Returns Simon basic information, total time, counter, max value and min value as a human readable string.
	 *
	 * @return basic information, total time, counter, max and min values
	 * @see AbstractSimon#toString()
	 */
	@Override
	public synchronized String toString() {
		return "Simon Stopwatch: total " + SimonUtils.presentNanoTime(total) +
			", counter " + counter +
			", max " + SimonUtils.presentNanoTime(max) +
			", min " + SimonUtils.presentNanoTime(min) +
			", mean " + SimonUtils.presentNanoTime((long) mean) +
			super.toString();
	}
}
