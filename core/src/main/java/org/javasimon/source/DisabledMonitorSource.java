package org.javasimon.source;

import org.javasimon.Manager;
import org.javasimon.Simon;

/**
 * Disabled monitor source.
 *
 * @author gquintana
 */
public class DisabledMonitorSource<L, M extends Simon> implements MonitorSource<L, M> {
	/**
	 * Singleton instance.
	 */
	private static final DisabledMonitorSource INSTANCE = new DisabledMonitorSource();

	/**
	 * Returns a singleton instance.
	 */
	@SuppressWarnings("unchecked")
	public static <L, M extends Simon> DisabledMonitorSource<L, M> get() {
		return (DisabledMonitorSource<L, M>) INSTANCE;
	}

	/**
	 * Always returns null.
	 */
	public M getMonitor(L location) {
		return null;
	}

	/**
	 * Always returns false.
	 */
	public boolean isMonitored(L location) {
		return false;
	}

	@Override
	public Manager getManager() {
		return null;
	}
}
