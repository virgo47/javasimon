package org.javasimon;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * AbstractSimon implements basic enable/disable and hierarchy functionality.
 * All Simon implementations extend this class.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
abstract class AbstractSimon implements Simon {
	/**
	 * Owning manager of this Simon.
	 */
	protected Manager manager;

	/**
	 * Simon's effective state.
	 */
	protected boolean enabled;

	/**
	 * Timestamp of the first usage.
	 */
	protected long firstUsage;

	/**
	 * Timestamp of the last usage.
	 */
	protected long lastUsage;

	private final String name;

	private SimonState state = SimonState.INHERIT;

	private Simon parent;

	private final List<Simon> children = new CopyOnWriteArrayList<Simon>();

	private String note;

	private long resetTimestamp;

	private Map<String, Object> attributes;

	/**
	 * Constructor of the abstract Simon is used internally by subclasses.
	 *
	 * @param name Simon's name
	 * @param manager owning Manager
	 */
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
	 * Sets parent for this Simon - used only internally.
	 *
	 * @param parent Simon's parent
	 */
	final void setParent(Simon parent) {
		this.parent = parent;
	}

	/**
	 * Adds child to this Simon with setting the parent of the child.
	 *
	 * @param simon future child of this Simon
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

	/**
	 * Saves the timestamp when the Simon was reset.
	 */
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

	/**
	 * Returns name and state of the Simon as a human readable string.
	 *
	 * @return name and state of the Simon
	 */
	@Override
	public String toString() {
		return "[" + name + " " + state + "]";
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

	/**
	 * Stores an attribute in this Simon. Attributes can be used to store any custom objects.
	 *
	 * @param name a String specifying the name of the attribute
	 * @param value the Object to be stored
	 * @since 2.3
	 */
	public void setAttribute(String name, Object value) {
		if (attributes == null) {
			attributes = new HashMap<String, Object>();
		}
		attributes.put(name, value);
	}

	/**
	 * Returns the value of the named attribute as an Object, or null if no attribute of
	 * the given name exists.
	 *
	 * @param name a String specifying the name of the attribute
	 * @return an Object containing the value of the attribute, or null if the attribute does not exist
	 * @since 2.3
	 */
	public Object getAttribute(String name) {
		if (attributes == null) {
			return null;
		}
		return attributes.get(name);
	}

	/**
	 * Removes an attribute from this Simon.
	 *
	 * @param name a String specifying the name of the attribute to remove
	 * @since 2.3
	 */
	public void removeAttribute(String name) {
		if (attributes != null) {
			attributes.remove(name);
		}
	}

	/**
	 * Returns an Iterator containing the names of the attributes available to this Simon.
	 * This method returns an empty Iterator if the Simon has no attributes available to it.
	 *
	 * @return an Iterator of strings containing the names of the Simon's attributes
	 * @since 2.3
	 */
	public Iterator<String> getAttributeNames() {
		if (attributes == null) {
			return Collections.<String>emptySet().iterator();
		}
		return attributes.keySet().iterator();
	}

	protected void sampleCommon(Sample sample) {
		sample.setNote(note);
		sample.setFirstUsage(firstUsage);
		sample.setLastUsage(lastUsage);
		sample.setLastReset(resetTimestamp);
	}
}
