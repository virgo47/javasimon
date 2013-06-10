package org.javasimon.source;

import org.javasimon.Split;
import org.javasimon.Stopwatch;

/**
 * Cached sources using {@link org.javasimon.Stopwatch} as monitors.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 3.4
 */
public abstract class CachedStopwatchSource<L, K> extends CachedMonitorSource<L, Stopwatch, K> implements StopwatchSource<L> {
	public CachedStopwatchSource(StopwatchSource<L> delegate) {
		super(delegate);
	}

	@Override
	public Split start(L location) {
		if (isMonitored(location)) {
			return getMonitor(location).start();
		}
		return Split.DISABLED;
	}
}
