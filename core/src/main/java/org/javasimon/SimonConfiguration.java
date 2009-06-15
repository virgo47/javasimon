package org.javasimon;

/**
 * Stores configuration for the particular Simon or the set of Simons.
 * Currently it holds only the state of the Simon.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Sep 30, 2008
 */
public final class SimonConfiguration {
	private SimonState state;

	/**
	 * Creates SimonConfiguration item.
	 *
	 * @param state prefered state - SimonManager sets ihnerit if null is specified here
	 */
	SimonConfiguration(SimonState state) {
		this.state = state;
	}

	/**
	 * Returns Simon state for this configuration item.
	 *
	 * @return configured Simon state or null if nothing was specified
	 */
	public SimonState getState() {
		return state;
	}

	/**
	 * Returns configuration information about Simon (stat processor type and state) as a human readable string.
	 *
	 * @return configuration information about Simon as string
	 */
	@Override
	public String toString() {
		return "SimonConfiguration {\n" +
			"  state=" + state + "\n" +
			"}";
	}
}
