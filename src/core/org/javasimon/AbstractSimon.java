package org.javasimon;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * AbstractSimon implements basic enable/disable and hierarchy functionality.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
abstract class AbstractSimon implements Simon {
	protected boolean enabled;

	private final String name;

	private SimonState state = SimonState.INHERIT;

	private Simon parent;

	private final List<Simon> children = new CopyOnWriteArrayList<Simon>();

	private StatProcessor statProcessor = NullStatProcessor.INSTANCE;

	private String note;

	protected long firstUsage;

	protected long lastUsage;

	AbstractSimon(String name) {
		this.name = name;
		if (name == null || name.equals(Manager.ROOT_SIMON_NAME)) {
			state = SimonState.ENABLED;
			enabled = true;
		}
	}

	/**
	 * Returns parent Simon.
	 *
	 * @return parent Simon
	 */
	public final Simon getParent() {
		return parent;
	}

	/**
	 * Returns list of children - direct sub-simons.
	 *
	 * @return list of children
	 */
	public final Collection<Simon> getChildren() {
		return children;
	}

	/**
	 * Sets the parent - used only internally.
	 *
	 * @param parent assigned parent Simon
	 */
	final void setParent(Simon parent) {
		this.parent = parent;
	}

	/**
	 * Adds new child - used only internally.
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
		// don't set inherit to anonymous (null) and root Simon!
		if (!isAnonymousOrRootSimon() || !state.equals(SimonState.INHERIT)) {
			this.state = state;
			updateAndPropagateEffectiveState(shouldBeEffectivlyEnabled(), overrule);
		}
	}

	private boolean isAnonymousOrRootSimon() {
		return (name == null || name.equals(Manager.ROOT_SIMON_NAME));
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
	 * Returns note for the Simon. Note enables Simon with an additional information in human
	 * readable form.
	 *
	 * @return note for the Simon.
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Sets note for the Simon. Note enables Simon with an additional information in human
	 * readable form.
	 *
	 * @param note note for the Simon.
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * Returns ms timestamp of the first usage of this Simon. First and last usage
	 * are updated when monitor performs the measuring (start/stop/count/etc). They
	 * are not updated when values are obtained from the monitor.
	 *
	 * @return ms timestamp of the first usage
	 */
	public long getFirstUsage() {
		return firstUsage;
	}

	/**
	 * Returns ms timestamp of the last usage of this Simon. First and last usage
	 * are updated when monitor performs the measuring (start/stop/count/etc). They
	 * are not updated when values are obtained from the monitor.
	 *
	 * @return ms timestamp of the last usage
	 */
	public long getLastUsage() {
		return lastUsage;
	}

	@Override
	public String toString() {
		return "[" + name + " " + state + "/stats=" + getStatProcessor().getType() + "]";
	}

	/**
	 * Updates usage statistics.
	 */
	protected void updateUsages() {
		lastUsage = System.currentTimeMillis();
		if (firstUsage == 0) {
			firstUsage = lastUsage;
		}
	}

	/**
	 * Replaces one of the children for a new one (unknown to concrete). Used only internally.
	 *
	 * @param simon original Simon (unknown)
	 * @param newSimon new Simon
	 */
	void replaceChild(Simon simon, AbstractSimon newSimon) {
		children.remove(simon);
		if (newSimon != null) {
			children.add(newSimon);
			newSimon.setParent(this);
		}
	}
}
