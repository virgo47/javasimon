package org.javasimon;

import java.util.concurrent.atomic.AtomicLong;

/**
 * SimonCounter.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public class SimonCounterImpl extends AbstractSimon implements SimonCounter {
	private AtomicLong counter = new AtomicLong(0);

	private AtomicLong max = new AtomicLong(0);

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
		counter.decrementAndGet();
	}

	public void increment(long inc) {
		long val = counter.addAndGet(inc);
		if (val > max.longValue()) {
			max.set(val);
		}
	}

	public void decrement(long dec) {
		counter.addAndGet(-dec);
	}

	public void reset() {
		counter.set(0);
		max.set(0);
	}

	public long getCounter() {
		return counter.longValue();
	}

	public String toString() {
		return "Simon Counter: " + super.toString() + " counter=" + counter;
	}
}
