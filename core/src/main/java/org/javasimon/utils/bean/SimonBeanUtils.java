package org.javasimon.utils.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

	private final Map<Class<?>, Converter> converters = new ConcurrentHashMap<>();

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

		converters.put(char.class, new ToCharConverter());
		converters.put(Character.class, new ToCharConverter());

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
		NestedResolver resolver = new NestedResolver(target, property);

		if (value instanceof String) {
			convertStringValue(resolver.getNestedTarget(), resolver.getProperty(), (String) value);
		} else {
			setObjectValue(resolver.getNestedTarget(), resolver.getProperty(), value);
		}
	}

	private void convertStringValue(Object target, String property, String strVal) {
		Set<Method> potentialSetters = ClassUtils.getSetters(target.getClass(), property);
		boolean converted = false;

		for (Method setter : potentialSetters) {
			try {
				Class<?> setterType = ClassUtils.getSetterType(setter);
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
			Field field = ClassUtils.getField(target.getClass(), property);
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

	private void setObjectValue(Object target, String property, Object value) {
		Method setter = ClassUtils.getSetter(target.getClass(), property, value.getClass());
		if (setter != null) {
			invokeSetter(target, setter, value);
		} else {
			Field field = ClassUtils.getField(target.getClass(), property);
			if (field != null) {
				setValueUsingField(field, target, value);
			} else {
				throw new BeanUtilsException("Failed to find field/setter for property " + property);
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
