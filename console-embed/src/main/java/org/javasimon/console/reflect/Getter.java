package org.javasimon.console.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Property getter (in the Java Bean terminalogy) with sub-type management.
 * Serves as a helper to get Getter methods from given class and use them to get
 * values from given instance
 *
 * @author gquintana
 */
public class Getter<T> {

	/**
	 * Property name
	 */
	private final String name;
	/**
	 * Property type
	 */
	private final Class<T> type;
	/**
	 * Property sub type
	 */
	private final String subType;
	/**
	 * Property getter method
	 */
	private final Method method;
	/**
	 * Hidden constructor use factory methods instead
	 */
	Getter(String name, Class<T> type, String subType, Method method) {
		this.name = name;
		this.type = type;
		this.method = method;
		this.subType = subType;
	}
	/**
	 * Getter method
	 */
	public Method getMethod() {
		return method;
	}
	/**
	 * Property name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Property type
	 */
	public Class<T> getType() {
		return type;
	}
	/**
	 * Property sub type
	 */
	public String getSubType() {
		return subType;
	}

	/**
	 * Get value from source object using getter method
	 * @param source Source object
	 * @return Value
	 */
	public T get(Object source) {
		try {
			if (!method.isAccessible()) {
				method.setAccessible(true);
			}
			@SuppressWarnings("unchecked")
			T value=(T) method.invoke(source);
			return value;
		} catch (IllegalAccessException illegalAccessException) {
			return null;
		} catch (IllegalArgumentException illegalArgumentException) {
			return null;
		} catch (InvocationTargetException invocationTargetException) {
			return null;
		}
	}
}
