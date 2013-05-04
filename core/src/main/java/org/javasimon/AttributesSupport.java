package org.javasimon;

import java.util.*;

/**
 * Attributes implementation that creates attributes map lazily. Is synchronized to ensure thread-safety.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 3.4
 */
final class AttributesSupport implements HasAttributes {
	private Map<String, Object> attributes;

	@Override
	public synchronized void setAttribute(String name, Object value) {
		if (attributes == null) {
			attributes = new HashMap<String, Object>();
		}
		attributes.put(name, value);
	}

	@Override
	public synchronized Object getAttribute(String name) {
		if (attributes == null) {
			return null;
		}
		return attributes.get(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAttribute(String name, Class<T> clazz) {
		return (T) getAttribute(name);
	}

	@Override
	public synchronized void removeAttribute(String name) {
		if (attributes != null) {
			attributes.remove(name);
		}
	}

	@Override
	public synchronized Iterator<String> getAttributeNames() {
		if (attributes == null) {
			return Collections.<String>emptySet().iterator();
		}
		return attributes.keySet().iterator();
	}

	@Override
	public Map<String, Object> getCopyAsSortedMap() {
		return new TreeMap<String, Object>(attributes);
	}
}
