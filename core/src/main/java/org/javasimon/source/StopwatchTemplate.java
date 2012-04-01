package org.javasimon.source;

import org.javasimon.Split;
import org.javasimon.Stopwatch;

/**
 * Stopwatch usage template helps using Stopwatch in conjunction with a StopwatchSource.
 *
 * @param <L> Location/invocation context
 * @author gquintana
 */
public final class StopwatchTemplate<L> {
	/**
	 * Stopwatch provider.
	 */
	private final MonitorSource<L, Stopwatch> stopwatchSource;

	/**
	 * Stopwatch template constructor.
	 */
	public StopwatchTemplate(MonitorSource<L, Stopwatch> stopwatchSource) {
		this.stopwatchSource = stopwatchSource;
	}

	/**
	 * If given location is monitored, then a Stopwatch is started and Split returned - otherwise {@code null}
	 * is returned.
	 *
	 * @param location Location
	 * @return Split from started stopwatch
	 */
	public Split start(L location) {
		if (stopwatchSource.isMonitored(location)) {
			return stopwatchSource.getMonitor(location).start();
		} else {
			return null;
		}
	}

	/**
	 * Stop given stopwatch split.
	 *
	 * @param split Split can be null
	 */
	public void stop(Split split) {
		if (split != null) {
			split.stop();
		}
	}
}
