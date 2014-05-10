package org.javasimon.callback.logging;

import org.javasimon.clock.Clock;
import org.javasimon.Split;
import org.javasimon.clock.ClockUtils;

import java.util.logging.Level;

/**
 * Factory for various implementations of {@link LogTemplate}s.
 *
 * @author gquintana
 */
public class LogTemplates {

	/** Produces a disabled log template which never logs anything. */
	public static <C> LogTemplate<C> disabled() {
		return DisabledLogTemplate.getInstance();
	}

	/**
	 * Produces a log template which logs something every N split.
	 *
	 * @param delegateLogger Concrete log template
	 * @param period N value, period
	 * @return Logger
	 */
	public static <C> LogTemplate<C> everyNSplits(LogTemplate<C> delegateLogger, int period) {
		return new CounterLogTemplate<>(delegateLogger, period);
	}

	/**
	 * Produces a log template which logs something at most every N milliseconds.
	 *
	 * @param delegateLogger Concrete log template
	 * @param period N value in milliseconds, maximum period
	 * @return Logger
	 */
	public static <C> LogTemplate<C> everyNMilliseconds(LogTemplate<C> delegateLogger, long period) {
		return new PeriodicLogTemplate<>(delegateLogger, period);
	}

	/**
	 * Produces a log template which logs something at most every N milliseconds.
	 *
	 * @param delegateLogger Concrete log template
	 * @param period N value in milliseconds, maximum period
	 * @param clock clock to get current system time
	 * @return Logger
	 */
	static <C> LogTemplate<C> everyNMilliseconds(LogTemplate<C> delegateLogger, long period, Clock clock) {
		return new PeriodicLogTemplate<>(delegateLogger, period, clock);
	}

	/**
	 * Produces a log template which logs something at most every N secoonds.
	 *
	 * @param delegateLogger Concrete log template
	 * @param period N value in seconds, maximum period
	 * @return Logger
	 */
	public static <C> LogTemplate<C> everyNSeconds(LogTemplate<C> delegateLogger, long period) {
		return everyNMilliseconds(delegateLogger, period * ClockUtils.MILLIS_IN_SECOND);
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
			return new SLF4JLogTemplate.Debug<>(loggerName, markerName);
		} else if ("info".equals(levelName)) {
			return new SLF4JLogTemplate.Info<>(loggerName, markerName);
		} else if ("warn".equals(levelName)) {
			return new SLF4JLogTemplate.Warn<>(loggerName, markerName);
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
		return new JULLogTemplate<>(loggerName, level);
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
		return whenSplitLongerThanNanoseconds(delegateLogger, threshold * ClockUtils.NANOS_IN_MILLIS);
	}
}
