package org.javasimon.utils.bean;

/**
 * Converter from String to Boolean.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToBooleanConverter implements Converter {

	@Override
	public Boolean convert(Class<?> targetClass, String strVal) throws ConvertException {
		if (strVal == null) {
			return null;
		}

		strVal = strVal.toLowerCase();
		if (strVal.equals("true")) {
			return true;
		} else if (strVal.equals("false")) {
			return false;
		} else {
			throw new ConvertException(String.format("Failed to parse '%s' as Boolean", strVal));
		}
	}
}
