package org.javasimon;

/**
 * SimonException is runtime exception thrown in case something goes seriously wrong (class cast or similar).
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public final class SimonException extends RuntimeException {
	public SimonException(String message) {
		super(message);
	}

	public SimonException(String message, Throwable cause) {
		super(message, cause);
	}

	public SimonException(Throwable cause) {
		super(cause);
	}
}
