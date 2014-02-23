package org.javasimon.utils.bean;

/**
 * Exception thrown by SimonBeanUtils class.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class BeanUtilsException extends RuntimeException {

	public BeanUtilsException(String msg) {
		super(msg);
	}

	public BeanUtilsException(Throwable cause) {
		super(cause);
	}

	public BeanUtilsException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
