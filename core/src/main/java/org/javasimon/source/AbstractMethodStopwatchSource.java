package org.javasimon.source;

import java.lang.reflect.Method;

import org.javasimon.Manager;
import org.javasimon.Stopwatch;

/**
 * Base class for Stopwatch sources working on method locations.
 *
 * @author gquintana
 */
public abstract class AbstractMethodStopwatchSource<T> extends AbstractStopwatchSource<T> {

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

	/** Returns the Stopwatch for given join point. */
	@Override
	public Stopwatch getMonitor(T location) {
		final Stopwatch stopwatch = super.getMonitor(location);
		if (stopwatch.getNote() == null) {
			stopwatch.setNote(getTargetClass(location).getName() + "." + getTargetMethod(location).getName());
		}
		return stopwatch;
	}

	/**
	 * Wraps this data source in a cache.
	 *
	 * @return Cache monitor source
	 */
	public CachedStopwatchSource<T, Method> cache() {
		return newCacheStopwatchSource(this);
	}

	/**
	 * Wraps given stopwatch source in a cache.
	 *
	 * @param stopwatchSource Stopwatch source
	 * @return Cached stopwatch source
	 */
	private static <T> CachedStopwatchSource<T, Method> newCacheStopwatchSource(final AbstractMethodStopwatchSource<T> stopwatchSource) {
		return new CachedStopwatchSource<T, Method>(stopwatchSource) {
			@Override
			protected Method getLocationKey(T location) {
				return stopwatchSource.getTargetMethod(location);
			}
		};
	}
}
