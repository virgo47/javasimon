package org.javasimon.clock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * Clock that should measure nanoseconds of CPU usage time.
 * Uses {@link java.lang.management.ThreadMXBean#getCurrentThreadCpuTime()} internally.
 * This Clock checks if CPU time is supported, but does not enable it, if it is disabled. User has to do this
 * externally calling {@code ManagementFactory.getThreadMXBean().setThreadCpuTimeEnabled(true)}.
 * <p/>
 * If CPU time is not supported, 0 is returned. If it is disabled, -1 should be returned (check Javadoc for
 * {@link java.lang.management.ThreadMXBean#getCurrentThreadCpuTime()}).
 *
 * @since 3.5
 */
final class CpuClock implements Clock {

	private final ThreadMXBean threadMXBean;

	CpuClock() {
		ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
		if (mxBean.isCurrentThreadCpuTimeSupported()) {
			threadMXBean = mxBean;
		} else {
			threadMXBean = null;
		}
	}


	@Override
	public long nanoTime() {
		if (threadMXBean == null) {
			return 0;
		}
		return threadMXBean.getCurrentThreadCpuTime();
	}

	@Override
	public long milliTime() {
		return millisForNano(nanoTime());
	}

	/** Here millis are simply nanos divided by {@link org.javasimon.clock.ClockUtils#NANOS_IN_MILLIS}. */
	@Override
	public long millisForNano(long nanos) {
		return nanos / ClockUtils.NANOS_IN_MILLIS;
	}
}
