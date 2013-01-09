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
}
