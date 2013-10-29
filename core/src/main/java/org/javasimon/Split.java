package org.javasimon;

import java.util.Iterator;
import java.util.Map;

import org.javasimon.utils.SimonUtils;

/**
 * Represents single time split - one Stopwatch measurement. Object is obtained by {@link org.javasimon.Stopwatch#start()}
 * and the measurement is ended using {@link #stop()} method on this object. Split will return 0 as the result
 * if the related Stopwatch was disabled when the Split was obtained. The Split can be stopped in any other thread.
 * Split measures real time (based on {@link System#nanoTime()}), it does not measure CPU time. Split can be garbage collected
 * and no resource leak occurs if it is not stopped, however Stopwatch's active counter ({@link org.javasimon.Stopwatch#getActive()})
 * will be stay incremented.
 * <p/>
 * Split can never be running ({@link #isRunning()}) if it is disabled. Enabled split is running until it is stopped.
 * Stopped split (not running) will never again be running. Split never changes enabled flag after creation.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @see Stopwatch
 */
public final class Split implements HasAttributes {
	/**
	 * Disabled split (implies not running) for cases where monitoring is disabled and {@code null} value is not an option.
	 */
	public static final Split DISABLED = new Split(false);

	/**
	 * Attribute name under which effectively used stopwatch is stored if the split was stopped with {@link #stop(String)}.
	 */
	public static final String ATTR_EFFECTIVE_STOPWATCH = "effective-stopwatch";

	private volatile Stopwatch stopwatch;
	private final boolean enabled;
	private volatile boolean running;

	private volatile long start;
	private volatile long total;

	private AttributesSupport attributesSupport = new AttributesSupport();

	private Split(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Creates a new Split for an enabled Stopwatch with a specific timestamp in nanoseconds - <b>called internally only</b>.
	 *
	 * @param stopwatch owning Stopwatch (enabled)
	 * @param start start timestamp in nanoseconds
	 */
	Split(Stopwatch stopwatch, long start) {
		assert start > 0 : "start ns value should not be 0 in this constructor!";

		this.stopwatch = stopwatch;
		this.start = start;
		enabled = true;
		running = true;
	}

	/**
	 * Creates a new Split for a disabled Stopwatch - <b>called internally only</b>.
	 * Disabled split
	 *
	 * @param stopwatch owning Stopwatch (disabled)
	 */
	Split(Stopwatch stopwatch) {
		assert !(stopwatch.isEnabled()) : "stopwatch must be disabled in this constructor!";
		this.enabled = false;
		this.stopwatch = stopwatch;
	}

	/**
	 * Creates a new Split for direct use without {@link Stopwatch} ("anonymous split"). Stop will not update any Stopwatch,
	 * value can be added to any chosen Stopwatch using {@link Stopwatch#addSplit(Split)} - even in conjunction with
	 * {@link #stop()} like this:
	 * <p/>
	 * <pre>Split split = Split.start();
	 * ...
	 * SimonManager.getStopwatch("codeBlock2.success").addSplit(split.stop());</pre>
	 * <p/>
	 * If the split is not needed afterwards calling {@link #stop()} is not necessary:
	 * <p/>
	 * <pre>Split split = Split.start();
	 * ...
	 * SimonManager.getStopwatch("codeBlock2.success").addSplit(split);</pre>
	 *
	 * @since 3.4
	 */
	public static Split start() {
		Split split = new Split(true);
		split.running = true;
		split.start = System.nanoTime();
		return split;
	}

	/**
	 * Creates simulated non-running Split that took specific time in nanos.
	 *
	 * @param nanos Split's total time in nanos
	 * @return created Split
	 */
	public static Split create(long nanos) {
		Split split = new Split(true);
		split.start = System.nanoTime();
		split.total = nanos;
		return split;
	}

	/**
	 * Returns the stopwatch that this split is running for. May be {@code null} for anonymous splits (directly created).
	 *
	 * @return owning stopwatch, may be {@code null}
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
		return stop(null);
	}

	/**
	 * Stops the split, updates the sub-stopwatch specified by parameter and returns this. Active counter of the original stopwatch
	 * is decreased as expected. Subsequent calls do not change the state of the split.
	 * <p/>
	 * <b>Important:</b> While {@link #stop()} (or this stop with {@code null} argument)
	 * results in {@link org.javasimon.callback.Callback#onStopwatchStop(Split, StopwatchSample)} callback method being invoked,
	 * if sub-simon is affected then {@link org.javasimon.callback.Callback#onStopwatchAdd(Stopwatch, Split, StopwatchSample)}
	 * is called instead.
	 * <p/>
	 * If the split was obtained from disabled Stopwatch, this method does not update sub-simon even if it is enabled, because
	 * split itself is disabled as well. If split is enabled, but sub-simon is disabled, the latter is not updated.
	 *
	 * @param subSimon name of the sub-stopwatch (hierarchy delimiter is added automatically) - if {@code null}
	 * it behaves exactly like {@link #stop()}
	 * @return this split object
	 * @since 3.4
	 */
	public Split stop(String subSimon) {
		if (!running) {
			return this;
		}
		running = false;
		long nowNanos = System.nanoTime();
		total = nowNanos - start; // we update total before calling the stop so that callbacks can use it
		if (stopwatch != null) {
			((StopwatchImpl) stopwatch).stop(this, start, nowNanos, subSimon);
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
	 * Returns true if this split was created from enabled Simon or via {@link #start()}.
	 *
	 * @return true if this split was created from enabled Simon
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Returns true if this split is still running ({@link #stop()} has not been called yet).
	 * Returns false for disabled Split.
	 *
	 * @return true if this split is still running
	 * @since 3.1.0
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Returns start nano timer value - can be converted to ms timestamp using {@link org.javasimon.utils.SimonUtils#millisForNano(long)}.
	 * Returns 0 if the split is not enabled (started for disabled Stopwatch).
	 *
	 * @return nano timer value when the Split was started or 0 if the split is not enabled
	 * @since 3.1
	 */
	public long getStart() {
		return start;
	}

	/**
	 * Stores an attribute in this Split. Attributes can be used to store any custom objects.
	 *
	 * @param name a String specifying the name of the attribute
	 * @param value the Object to be stored
	 * @since 2.3
	 */
	@Override
	public void setAttribute(String name, Object value) {
		attributesSupport.setAttribute(name, value);
	}

	/**
	 * Returns the value of the named attribute as an Object, or {@code null} if no attribute of
	 * the given name exists.
	 *
	 * @param name a String specifying the name of the attribute
	 * @return an Object containing the value of the attribute, or {@code null} if the attribute does not exist
	 * @since 2.3
	 */
	@Override
	public Object getAttribute(String name) {
		return attributesSupport.getAttribute(name);
	}

	/**
	 * Returns the value of the named attribute typed to the specified class, or {@code null} if no attribute of
	 * the given name exists.
	 *
	 * @param name a String specifying the name of the attribute
	 * @return the value of the attribute typed to T, or {@code null} if the attribute does not exist
	 * @since 3.4
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAttribute(String name, Class<T> clazz) {
		return attributesSupport.getAttribute(name, clazz);
	}

	/**
	 * Removes an attribute from this Split.
	 *
	 * @param name a String specifying the name of the attribute to remove
	 * @since 2.3
	 */
	@Override
	public void removeAttribute(String name) {
		attributesSupport.removeAttribute(name);
	}

	/**
	 * Returns an Iterator containing the names of the attributes available to this Split.
	 * This method returns an empty Iterator if the Split has no attributes available to it.
	 *
	 * @return an Iterator of strings containing the names of the Split's attributes
	 * @since 2.3
	 */
	@Override
	public Iterator<String> getAttributeNames() {
		return attributesSupport.getAttributeNames();
	}

	@Override
	public Map<String, Object> getCopyAsSortedMap() {
		return attributesSupport.getCopyAsSortedMap();
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
