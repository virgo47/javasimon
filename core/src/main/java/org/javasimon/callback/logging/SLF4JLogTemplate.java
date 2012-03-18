package org.javasimon.callback.logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
/**
 * Concrete log template using SLF4J {@link Logger}
 * @author gquintana
 */
public abstract class SLF4JLogTemplate<C> extends LogTemplate<C> {
	/**
	 * Logger
	 */
	protected final Logger logger;
	/**
	 * Marker, can be null
	 */
	protected final Marker marker;
	/**
	 * Constructor
	 * @param logger Logger
	 * @param marker Marker (can be null)
	 */
	protected SLF4JLogTemplate(Logger logger, Marker marker) {
		this.logger = logger;
		this.marker = marker;
	}

	/**
	 * Constructor
	 * @param loggerName Logger name
	 * @param levelName Level name
	 * @param markerName  Marker name (can be null)
	 */
	protected SLF4JLogTemplate(String loggerName, String markerName) {
		this.logger = LoggerFactory.getLogger(loggerName);
		this.marker=markerName==null?null:MarkerFactory.getMarker(markerName);
	}
//	/**
//	 * Init logger methods depending on level name
//	 * @param levelName Level name
//	 */
//	private void initMethods(String levelName) {
//		levelName=levelName.toLowerCase();
//		try {
//			isEnabledMethod = Logger.class.getMethod("is" + Character.toUpperCase(levelName.charAt(0)) + levelName.substring(1) + "Enabled", new Class[]{Marker.class});
//			if (marker==null) {
//				logMethod = Logger.class.getMethod(levelName, new Class[]{String.class});								
//			} else {
//				logMethod = Logger.class.getMethod(levelName, new Class[]{Marker.class, String.class});				
//			}
//		} catch (NoSuchMethodException noSuchMethodException) {
//			throw new IllegalArgumentException("Invalid level name: "+levelName, noSuchMethodException);
//		} catch (SecurityException securityException) {
//			throw new IllegalStateException("Unable to get logger methods", securityException);
//		}
//		this.levelName=levelName;
//	} 
	/**
	 * Get logger
	 * @return Logger
	 */
	public Logger getLogger() {
		return logger;
	}
	/**
	 * Marker 
	 * @return marker 
	 */
	public Marker getMarker() {
		return marker;
	}
	/**
	 * SLF4J Log template with Debug level
	 */
	public static class Debug<C> extends SLF4JLogTemplate<C> {
		public Debug(String loggerName, String markerName) {
			super(loggerName, markerName);
		}
		public Debug(Logger logger, Marker marker) {
			super(logger, marker);
		}
		/**
		 * {@inheritDoc }
		 */
		@Override
		protected boolean isEnabled(C context) {
			return logger.isDebugEnabled(marker);
		}
		/**
		 * {@inheritDoc }
		 */
		@Override
		protected void log(String message) {
			logger.debug(marker, message);
		}
	}
	/**
	 * SLF4J Log template with Info level
	 */
	public static class Info<C> extends SLF4JLogTemplate<C> {
		public Info(String loggerName, String markerName) {
			super(loggerName, markerName);
		}
		public Info(Logger logger, Marker marker) {
			super(logger, marker);
		}
		/**
		 * {@inheritDoc }
		 */		
		@Override
		protected boolean isEnabled(C context) {
			return logger.isInfoEnabled(marker);
		}
		/**
		 * {@inheritDoc }
		 */
		@Override
		protected void log(String message) {
			logger.info(marker, message);
		}
	}
	/**
	 * SLF4J Log template with Warn level
	 */
	public static class Warn<C> extends SLF4JLogTemplate<C> {
		public Warn(String loggerName, String markerName) {
			super(loggerName, markerName);
		}
		public Warn(Logger logger, Marker marker) {
			super(logger, marker);
		}
		/**
		 * {@inheritDoc }
		 */
		@Override
		protected boolean isEnabled(C context) {
			return logger.isWarnEnabled(marker);
		}
		/**
		 * {@inheritDoc }
		 */
		@Override
		protected void log(String message) {
			logger.warn(marker, message);
		}
	}
}
