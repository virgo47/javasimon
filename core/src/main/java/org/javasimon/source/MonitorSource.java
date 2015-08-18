package org.javasimon.source;

import org.javasimon.Manager;
import org.javasimon.Simon;

/**
 * Monitor source provides monitors (Simons) for a specific "location" name. This mechanism enables
 * caching of monitors for "locations" that do not change but may be expensive to transform to
 * Simon names, for instance.
 *
 * @param <L> Location/invocation context/name
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
