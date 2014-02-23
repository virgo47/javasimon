package org.javasimon.utils.bean;

/**
 * Converter for String type.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToStringConverter implements Converter {

	@Override
	public String convert(Class<?> targetClass, String strVal) {
		return strVal;
	}
}
