package org.javasimon.source;

import org.javasimon.Simon;

/**
 * Disabled monitor source
 * @author gquintana
 */
public class DisabledMonitorSource<L, M extends Simon> implements MonitorSource<L, M> {
	/**
	 * Singleton constructor is not hidden for reflection purpose
	 */
	public DisabledMonitorSource() {
	}
	/**
	 * Always return null
	 */
	public M getMonitor(L location) {
		return null;
	}
	/**
	 * Always return false
	 */
	public boolean isMonitored(L location) {
		return false;
	}
	/**
	 * Singleton instance
	 */
	private static final DisabledMonitorSource INSTANCE=new DisabledMonitorSource();
	/**
	 * Returns a singleton instance
	 */
	@SuppressWarnings("unchecked")
	public static <L, M extends Simon> DisabledMonitorSource<L, M> get() {
		return (DisabledMonitorSource<L, M>)INSTANCE;
	}
}
