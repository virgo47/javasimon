package org.javasimon;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * AbstractSimon implements basic enable/disable and hierarchy functionality.
 * All Simon implementations extend this class.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
abstract class AbstractSimon implements Simon {
	protected Manager manager;

	protected boolean enabled;

	protected long firstUsage;

	protected long lastUsage;

	private final String name;

	private SimonState state = SimonState.INHERIT;

	private Simon parent;

	private final List<Simon> children = new CopyOnWriteArrayList<Simon>();

	private StatProcessor statProcessor = NullStatProcessor.INSTANCE;

	private String note;

	private long resetTimestamp;

	AbstractSimon(String name, Manager manager) {
		this.name = name;
		this.manager = manager;
		if (name == null || name.equals(Manager.ROOT_SIMON_NAME)) {
			state = SimonState.ENABLED;
			enabled = true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final Simon getParent() {
		return parent;
	}

	/**
	 * {@inheritDoc}
	 */
	public final List<Simon> getChildren() {
		return children;
	}

	/**
	 * {@inheritDoc}
	 */
	final void setParent(Simon parent) {
		this.parent = parent;
	}

	/**
	 * {@inheritDoc}
	 */
	final void addChild(AbstractSimon simon) {
		children.add(simon);
		simon.setParent(this);
		simon.enabled = enabled;
	}

	/**
	 * {@inheritDoc}
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
	 * {@inheritDoc}
	 */
	public boolean isEnabled() {
		return enabled;
	}

	protected void saveResetTimestamp() {
		resetTimestamp = System.currentTimeMillis();
	}

	/**
	 * {@inheritDoc}
	 */
	public long getLastReset() {
		return resetTimestamp;
	}

	/**
	 * {@inheritDoc}
	 */
	public final SimonState getState() {
		return state;
	}

	/**
	 * {@inheritDoc}
	 */
	public StatProcessor getStatProcessor() {
		return statProcessor;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setStatProcessor(StatProcessor statProcessor) {
		this.statProcessor = statProcessor;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getNote() {
		return note;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getFirstUsage() {
		return firstUsage;
	}

	/**
	 * {@inheritDoc}
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
