package org.javasimon.callback.logging;

/**
 * Log template class is the root of a hierarchy of implementations with different purposes.
 *
 * @author gquintana
 */
public abstract class LogTemplate<C> {

	/**
	 * If enabled, logs the message for context.
	 *
	 * @param context Context
	 * @param messageSource Message producer
	 * @return true if logging is enabled, false otherwise
	 */
	public final boolean log(C context, LogMessageSource<C> messageSource) {
		final boolean enabled = isEnabled(context);
		if (enabled) {
			log(messageSource.getLogMessage(context));
		}
		return enabled;
	}

	/**
	 * Tells whether logging is enabled.
	 *
	 * @return Logging enabled
	 */
	protected abstract boolean isEnabled(C context);

	/** Logs a message. */
	protected abstract void log(String message);
}
