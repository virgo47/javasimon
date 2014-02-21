package org.javasimon.utils.bean;

/**
 * Class for converting String values to an instance of an enum of the specified type.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToEnumConverter implements Converter {
	@Override
	public Object convert(Class<?> tClass, String strVal) throws ConvertException {

		Enum[] enumConstants = (Enum[]) tClass.getEnumConstants();

		for (Enum enumConstant : enumConstants) {
			if (enumConstant.name().equals(strVal)) {
				return enumConstant;
			}
		}

		throw new ConvertException(String.format("Failed to convert %s value to %s class", strVal, tClass.toString()));
	}
}
