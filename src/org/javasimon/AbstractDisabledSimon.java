package org.javasimon;

import java.util.List;

/**
 * DisabledSimon implements emtpy action methods. Management methods however are fully
 * functional.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public abstract class AbstractDisabledSimon implements Simon {
	private Simon simon;

	public AbstractDisabledSimon(Simon simon) {
		this.simon = simon;
	}

	public final Simon getParent() {
		return simon.getParent();
	}

	public final List<Simon> getChildren() {
		return simon.getChildren();
	}

	public final String getName() {
		return simon.getName();
	}

	public final SimonState getState() {
		return SimonState.DISABLED;
	}

	public final void enable(boolean resetSubtree) {
		simon.enable(resetSubtree);
	}

	public final void disable(boolean resetSubtree) {
		simon.disable(resetSubtree);
	}

	public final void inheritState(boolean resetSubtree) {
		simon.inheritState(resetSubtree);
	}

	public final void resetSubtreeState() {
		simon.resetSubtreeState();
	}

	public final boolean isEnabled() {
		return false;
	}

	public final void reset() {
		simon.reset();
	}

	public final Simon getDisabledDecorator() {
		return this;
	}

	public final Simon getWrappedSimon() {
		return simon;
	}

	public String toString() {
		return "DisabledDecorator for " +
			simon;
	}
}
