package org.javasimon.console.reflect;

import org.javasimon.console.SimonTypeFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * {@link Getter} factory.
 * Contains a cache Class &rarr; Property Name &rarr; Getter
 *
 * @author gquintana
 */
public class GetterFactory {
	private static final Map<Class, Map<String, Getter>> GETTER_CACHE = new HashMap<>();

	/** Test whether method is a getter. */
	private static boolean isGetterMethod(Method method) {
		return !Modifier.isStatic(method.getModifiers())
			&& (method.getName().startsWith("get") || method.getName().startsWith("is"))
			&& method.getParameterTypes().length == 0;
	}

	/** Transform method name into property name. */
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

	/** Constrains the content of the SubTypes.properties file. */
	private static final Properties subTypeProperties = new Properties();

	static {
		try {
			InputStream inputStream = GetterFactory.class.getResourceAsStream("SubTypes.properties");
			subTypeProperties.load(inputStream);
		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
	}

	/**
	 * Get subtype for given Class and property.
	 *
	 * @param type Parent class
	 * @param propertyName Property name
	 * @return Sub type name
	 */
	private static String getSubType(Class type, String propertyName) {
		@SuppressWarnings("unchecked")
		Class normalizedType = SimonTypeFactory.normalizeType(type);
		if (normalizedType == null) {
			return null;
		} else {
			return subTypeProperties.getProperty(normalizedType.getName() + "." + propertyName);
		}
	}

	/**
	 * Extract all getters for given class.
	 *
	 * @param type Class
	 * @return List of getters
	 */
	@SuppressWarnings("unchecked")
	public static Collection<Getter> getGetters(Class type) {
		return getGettersAsMap(type).values();
	}

	/**
	 * Extract all getters for given class.
	 *
	 * @param type Class
	 * @return Map property name &rarr; Getter
	 */
	private static Map<String, Getter> getGettersAsMap(Class type) {
		Map<String, Getter> typeGetters = GETTER_CACHE.get(type);
		if (typeGetters == null) {
			typeGetters = new HashMap<>();
			for (Method method : type.getMethods()) {
				if (isGetterMethod(method)) {
					String propertyName = getPropertyName(method);
					Class propertyType = method.getReturnType();
					//noinspection unchecked
					typeGetters.put(propertyName, new Getter(propertyName, propertyType, getSubType(type, propertyName), method));
				}
			}
			GETTER_CACHE.put(type, typeGetters);
		}
		return typeGetters;
	}

	/**
	 * Search getter for given class and property name.
	 *
	 * @param type Class
	 * @param name Property name
	 * @return Getter or null if not found
	 */
	@SuppressWarnings("unchecked")
	public static Getter getGetter(Class type, String name) {
		return getGettersAsMap(type).get(name);
	}
}
