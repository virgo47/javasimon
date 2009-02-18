package org.javasimon.utils;

import org.javasimon.CallbackSkeleton;
import org.javasimon.Split;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * LoggingCallback logs events via JDK logging API.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Feb 7, 2009
 */
public final class LoggingCallback extends CallbackSkeleton {
	private Logger logger = Logger.getAnonymousLogger();
	private Level level = Level.FINE;

	public void stopwatchStart(Split split) {
		logger.log(level, "SIMON START: " + split.getStopwatch());
	}

	public void stopwatchStop(Split split) {
		logger.log(level, "SIMON STOP: " + split.getStopwatch() + " (" + split.runningFor() + ")");
	}

	public void warning(String warning, Exception cause) {
		logger.log(level, "SIMON WARNING: " + warning, cause);
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void setLogger(String logger) {
		this.logger = Logger.getLogger(logger);
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void setLevel(String level) {
		this.level = Level.parse(level);
	}
}
