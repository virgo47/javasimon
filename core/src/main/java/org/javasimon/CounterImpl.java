package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * Class implements {@link org.javasimon.Counter} interface - see there for how to use Counter.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @see org.javasimon.Counter
 */
final class CounterImpl extends AbstractSimon implements Counter {
	/**
	 * An internal counter.
	 */
	private long counter;

	/**
	 * Sum of all increments.
	 */
	private long incrementSum;

	/**
	 * Sum of all decrements.
	 */
	private long decrementSum;

	/**
	 * A maximum tracker.
	 */
	private long max = Long.MIN_VALUE;

	private long maxTimestamp;

	/**
	 * A minimum tracker - only negative values.
	 */
	private long min = Long.MAX_VALUE;

	private long minTimestamp;

	/**
	 * Construts Counter Simon with a specified name and for the specified manager.
	 *
	 * @param name Simon's name
	 * @param manager owning manager
	 */
	CounterImpl(String name, Manager manager) {
		super(name, manager);
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter set(long val) {
		long now = System.currentTimeMillis();
		CounterSample sample;
		synchronized (this) {
			privateSet(val, now);
			sample = sample();
		}
		manager.callback().onCounterSet(this, val, sample);
		return this;
	}

	// must be called from synchronized block
	private void privateSet(long val, long now) {
		updateUsages(now);
		counter = val;
		if (counter >= max) {
			max = counter;
			maxTimestamp = getLastUsage();
		}
		if (counter <= min) {
			min = counter;
			minTimestamp = getLastUsage();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter increase() {
		long now = System.currentTimeMillis();
		CounterSample sample;
		synchronized (this) {
			updateUsages(now);
			counter++;
			incrementSum++;
			if (counter >= max) {
				max = counter;
				maxTimestamp = getLastUsage();
			}
			sample = sample();
		}
		manager.callback().onCounterIncrease(this, 1, sample);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter decrease() {
		long now = System.currentTimeMillis();
		CounterSample sample;
		synchronized (this) {
			updateUsages(now);
			counter--;
			decrementSum++;
			if (counter <= min) {
				min = counter;
				minTimestamp = getLastUsage();
			}
			sample = sample();
		}
		manager.callback().onCounterDecrease(this, 1, sample);
		return this;
	}

	/**
	 * Updates usage statistics.
	 *
	 * @param now current millis timestamp
	 */
	private void updateUsages(long now) {
		lastUsage = now;
		if (firstUsage == 0) {
			firstUsage = lastUsage;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter increase(long inc) {
		long now = System.currentTimeMillis();
		CounterSample sample;
		synchronized (this) {
			incrementSum += inc;
			privateSet(counter + inc, now);
			sample = sample();
		}
		manager.callback().onCounterIncrease(this, inc, sample);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Counter decrease(long dec) {
		long now = System.currentTimeMillis();
		CounterSample sample;
		synchronized (this) {
			decrementSum += dec;
			privateSet(counter - dec, now);
			sample = sample();
		}
		manager.callback().onCounterDecrease(this, dec, sample);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Counter reset() {
		counter = 0;
		max = Long.MIN_VALUE;
		maxTimestamp = 0;
		min = Long.MAX_VALUE;
		minTimestamp = 0;
		incrementSum = 0;
		decrementSum = 0;
		resetCommon();
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized CounterSample sampleAndReset() {
		CounterSample sample = sample();
		reset();
		return sample;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized CounterSample sample() {
		CounterSample sample = new CounterSample();
		sample.setCounter(counter);
		sample.setMin(min);
		sample.setMax(max);
		sample.setMinTimestamp(minTimestamp);
		sample.setMaxTimestamp(maxTimestamp);
		sample.setIncrementSum(incrementSum);
		sample.setDecrementSum(decrementSum);
		sampleCommon(sample);
		return sample;
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
	public synchronized long getMin() {
		return min;
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
	public synchronized long getMax() {
		return max;
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
	public long getIncrementSum() {
		return incrementSum;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getDecrementSum() {
		return decrementSum;
	}

	/**
	 * Returns Simon basic information, counter, max value and min value as a human readable string.
	 *
	 * @return basic information, counter, max and min values
	 * @see AbstractSimon#toString()
	 */
	@Override
	public String toString() {
		return "Simon Counter: counter=" + counter +
			", max=" + SimonUtils.presentMinMaxCount(max) +
			", min=" + SimonUtils.presentMinMaxCount(min) +
			super.toString();
	}
}
