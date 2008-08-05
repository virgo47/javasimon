package org.javasimon;

import java.util.List;

/**
 * DisabledSimon implements emtpy action methods. Management methods however are fully
 * functional.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public final class DisabledSimon implements SimonCounter, SimonStopwatch {
// TODO: If we want to use covariant return types for enable/disable/inheritState,
// we have to implement disabled simon separately for every interface.

	private Simon simon;

	public DisabledSimon(Simon simon) {
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

	public void enable() {
		simon.enable();
	}

	public void disable() {
		simon.disable();
	}

	public void inheritState() {
		simon.inheritState();
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

	public void start() {
	}

	public void restart() {
	}

	public void stop() {
	}
}
