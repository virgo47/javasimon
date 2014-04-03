package org.javasimon.callback.logging;

import org.javasimon.Clock;
import org.javasimon.Split;
import org.javasimon.utils.SimonUtils;

import java.util.logging.Level;

/**
 * Factory of {@link LogTemplate}s.
 * Produces various implementations
 *
 * @author gquintana
 */
public class LogTemplates {
	/**
	 * Produces a disabled log template wich never log anything
	 */
	public static <C> DisabledLogTemplate<C> disabled() {
		return DisabledLogTemplate.getInstance();
	}

	/**
	 * Produces a log template which logs something every N split
	 *
	 * @param delegateLogger Concrete log template
	 * @param period N value, period
	 * @return Logger
	 */
	public static <C> CounterLogTemplate<C> everyNSplits(LogTemplate<C> delegateLogger, int period) {
		return new CounterLogTemplate<C>(delegateLogger, period);
	}

	/**
	 * Produces a log template which logs something at most every N milliseconds
	 *
	 * @param delegateLogger Concrete log template
	 * @param period N value in milliseconds, maximum period
	 * @return Logger
	 */
	public static <C> PeriodicLogTemplate<C> everyNMilliseconds(LogTemplate<C> delegateLogger, long period) {
		return new PeriodicLogTemplate<C>(delegateLogger, period);
	}

	/**
	 * Produces a log template which logs something at most every N milliseconds
	 *
	 * @param delegateLogger Concrete log template
	 * @param period N value in milliseconds, maximum period
	 * @param clock clock to get current system time
	 * @return Logger
	 */
	static <C> PeriodicLogTemplate<C> everyNMilliseconds(LogTemplate<C> delegateLogger, long period, Clock clock) {
		return new PeriodicLogTemplate<C>(delegateLogger, period, clock);
	}

	/**
	 * Produces a log template which logs something at most every N secoonds
	 *
	 * @param delegateLogger Concrete log template
	 * @param period N value in seconds, maximum period
	 * @return Logger
	 */
	public static <C> PeriodicLogTemplate<C> everyNSeconds(LogTemplate<C> delegateLogger, long period) {
		return everyNMilliseconds(delegateLogger, period * SimonUtils.MILLIS_IN_SECOND);
	}

	/**
	 * Produces a concrete log template which logs messages into a SLF4J Logger.
	 *
	 * @param loggerName Logger name
	 * @param levelName Level name (info, debug, warn, etc.)
	 * @param markerName Marker name
	 * @return Logger
	 */
	public static <C> SLF4JLogTemplate<C> toSLF4J(String loggerName, String levelName, String markerName) {
		levelName = levelName.toLowerCase();
		if ("debug".equals(levelName)) {
			return new SLF4JLogTemplate.Debug<C>(loggerName, markerName);
		} else if ("info".equals(levelName)) {
			return new SLF4JLogTemplate.Info<C>(loggerName, markerName);
		} else if ("warn".equals(levelName)) {
			return new SLF4JLogTemplate.Warn<C>(loggerName, markerName);
		} else {
			throw new IllegalArgumentException("Invalid level name " + levelName);
		}
	}

	/**
	 * Produces a concrete log template which logs messages into a SLF4J Logger.
	 *
	 * @param loggerName Logger name
	 * @param levelName Level name (info, debug, warn, etc.)
	 * @return Logger
	 */
	public static <C> SLF4JLogTemplate<C> toSLF4J(String loggerName, String levelName) {
		return toSLF4J(loggerName, levelName, null);
	}

	/**
	 * Produces a concrete log template which logs messages into a Java Util Logging Logger.
	 *
	 * @param loggerName Logger name
	 * @param level Level (warn, fine, finer, etc.)
	 * @return Logger
	 */
	public static <C> JULLogTemplate<C> toJUL(String loggerName, Level level) {
		return new JULLogTemplate<C>(loggerName, level);
	}

	/**
	 * Produces a log template which logs something when stopwatch split is longer than threshold.
	 *
	 * @param delegateLogger Concrete log template
	 * @param threshold Threshold (in nanoseconds), above which logging is enabled
	 * @return Logger
	 */
	public static SplitThresholdLogTemplate whenSplitLongerThanNanoseconds(LogTemplate<Split> delegateLogger, long threshold) {
		return new SplitThresholdLogTemplate(delegateLogger, threshold);
	}

	/**
	 * Produces a log template which logs something when stopwatch split is longer than threshold.
	 *
	 * @param delegateLogger Concrete log template
	 * @param threshold Threshold (in milliseconds), above which logging is enabled
	 * @return Logger
	 */
	public static SplitThresholdLogTemplate whenSplitLongerThanMilliseconds(LogTemplate<Split> delegateLogger, long threshold) {
		return whenSplitLongerThanNanoseconds(delegateLogger, threshold * SimonUtils.NANOS_IN_MILLIS);
	}
}
