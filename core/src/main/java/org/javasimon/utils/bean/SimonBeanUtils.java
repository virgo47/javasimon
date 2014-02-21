package org.javasimon.utils.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utils for setting properties values in Java bean object. It can convert values that should be set from String
 * to a type of specified properties.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class SimonBeanUtils {
	private static final Logger logger = LoggerFactory.getLogger(SimonBeanUtils.class);

	private static final SimonBeanUtils INSTANCE = new SimonBeanUtils();

	private final Map<Class<?>, Converter> converters = new ConcurrentHashMap<Class<?>, Converter>();

	public SimonBeanUtils() {
		converters.put(String.class, new ToStringConverter());

		converters.put(boolean.class, new ToBooleanConverter());
		converters.put(Boolean.class, new ToBooleanConverter());

		converters.put(byte.class, new ToByteConverter());
		converters.put(Byte.class, new ToByteConverter());

		converters.put(short.class, new ToShortConverter());
		converters.put(Short.class, new ToShortConverter());

		converters.put(int.class, new ToIntegerConverter());
		converters.put(Integer.class, new ToIntegerConverter());

		converters.put(long.class, new ToLongConverter());
		converters.put(Long.class, new ToLongConverter());

		converters.put(float.class, new ToFloatConverter());
		converters.put(Float.class, new ToFloatConverter());

		converters.put(double.class, new ToDoubleConverter());
		converters.put(Double.class, new ToDoubleConverter());

	}

	public static SimonBeanUtils getInstance() {
		return INSTANCE;
	}

	/**
	 * Set property in object target. If values has type other than String setter method or field
	 * with specified type will be used to set value. If value has String type value will be converted
	 * using available converters. If conversion to all of the types accepted by setters fails, field
	 * with corresponding name will be used
	 *
	 * @param target Java bean where a property will be set
	 * @param property property to be set
	 * @param value value to be set
	 */
	public void setProperty(Object target, String property, Object value) {
		if (value instanceof String) {
			convertStringValue(target, property, (String) value);
		} else {
			setObjectValue(target, property, value);
		}
	}

	private void convertStringValue(Object target, String property, String strVal) {
		Set<Method> setters = getPotentialSetters(target, property);
		boolean converted = false;
		for (Method setter : setters) {
			try {
				Class<?> setterType = getSetterType(setter);
				Converter converter = getConverterTo(setterType);
				if (converter != null) {
					Object value = converter.convert(setterType, strVal);
					invokeSetter(target, setter, value);
					converted = true;
					break;
				} else {
					logger.debug(String.format("Failed to find converter for method '%s'", setter.toString()));
				}
			} catch (ConvertException ex) {
				logger.debug(String.format("Failed to convert value '%s' for method '%s'", strVal, setter.toString()));
			}
		}
		
		if (!converted) {
			Field field = getField(target, property);
			if (field != null) {
				Class<?> fieldType = field.getType();
				Converter converter = getConverterTo(fieldType);
				if (converter != null) {
					Object value = converter.convert(fieldType, strVal);
					setValueUsingField(field, target, value);
					return;
				}
			}
			throw new BeanUtilsException(
						String.format("Failed to find find setter/field for property '%s' and value '%s'", property, strVal));
		}
	}

	private Converter getConverterTo(Class<?> setterType) {
		return converters.get(setterType);
	}

	private Class<?> getSetterType(Method setter) {
		return setter.getParameterTypes()[0];
	}

	private void setObjectValue(Object target, String property, Object value) {
		Method setter = getSetterMethod(target, property, value.getClass());
		if (setter != null) {
			invokeSetter(target, setter, value);
		} else {
			Field field = getField(target, property);
			if (field != null) {
				setValueUsingField(field, target, value);
			} else {
				//
			}
		}
	}

	private void invokeSetter(Object target, Method setter, Object value) {
		try {
			setter.setAccessible(true);
			setter.invoke(target, value);
		} catch (IllegalAccessException e) {
			throw new BeanUtilsException(e);
		} catch (InvocationTargetException e) {
			throw new BeanUtilsException(e);
		}
	}

	private Set<Method> getPotentialSetters(Object target, String property) {
		Class<?> targetClass = target.getClass();
		String setterName = setterName(property);
		Set<Method> setters = new HashSet<Method>();
		for (Method method : targetClass.getDeclaredMethods()) {
			if (method.getName().equals(setterName) && method.getParameterTypes().length == 1) {
				setters.add(method);
			}
		}

		return setters;
	}

	private void setValueUsingField(Field field, Object target, Object value) {
		try {
			field.setAccessible(true);
			field.set(target, value);
		} catch (SecurityException ex) {
			throw new BeanUtilsException(ex);
		} catch (IllegalAccessException e) {
			throw new BeanUtilsException(e);
		}
	}

	private Field getField(Object target, String fieldName) {
		try {
			return target.getClass().getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

	private Method getSetterMethod(Object target, String propertyName, Class<?>... types) {
		try {
			String setterMethodName = setterName(propertyName);
			return target.getClass().getMethod(setterMethodName, types);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	private String setterName(String name) {
		return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	/**
	 * Register converter for a specified class.
	 *
	 * @param targetClass property type for which a converter will be used
	 * @param converter converter that will be used to convert String values for a specified target class
	 */
	public void registerConverter(Class<?> targetClass, Converter converter) {
		converters.put(targetClass, converter);
	}
}
