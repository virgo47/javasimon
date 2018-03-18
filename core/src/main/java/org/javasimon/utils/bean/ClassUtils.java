package org.javasimon.utils.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility methods for working with class objects.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 * @author <a href="mailto:anton.rybochkin@axibase.com">Anton Rybochkin</a>
 */
class ClassUtils {

	private static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);

	private ClassUtils() {
		throw new AssertionError("ClassUtils is a collections of utility methods");
	}

	/**
	 * Get field with the specified name.
	 * @param targetClass class for which a field will be returned
	 * @param fieldName name of the field that should be returned
	 * @return field with the specified name if one exists, null otherwise
	 */
	static Field getField(Class<?> targetClass, String fieldName) {
		while (targetClass != null) {
			try {
				Field field = targetClass.getDeclaredField(fieldName);
				logger.debug("Found field {} in class {}", fieldName, targetClass.getName());
				return field;
			} catch (NoSuchFieldException e) {
				logger.debug("Failed to find field {} in class {}", fieldName, targetClass.getName());
			}
			targetClass = targetClass.getSuperclass();
		}

		return null;
	}

	/**
	 * Get setter for the specified property with the specified type.	 *
	 * @param targetClass class for which a setter will be returned
	 * @param propertyName name of the property for which a setter will be returned
	 * @param type a target setter accepts
	 * @return setter method for the specified property that accepts specified type if one exists, null otherwise
	 */
	static Method getSetter(Class<?> targetClass, String propertyName, Class<?> type) {
		if (targetClass == null) {
			return null;
		}
		String setterMethodName = setterName(propertyName);
		try {
			Method setter = targetClass.getMethod(setterMethodName, type);
			logger.debug("Found public setter {} in class {}", setterMethodName, setter.getDeclaringClass().getName());
			return setter;
		} catch (NoSuchMethodException e) {
			logger.debug("Failed to found public setter {} in class {}", setterMethodName, targetClass.getName());
		}
		while (targetClass != null) {
			try {
				Method setter = targetClass.getDeclaredMethod(setterMethodName, type);
				logger.debug("Found setter {} in class {}", setterMethodName, targetClass.getName());
				return setter;
			} catch (NoSuchMethodException e) {
				logger.debug("Failed to found setter {} in class {}", setterMethodName, targetClass.getName());
			}
			targetClass = targetClass.getSuperclass();
		}

		return null;
	}

	private static String setterName(String name) {
		return "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

	/**
	 * Get all setters for the specified property
	 * @param targetClass class for which setters will be returned
	 * @param propertyName name of the property for which setters will be returned
	 * @return possible setters for the specified property	 *
	 */
	static Set<Method> getSetters(Class<?> targetClass, String propertyName) {
		String setterName = setterName(propertyName);
		Set<Method> setters = new HashSet<>();

		while (targetClass != null) {
			for (Method method : targetClass.getDeclaredMethods()) {
				if (method.getName().equals(setterName) && method.getParameterTypes().length == 1) {
					logger.debug("Found setter {} in class {}", method, targetClass);
					setters.add(method);
				}
			}
			targetClass = targetClass.getSuperclass();
		}

		return setters;
	}

	/**
	 * Get property type by a setter method.
	 * @param setter setter of Java bean class
	 * @return type of the specified setter method
	 * @throws org.javasimon.utils.bean.BeanUtilsException if specified method does not has setter signature
	 */
	static Class<?> getSetterType(Method setter) {
		Class<?>[] parameterTypes = setter.getParameterTypes();
		if (parameterTypes.length != 1) {
			throw new BeanUtilsException(
					String.format("Method %s has %d parameters and cannot be a setter", setter.getName(), parameterTypes.length));
		}

		return parameterTypes[0];
	}

	/**
	 * Get getter method for a specified property.
	 * @param targetClass class for which a getter will be returned
	 * @param propertyName name of the property for which a getter will be returned
	 * @return getter of a specified property if one exists, null otherwise
	 */
	static Method getGetter(Class<?> targetClass, String propertyName) {
		if (targetClass == null) {
			return null;
		}
		final String getterName = getterName(propertyName, false);
		Method result = findPublicGetter(targetClass, getterName, propertyName, false);
		if (result == null) {
			final String booleanGetterName = getterName(propertyName, true);
			result = findPublicGetter(targetClass, booleanGetterName, propertyName, true);
			if (result == null) {
				do {
					result = findNonPublicGetter(targetClass, getterName, propertyName, false);
					if (result == null) {
						result = findNonPublicGetter(targetClass, booleanGetterName, propertyName, true);
					}
				} while (result == null && (targetClass = targetClass.getSuperclass()) != null);
			}
		}

		return result;
	}

	private static Method findPublicGetter(Class<?> targetClass, String getterName, String propertyName, boolean logError) {
		try {
			Method getter = targetClass.getMethod(getterName);
			logger.debug("Found public getter {} in class {}", getter.getName(), getter.getDeclaringClass().getName());
			return getter;
		} catch (NoSuchMethodException e) {
			if (logError) {
				logger.debug("Failed to find public getter for property {} in class {}", propertyName, targetClass.getName());
			}
			return null;
		}
	}

	private static Method findNonPublicGetter(Class<?> targetClass, String getterName, String propertyName, boolean logError) {
		try {
			Method getter = targetClass.getDeclaredMethod(getterName);
			logger.debug("Found getter {} in class {}", getter.getName(), targetClass.getName());
			return getter;
		} catch (NoSuchMethodException e) {
			if (logError) {
				logger.debug("Failed to find getter for property {} in class {}", propertyName, targetClass.getName());
			}
			return null;
		}
	}

	private static String getterName(String propertyName, boolean isBooleanProperty) {
		final String prefix = isBooleanProperty ? "is" : "get";
		return prefix + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
	}
}
