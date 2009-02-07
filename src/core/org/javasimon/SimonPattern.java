package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * Matches Simon name patterns from configuration.
 */
class SimonPattern {
	private static final String WILDCARD_STAR = "*";

	private String pattern;
	private String all;
	private String start;
	private String end;
	private String middle;
	private static final String INVALID_PATTERN = "Invalid configuration pattern: ";

	/**
	 * Creates Simon name pattern used to match config file entries.
	 *
	 * @param pattern Simon name pattern
	 * @throws SimonException if pattern is not valid (runtime exception)
	 */
	SimonPattern(String pattern) {
		this.pattern = pattern;
		if (!pattern.contains(WILDCARD_STAR)) {
			all = pattern;
			if (!SimonUtils.checkName(all)) {
				throw new SimonException(INVALID_PATTERN + pattern);
			}
			return;
		}
		if (pattern.equals(WILDCARD_STAR)) {
			middle = ""; // everything contains this
			return;
		}
		if (pattern.startsWith(WILDCARD_STAR) && pattern.endsWith(WILDCARD_STAR)) {
			middle = pattern.substring(1, pattern.length() - 2);
			if (!SimonUtils.checkName(middle)) {
				throw new SimonException(INVALID_PATTERN + pattern);
			}
			return;
		}
		int ix = pattern.lastIndexOf('*');
		if (ix != pattern.indexOf('*')) {
			throw new SimonException(INVALID_PATTERN + pattern);
		}
		if (!pattern.endsWith(WILDCARD_STAR)) {
			end = pattern.substring(ix + 1);
			if (!SimonUtils.checkName(end)) {
				throw new SimonException(INVALID_PATTERN + pattern);
			}
		}
		if (!pattern.startsWith(WILDCARD_STAR)) {
			start = pattern.substring(0, ix);
			if (!SimonUtils.checkName(start)) {
				throw new SimonException(INVALID_PATTERN + pattern);
			}
		}
	}

	/**
	 * Checks if Simon name matches this pattern.
	 *
	 * @param name tested name
	 * @return true if tested pattern matches this pattern
	 */
	public boolean matches(String name) {
		if (all != null) {
			return all.equals(name);
		}
		if (middle != null) {
			return name.contains(middle);
		}
		if (start != null && !name.startsWith(start)) {
			return false;
		}
		return end == null || name.endsWith(end);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SimonPattern that = (SimonPattern) o;

		return !(pattern != null ? !pattern.equals(that.pattern) : that.pattern != null);
	}

	@Override
	public int hashCode() {
		return (pattern != null ? pattern.hashCode() : 0);
	}

	@Override
	public String toString() {
		return "ConfigPattern: " + pattern;
	}
}
