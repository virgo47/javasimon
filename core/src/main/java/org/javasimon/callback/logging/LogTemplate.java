package org.javasimon.callback.logging;

/**
 * Log template interface is the root of a hierarchy of implementations with different purposes.
 *
 * @author gquintana
 */
public abstract class LogTemplate<C> {
	/**
	 * Tells whether logging is enabled.
	 *
	 * @return Logging enabled
	 */
	protected abstract boolean isEnabled(C context);

	/**
	 * If enabled, get message for context and log id.
	 *
	 * @param context Context
	 * @param messageSource Message producer
	 */
	public final boolean log(C context, LogMessageSource<C> messageSource) {
		final boolean enabled = isEnabled(context);
		if (enabled) {
			log(messageSource.getLogMessage(context));
		}
		return enabled;
	}

	/**
	 * Log a message.
	 */
	protected abstract void log(String message);
}
