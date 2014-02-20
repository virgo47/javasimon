package org.javasimon.javaee;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

/**
 * Class for converting String values to an instance of an enum of the specified type.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
class EnumConverter implements Converter {

	@Override
	public <T> T convert(Class<T> tClass, Object o) {
		if (! (o instanceof String)) {
			throw new ConversionException(
					String.format("Cannot convert %s to enum. Only conversion from String is supported", o.getClass().getName()));
		}

		String enumValName = (String) o;
		Enum[] enumConstants = (Enum[]) tClass.getEnumConstants();

		for (Enum enumConstant : enumConstants) {
			if (enumConstant.name().equals(enumValName)) {
				return (T) enumConstant;
			}
		}

		throw new ConversionException(String.format("Failed to convert %s value to %s class", enumValName, tClass.toString()));
	}
}
