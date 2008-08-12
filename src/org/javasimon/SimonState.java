package org.javasimon;

/**
 * Status of monitor.
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
	 * Effective state (enabled/disabled) is going to be derived from a parrent monitor.
	 */
	INHERIT
	
}
