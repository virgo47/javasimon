package org.javasimon.jdbc.logging;

import org.javasimon.Callback;
import org.javasimon.jdbc.logging.CallbackLogParams;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * SimonFormatter is abstract root for custom log formatters for logging feature of Simon JDBC driver.
 * <p/>
 * Main format method is {@link #formatRecord(java.util.logging.LogRecord, CallbackLogParams)}. It consumes standard
 * logging {@link java.util.logging.LogRecord} and object {@link CallbackLogParams}. {@link CallbackLogParams} contains
 * parameters from callbacks like name of Simon, split, etc.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @see CallbackLogParams
 * @see LoggingCallback#formatter()
 * @since 2.0
 */
public abstract class SimonFormatter extends Formatter {
	/**
	 * {@inheritDoc}
	 */
	public final synchronized String format(LogRecord record) {
		return formatRecord(record, (CallbackLogParams) record.getParameters()[0]);
	}

	/**
	 * Custom format method.
	 *
	 * @param record standard logging log record
	 * @param params callback logging parameters
	 * @return log message formated to string
	 */
	protected abstract String formatRecord(LogRecord record, CallbackLogParams params);

	/**
	 * Checks if callback was start event.
	 *
	 * @param p callback logging params
	 * @return {@code true} if event was start; otherwise {@code false}
	 */
	protected boolean isStart(CallbackLogParams p) {
		return p.getEvent() == Callback.Event.STOPWATCH_START;
	}

	/**
	 * Getter for note.
	 *
	 * @param p callback logging params
	 * @return note if present; otherwise empty string
	 */
	protected String note(CallbackLogParams p) {
		return p.getNote() != null ? "{" + p.getNote() + "}" : "";
	}
}
