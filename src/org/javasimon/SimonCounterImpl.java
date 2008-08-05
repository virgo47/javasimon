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

	public SimonCounterImpl(String name) {
		super(name);
	}

	public void increment() {
		counter.incrementAndGet();
	}

	public void increment(long inc) {
		counter.addAndGet(inc);
	}

	public void reset() {
		counter.set(0);
	}

	public long getCounter() {
		return counter.longValue();
	}

	public String toString() {
		return "Simon Counter: " + super.toString() + " counter=" + counter;
	}
}
