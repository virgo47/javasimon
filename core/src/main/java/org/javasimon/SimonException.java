package org.javasimon;

/**
 * SimonException is runtime exception thrown in case something goes seriously wrong (class cast or similar).
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class SimonException extends RuntimeException {

	/**
	 * Creates SimonException with the message.
	 *
	 * @param message exception message
	 */
	public SimonException(String message) {
		super(message);
	}

	/**
	 * Creates SimonException with the chained exception causing this exception.
	 *
	 * @param cause chained exception
	 */
	public SimonException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates SimonException with message and the chained exception causing this exception.
	 *
	 * @param message exception message
	 * @param cause chained exception
	 */
	public SimonException(String message, Throwable cause) {
		super(message, cause);
	}
}
