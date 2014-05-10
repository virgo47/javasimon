package org.javasimon;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * AbstractSimon implements basic enable/disable and hierarchy functionality.
 * All Simon implementations extend this class.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
abstract class AbstractSimon implements Simon {

	/** Owning manager of this Simon. */
	protected Manager manager;

	/** Simon's effective state. */
	protected volatile boolean enabled;

	/** Timestamp of the first usage. */
	protected long firstUsage;

	/** Timestamp of the last usage. */
	protected long lastUsage;

	private final String name;

	private SimonState state = SimonState.INHERIT;

	private Simon parent;

	private final List<Simon> children = new CopyOnWriteArrayList<>();

	private String note;

	private AttributesSupport attributesSupport = new AttributesSupport();

	private Map<Object, Simon> incrementalSimons;

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

	@Override
	public final Simon getParent() {
		return parent;
	}

	@Override
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
	final synchronized void addChild(AbstractSimon simon) {
		children.add(simon);
		simon.setParent(this);
		simon.enabled = enabled;
	}

	@Override
	public final String getName() {
		return name;
	}

	public final Manager getManager() {
		return manager;
	}

	public synchronized final void setState(SimonState state, boolean overrule) {
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

	private synchronized boolean shouldBeEffectivlyEnabled() {
		if (state.equals(SimonState.INHERIT)) {
			return parent.isEnabled();
		}
		return state.equals(SimonState.ENABLED);
	}

	private synchronized void updateAndPropagateEffectiveState(boolean enabled, boolean overrule) {
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

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Updates usage statistics.
	 *
	 * @param now current millis timestamp
	 */
	void updateUsages(long now) {
		lastUsage = now;
		if (firstUsage == 0) {
			firstUsage = lastUsage;
		}
	}

	@Override
	public synchronized final SimonState getState() {
		return state;
	}

	@Override
	public String getNote() {
		return note;
	}

	@Override
	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public long getFirstUsage() {
		return firstUsage;
	}

	@Override
	public long getLastUsage() {
		return lastUsage;
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
	@Override
	public void setAttribute(String name, Object value) {
		attributesSupport.setAttribute(name, value);
	}

	/**
	 * Returns the value of the named attribute as an Object, or null if no attribute of
	 * the given name exists.
	 *
	 * @param name a String specifying the name of the attribute
	 * @return an Object containing the value of the attribute, or null if the attribute does not exist
	 * @since 2.3
	 */
	@Override
	public Object getAttribute(String name) {
		return attributesSupport.getAttribute(name);
	}

	/**
	 * Returns the value of the named attribute typed to the specified class, or {@code null} if no attribute of
	 * the given name exists.
	 *
	 * @param name a String specifying the name of the attribute
	 * @return the value of the attribute typed to T, or {@code null} if the attribute does not exist
	 * @since 3.4
	 */
	@Override
	public <T> T getAttribute(String name, Class<T> clazz) {
		return attributesSupport.getAttribute(name, clazz);
	}

	/**
	 * Removes an attribute from this Simon.
	 *
	 * @param name a String specifying the name of the attribute to remove
	 * @since 2.3
	 */
	@Override
	public void removeAttribute(String name) {
		attributesSupport.removeAttribute(name);
	}

	/**
	 * Returns an Iterator containing the names of the attributes available to this Simon.
	 * This method returns an empty Iterator if the Simon has no attributes available to it.
	 *
	 * @return an Iterator of strings containing the names of the Simon's attributes
	 * @since 2.3
	 */
	@Override
	public Iterator<String> getAttributeNames() {
		return attributesSupport.getAttributeNames();
	}

	@Override
	public Map<String, Object> getCopyAsSortedMap() {
		return attributesSupport.getCopyAsSortedMap();
	}

	void sampleCommon(Sample sample) {
		sample.setName(name);
		sample.setNote(note);
		sample.setFirstUsage(firstUsage);
		sample.setLastUsage(lastUsage);
	}

	// incremental Simons methods
	Collection<Simon> incrementalSimons() {
		return incrementalSimons != null ? incrementalSimons.values() : null;
	}

	Simon getAndResetSampleKey(Object key, Simon newSimon) {
		if (incrementalSimons == null) {
			incrementalSimons = new HashMap<>();
		}
		Simon simon = incrementalSimons.get(key);
		incrementalSimons.put(key, newSimon);
		return simon;
	}

	Sample sampleIncrementHelper(Object key, Simon newSimon) {
		Simon simon = getAndResetSampleKey(key, newSimon);
		if (simon != null) {
			return simon.sample();
		} else {
			return sample();
		}
	}

	@Override
	public synchronized boolean stopIncrementalSampling(Object key) {
		return incrementalSimons != null && incrementalSimons.remove(key) != null;
	}

	synchronized void purgeIncrementalSimonsOlderThan(long thresholdMs) {
		if (incrementalSimons == null) {
			return;
		}
		Iterator<Map.Entry<Object, Simon>> iterator = incrementalSimons.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Object, Simon> entry = iterator.next();
			if (entry.getValue().getLastUsage() < thresholdMs) {
				iterator.remove();
			}
		}
		if (incrementalSimons.isEmpty()) {
			incrementalSimons = null;
		}
	}

	/**
	 * Returns name and state of the Simon as a human readable string.
	 *
	 * @return name and state of the Simon
	 */
	@Override
	public synchronized String toString() {
		return " [" + name + " " + state +
			(getNote() != null && getNote().length() != 0 ? " \"" + getNote() + "\"]" : "]");
	}
}
