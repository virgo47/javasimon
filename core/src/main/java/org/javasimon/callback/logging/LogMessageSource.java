package org.javasimon.callback.logging;

/**
 * Message provider, converts context into a loggable string.
 *
 * @author gquintana
 */
public interface LogMessageSource<C> {

	/**
	 * Returns message for given context.
	 *
	 * @param context Context
	 * @return Message
	 */
	String getLogMessage(C context);
}
