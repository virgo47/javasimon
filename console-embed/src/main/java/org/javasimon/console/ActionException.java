package org.javasimon.console;

/**
 * Exception raised during action execution
 *
 * @author gquintana
 */
public class ActionException extends Exception {
	public static final long serialVersionUID=1;
	public ActionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActionException(String message) {
		super(message);
	}
}
