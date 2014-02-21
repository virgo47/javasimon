package org.javasimon.utils.bean;

/**
 * Converter from String to Byte.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToByteConverter implements Converter {

	private ToLongConverter toLongConverter = new ToLongConverter();

	@Override
	public Byte convert(Class<?> targetClass, String strVal) throws ConvertException {
		Long longRes = toLongConverter.convert(Byte.class, strVal);
		return longRes.byteValue();
	}
}
