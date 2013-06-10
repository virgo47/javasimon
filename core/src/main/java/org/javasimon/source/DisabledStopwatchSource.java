package org.javasimon.source;

import org.javasimon.Manager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

/**
 * Disabled stopwatch source.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 3.4
 */
public class DisabledStopwatchSource<L> implements StopwatchSource<L> {
	/**
	 * Singleton instance.
	 */
	private static final DisabledStopwatchSource INSTANCE = new DisabledStopwatchSource();

	/**
	 * Returns a singleton instance.
	 */
	@SuppressWarnings("unchecked")
	public static <L> DisabledStopwatchSource<L> get() {
		return (DisabledStopwatchSource<L>) INSTANCE;
	}

	@Override
	public Split start(L location) {
		return Split.DISABLED;
	}

	@Override
	public boolean isMonitored(L location) {
		return false;
	}

	@Override
	public Stopwatch getMonitor(L location) {
		return null;
	}

	@Override
	public Manager getManager() {
		return null;
	}
}
