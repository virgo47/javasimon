package org.javasimon.source;

import org.javasimon.Manager;
import org.javasimon.Stopwatch;

/**
 * Base implementation for {@link MonitorSource} producings stopwatches.
 *
 * @author gquintana
 */
public abstract class AbstractStopwatchSource<T> implements MonitorSource<T, Stopwatch> {
	/**
	 * Simon manager used for producing Stopwatches.
	 */
	private final Manager manager;

	/**
	 * Constructor with {@link Manager}.
	 *
	 * @param manager Simon manager used for producing Stopwatches
	 */
	public AbstractStopwatchSource(Manager manager) {
		this.manager = manager;
	}

	/**
	 * Get used Simon manager.
	 *
	 * @return Simon manager
	 */
	public Manager getManager() {
		return manager;
	}

	/**
	 * @return always true
	 */
	public boolean isMonitored(T location) {
		return true;
	}

	/**
	 * Get monitor name for the given location.
	 */
	protected abstract String getMonitorName(T location);

	/**
	 * Provide a Stopwatch for given location.
	 *
	 * @param location Location
	 * @return Stopwatch
	 */
	public Stopwatch getMonitor(T location) {
		return manager.getStopwatch(getMonitorName(location));
	}
}
