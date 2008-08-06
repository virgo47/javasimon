package org.javasimon;

import java.util.List;

/**
 * DisabledSimon implements emtpy action methods. Management methods however are fully
 * functional.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public class DisabledDecorator implements SimonCounter, SimonStopwatch {
	private Simon simon;

	public DisabledDecorator(Simon simon) {
		this.simon = simon;
	}

	public Simon getParent() {
		return simon.getParent();
	}

	public List<Simon> getChildren() {
		return simon.getChildren();
	}

	public void addChild(Simon simon) {
		simon.addChild(simon);
	}

	public String getName() {
		return simon.getName();
	}

	public SimonState getState() {
		return SimonState.DISABLED;
	}

	public void enable(boolean resetSubtree) {
		simon.enable(resetSubtree);
	}

	public void disable(boolean resetSubtree) {
		simon.disable(resetSubtree);
	}

	public void inheritState(boolean resetSubtree) {
		simon.inheritState(resetSubtree);
	}

	public void resetSubtree() {
		simon.resetSubtree();
	}

	public boolean isEnabled() {
		return false;
	}

	public void reset() {
		simon.reset();
	}

	// EMPTY action methods follows
	public void addTime(long ns) {
	}

	public void increment() {
	}

	public void increment(long inc) {
	}

	public void decrement() {
	}

	public void decrement(long inc) {
	}

	public void start() {
	}

	public void stop() {
	}

	public long getElapsedNanos() {
		return 0;
	}

	public long getCounter() {
		return 0;
	}

	Simon getWrappedSimon() {
		return simon;
	}

	public String toString() {
		return "DisabledDecorator for " +
			simon;
	}
}
