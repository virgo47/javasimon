package org.javasimon;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * AbstractSimon implements basic enable/disable and hierarchy functionality.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public abstract class AbstractSimon implements Simon {
	private String name;

	private SimonState state = SimonState.INHERIT;

	private Simon parent = null;

	private List<Simon> children = new ArrayList<Simon>();

	public AbstractSimon(String name) {
		this.name = name;
		if (name != null && name.equals(SimonFactory.ROOT_SIMON_NAME)) {
			enable(false);
		}
	}

	public final Simon getParent() {
		return parent;
	}

	public final List<Simon> getChildren() {
		return Collections.unmodifiableList(children);
	}

	final void setParent(Simon parent) {
		this.parent = parent;
	}

	public final void addChild(AbstractSimon simon) {
		children.add(simon);
		simon.setParent(this);
	}

	public final String getName() {
		return name;
	}

	public final void enable(boolean resetSubtree) {
		state = SimonState.ENABLED;
		if (resetSubtree) {
			resetSubtreeState();
		}
	}

	public final void disable(boolean resetSubtree) {
		state = SimonState.DISABLED;
		if (resetSubtree) {
			resetSubtreeState();
		}
	}

	public final void inheritState(boolean resetSubtree) {
		if (name != null && !name.equals(SimonFactory.ROOT_SIMON_NAME)) {
			state = SimonState.INHERIT;
		}
		if (resetSubtree) {
			resetSubtreeState();
		}
	}

	public final void resetSubtreeState() {
		for (Simon child : children) {
			child.inheritState(true);
		}
	}

	public final boolean isEnabled() {
		if (state.equals(SimonState.INHERIT)) {
			return parent.isEnabled();
		}
		return state.equals(SimonState.ENABLED);
	}

	public final SimonState getState() {
		return state;
	}

	public String toString() {
		return "[" + name + " (" + state + ")]";
	}
}
