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

	public void reset() {
	}

	public void increment() {
	}

	public void decrement() {
	}

	public void increment(long inc) {
	}

	public void decrement(long inc) {
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

	public StatProcessor getObservationProcessor() {
		return null;
	}

	public String toString() {
		return "Null Simon";
	}
}
