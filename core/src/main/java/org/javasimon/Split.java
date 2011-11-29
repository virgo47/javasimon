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
	private volatile Stopwatch stopwatch;
	private volatile long start;
	private volatile long total;
	private volatile boolean enabled;
	private volatile boolean running;

	/**
	 * Creates a new Split for direct use without {@link Stopwatch} ("anonymous split"). Stop will not update any Stopwatch,
	 * value can be added to any chosen Stopwatch using {@link Stopwatch#addSplit(Split)} in conjuction with
	 * {@link #stop()} like this:
	 *
	 * <pre>Split split = new Split();
	 * ...
	 * SimonManager.getStopwatch("codeBlock2.success").addTime(split.stop());</pre>
	 *
	 * If the split is not needed afterwards calling {@link #stop()} is not necessary:
	 *
	 * <pre>Split split = new Split();
	 * ...
	 * SimonManager.getStopwatch("codeBlock2.success").addTime(split);</pre>
	 *
	 * @since 3.1
	 */
	public Split() {
		enabled = true;
		running = true;
		start = System.nanoTime();
	}

	/**
	 * Creates a new Split for an enabled Stopwatch with a specific timestamp in nanoseconds - <b>called internally only</b>.
	 *
	 * @param stopwatch owning Stopwatch (enabled)
	 * @param start start timestamp in nanoseconds
	 */
	Split(Stopwatch stopwatch, long start) {
		assert start > 0 : "start ns value should not be 0 in this constructor!";
		assert stopwatch.isEnabled() : "stopwatch must be enabled in this constructor!";

		this.stopwatch = stopwatch;
		this.start = start;
		enabled = true;
		running = true;
	}

	/**
	 * Creates a new Split for a disabled Stopwatch - <b>called internally only</b>.
	 *
	 * @param stopwatch owning Stopwatch (disabled)
	 */
	Split(Stopwatch stopwatch) {
		assert !(stopwatch.isEnabled()) : "stopwatch must be disabled in this constructor!";

		this.stopwatch = stopwatch;
	}

	/**
	 * Returns the stopwatch that this split is running for. May be null for anonymous splits (directly created).
	 *
	 * @return owning stopwatch, may be null
	 */
	public Stopwatch getStopwatch() {
		return stopwatch;
	}

	/**
	 * Stops the split, updates the stopwatch and returns this. Subsequent calls do not change the state of the split.
	 *
	 * @return this split object
	 * @since 3.1 - previously returned split time in ns, call additional {@link #runningFor()} for the same result
	 */
	public Split stop() {
		if (!running) {
			return this;
		}
		running = false;
		long nowNanos = System.nanoTime();
		total = nowNanos - start; // we update total before calling the stop so that callbacks can use it
		if (stopwatch != null) {
			((StopwatchImpl) stopwatch).stop(this, start, nowNanos);
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
		if (!running) {
			return total;
		}
		if (enabled) {
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
	 * Returns 0 if the split is not enabled (started for disabled Stopwatch).
	 *
	 * @return nano timer value when the Split was started or 0 if the split is not enabled
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
		if (running) {
			return "Running split" + (stopwatch != null ? " for Stopwatch '" + stopwatch.getName() + "': " : ": ") + SimonUtils.presentNanoTime(runningFor());
		}
		return "Stopped split" + (stopwatch != null ? " for Stopwatch '" + stopwatch.getName() + "': " : ": ") + SimonUtils.presentNanoTime(total);
	}
}
