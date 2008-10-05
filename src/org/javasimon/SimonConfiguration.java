package org.javasimon;

/**
 * Stores configuration for particular Simon or the set of Simons.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Sep 30, 2008
 */
public final class SimonConfiguration {
	private String type;
	private StatProcessorType statProcessorType;
	private SimonState state;

	/**
	 * Creates SimonConfiguration item.
	 *
	 * @param type Simon type
	 * @param statProcessorType stat processor type
	 * @param state prefered state - SimonManager sets ihnerit if null is specified here
	 */
	SimonConfiguration(String type, StatProcessorType statProcessorType, SimonState state) {
		this.type = type;
		this.statProcessorType = statProcessorType;
		this.state = state;
	}

	/**
	 * Returns Simon type for this configuration item.
	 *
	 * @return configured Simon type or null if nothing was specified
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns stat processor type for this configuration item.
	 *
	 * @return configured processor type or null if nothing was specified
	 */
	public StatProcessorType getStatProcessorType() {
		return statProcessorType;
	}

	/**
	 * Returns Simon state for this configuration item.
	 *
	 * @return configured Simon state or null if nothing was specified
	 */
	public SimonState getState() {
		return state;
	}

	@Override
	public String toString() {
		return "SimonConfiguration {\n" +
			"  type=" + type + "\n" +
			"  statProcessorType=" + statProcessorType + "\n" +
			"  state=" + state + "\n}";
	}
}
