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
abstract class AbstractSimon implements Simon {
	private String name;

	private SimonState state = SimonState.INHERIT;

	private Simon parent = null;

	private List<Simon> children = new ArrayList<Simon>();

	protected boolean enabled;

	public AbstractSimon(String name) {
		this.name = name;
		if (name == null || name.equals(SimonFactory.ROOT_SIMON_NAME)) {
			state = SimonState.ENABLED;
			enabled = true;
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
	public final void setState(SimonState state, boolean overrule) {
		if (state == null) {
			throw new IllegalArgumentException();
		}
		// don't set inherit to anonymous (null) and root simons!
		if (!isAnonymousOrRootSimon() || !state.equals(SimonState.INHERIT)) {
			this.state = state;
			updateAndPropagateEffectiveState(shouldBeEffectivlyEnabled(), overrule);
		}
	}

	private boolean isAnonymousOrRootSimon() {
		return (name == null || name.equals(SimonFactory.ROOT_SIMON_NAME));
	}

	private boolean shouldBeEffectivlyEnabled() {
		if (state.equals(SimonState.INHERIT)) {
			return parent.isEnabled();
		}
		return state.equals(SimonState.ENABLED);
	}

	private void updateAndPropagateEffectiveState(boolean enabled, boolean overrule) {
		this.enabled = enabled;
		for (Simon child : children) {
			if (overrule) {
				((AbstractSimon) child).state = SimonState.INHERIT;
			}
			if (child.getState().equals(SimonState.INHERIT)) {
				((AbstractSimon) child).updateAndPropagateEffectiveState(enabled, overrule);
			}
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public final SimonState getState() {
		return state;
	}

	public String toString() {
		return "[" + name + " (" + state + ")]";
	}

	public void replace(Simon simon, AbstractSimon newSimon) {
		children.remove(simon);
		children.add(newSimon);
		newSimon.setParent(this);
	}
}
