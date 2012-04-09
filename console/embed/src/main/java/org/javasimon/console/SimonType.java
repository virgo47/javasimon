package org.javasimon.console;

import org.javasimon.*;

/**
 * Simon type enumeration
 *
 * @author gquintana
 */
public enum SimonType {

	STOPWATCH(Stopwatch.class, StopwatchSample.class), COUNTER(Counter.class, CounterSample.class), UNKNOWN(Simon.class, UnknownSample.class);
	/**
	 * Simon Interface
	 */
	private final Class<? extends Simon> type;
	/**
	 * Sample interface
	 */
	private final Class<? extends Sample> sampleType;

	private SimonType(Class<? extends Simon> type, Class<? extends Sample> sampleType) {
		this.type = type;
		this.sampleType = sampleType;
	}

	public Class<? extends Simon> getType() {
		return type;
	}

	public Class<? extends Sample> getSampleType() {
		return sampleType;
	}

	/**
	 * Get simon type from simon class
	 *
	 * @param type Simon class
	 * @return Type
	 */
	public static SimonType getValueFromType(Class<? extends Simon> type) {
		for (SimonType simonType : values()) {
			if (simonType.getType().isAssignableFrom(type)) {
				return simonType;
			}
		}
		return null;
	}

	/**
	 * Get simon type from simon sample class
	 *
	 * @param type Sample class
	 * @return Type
	 */
	public static SimonType getValueFromSampleType(Class<? extends Sample> sampleType) {
		for (SimonType simonType : values()) {
			if (simonType.getSampleType().isAssignableFrom(sampleType)) {
				return simonType;
			}
		}
		return null;
	}

	/**
	 * Get simon type from simon instance
	 *
	 * @param simon Simon
	 * @return Type
	 */
	public static SimonType getValueFromInstance(Simon simon) {
		return simon == null ? null : getValueFromType(simon.getClass());
	}

	/**
	 * Get simon type from simon sample instance
	 *
	 * @param sample Simon sample
	 * @return Type
	 */
	public static SimonType getValueFromInstance(Sample sample) {
		return sample == null ? null : getValueFromSampleType(sample.getClass());
	}
}
