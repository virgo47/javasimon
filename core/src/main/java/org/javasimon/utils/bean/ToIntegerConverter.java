package org.javasimon.utils.bean;

/**
 * Converter from String to Integer.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToIntegerConverter implements Converter {
	private ToLongConverter toLongConverter = new ToLongConverter();

	@Override
	public Integer convert(Class<?> targetClass, String strVal) throws ConvertException {
		Long longRes = toLongConverter.convert(Integer.class, strVal);
		return longRes.intValue();
	}
}
