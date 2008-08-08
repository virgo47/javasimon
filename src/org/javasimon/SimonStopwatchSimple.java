package org.javasimon;

/**
 * Simonatch.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public final class SimonStopwatchSimple extends AbstractSimon implements SimonStopwatch {
	private long elapsedNanos = 0;

	private long counter = 0;

	private long start = 0;

	private long max = 0;

	private long min = Long.MAX_VALUE;

	public SimonStopwatchSimple(String name) {
		super(name);
	}

	public void addTime(long ns) {
		elapsedNanos += ns;
		counter++;
	}

	public void reset() {
		elapsedNanos = 0;
		counter = 0;
	}

	public void start() {
		start = System.nanoTime();
	}

	public void stop() {
		counter++;
		long split = System.nanoTime() - start;
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