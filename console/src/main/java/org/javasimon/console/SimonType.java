package org.javasimon.console;

import org.javasimon.Counter;
import org.javasimon.Simon;
import org.javasimon.Stopwatch;

/**
 * Simon type enumeration
 *
 * @author gquintana
 */
public enum SimonType {

	STOPWATCH, COUNTER, UNKNOWN;
	/**
	 * Get simon type from simon instance
	 * @param simon Simon
	 * @return Type
	 */
	public static SimonType getValueFromInstance(Simon simon) {
		SimonType lType;
		if (simon instanceof Stopwatch) {
			lType = SimonType.STOPWATCH;
		} else if (simon instanceof Counter) {
			lType = SimonType.COUNTER;
		} else {
			lType = SimonType.UNKNOWN;
		}
		return lType;
	}
}
