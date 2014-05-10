package org.javasimon;

import org.javasimon.utils.SimonUtils;

import java.util.Collection;

/**
 * Class implements {@link org.javasimon.Counter} interface - see there for how to use Counter.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @see org.javasimon.Counter
 */
final class CounterImpl extends AbstractSimon implements Counter {

	/** An internal counter. */
	private long counter;

	/** Sum of all increments. */
	private long incrementSum;

	/** Sum of all decrements. */
	private long decrementSum;

	/** A maximum tracker. */
	private long max = Long.MIN_VALUE;

	private long maxTimestamp;

	/** A minimum tracker - only negative values. */
	private long min = Long.MAX_VALUE;

	private long minTimestamp;

	/**
	 * Constructs Counter Simon with a specified name and for the specified manager.
	 *
	 * @param name Simon's name
	 * @param manager owning manager
	 */
	CounterImpl(String name, Manager manager) {
		super(name, manager);
	}

	@Override
	public Counter set(long val) {
		if (!enabled) {
			return this;
		}

		long now = manager.milliTime();
		CounterSample sample;
		synchronized (this) {
			setPrivate(val, now);
			updateIncrementalSimonsSet(val, now);
			sample = sampleIfCallbacksNotEmpty();
		}
		manager.callback().onCounterSet(this, val, sample);
		return this;
	}

	private void setPrivate(long val, long now) {
		updateUsages(now);
		counter = val;
		updateMax();
		updateMin();
	}

	private void updateIncrementalSimonsSet(long val, long now) {
		Collection<Simon> simons = incrementalSimons();
		if (simons != null) {
			for (Simon simon : simons) {
				((CounterImpl) simon).setPrivate(val, now);
			}
		}
	}

	private void updateMin() {
		if (counter <= min) {
			min = counter;
			minTimestamp = getLastUsage();
		}
	}

	private void updateMax() {
		if (counter >= max) {
			max = counter;
			maxTimestamp = getLastUsage();
		}
	}

	@Override
	public Counter increase() {
		return increase(1);
	}

	@Override
	public Counter increase(long inc) {
		if (!enabled) {
			return this;
		}

		long now = manager.milliTime();
		CounterSample sample;
		synchronized (this) {
			increasePrivate(inc, now);
			updateIncrementalSimonsIncrease(inc, now);
			sample = sampleIfCallbacksNotEmpty();
		}
		manager.callback().onCounterIncrease(this, inc, sample);
		return this;
	}

	private void increasePrivate(long inc, long now) {
		updateUsages(now);
		incrementSum += inc;
		counter += inc;
		if (inc > 0) {
			updateMax();
		} else {
			updateMin();
		}
	}

	private void updateIncrementalSimonsIncrease(long inc, long now) {
		Collection<Simon> simons = incrementalSimons();
		if (simons != null) {
			for (Simon simon : simons) {
				((CounterImpl) simon).increasePrivate(inc, now);
			}
		}
	}

	@Override
	public Counter decrease() {
		return decrease(1);
	}

	@Override
	public synchronized Counter decrease(long dec) {
		if (!enabled) {
			return this;
		}

		long now = manager.milliTime();
		CounterSample sample;
		synchronized (this) {
			decreasePrivate(dec, now);
			sample = sampleIfCallbacksNotEmpty();
			updateIncrementalSimonsDecrease(dec, now);
		}
		manager.callback().onCounterDecrease(this, dec, sample);
		return this;
	}

	private void decreasePrivate(long dec, long now) {
		updateUsages(now);
		decrementSum += dec;
		counter -= dec;
		if (dec > 0) {
			updateMin();
		} else {
			updateMax();
		}
	}

	private void updateIncrementalSimonsDecrease(long dec, long now) {
		Collection<Simon> simons = incrementalSimons();
		if (simons != null) {
			for (Simon simon : simons) {
				((CounterImpl) simon).decreasePrivate(dec, now);
			}
		}
	}

	private CounterSample sampleIfCallbacksNotEmpty() {
		if (!manager.callback().callbacks().isEmpty()) {
			return sample();
		}
		return null;
	}

	@Override
	public synchronized long getCounter() {
		return counter;
	}

	@Override
	public synchronized long getMin() {
		return min;
	}

	@Override
	public synchronized long getMinTimestamp() {
		return minTimestamp;
	}

	@Override
	public synchronized long getMax() {
		return max;
	}

	@Override
	public synchronized long getMaxTimestamp() {
		return maxTimestamp;
	}

	@Override
	public synchronized long getIncrementSum() {
		return incrementSum;
	}

	@Override
	public synchronized long getDecrementSum() {
		return decrementSum;
	}

	@Override
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

	@Override
	public CounterSample sampleIncrement(Object key) {
		return (CounterSample) sampleIncrementHelper(key, new CounterImpl(null, manager));
	}

	/**
	 * Returns Simon basic information, counter, max value and min value as a human readable string.
	 *
	 * @return basic information, counter, max and min values
	 * @see AbstractSimon#toString()
	 */
	@Override
	public synchronized String toString() {
		return "Simon Counter: counter=" + counter +
			", max=" + SimonUtils.presentMinMaxCount(max) +
			", min=" + SimonUtils.presentMinMaxCount(min) +
			super.toString();
	}
}
