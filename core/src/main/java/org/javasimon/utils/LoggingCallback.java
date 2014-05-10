package org.javasimon.utils;

import org.javasimon.Split;
import org.javasimon.StopwatchSample;
import org.javasimon.callback.CallbackSkeleton;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * LoggingCallback logs events via JDK logging API.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
@SuppressWarnings("UnusedDeclaration")
public class LoggingCallback extends CallbackSkeleton {

	private Logger logger = Logger.getAnonymousLogger();
	private Level level = Level.FINE;

	/** Logs Simon start on a specified log level. */
	@Override
	public void onStopwatchStart(Split split) {
		logger.log(level, "SIMON START: " + split.getStopwatch());
	}

	/** Logs Simon stop on a specified log level. */
	@Override
	public void onStopwatchStop(Split split, StopwatchSample sample) {
		logger.log(level, "SIMON STOP: " + sample.simonToString() + " (" + split.runningFor() + ")");
	}

	/**
	 * Logs the warning on a specified log level.
	 *
	 * @param warning warning message
	 * @param cause throwable cause
	 */
	@Override
	public void onManagerWarning(String warning, Exception cause) {
		logger.log(level, "SIMON WARNING: " + warning, cause);
	}

	/**
	 * Logs the message on a specified log level.
	 *
	 * @param message message
	 */
	@Override
	public void onManagerMessage(String message) {
		logger.log(level, "SIMON MESSAGE: " + message);
	}

	/**
	 * Returns logger used to log messages.
	 *
	 * @return used logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Sets the logger that will be used to log messages.
	 *
	 * @param logger new specified logger
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * Sets the logger by the name of the logger - used by the configure facility to configure the callback.
	 *
	 * @param logger name of the logger
	 */
	public void setLogger(String logger) {
		this.logger = Logger.getLogger(logger);
	}

	/**
	 * Returns the specified log level for messages.
	 *
	 * @return log level for messages
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * Sets the level that will be used to log messages.
	 *
	 * @param level log level used to log messages
	 */
	public void setLevel(Level level) {
		this.level = level;
	}

	/**
	 * Sets the level via level name - used by the configure facility to configure the callback.
	 *
	 * @param level name of the level
	 */
	public void setLevel(String level) {
		this.level = Level.parse(level);
	}
}
