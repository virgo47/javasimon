package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * Represents single time split - one Stopwatch measurement. Object is obtained by {@link org.javasimon.Stopwatch#start()}
 * and the measurement is ended using {@link #stop()} method on this object. Split will return 0 as the result
 * if the related Stopwatch was disabled when the Split was obtained. The Split can be stopped in any other thread.
 * Split measures real time (based on {@link System#nanoTime()}), it does not measure CPU time.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @see Stopwatch
 */
public final class Split {
	private Stopwatch stopwatch;
	private long start;
	private long total;
	private boolean enabled;

	/**
	 * Creates a new Split for direct use without {@link Stopwatch}. Stop will not update any Stopwatch, value can
	 * be added to any chosen Stopwatch using {@link Stopwatch#addSplit(Split)} in conjuction with
	 * {@link #stop()} like this:
	 * <pre>Split split = new Split();
	 * ...
	 * SimonManager.getStopwatch("codeBlock2.success").addTime(split.stop());</pre>
	 *
	 * @since 3.1
	 */
	public Split() {
		start = System.nanoTime();
	}

	/**
	 * Creates a new Split for a Stopwatch with a specific timestamp in nanoseconds.
	 *
	 * @param stopwatch owning Stopwatch
	 * @param start start timestamp in nanoseconds
	 */
	Split(Stopwatch stopwatch, long start) {
		this.stopwatch = stopwatch;
		enabled = stopwatch.isEnabled();
		this.start = start;
	}

	/**
	 * Returns the stopwatch that this split is running for. May be null for directly created splits.
	 *
	 * @return owning stopwatch, may be null
	 */
	public Stopwatch getStopwatch() {
		return stopwatch;
	}

	/**
	 * Stops the split, updates the stopwatch and returns this. Returns 0 if the Split is stopped already.
	 *
	 * @return this split object
	 * @since 3.1 - previously returned split time in ns, call additional {@link #runningFor()} for the same result
	 */
	public Split stop() {
		if (stopwatch == null) {
			total = System.nanoTime() - start;
		} else if (enabled && start != 0) {
			total = ((StopwatchImpl) stopwatch).stop(this, start);
			start = 0;
		}
		return this;
	}

	/**
	 * Returns the current running nano-time from the start to the method call or the total split time
	 * if the Split has been stopped already.
	 *
	 * @return current running nano-time of the split
	 */
	public long runningFor() {
		if (total != 0) {
			return total;
		}
		if (enabled || stopwatch == null) {
			return System.nanoTime() - start;
		}
		return 0;
	}

	/**
	 * Returns printable form of how long this split was running for.
	 *
	 * @return short information about the Split  time as a human readable string
	 * @since 2.2
	 */
	public String presentRunningFor() {
		return SimonUtils.presentNanoTime(runningFor());
	}

	/**
	 * Returns true if this split was created from enabled Simon.
	 *
	 * @return true if this split was created from enabled Simon
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Returns start nano timer value - can be converted to ms timestamp using {@link SimonManager#millisForNano(long)}.
	 *
	 * @return nano timer value when the Split was started
	 * @since 3.1
	 */
	public long getStart() {
		return start;
	}

	/**
	 * Returns information about this Split, if it's running, name of the related Stopwatch and split's time.
	 *
	 * @return information about the Split as a human readable string
	 */
	@Override
	public String toString() {
		if (!enabled) {
			return "Split created from disabled Stopwatch";
		}
		if (total == 0) {
			return "Running split for Stopwatch '" + stopwatch.getName() + "': " + SimonUtils.presentNanoTime(runningFor());
		}
		return "Stopped split for Stopwatch '" + stopwatch.getName() + "': " + SimonUtils.presentNanoTime(total);
	}
}
