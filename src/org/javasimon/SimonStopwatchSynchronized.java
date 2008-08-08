package org.javasimon;

/**
 * Simonatch.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public final class SimonStopwatchSynchronized extends AbstractSimon implements SimonStopwatch {
	private long elapsedNanos = 0;

	private long counter = 0;

	private ThreadLocal<Long> start = new ThreadLocal<Long>();

	private long max = 0;

	private long min = Long.MAX_VALUE;

	public SimonStopwatchSynchronized(String name) {
		super(name);
	}

	public synchronized void addTime(long ns) {
		elapsedNanos += ns;
		counter++;
	}

	public synchronized void reset() {
		elapsedNanos = 0;
		counter = 0;
	}

	public synchronized void start() {
		start.set(System.nanoTime());
	}

	public synchronized void stop() {
		counter++;
		long split = System.nanoTime() - start.get();
		elapsedNanos += split;
		if (split > max) {
			max = split;
		}
		if (split < min) {
			min = split;
		}
	}

	public long getTotal() {
		return elapsedNanos;
	}

	public long getCounter() {
		return counter;
	}

	public long getMax() {
		return max;
	}

	public long getMin() {
		return min;
	}

	public String toString() {
		return "Simon Stopwatch: " + super.toString() +
			" elapsed " + SimonUtils.presentNanoTime(elapsedNanos) +
			", counter " + counter +
			", max " + SimonUtils.presentNanoTime(max) +
			", min " + SimonUtils.presentNanoTime(min);
	}

	public SimonStopwatch getDisabledDecorator() {
		return new DisabledStopwatch(this);
	}
}