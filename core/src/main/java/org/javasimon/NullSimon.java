package org.javasimon;

import org.javasimon.clock.Clock;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Null Simon implements Simon returned by the disabled {@link Manager#getSimon(String)}
 * or {@link org.javasimon.Manager#getRootSimon()}. Null Simon does nothing, returns {@code null}
 * or zeroes wherever return value is expected ({@code Double.NaN} for statistics)
 * and has minimal performance impact on the system.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
class NullSimon implements Simon {

	/**
	 * Internal singleton value of the null Simon. Null Simon is never directly used in the client code,
	 * it is always hidden behind the {@link Simon} interface - only its behavior manifests to the client.
	 */
	static final NullSimon INSTANCE = new NullSimon();

	/** Used only by the subclasses, otherwise should not be used at all except for a single {@link #INSTANCE}. */
	NullSimon() {
	}

	@Override
	public Simon getParent() {
		return null;
	}

	@Override
	public final List<Simon> getChildren() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Manager getManager() {
		return null;
	}

	@Override
	public SimonState getState() {
		return null;
	}

	@Override
	public void setState(SimonState state, boolean overrule) {
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public long getFirstUsage() {
		return 0;
	}

	@Override
	public long getLastUsage() {
		return 0;
	}

	/**
	 * Returns note for the Simon.
	 *
	 * @return note for the Simon.
	 */
	@Override
	public String getNote() {
		return null;
	}

	/**
	 * Sets note for the Simon.
	 *
	 * @param note note for the Simon.
	 */
	@Override
	public void setNote(String note) {
	}

	@Override
	public Sample sample() {
		return null;
	}

	@Override
	public Sample sampleIncrement(Object key) {
		return null;
	}

	@Override
	public boolean stopIncrementalSampling(Object key) {
		return false;
	}

	/**
	 * Does nothing.
	 *
	 * @param name ignored
	 * @param value ignored
	 */
	@Override
	public void setAttribute(String name, Object value) {
	}

	/**
	 * Returns {@code null}.
	 *
	 * @param name ignored
	 * @return {@code null}
	 */
	@Override
	public Object getAttribute(String name) {
		return null;
	}

	/**
	 * Returns {@code null}.
	 *
	 * @param name ignored
	 * @param clazz ignored
	 * @return {@code null}
	 */
	@Override
	public <T> T getAttribute(String name, Class<T> clazz) {
		return null;
	}

	/**
	 * Does nothing.
	 *
	 * @param name ignored
	 */
	@Override
	public void removeAttribute(String name) {
	}

	/**
	 * Returns empty iterator.
	 *
	 * @return empty iterator
	 */
	@Override
	public Iterator<String> getAttributeNames() {
		return Collections.<String>emptySet().iterator();
	}

	/**
	 * Returns empty set.
	 *
	 * @return empty set
	 */
	@Override
	public Map<String, Object> getCopyAsSortedMap() {
		return Collections.emptyMap();
	}

	/**
	 * Returns string {@code Null Simon}.
	 *
	 * @return string {@code Null Simon}
	 */
	@Override
	public String toString() {
		return "Null Simon";
	}
}

/**
 * Null Stopwatch implements Simon returned by the disabled {@link Manager#getStopwatch(String)}.
 * It extends {@link NullSimon} to satisfy {@link Stopwatch} interface.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
final class NullStopwatch extends NullSimon implements Stopwatch {

	/**
	 * Internal singleton value of the null Stopwatch. Null Stopwatch is never directly used in the client code,
	 * it is always hidden behind the {@link Stopwatch} interface - only its behavior manifests to the client.
	 */
	static final NullStopwatch INSTANCE = new NullStopwatch();

	private static final Split NULL_SPLIT = new Split(INSTANCE, Clock.SYSTEM);

	private NullStopwatch() {
	}

	@Override
	public long getCounter() {
		return 0;
	}

	@Override
	public long getMin() {
		return 0;
	}

	@Override
	public long getMax() {
		return 0;
	}

	@Override
	public long getMaxTimestamp() {
		return 0;
	}

	@Override
	public long getMinTimestamp() {
		return 0;
	}

	@Override
	public Stopwatch addSplit(Split split) {
		return this;
	}

	@Override
	public Split start() {
		return NULL_SPLIT;
	}

	@Override
	public long getTotal() {
		return 0;
	}

	@Override
	public long getLast() {
		return 0;
	}

	@Override
	public long getActive() {
		return 0;
	}

	@Override
	public long getMaxActive() {
		return 0;
	}

	@Override
	public long getMaxActiveTimestamp() {
		return 0;
	}

	/**
	 * Returns {@code Double.NaN}.
	 *
	 * @return {@code Double.NaN}
	 */
	@Override
	public double getMean() {
		return Double.NaN;
	}

	/**
	 * Returns {@code Double.NaN}.
	 *
	 * @return {@code Double.NaN}
	 */
	@Override
	public double getStandardDeviation() {
		return Double.NaN;
	}

	/**
	 * Returns {@code Double.NaN}.
	 *
	 * @return {@code Double.NaN}
	 */
	@Override
	public double getVariance() {
		return Double.NaN;
	}

	/**
	 * Returns {@code Double.NaN}.
	 *
	 * @return {@code Double.NaN}
	 */
	@Override
	public double getVarianceN() {
		return Double.NaN;
	}

	@Override
	public StopwatchSample sample() {
		return null;
	}

	@Override
	public StopwatchSample sampleIncrement(Object key) {
		return null;
	}
}

/**
 * Null Counter implements Simon returned by the disabled {@link Manager#getCounter(String)}.
 * It extends {@link NullSimon} to satisfy {@link Counter} interface.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
final class NullCounter extends NullSimon implements Counter {

	/**
	 * Internal singleton value of the null Counter. Null Counter is never directly used in the client code,
	 * it is always hidden behind the {@link Counter} interface - only its behavior manifests to the client.
	 */
	static final NullCounter INSTANCE = new NullCounter();

	private NullCounter() {
	}

	@Override
	public Counter set(long val) {
		return this;
	}

	@Override
	public Counter increase() {
		return this;
	}

	@Override
	public Counter decrease() {
		return this;
	}

	@Override
	public Counter increase(long inc) {
		return this;
	}

	@Override
	public Counter decrease(long dec) {
		return this;
	}

	@Override
	public long getCounter() {
		return 0;
	}

	@Override
	public long getMin() {
		return 0;
	}

	@Override
	public long getMax() {
		return 0;
	}

	@Override
	public long getMaxTimestamp() {
		return 0;
	}

	@Override
	public long getMinTimestamp() {
		return 0;
	}

	@Override
	public long getIncrementSum() {
		return 0;
	}

	@Override
	public long getDecrementSum() {
		return 0;
	}

	@Override
	public CounterSample sample() {
		return null;
	}

	@Override
	public CounterSample sampleIncrement(Object key) {
		return null;
	}
}
