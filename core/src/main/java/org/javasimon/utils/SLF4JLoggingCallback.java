package org.javasimon.utils;

import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.Split;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * LoggingCallback logs events via JDK logging API.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @author nigel.thomas@york.ac.uk
 * @since 3.0
 */
public final class SLF4JLoggingCallback extends CallbackSkeleton {
	private Logger logger = LoggerFactory.getLogger(SLF4JLoggingCallback.class);
	private Marker marker = MarkerFactory.getMarker("FINE");

	/**
	 * Logs Simon start on a sprecified log marker.
	 *
	 * @param split started split
	 */
	public void stopwatchStart(Split split) {
		logger.debug(marker, "SIMON START: " + split.getStopwatch());
	}

	/**
	 * Logs Simon stop on a specified log marker.
	 *
	 * @param split stopped split
	 */
	public void stopwatchStop(Split split) {
		logger.debug(marker, "SIMON STOP: " + split.getStopwatch() + " (" + split.runningFor() + ")");
	}

	/**
	 * Logs the warning on a specified log marker.
	 *
	 * @param warning warning message
	 * @param cause throwable cause
	 */
	public void warning(String warning, Exception cause) {
		logger.debug(marker, "SIMON WARNING: " + warning, cause);
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
	 * Returns the specified log marker for messages.
	 *
	 * @return log marker for messages
	 */
	public Marker getMarker() {
		return marker;
	}

	/**
	 * Sets the marker that will be used to log messages.
	 *
	 * @param marker log marker used to log messages
	 */
	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	/**
	 * Sets the marker via marker name - used by the configure facility to configure the callback.
	 *
	 * @param marker name of the marker
	 */
	public void setMarker(String marker) {
		this.marker = MarkerFactory.getMarker(marker);
	}
}