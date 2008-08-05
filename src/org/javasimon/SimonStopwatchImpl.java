package org.javasimon;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simonatch.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public class SimonStopwatchImpl extends AbstractSimon implements SimonStopwatch {
	private AtomicLong elapsedNanos = new AtomicLong(0);

	private AtomicLong counter = new AtomicLong(0);

	private AtomicLong start = new AtomicLong(0);

	private AtomicInteger factor = new AtomicInteger(0);

	public SimonStopwatchImpl(String name) {
		super(name);
	}

	public void addTime(long ns) {
		elapsedNanos.addAndGet(ns);
		counter.incrementAndGet();
	}

	public void reset() {
		elapsedNanos.set(0);
		counter.set(0);
		start.set(System.nanoTime());
	}

	public void start() {
		int mul = factor.getAndIncrement();
		long currentNano = System.nanoTime();
		long ns = start.getAndSet(currentNano);
		if (mul > 0) {
			elapsedNanos.addAndGet((currentNano - ns) * mul);
		}
	}

	public void stop() {
		int mul = factor.getAndDecrement();
		if (mul < 1) {
			throw new SimonException("Stop used more times than start for Simon '" + getName() + "'");
		}
		long currentNano = System.nanoTime();
		long ns = start.getAndSet(currentNano);
		elapsedNanos.addAndGet((currentNano - ns) * mul);
		counter.incrementAndGet();
	}

	public long getElapsedNanos() {
		return elapsedNanos.longValue();
	}

	public long getCounter() {
		return counter.longValue();
	}

	public String toString() {
		return "Simon Stopwatch: " + super.toString() + " elapsedNanos=" + elapsedNanos + ", counter=" + counter;
	}
}
