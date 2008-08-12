package org.javasimon;

/**
 * Status of monitor. While effective state can be either enabled or disabled, inner
 * state of monitor accepts one addtional state - INHERIT, which means that effective
 * state will be inherited recursively from the parent.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public enum SimonState {
	/**
	 * Enabled.
	 */
	ENABLED,

	/**
	 * Disabled.
	 */
	DISABLED,

	/**
	 * Effective state (enabled/disabled) is going to be derived from the parent.
	 */
	INHERIT
}
