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
	STOPWATCH(Stopwatch.class), COUNTER(Counter.class), UNKNOWN(Simon.class);
	/**
	 * Interface
	 */
	private final Class<? extends Simon> type;

	private SimonType(Class<? extends Simon> type) {
		this.type = type;
	}

	public Class<? extends Simon> getType() {
		return type;
	}
	
	/**
	 * Get simon type from simon class
	 * @param type Simon class
	 * @return Type
	 */
	public static SimonType getValueFromType(Class<? extends Simon> type) {
		for(SimonType simonType:values()) {
			if (simonType.getType().isAssignableFrom(type)) {
				return simonType;
			}
		}
		return null;
	}
	/**
	 * Get simon type from simon instance
	 * @param simon Simon
	 * @return Type
	 */
	public static SimonType getValueFromInstance(Simon simon) {
		return simon==null?null:getValueFromType(simon.getClass());
	}
}
