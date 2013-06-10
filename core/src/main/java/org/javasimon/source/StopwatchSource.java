package org.javasimon.source;

import org.javasimon.Split;
import org.javasimon.Stopwatch;

/**
 * Stopwatch source is source that uses {@link org.javasimon.Stopwatch} as the monitor type.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 3.4
 */
public interface StopwatchSource<L> extends MonitorSource<L, Stopwatch> {
	/**
	 * Convenient method to return split for the location or disabled split, if the location is not monitored.
	 *
	 * @param location location to be monitored
	 * @return {@link Split} for the location or {Split#DISABLED} if the location is not monitored
	 * @since 3.4
	 */
	Split start(L location);
}
