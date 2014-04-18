package org.javasimon.aggregation;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ManagerCreationException extends Exception {

	public ManagerCreationException(String msg) {
		super(msg);
	}

	public ManagerCreationException(Throwable cause) {
		super(cause);
	}

	public ManagerCreationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
