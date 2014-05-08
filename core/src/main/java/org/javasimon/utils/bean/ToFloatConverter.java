package org.javasimon.utils.bean;

/**
 * Converter from String to Float.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToFloatConverter implements Converter {

	@Override
	public Float convert(Class<?> targetClass, String strVal) throws ConvertException {
		if (strVal == null) {
			return null;
		}

		try {
			return Float.parseFloat(strVal);
		} catch (NumberFormatException ex) {
			throw new ConvertException(String.format("Failed to parse '%s' to Float", strVal));
		}
	}
}
