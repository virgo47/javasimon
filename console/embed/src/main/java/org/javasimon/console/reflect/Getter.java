package org.javasimon.console.reflect;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.javasimon.console.SimonType;

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
	private Getter(String name, Class<T> type, String subType, Method method) {
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
	 * Constains the content of the SubTypes.properties file.
	 */
	private static final Properties subTypeProperties=new Properties();
	static {
		try {
			InputStream inputStream = Getter.class.getResourceAsStream("SubTypes.properties");
			subTypeProperties.load(inputStream);
		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
	}
	/**
	 * Get subtype for given Class and property
	 * @param type Parent class
	 * @param propertyName Property name
	 * @return Sub type name
	 */
	private static String getSubType(Class type, String propertyName) {
		@SuppressWarnings("unchecked")
		SimonType simonType=SimonType.getValueFromType(type);
		if (simonType == null) {
			simonType = SimonType.getValueFromSampleType(type);
		}
		if (simonType==null) {
			return null;
		} else {
			return subTypeProperties.getProperty(simonType.getType().getName()+"."+propertyName);
		}
	}
	/**
	 * Extract all getters for given class
	 * @param type Class
	 * @return List of getters
	 */
	@SuppressWarnings("unchecked")
	public static List<Getter> getGetters(Class type) {
		List<Getter> getters = new ArrayList<Getter>();
		for (Method method : type.getMethods()) {
			if (isGetterMethod(method)) {
				String propertyName = getPropertyName(method);
				Class propertyType = method.getReturnType();
				getters.add(new Getter(propertyName, propertyType, getSubType(type, propertyName), method));
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
	@SuppressWarnings("unchecked")
	public static Getter getGetter(Class type, String name) {
		for (Method method : type.getMethods()) {
			if (isGetterMethod(method)) {
				String propertyName = getPropertyName(method);
				if (name.equals(propertyName)) {
					Class propertyType = method.getReturnType();
					return new Getter(propertyName, propertyType, getSubType(type, propertyName), method);
				}
			}
		}
		return null;
	}
}
