package org.javasimon.utils.bean;

/**
 * Converter from String to Float.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToFloatConverter implements Converter {
	private ToDoubleConverter toDoubleConverter = new ToDoubleConverter();

	@Override
	public Float convert(Class<?> targetClass, String strVal) throws ConvertException {
		Double doubleVal = toDoubleConverter.convert(Float.class, strVal);
		return doubleVal.floatValue();
	}
}
