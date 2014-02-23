package org.javasimon.utils.bean;

/**
 * Converter from String to Short.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToShortConverter implements Converter {

	@Override
	public Short convert(Class<?> targetClass, String strVal) throws ConvertException {
		if (strVal == null) {
			return null;
		}

		try {
			return Short.parseShort(strVal);
		} catch (NumberFormatException ex) {
			throw new ConvertException(
				String.format("Cannot convert string '%s' to Short", strVal));
		}
	}
}
