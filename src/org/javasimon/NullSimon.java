package org.javasimon;

import java.util.List;
import java.util.Map;
import java.util.Collections;

/**
 * Null Simon implements Simon returned by the disabled {@link SimonManager}. Null
 * Simon does nothing, returns empty values or zeroes wherever return value is expected and has
 * minimal performance impact on the system.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
final class NullSimon implements Counter, Stopwatch {
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
	public Counter set(long val) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter increment() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter decrement() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter increment(long inc) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter decrement(long dec) {
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
	public Stopwatch start() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public long stop() {
		return 0;
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
	 * {@inheritDoc}
	 */
	public StatProcessor getStatProcessor() {
		return NullStatProcessor.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setStatProcessor(StatProcessor statProcessor) {
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

	public Map<String, String> sample(boolean reset) {
		return Collections.emptyMap();
	}

	@Override
	public String toString() {
		return "Null Simon";
	}
}
