package org.javasimon;

import org.javasimon.utils.SimonUtils;

import java.util.HashMap;
import java.util.Map;

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

	private Map<Object, StopwatchImpl> sampleKeys;

	/**
	 * Constructs Stopwatch Simon with a specified name and for the specified manager.
	 *
	 * @param name Simon's name
	 * @param manager owning manager
	 */
	StopwatchImpl(String name, Manager manager) {
		super(name, manager);
	}

	@Override
	public Stopwatch addSplit(Split split) {
		if (!enabled) {
			return this;
		}

		long splitNs = split.runningFor();
		StopwatchSample sample = null;
		synchronized (this) {
			// using parameter version saves one currentTimeMillis call
			long nowNanos = split.getStart() + splitNs;
			updateUsages(nowNanos);
			addSplit(splitNs);
			if (!manager.callback().callbacks().isEmpty()) {
				sample = sample();
			}
			updateSampleKeys(splitNs, nowNanos);
		}
		manager.callback().onStopwatchAdd(this, split, sample);
		return this;
	}

	private void updateSampleKeys(long splitNs, long nowNanos) {
		if (sampleKeys != null) {
			for (StopwatchImpl stopwatch : sampleKeys.values()) {
				stopwatch.addSplit(splitNs);
				stopwatch.updateUsages(nowNanos);
			}
		}
	}

	@Override
	public Split start() {
		if (!enabled) {
			return new Split(this);
		}

		long nowNanos = System.nanoTime();
		Split split;
		synchronized (this) {
			updateUsages(nowNanos);
			activeStart();
		}
		split = new Split(this, nowNanos);
		manager.callback().onStopwatchStart(split);
		return split;
	}

	/**
	 * Protected method doing the stop work based on provided start nano-time.
	 *
	 * @param split Split object that has been stopped
	 * @param start start nano-time of the split @return split time in ns
	 * @param nowNanos current nano time
	 * @param subSimon name of the sub-stopwatch (hierarchy delimiter is added automatically), may be {@code null}
	 */
	void stop(final Split split, final long start, final long nowNanos, final String subSimon) {
		StopwatchSample sample = null;
		synchronized (this) {
			active--;
			updateUsages(nowNanos);
			if (subSimon != null) {
				Stopwatch effectiveStopwatch = manager.getStopwatch(getName() + Manager.HIERARCHY_DELIMITER + subSimon);
				split.setAttribute(Split.ATTR_EFFECTIVE_STOPWATCH, effectiveStopwatch);
				effectiveStopwatch.addSplit(split);
				return;
			}
			long splitNs = nowNanos - start;
			addSplit(splitNs);
			if (!manager.callback().callbacks().isEmpty()) {
				sample = sample();
			}
			updateSampleKeys(splitNs, nowNanos);
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

	@Override
	void concreteReset() {
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

	@Override
	public synchronized double getMean() {
		return mean;
	}

	@Override
	public synchronized double getVarianceN() {
		if (counter == 0) {
			return Double.NaN;
		}
		if (counter == 1) {
			return 0d;
		}
		return mean2 / counter;
	}

	@Override
	public synchronized double getVariance() {
		if (counter == 0) {
			return Double.NaN;
		}
		if (counter == 1) {
			return 0d;
		}
		return mean2 / (counter - 1);
	}

	@Override
	public synchronized double getStandardDeviation() {
		return Math.sqrt(getVariance());
	}

	@Override
	public synchronized long getTotal() {
		return total;
	}

	@Override
	public synchronized long getLast() {
		return last;
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
	public synchronized long getActive() {
		return active;
	}

	@Override
	public synchronized long getMaxActive() {
		return maxActive;
	}

	@Override
	public synchronized long getMaxActiveTimestamp() {
		return maxActiveTimestamp;
	}

	@Override
	public synchronized StopwatchSample sampleAndReset() {
		StopwatchSample sample = sample();
		reset();
		return sample;
	}

	@Override
	public synchronized StopwatchSample sample(Object key) {
		StopwatchSample sample = sample();
		StopwatchImpl stopwatch = getSampleKey(key);
		if (stopwatch != null) {
			sample.setIncrement(stopwatch.sample());
		}
		sampleKeys.put(key, new StopwatchImpl(null, null));
		return sample;
	}

	private StopwatchImpl getSampleKey(Object key) {
		if (sampleKeys == null) {
			sampleKeys = new HashMap<Object, StopwatchImpl>();
		}
		return sampleKeys.get(key);
	}

	@Override
	public synchronized boolean removeSampleKey(Object key) {
		return sampleKeys != null && sampleKeys.remove(key) != null;
	}

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
		sample.setMean(mean);
		sample.setVariance(getVariance());
		sample.setVarianceN(getVarianceN());
		sample.setStandardDeviation(getStandardDeviation());
		sample.setLast(last);
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
		lastUsage = SimonUtils.millisForNano(nowNanos);
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
