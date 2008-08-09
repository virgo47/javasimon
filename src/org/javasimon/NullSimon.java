package org.javasimon;

import java.util.List;

/**
 * DisabledSimon implements emtpy action methods. Management methods however are fully
 * functional.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public final class NullSimon implements Simon {
	public NullSimon() {
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

	public void setState(SimonState state, boolean resetSubtree) {
	}

	public void enable(boolean resetSubtree) {
	}

	public void disable(boolean resetSubtree) {
	}

	public void inheritState(boolean resetSubtree) {
	}

	public boolean isEnabled() {
		return false;
	}

	public void reset() {
	}

	public String toString() {
		return "Null Simon";
	}
}
