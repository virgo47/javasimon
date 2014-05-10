package org.javasimon.source;

import org.javasimon.Manager;
import org.javasimon.Simon;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Monitor source playing the role of cache for delegate monitor source.
 *
 * @param <L> Location/invocation context
 * @param <M> Simon type
 * @param <K> Location key
 * @author gquintana
 */
public abstract class CachedMonitorSource<L, M extends Simon, K> implements MonitorSource<L, M> {

	/** Real monitor source. */
	private final MonitorSource<L, M> delegate;

	/** Monitor/location information. */
	private static class MonitorInformation {
		private final boolean monitored;
		private final String name;

		public MonitorInformation(boolean monitored, Simon simon) {
			this.monitored = monitored;
			if (simon == null) {
				name = null;
			} else {
				this.name = simon.getName();
			}
		}

		public boolean isMonitored() {
			return monitored;
		}

		public String getName() {
			return name;
		}

		public Simon getMonitor(Manager manager) {
			if (name == null) {
				return null;
			} else {
				return manager.getSimon(name);
			}
		}
	}

	/** Not monitored monitor information. */
	private static final MonitorInformation NULL_MONITOR_INFORMATION = new MonitorInformation(false, null);

	/** Map location key &rarr; monitor information. */
	private final Map<K, MonitorInformation> monitorInformations = new ConcurrentHashMap<>();

	/**
	 * Constructor with real {@link MonitorSource}.
	 *
	 * @param delegate Delegate provider monitors for real
	 */
	public CachedMonitorSource(MonitorSource<L, M> delegate) {
		this.delegate = delegate;
	}

	/** Get location for given location. */
	protected abstract K getLocationKey(L location);

	/**
	 * Get monitor information for given location.
	 * First monitor information is looked up in cache.
	 * Then, when not found, delegate is called.
	 *
	 * @param location Location
	 * @return Monitor information
	 */
	private MonitorInformation getMonitorInformation(L location) {
		final K monitorKey = getLocationKey(location);
		MonitorInformation monitorInformation = monitorInformations.get(monitorKey);
		if (monitorInformation == null) {
			// Not found, let's call delegate
			if (delegate.isMonitored(location)) {
				monitorInformation = new MonitorInformation(true, delegate.getMonitor(location));
			} else {
				monitorInformation = NULL_MONITOR_INFORMATION;
			}
			monitorInformations.put(monitorKey, monitorInformation);
		}
		return monitorInformation;
	}

	/** Remove monitor information for given location. */
	private void removeMonitorInformation(L location) {
		monitorInformations.remove(getLocationKey(location));
	}

	/**
	 * Check whether location should be monitored.
	 * Response is entirely based on cache
	 */
	@Override
	public boolean isMonitored(L location) {
		return getMonitorInformation(location).isMonitored();
	}

	@SuppressWarnings("unchecked")
	private M getMonitorOnce(L location) {
		return (M) getMonitorInformation(location).getMonitor(getManager());
	}

	/**
	 * Get Simon for the specified location.
	 * Simon is retrieved from name in cache.
	 *
	 * @param location Location
	 * @return Simon for the specified location
	 */
	@Override
	public M getMonitor(L location) {
		M monitor = getMonitorOnce(location);
		// In case monitor was removed from manager, we retry
		if (monitor == null) {
			removeMonitorInformation(location);
			monitor = getMonitorOnce(location);
		}
		return monitor;
	}

	@Override
	public Manager getManager() {
		return delegate.getManager();
	}
}
