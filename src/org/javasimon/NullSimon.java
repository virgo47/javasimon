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
	public SimonState getState() {
		return null;
	}

	@Override
	public void setState(SimonState state, boolean overrule) {
		if (state == null) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public NullSimon reset() {
		return this;
	}

	@Override
	public Counter set(long val) {
		return this;
	}

	@Override
	public Counter increment() {
		return this;
	}

	@Override
	public Counter decrement() {
		return this;
	}

	@Override
	public Counter increment(long inc) {
		return this;
	}

	@Override
	public Counter decrement(long dec) {
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
	public Stopwatch addTime(long ns) {
		return this;
	}

	@Override
	public Stopwatch start() {
		return this;
	}

	@Override
	public long stop() {
		return 0;
	}

	@Override
	public long getTotal() {
		return 0;
	}

	@Override
	public long getFirstUsage() {
		return 0;
	}

	@Override
	public long getLastUsage() {
		return 0;
	}

	@Override
	public StatProcessor getStatProcessor() {
		return NullStatProcessor.INSTANCE;
	}

	@Override
	public void setStatProcessor(StatProcessor statProcessor) {
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
	public Map<String, String> sample(boolean reset) {
		return Collections.emptyMap();
	}

	@Override
	public String toString() {
		return "Null Simon";
	}
}
