package org.javasimon.console;

/**
 * @author gquintana
 */
public class ActionException extends Exception {

	public ActionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActionException(String message) {
		super(message);
	}

}
