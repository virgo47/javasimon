package org.javasimon;

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

	private ThreadLocal<Long> start = new ThreadLocal<Long>();

	private AtomicLong max = new AtomicLong(0);

	private AtomicLong min = new AtomicLong(Long.MAX_VALUE);

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
		start.set(System.nanoTime());
	}

	public void stop() {
		long split = System.nanoTime() - start.get();
		elapsedNanos.addAndGet(split);
		counter.incrementAndGet();

		if (split > max.get()) {
			long val = split;
			while (true) {
				long preval = max.getAndSet(val);
				if (preval <= val) {
					break;
				}
				val = preval;
			}
		}

		if (split < min.get()) {
			long val = split;
			while (true) {
				long preval = min.getAndSet(val);
				if (preval >= val) {
					break;
				}
				val = preval;
			}
		}
	}

	public long getElapsedNanos() {
		return elapsedNanos.longValue();
	}

	public long getCounter() {
		return counter.longValue();
	}

	public String toString() {
		return "Simon Stopwatch: " + super.toString() +
			" elapsed " + SimonUtils.presentTime(elapsedNanos.longValue()) +
			", counter " + counter.longValue() +
			", max " + SimonUtils.presentTime(max.longValue()) +
			", min " + SimonUtils.presentTime(min.longValue());
	}

	public SimonStopwatch getDisabledDecorator() {
		return new DisabledStopwatch(this);
	}
}

class DisabledStopwatch extends DisabledDecorator implements SimonStopwatch {
	public DisabledStopwatch(Simon simon) {
		super(simon);
	}

	public void addTime(long ns) {
	}

	public void start() {
	}

	public void stop() {
	}

	public long getElapsedNanos() {
		return 0;
	}

	public long getCounter() {
		return 0;
	}
}