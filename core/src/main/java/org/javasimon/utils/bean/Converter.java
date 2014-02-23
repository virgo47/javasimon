package org.javasimon.utils.bean;

/**
 * Interface for converting string values to a values of a property in Java bean.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public interface Converter {
	/**
	 * Converts string values to an object of a bean property.
	 *
	 * @param targetClass type of property
	 * @param strVal value to convert
	 * @return conversion result
	 * @throws ConvertException in case if string value cannot be converted a target class
	 */
	Object convert(Class<?> targetClass, String strVal) throws ConvertException;
}
