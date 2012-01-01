package org.javasimon.console;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Reflection helper to get Getter methods from given class and use them to get
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
	 * Property getter method
	 */
	private final Method method;
	/**
	 * Hidden constructor use factory methods instead
	 */
	private Getter(String name, Class<T> type, Method method) {
		this.name = name;
		this.type = type;
		this.method = method;
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
	 * Get value from source object using getter method
	 * @param source Source object
	 * @return Value
	 */
	public T get(Object source) {
		try {
			if (!method.isAccessible()) {
				method.setAccessible(true);
			}
			return (T) method.invoke(source);
		} catch (IllegalAccessException illegalAccessException) {
			return null;
		} catch (IllegalArgumentException illegalArgumentException) {
			return null;
		} catch (InvocationTargetException invocationTargetException) {
			return null;
		}
	}
	/**
	 * Test whether method is a getter
	 */
	private static boolean isGetterMethod(Method method) {
		return !Modifier.isStatic(method.getModifiers())
			&& (method.getName().startsWith("get") || method.getName().startsWith("is"))
			&& method.getParameterTypes().length == 0;
	}
	/**
	 * Transform method name into property name
	 */
	private static String getPropertyName(Method method) {
		String propertyName = method.getName();
		if (propertyName.startsWith("get")) {
			propertyName = propertyName.substring(3);
		} else if (propertyName.startsWith("is")) {
			propertyName = propertyName.substring(2);
		}
		propertyName = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
		return propertyName;
	}
	/**
	 * Extract all getters for given class
	 * @param type Class
	 * @return List of getters
	 */
	public static List<Getter> getGetters(Class type) {
		List<Getter> getters = new ArrayList<Getter>();
		for (Method method : type.getMethods()) {
			if (isGetterMethod(method)) {
				String propertyName = getPropertyName(method);
				Class propertyType = method.getReturnType();
				getters.add(new Getter(propertyName, propertyType, method));
			}
		}
		return getters;
	}
	/**
	 * Search getter for given class and property name
	 * @param type Class
	 * @param name Property name
	 * @return Getter or null if not found
	 */
	public static Getter getGetter(Class type, String name) {
		for (Method method : type.getMethods()) {
			if (isGetterMethod(method)) {
				String propertyName = getPropertyName(method);
				if (name.equals(propertyName)) {
					Class propertyType = method.getReturnType();
					return new Getter(propertyName, propertyType, method);
				}
			}
		}
		return null;
	}
}
