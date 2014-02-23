package org.javasimon.utils.bean;

/**
 * Converter from String to Long.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToLongConverter implements Converter {

	@Override
	public Long convert(Class<?> targetClass, String strVal) {
		if (strVal == null) {
			return null;
		}

		try {
			return Long.parseLong(strVal);
		} catch (NumberFormatException ex) {
			throw new ConvertException(
				String.format("Cannot convert string '%s' to Long", strVal));
		}
	}
}
