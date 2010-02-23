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
 * @created Aug 4, 2008
 */
final class NullSimon implements Counter, Stopwatch {
	/**
	 * Internal singleton value of the null Simon. Null Simon is never used in client code, only its
	 * behavior manifestates to the client.
	 */
	static final NullSimon INSTANCE = new NullSimon();

	private NullSimon() {
	}

	/**
	 * {@inheritDoc}
	 */
	public Simon getParent() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public final List<Simon> getChildren() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public SimonState getState() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setState(SimonState state, boolean overrule) {
		if (state == null) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEnabled() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public NullSimon reset() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getLastReset() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter set(long val) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter increase() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter decrease() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter increase(long inc) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter decrease(long dec) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getCounter() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getMin() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getMax() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getMaxTimestamp() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getMinTimestamp() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public Stopwatch addTime(long ns) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public Split start() {
		return new Split(this, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	public long getTotal() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getLast() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getIncrementSum() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getDecrementSum() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getActive() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getMaxActive() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getMaxActiveTimestamp() {
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
