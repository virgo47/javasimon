package org.javasimon;

import java.util.List;
import java.util.ArrayList;

/**
 * AbstractSimon implements basic enable/disable and hierarchy functionality.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public abstract class AbstractSimon implements Simon {
	private String name;

	private SimonState state = SimonState.INHERIT;

	private Simon parent;

	private List<Simon> children = new ArrayList<Simon>();

	public AbstractSimon(String name) {
		this.name = name;
	}

	public Simon getParent() {
		return parent;
	}

	void setParent(Simon parent) {
		this.parent = parent;
	}

	public List<Simon> getChildren() {
		return children;
	}

	public void addChild(Simon simon) {
		children.add(simon);
		((AbstractSimon) simon).setParent(this);
	}

	public String getName() {
		return name;
	}

	public void enable(boolean resetSubtree) {
		state = SimonState.ENABLED;
		if (resetSubtree) {
			resetSubtree();
		}
	}

	public void disable(boolean resetSubtree) {
		state = SimonState.DISABLED;
		if (resetSubtree) {
			resetSubtree();
		}
	}

	public void inheritState(boolean resetSubtree) {
		state = SimonState.INHERIT;
		if (resetSubtree) {
			resetSubtree();
		}
	}

	public void resetSubtree() {
		for (Simon child : children) {
			child.inheritState(true);
		}
	}

	public boolean isEnabled() {
		if (state.equals(SimonState.INHERIT)) {
			return parent.isEnabled();
		}
		return state.equals(SimonState.ENABLED);
	}

	public SimonState getState() {
		return state;
	}

	public String toString() {
		return "[" + name + " (" + state + ")]";
	}
}
