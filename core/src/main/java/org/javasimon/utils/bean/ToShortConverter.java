package org.javasimon.utils.bean;

/**
 * Converter from String to Short.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToShortConverter implements Converter {
	private ToLongConverter toLongConverter = new ToLongConverter();

	@Override
	public Object convert(Class<?> targetClass, String strVal) throws ConvertException {
		Long longResult = toLongConverter.convert(Short.class, strVal);
		return longResult.shortValue();
	}
}
