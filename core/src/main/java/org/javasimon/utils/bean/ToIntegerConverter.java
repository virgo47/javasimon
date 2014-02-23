package org.javasimon.utils.bean;

/**
 * Converter from String to Integer.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToIntegerConverter implements Converter {

	@Override
	public Integer convert(Class<?> targetClass, String strVal) throws ConvertException {
		if (strVal == null) {
			return null;
		}

		try {
			return Integer.parseInt(strVal);
		} catch (NumberFormatException ex) {
			throw new ConvertException(
				String.format("Cannot convert string '%s' to Integer", strVal));
		}
	}
}
