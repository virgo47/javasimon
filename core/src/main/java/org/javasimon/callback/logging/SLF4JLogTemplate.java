package org.javasimon.callback.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Concrete log template using SLF4J {@link Logger}.
 *
 * @author gquintana
 * @since 3.2
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class SLF4JLogTemplate<C> extends LogTemplate<C> {

	/** Logger. */
	protected final Logger logger;

	/** Marker, can be null. */
	protected final Marker marker;

	/**
	 * Constructor with {@link Logger} and {@link Marker}.
	 *
	 * @param logger logger
	 * @param marker Marker (can be null)
	 */
	protected SLF4JLogTemplate(Logger logger, Marker marker) {
		this.logger = logger;
		this.marker = marker;
	}

	/**
	 * Constructor with logger name and marker name.
	 *
	 * @param loggerName logger name
	 * @param markerName marker name (can be null)
	 */
	protected SLF4JLogTemplate(String loggerName, String markerName) {
		this.logger = LoggerFactory.getLogger(loggerName);
		this.marker = markerName == null ? null : MarkerFactory.getMarker(markerName);
	}

	/**
	 * Returns logger.
	 *
	 * @return logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Returns marker.
	 *
	 * @return marker
	 */
	public Marker getMarker() {
		return marker;
	}

	/** SLF4J Log template with DEBUG level. */
	public static class Debug<C> extends SLF4JLogTemplate<C> {
		public Debug(String loggerName, String markerName) {
			super(loggerName, markerName);
		}

		public Debug(Logger logger, Marker marker) {
			super(logger, marker);
		}

		@Override
		protected boolean isEnabled(C context) {
			return logger.isDebugEnabled(marker);
		}

		@Override
		protected void log(String message) {
			logger.debug(marker, message);
		}
	}

	/** SLF4J Log template with INFO level. */
	public static class Info<C> extends SLF4JLogTemplate<C> {
		public Info(String loggerName, String markerName) {
			super(loggerName, markerName);
		}

		public Info(Logger logger, Marker marker) {
			super(logger, marker);
		}

		@Override
		protected boolean isEnabled(C context) {
			return logger.isInfoEnabled(marker);
		}

		@Override
		protected void log(String message) {
			logger.info(marker, message);
		}
	}

	/** SLF4J Log template with WARN level. */
	public static class Warn<C> extends SLF4JLogTemplate<C> {
		public Warn(String loggerName, String markerName) {
			super(loggerName, markerName);
		}

		public Warn(Logger logger, Marker marker) {
			super(logger, marker);
		}

		@Override
		protected boolean isEnabled(C context) {
			return logger.isWarnEnabled(marker);
		}

		@Override
		protected void log(String message) {
			logger.warn(marker, message);
		}
	}
}
