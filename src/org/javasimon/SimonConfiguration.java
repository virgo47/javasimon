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

	SimonConfiguration(String type, StatProcessorType statProcessorType, SimonState state) {
		this.type = type;
		this.statProcessorType = statProcessorType;
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public StatProcessorType getStatProcessorType() {
		return statProcessorType;
	}

	public SimonState getState() {
		return state;
	}
}
