package org.javasimon.utils.bean;

/**
 * Thrown in case of conversion errors.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ConvertException extends RuntimeException {

	public ConvertException(String msg) {
		super(msg);
	}

	public ConvertException(Throwable cause) {
		super(cause);
	}

	public ConvertException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
