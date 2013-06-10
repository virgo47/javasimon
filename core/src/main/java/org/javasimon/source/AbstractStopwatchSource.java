package org.javasimon.source;

import org.javasimon.Manager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

/**
 * Base implementation for {@link MonitorSource} producings stopwatches.
 *
 * @author gquintana
 */
public abstract class AbstractStopwatchSource<T> implements StopwatchSource<T> {
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

	@Override
	public Manager getManager() {
		return manager;
	}

	/**
	 * Default implementation returns always true.
	 *
	 * @return always true
	 */
	@Override
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
	@Override
	public Stopwatch getMonitor(T location) {
		return manager.getStopwatch(getMonitorName(location));
	}

	@Override
	public Split start(T location) {
		if (isMonitored(location)) {
			return getMonitor(location).start();
		}
		return Split.DISABLED;
	}
}
