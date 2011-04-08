package org.javasimon.utils;

import org.javasimon.CallbackSkeleton;
import org.javasimon.Split;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * LoggingCallback logs events via JDK logging API.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class LoggingCallback extends CallbackSkeleton {
	private Logger logger = Logger.getAnonymousLogger();
	private Level level = Level.FINE;

	/**
	 * Logs Simon start on a sprecified log level.
	 *
	 * @param split started split
	 */
	public void stopwatchStart(Split split) {
		logger.log(level, "SIMON START: " + split.getStopwatch());
	}

	/**
	 * Logs Simon stop on a specified log level.
	 *
	 * @param split stopped split
	 */
	public void stopwatchStop(Split split) {
		logger.log(level, "SIMON STOP: " + split.getStopwatch() + " (" + split.runningFor() + ")");
	}

	/**
	 * Logs the warning on a specified log level.
	 *
	 * @param warning warning message
	 * @param cause throwable cause
	 */
	public void warning(String warning, Exception cause) {
		logger.log(level, "SIMON WARNING: " + warning, cause);
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
