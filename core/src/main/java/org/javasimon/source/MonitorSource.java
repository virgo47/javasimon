package org.javasimon.source;

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
	 * Get monitor for given location.
	 *
	 * @param location Location
	 * @return Monitor
	 */
	M getMonitor(L location);
}
