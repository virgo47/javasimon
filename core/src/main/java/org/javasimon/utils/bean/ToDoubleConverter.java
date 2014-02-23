package org.javasimon.utils.bean;

/**
 * Converter from String to Double.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToDoubleConverter implements Converter {

	@Override
	public Double convert(Class<?> targetClass, String strVal) throws ConvertException {
		if (strVal == null) {
			return null;
		}

		try {
			return Double.parseDouble(strVal);
		} catch (NumberFormatException ex) {
			throw new ConvertException(String.format("Failed to parse '%s' to Double", strVal));
		}
	}
}
