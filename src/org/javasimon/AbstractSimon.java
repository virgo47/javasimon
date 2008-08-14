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
	protected boolean enabled;

	private String name;

	private SimonState state = SimonState.INHERIT;

	private Simon parent = null;

	private List<Simon> children = new ArrayList<Simon>();

	private StatProcessor statProcessor = NullStatProcessor.INSTANCE;

	private String note;

	AbstractSimon(String name) {
		this.name = name;
		if (name == null || name.equals(SimonFactory.ROOT_SIMON_NAME)) {
			state = SimonState.ENABLED;
			enabled = true;
		}
	}

	/**
	 * Returns parent simon.
	 *
	 * @return parent simon
	 */
	public final Simon getParent() {
		return parent;
	}

	/**
	 * Returns list of children - direct sub-simons.
	 *
	 * @return list of children
	 */
	public final List<Simon> getChildren() {
		return Collections.unmodifiableList(children);
	}

	/**
	 * Sets the parent - used only by the factory/API.
	 *
	 * @param parent assigned parent Simon
	 */
	final void setParent(Simon parent) {
		this.parent = parent;
	}

	/**
	 * Adds new child - used only by the factory/API.
	 *
	 * @param simon added child Simon
	 */
	final void addChild(AbstractSimon simon) {
		children.add(simon);
		simon.setParent(this);
		simon.enabled = enabled;
	}

	/**
	 * Returns Simon name. Simon name is always fully qualified
	 * and determines also position of the Simon in the monitor hierarchy.
	 *
	 * @return name of the Simon
	 */
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
		if (this.enabled != enabled) {
			if (enabled) {
				enabledObserver();
			} else {
				disabledObserver();
			}
		}
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

	/**
	 * Called if the effective state of the Simon is changed to disabled.
	 */
	protected void disabledObserver() {
	}

	/**
	 * Called if the effective state of the Simon is changed to enabled.
	 */
	protected void enabledObserver() {
	}

	/**
	 * Returns true, if the Simon is enabled or if the enabled state is inherited.
	 *
	 * @return true, if the Simon is effectively enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Returns state of the Simon that can be enabled, disabled or ihnerited.
	 *
	 * @return state of the Simon
	 */
	public final SimonState getState() {
		return state;
	}

	/**
	 * Returns statistics processor assigned to the Simon. StatProcessor extends Simon
	 * with providing more statistic information about measured data (observations).
	 *
	 * @return statistics processor
	 */
	public StatProcessor getStatProcessor() {
		return statProcessor;
	}

	/**
	 * Sets statistics processor assigned to the Simon. StatProcessor extends Simon
	 * with providing more statistic information about measured data (observations).
	 *
	 * @param statProcessor statistics processor
	 */
	public void setStatProcessor(StatProcessor statProcessor) {
		this.statProcessor = statProcessor;
	}

	/**
	 * Returns note for the Simon. Note allows you to add additional information in human
	 * readable form to the Simon.
	 *
	 * @return note for the Simon.
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Sets note for the Simon. Note allows you to add additional information in human
	 * readable form to the Simon.
	 *
	 * @param note note for the Simon.
	 */
	public void setNote(String note) {
		this.note = note;
	}

	public String toString() {
		return "[" + name + " " + state + "/opt=" + getStatProcessor().getType() + "]";
	}

	/**
	 * Replaces one of the children for a new one (unknown to concrete). Used only by the factory/API.
	 *
	 * @param simon original Simon (unknown)
	 * @param newSimon new Simon
	 */
	void replaceChild(Simon simon, AbstractSimon newSimon) {
		children.remove(simon);
		children.add(newSimon);
		newSimon.setParent(this);
	}
}
