package org.javasimon.source;

import org.javasimon.Manager;
import org.javasimon.Stopwatch;

import java.lang.reflect.Method;

/**
 * Base class for Stopwatch sources working on method locations.
 *
 * @author gquintana
 */
public abstract class AbstractMethodStopwatchSource<T> extends AbstractStopwatchSource<T> {
	/**
	 * Default constructor using default simon manager.
	 */
	public AbstractMethodStopwatchSource() {
	}

	/**
	 * Constructor using specific simon manager.
	 *
	 * @param manager Simon manager
	 */
	public AbstractMethodStopwatchSource(Manager manager) {
		super(manager);
	}

	/**
	 * Get target class from location.
	 *
	 * @param location Location
	 * @return Target class
	 */
	protected abstract Class<?> getTargetClass(T location);

	/**
	 * Get target method from location.
	 *
	 * @param location Location
	 * @return Target method
	 */
	protected abstract Method getTargetMethod(T location);

	/**
	 * Returns the Stopwatch for given join point.
	 */
	@Override
	public Stopwatch getMonitor(T location) {
		final Stopwatch stopwatch = super.getMonitor(location);
		if (stopwatch.getNote() == null) {
			stopwatch.setNote(getTargetClass(location).getName() + "." + getTargetMethod(location).getName());
		}
		return stopwatch;
	}

	/**
	 * Wraps given stopwatch source in a cache.
	 *
	 * @param stopwatchSource Stopwatch source
	 * @return Cached stopwatch source
	 */
	public static <T> CacheMonitorSource<T, Stopwatch, Method> newCacheStopwatchSource(final AbstractMethodStopwatchSource<T> stopwatchSource) {
		return new CacheMonitorSource<T, Stopwatch, Method>(stopwatchSource) {
			@Override
			protected Method getLocationKey(T location) {
				return stopwatchSource.getTargetMethod(location);
			}
		};
	}

	/**
	 * Wraps this data source in a cache.
	 *
	 * @return Cache monitor source
	 */
	public CacheMonitorSource<T, Stopwatch, Method> cache() {
		return newCacheStopwatchSource(this);
	}
}
