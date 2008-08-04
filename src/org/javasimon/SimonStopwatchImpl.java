package org.javasimon;

/**
 * Simonatch.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public class SimonStopwatchImpl extends AbstractSimon implements SimonStopwatch {
	private long elapsedNanos;

	private long counter;

	private long start;

	public SimonStopwatchImpl(String name) {
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

	public String toString() {
		return "Simon Stopwatch: " + super.toString() + " elapsedNanos=" + elapsedNanos + ", counter=" + counter;
	}

	public void start() {
		if (start != 0) {
			throw new SimonException("Simon Stopwatch '" + getName() + "' started again without previous stop!");
		}
		restart();
	}

	public void restart() {
		start = System.nanoTime();
	}

	public void stop() {
		elapsedNanos = System.nanoTime() - start;
		start = 0;
	}
}
