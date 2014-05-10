package org.javasimon;

import org.javasimon.utils.SimonUtils;

import java.util.Collection;

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
		long nowNanos = nanoTimeFromSplit(split, splitNs);

		StopwatchSample sample = null;
		synchronized (this) {
			// using parameter version saves one currentTimeMillis call
			updateUsagesNanos(nowNanos);
			addSplit(splitNs);
			if (!manager.callback().callbacks().isEmpty()) {
				sample = sample();
			}
			updateIncrementalSimons(splitNs, nowNanos);
		}
		manager.callback().onStopwatchAdd(this, split, sample);
		return this;
	}

	private long nanoTimeFromSplit(Split split, long splitNs) {
		if (split.getStopwatch() != null) {
			return split.getStart() + splitNs;
		} else {
			return manager.nanoTime();
		}
	}

	private void updateIncrementalSimons(long splitNs, long nowNanos) {
		Collection<Simon> simons = incrementalSimons();
		if (simons != null) {
			for (Simon simon : simons) {
				StopwatchImpl stopwatch = (StopwatchImpl) simon;
				stopwatch.addSplit(splitNs);
				stopwatch.updateUsagesNanos(nowNanos);
			}
		}
	}

	@Override
	public Split start() {
		if (!enabled) {
			return new Split(this, manager);
		}

		long nowNanos = manager.nanoTime();
		Split split;
		synchronized (this) {
			updateUsagesNanos(nowNanos);
			activeStart();
		}
		split = new Split(this, manager, nowNanos);
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
			updateUsagesNanos(nowNanos);
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
			updateIncrementalSimons(splitNs, nowNanos);
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

	@Override
	public synchronized StopwatchSample sampleIncrement(Object key) {
		return (StopwatchSample) sampleIncrementHelper(key, new StopwatchImpl(null, manager));
	}

	/**
	 * Updates usage statistics without using {@link System#currentTimeMillis()} if client code already has
	 * current nano timer value.
	 *
	 * @param nowNanos current value of nano timer
	 */
	private void updateUsagesNanos(long nowNanos) {
		updateUsages(manager.millisForNano(nowNanos));
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
