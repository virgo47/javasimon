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
		String setterMethodName = setterName(propertyName);

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
		return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
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
		String getterName = getterName(propertyName);

		while (targetClass != null) {
			try {
				Method getter = targetClass.getDeclaredMethod(getterName);
				logger.debug("Found getter {} in class {}", getter.getName(), targetClass.getName());
				return getter;
			} catch (NoSuchMethodException e) {
				logger.debug("Failed  to find getter for property {} in class {}", propertyName, targetClass.getName());
			}
			targetClass = targetClass.getSuperclass();
		}

		return null;
	}

	private static String getterName(String propertyName) {
		return "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	}
}
