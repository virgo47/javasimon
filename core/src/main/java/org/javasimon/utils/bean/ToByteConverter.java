package org.javasimon.utils.bean;

/**
 * Converter from String to Byte.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToByteConverter implements Converter {

	@Override
	public Byte convert(Class<?> targetClass, String strVal) throws ConvertException {
		if (strVal == null) {
			return null;
		}

		try {
			return Byte.parseByte(strVal);
		} catch (NumberFormatException ex) {
			throw new ConvertException(
				String.format("Cannot convert string '%s' to Byte", strVal));
		}
	}
}
