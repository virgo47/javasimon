package org.javasimon.utils.bean;

/**
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToCharConverter implements Converter {
	@Override
	public Character convert(Class<?> targetClass, String strVal) throws ConvertException {
		if (strVal == null) {
			return null;
		}

		if (strVal.length() == 1) {
			return strVal.charAt(0);
		}

		throw new ConvertException(String.format("Failed to convert '%s' to character", strVal));
	}
}
