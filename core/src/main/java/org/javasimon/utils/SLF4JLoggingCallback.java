package org.javasimon.utils;

import org.javasimon.Split;
import org.javasimon.StopwatchSample;
import org.javasimon.callback.CallbackSkeleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * LoggingCallback logs events via SLF4J logging.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @author nigel.thomas@york.ac.uk
 * @since 3.0
 */
@SuppressWarnings("UnusedDeclaration")
public class SLF4JLoggingCallback extends CallbackSkeleton {

	private Logger logger = LoggerFactory.getLogger(SLF4JLoggingCallback.class);
	private Marker marker = MarkerFactory.getMarker("FINE");

	/**
	 * Logs Simon start on a specified log marker.
	 *
	 * @param split started split
	 */
	@Override
	public void onStopwatchStart(Split split) {
		logger.debug(marker, "SIMON START: " + split.getStopwatch());
	}

	/**
	 * Logs Simon stop on a specified log marker.
	 *
	 * @param split stopped split
	 * @param sample stopwatch sample
	 */
	@Override
	public void onStopwatchStop(Split split, StopwatchSample sample) {
		logger.debug(marker, "SIMON STOP: " + sample.simonToString() + " (" + split.runningFor() + ")");
	}

	/**
	 * Logs the warning on a specified log marker.
	 *
	 * @param warning warning message
	 * @param cause throwable cause
	 */
	@Override
	public void onManagerWarning(String warning, Exception cause) {
		logger.debug(marker, "SIMON WARNING: " + warning, cause);
	}

	/**
	 * Logs the message on a specified log marker.
	 *
	 * @param message message
	 */
	@Override
	public void onManagerMessage(String message) {
		logger.debug(marker, "SIMON MESSAGE: " + message);
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