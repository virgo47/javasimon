package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * Class implements {@link org.javasimon.Counter} interface - see there for how to use Counter.
 *
 * @see org.javasimon.Counter
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
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
	public synchronized Counter set(long val) {
		try {
			return privateSet(val);
		} finally {
			manager.callback().counterSet(this, val);
		}
	}

	private Counter privateSet(long val) {
		updateUsages();
		counter = val;
		if (counter >= max) {
			max = counter;
			maxTimestamp = getLastUsage();
		}
		if (counter <= min) {
			min = counter;
			minTimestamp = getLastUsage();
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Counter increase() {
		try {
			updateUsages();
			counter++;
			incrementSum++;
			if (counter >= max) {
				max = counter;
				maxTimestamp = getLastUsage();
			}
			return this;
		} finally {
			manager.callback().counterIncrease(this, 1);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Counter decrease() {
		try {
			updateUsages();
			counter--;
			decrementSum++;
			if (counter <= min) {
				min = counter;
				minTimestamp = getLastUsage();
			}
			return this;
		} finally {
			manager.callback().counterDecrease(this, 1);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Counter increase(long inc) {
		try {
			incrementSum += inc;
			return set(counter + inc);
		} finally {
			manager.callback().counterIncrease(this, inc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Counter decrease(long dec) {
		try {
			decrementSum -= dec;
			return set(counter - dec);
		} finally {
			manager.callback().counterDecrease(this, dec);
		}
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
		saveResetTimestamp();
		manager.callback().reset(this);
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
		sample.setNote(getNote());
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
		return "Simon Counter: " + super.toString() +
			" counter=" + counter +
			", max=" + SimonUtils.presentMinMax(max) +
			", min=" + SimonUtils.presentMinMax(min) +
			(getNote() != null && getNote().length() != 0 ? ", note '" + getNote() + "'" : "");
	}
}
