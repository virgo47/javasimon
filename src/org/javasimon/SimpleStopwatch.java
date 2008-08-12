package org.javasimon;

/**
 * Simple single-threaded stopwatch simon.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 * @deprecated no serious performance reason - remove after publishing results
 */
final class SimpleStopwatch extends AbstractSimon implements Stopwatch {
	private long total = 0;

	private long counter = 0;

	private long start = 0;

	private long max = 0;

	private long min = Long.MAX_VALUE;

	public SimpleStopwatch(String name) {
		super(name);
	}

	public Stopwatch addTime(long ns) {
		total += ns;
		counter++;
		return this;
	}

	public Stopwatch reset() {
		total = 0;
		counter = 0;
		max = 0;
		min = Long.MAX_VALUE;
		start = 0;
		return this;
	}

	public Stopwatch start() {
		start = System.nanoTime();
		return this;
	}

	public Stopwatch stop() {
		counter++;
		long split = System.nanoTime() - start;
		total += split;
		if (split > max) {
			max = split;
		}
		if (split < min) {
			min = split;
		}
		return this;
	}

	public long getTotal() {
		return total;
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
			" total " + SimonUtils.presentNanoTime(total) +
			", counter " + counter +
			", max " + SimonUtils.presentNanoTime(max) +
			", min " + SimonUtils.presentNanoTime(min);
	}
}