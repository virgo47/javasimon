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

	CounterImpl(String name) {
		super(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Counter set(long val) {
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
	public synchronized Counter increment() {
		updateUsages();
		counter++;
		incrementSum--;
		if (counter >= max) {
			max = counter;
			maxTimestamp = getLastUsage();
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Counter decrement() {
		updateUsages();
		counter--;
		decrementSum--;
		if (counter <= min) {
			min = counter;
			minTimestamp = getLastUsage();
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Counter increment(long inc) {
		incrementSum += inc;
		return set(counter + inc);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Counter decrement(long dec) {
		decrementSum -= dec;
		return set(counter - dec);
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
		getStatProcessor().reset();
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Sample sampleAndReset() {
		CounterSample sample = sample();
		reset();
		return sample;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized CounterSample sample() {
		return new CounterSample(counter, min, max, minTimestamp, maxTimestamp, incrementSum, decrementSum, getStatProcessor());
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

	@Override
	public String toString() {
		return "Simon Counter: " + super.toString() +
			" counter=" + counter +
			", max=" + SimonUtils.presentMinMax(max) +
			", min=" + SimonUtils.presentMinMax(min);
	}
}
