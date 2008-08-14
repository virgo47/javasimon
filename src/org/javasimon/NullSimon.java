package org.javasimon;

import java.util.List;

/**
 * DisabledSimon implements emtpy action methods. Management methods however are fully
 * functional.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
final class NullSimon implements Counter, Stopwatch {
	public static final NullSimon INSTANCE = new NullSimon();

	private NullSimon() {
	}

	public Simon getParent() {
		return null;
	}

	public final List<Simon> getChildren() {
		return null;
	}

	public String getName() {
		return null;
	}

	public SimonState getState() {
		return null;
	}

	public void setState(SimonState state, boolean overrule) {
		if (state == null) {
			throw new IllegalArgumentException();
		}
	}

	public boolean isEnabled() {
		return false;
	}

	public NullSimon reset() {
		return this;
	}

	public Counter increment() {
		return this;
	}

	public Counter decrement() {
		return this;
	}

	public Counter increment(long inc) {
		return this;
	}

	public Counter decrement(long inc) {
		return this;
	}

	public long getCounter() {
		return 0;
	}

	public long getMin() {
		return 0;
	}

	public long getMax() {
		return 0;
	}

	public Stopwatch addTime(long ns) {
		return this;
	}

	public Stopwatch start() {
		return this;
	}

	public Stopwatch stop() {
		return this;
	}

	public long getTotal() {
		return 0;
	}

	public StatProcessor getStatProcessor() {
		return NullStatProcessor.INSTANCE;
	}

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

	public String toString() {
		return "Null Simon";
	}
}
