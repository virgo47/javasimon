package org.javasimon;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Simonatch.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public final class SimonStopwatchImpl extends AbstractSimon implements SimonStopwatch {
	private AtomicLong total = new AtomicLong(0);

	private AtomicLong counter = new AtomicLong(0);

	private ThreadLocal<Long> start = new ThreadLocal<Long>();

	private AtomicLong max = new AtomicLong(0);

	private AtomicLong min = new AtomicLong(Long.MAX_VALUE);

	public SimonStopwatchImpl(String name) {
		super(name);
	}

	public void addTime(long ns) {
		total.addAndGet(ns);
		counter.incrementAndGet();
	}

	public void reset() {
		total.set(0);
		counter.set(0);
	}

	public void start() {
		start.set(System.nanoTime());
	}

	public void stop() {
		long split = System.nanoTime() - start.get();
		total.addAndGet(split);
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

	public long getTotal() {
		return total.longValue();
	}

	public long getCounter() {
		return counter.longValue();
	}

	public long getMax() {
		return max.longValue();
	}

	public long getMin() {
		return min.longValue();
	}

	public String toString() {
		return "Simon Stopwatch: " + super.toString() +
			" total " + SimonUtils.presentNanoTime(total.longValue()) +
			", counter " + counter.longValue() +
			", max " + SimonUtils.presentNanoTime(max.longValue()) +
			", min " + SimonUtils.presentNanoTime(min.longValue());
	}

	public SimonStopwatch getDisabledDecorator() {
		return new DisabledStopwatch(this);
	}
}

class DisabledStopwatch extends AbstractDisabledSimon implements SimonStopwatch {
	public DisabledStopwatch(Simon simon) {
		super(simon);
	}

	public void addTime(long ns) {
	}

	public void start() {
	}

	public void stop() {
	}

	public long getTotal() {
		return 0;
	}

	public long getCounter() {
		return 0;
	}

	public long getMax() {
		return 0;
	}

	public long getMin() {
		return 0;
	}
}