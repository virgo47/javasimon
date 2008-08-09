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

	protected boolean enabled;

	public AbstractSimon(String name) {
		this.name = name;
		if (name == null || name.equals(SimonFactory.ROOT_SIMON_NAME)) {
			setState(SimonState.ENABLED, false);
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

	final void addChild(AbstractSimon simon) {
		children.add(simon);
		simon.setParent(this);
		simon.enabled = enabled;
	}

	public final String getName() {
		return name;
	}


	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException if {@code state} is {@code null}.
	 */
	public final void setState(SimonState state, boolean resetSubtree) {
		if (state == null) {
			throw new IllegalArgumentException();
		}
		switch (state) {
			case ENABLED:
				enable(resetSubtree);
				break;
			case DISABLED:
				disable(resetSubtree);
				break;
			case INHERIT:
				inheritState(resetSubtree);
				break;
			default:
				break;
		}
	}

	private void enable(boolean resetSubtree) {
		state = SimonState.ENABLED;
		if (resetSubtree) {
			resetSubtreeState();
		}
		updateAndPropagateEffectiveState(true);
	}

	private void disable(boolean resetSubtree) {
		state = SimonState.DISABLED;
		if (resetSubtree) {
			resetSubtreeState();
		}
		updateAndPropagateEffectiveState(false);
	}

	private void inheritState(boolean resetSubtree) {
		if (name != null && !name.equals(SimonFactory.ROOT_SIMON_NAME)) {
			state = SimonState.INHERIT;
			if (resetSubtree) {
				resetSubtreeState();
			}
			updateAndPropagateEffectiveState(shouldBeEffectivlyEnabled());
		}
	}

	private void updateAndPropagateEffectiveState(boolean enabled) {
		this.enabled = enabled;
		for (Simon child : children) {
			if (child.getState().equals(SimonState.INHERIT)) {
				((AbstractSimon) child).updateAndPropagateEffectiveState(enabled);
			}
		}
	}

	private void resetSubtreeState() {
		for (final Simon child : children) {
			child.setState(SimonState.INHERIT, true);
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	private boolean shouldBeEffectivlyEnabled() {
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
