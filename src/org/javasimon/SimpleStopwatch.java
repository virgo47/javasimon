package org.javasimon;

/**
 * Simple single-threaded stopwatch simon.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
final class SimpleStopwatch extends AbstractSimon implements Stopwatch {
	private long elapsedNanos = 0;

	private long counter = 0;

	private long start = 0;

	private long max = 0;

	private long min = Long.MAX_VALUE;

	public SimpleStopwatch(String name, ObservationProcessor observationProcessor) {
		super(name, observationProcessor);
	}

	public Stopwatch addTime(long ns) {
		elapsedNanos += ns;
		counter++;
		return this;
	}

	public void reset() {
		elapsedNanos = 0;
		counter = 0;
	}

	public Stopwatch start() {
		start = System.nanoTime();
		return this;
	}

	public Stopwatch stop() {
		counter++;
		long split = System.nanoTime() - start;
		elapsedNanos += split;
		if (split > max) {
			max = split;
		}
		if (split < min) {
			min = split;
		}
		return this;
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
}