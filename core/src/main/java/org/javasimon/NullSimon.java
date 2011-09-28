package org.javasimon;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Null Simon implements Simon returned by the disabled {@link SimonManager}. Null
 * Simon does nothing, returns null or zeroes wherever return value is expected and has
 * minimal performance impact on the system.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
class NullSimon implements Simon {
	/**
	 * Internal singleton value of the null Simon. Null Simon is never used in client code, only its
	 * behavior manifestates to the client.
	 */
	static final NullSimon INSTANCE = new NullSimon();

	NullSimon() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Simon getParent() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<Simon> getChildren() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimonState getState() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setState(SimonState state, boolean overrule) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullSimon reset() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLastReset() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getFirstUsage() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getLastUsage() {
		return 0;
	}

	/**
	 * Returns note for the Simon.
	 *
	 * @return note for the Simon.
	 */
	public String getNote() {
		return null;
	}

	/**
	 * Sets note for the Simon.
	 *
	 * @param note note for the Simon.
	 */
	public void setNote(String note) {
	}

	/**
	 * {@inheritDoc}
	 */
	public Sample sample() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Sample sampleAndReset() {
		return null;
	}

	/**
	 * Does nothing.
	 *
	 * @param name ignored
	 * @param value ignored
	 */
	public void setAttribute(String name, Object value) {
	}

	/**
	 * Returns null.
	 *
	 * @param name ignored
	 * @return null
	 */
	public Object getAttribute(String name) {
		return null;
	}

	/**
	 * Does nothing.
	 */
	public void removeAttribute(String name) {
	}

	/**
	 * Returns empty iterator.
	 *
	 * @return empty iterator
	 */
	public Iterator<String> getAttributeNames() {
		return Collections.<String>emptySet().iterator();
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

final class NullStopwatch extends NullSimon implements Stopwatch {
	/**
	 * Internal singleton value of the null Simon. Null Simon is never used in client code, only its
	 * behavior manifestates to the client.
	 */
	static final NullStopwatch INSTANCE = new NullStopwatch();

	private NullStopwatch() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullStopwatch reset() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getCounter() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMin() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMax() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMaxTimestamp() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMinTimestamp() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stopwatch addTime(long ns) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Split start() {
		return new Split(this, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getTotal() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLast() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getActive() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMaxActive() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMaxActiveTimestamp() {
		return 0;
	}

	/**
	 * Returns zero.
	 *
	 * @return zero
	 */
	@Override
	public double getMean() {
		return 0;
	}

	/**
	 * Returns zero.
	 *
	 * @return zero
	 */
	@Override
	public double getStandardDeviation() {
		return 0;
	}

	/**
	 * Returns zero.
	 *
	 * @return zero
	 */
	@Override
	public double getVariance() {
		return 0;
	}

	/**
	 * Returns zero.
	 *
	 * @return zero
	 */
	@Override
	public double getVarianceN() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StopwatchSample sample() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StopwatchSample sampleAndReset() {
		return null;
	}
}

final class NullCounter extends NullSimon implements Counter {
	/**
	 * Internal singleton value of the null Counter. Null Counter is never used in client code, only its
	 * behavior manifestates to the client.
	 */
	static final NullCounter INSTANCE = new NullCounter();

	private NullCounter() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullCounter reset() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Counter set(long val) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Counter increase() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Counter decrease() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Counter increase(long inc) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Counter decrease(long dec) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getCounter() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMin() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMax() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMaxTimestamp() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMinTimestamp() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getIncrementSum() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getDecrementSum() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public CounterSample sample() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public CounterSample sampleAndReset() {
		return null;
	}
}
