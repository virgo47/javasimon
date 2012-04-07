package org.javasimon.source;

import org.javasimon.Manager;
import org.javasimon.Simon;

/**
 * Interface implemented by monitor providers
 *
 * @param <L> Location/invocation context
 * @param <M> Simon type
 * @author gquintana
 */
public interface MonitorSource<L, M extends Simon> {
	/**
	 * Indicates whether given location should be monitored or not.
	 *
	 * @param location Location
	 * @return true means monitored
	 */
	boolean isMonitored(L location);

	/**
	 * Returns the monitor for given location.
	 *
	 * @param location Location
	 * @return Monitor
	 */
	M getMonitor(L location);

	/**
	 * Returns the {@link Manager} used as a real source of monitors.
	 *
	 * @return Manager to get the monitors from
	 */
	Manager getManager();
}
