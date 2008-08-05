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

	public void enable() {
		state = SimonState.ENABLED;
	}

	public void disable() {
		state = SimonState.DISABLED;
	}

	public void inheritState() {
		state = SimonState.INHERIT;
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
