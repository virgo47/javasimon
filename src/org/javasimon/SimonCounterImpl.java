package org.javasimon;

import java.util.concurrent.atomic.AtomicLong;

/**
 * SimonCounter.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public final class SimonCounterImpl extends AbstractSimon implements SimonCounter {

	/**
	 * An internal counter.
	 */
	private final AtomicLong counter = new AtomicLong(0);

	/**
	 * A maximum tracker.
	 */
	private final AtomicLong max = new AtomicLong(0);

	/**
	 * A minimum tracker.
	 */
	private final AtomicLong min = new AtomicLong(0);

	public SimonCounterImpl(String name) {
		super(name);
	}

	public void increment() {
		long val = counter.incrementAndGet();
		if (val > max.longValue()) {
			max.set(val);
		}
	}

	public void decrement() {
		final long val = counter.decrementAndGet();
		if (val < min.longValue()) {
			min.set(val);
		}
	}

	public void increment(long inc) {
		final long val = counter.addAndGet(inc);
		if (val > max.longValue()) {
			max.set(val);
		} else if (val < min.longValue()) {
			min.set(val);
		}
	}

	public void decrement(long dec) {
		final long val = counter.addAndGet(-dec);
		if (val < min.longValue()) {
			min.set(val);
		} else if (val > max.longValue()) {
			max.set(val);
		}
	}

	public void reset() {
		counter.set(0);
		max.set(0);
		min.set(0);
	}

	public long getCounter() {
		return counter.longValue();
	}

	public String toString() {
		return "Simon Counter: " + super.toString() + " counter=" + counter;
	}

	public SimonCounter getDisabledDecorator() {
		return new DisabledCounter(this);
	}
}

class DisabledCounter extends AbstractDisabledSimon implements SimonCounter {
	public DisabledCounter(Simon simon) {
		super(simon);
	}

	public void increment() {
	}

	public void decrement() {
	}

	public void increment(long inc) {
	}

	public void decrement(long inc) {
	}

	public long getCounter() {
		return 0;
	}
}
