package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * Represents single time split - one stopwatch measurement. Object is obtained by {@link org.javasimon.Stopwatch#start()}
 * and the measurement is ended via {@link #stop()} method on this object.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Jan 7, 2009
 */
public final class Split {
	private StopwatchImpl stopwatch;
	private long start;

	Split(StopwatchImpl stopwatch, long start) {
		this.stopwatch = stopwatch;
		this.start = start;
	}

	/**
	 * Returns the stopwatch that this split is running for.
	 *
	 * @return owning stopwatch
	 */
	public Stopwatch getStopwatch() {
		return stopwatch;
	}

	/**
	 * Stops the time split.
	 *
	 * @return split time in ns
	 */
	public long stop() {
		if (stopwatch != null) {
			return stopwatch.stop(start);
		}
		return 0;
	}

	/**
	 * Returns the current running nano-time from the start to the method call.
	 *
	 * @return current running nano-time of the split
	 */
	public long runningFor() {
		if (stopwatch != null) {
			return System.nanoTime() - start;
		}
		return 0;
	}

	@Override
	public String toString() {
		return " Running split for Stopwatch '" + stopwatch.getName() + "': " + SimonUtils.presentNanoTime(runningFor());
	}
}
