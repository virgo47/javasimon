package org.javasimon.examples.jmx.custom;

import org.javasimon.Stopwatch;
import org.javasimon.clock.ClockUtils;
import org.javasimon.jmx.StopwatchMXBeanImpl;

/**
 * Customized Stopwatch JMX bean displaying values in milliseconds.
 *
 * @author <a href="mailto:gerald.quintana@gmail.com">Gerald Quintana</a>
 */
public class CustomStopwatchMXBeanImpl extends StopwatchMXBeanImpl implements CustomStopwatchMXBean {
	public CustomStopwatchMXBeanImpl(Stopwatch stopwatch) {
		super(stopwatch);
	}

	private double nanosToMillis(double nanos) {
		return nanos / ClockUtils.NANOS_IN_MILLIS;
	}

	private long nanosToMillis(long nanos) {
		return nanos / ClockUtils.NANOS_IN_MILLIS;
	}

	@Override
	public double getMeanInMillis() {
		return nanosToMillis(stopwatch.getMean());
	}

	@Override
	public long getMinInMillis() {
		return nanosToMillis(stopwatch.getMin());
	}

	@Override
	public long getMaxInMillis() {
		return nanosToMillis(stopwatch.getMax());
	}

	@Override
	public long getTotalInMillis() {
		return nanosToMillis(stopwatch.getTotal());
	}

	@Override
	public long getLastInMillis() {
		return nanosToMillis(stopwatch.getLast());
	}

	@Override
	public long getCounter() {
		return stopwatch.getCounter();
	}

	@Override
	public double getStandardDeviationInMillis() {
		return nanosToMillis(stopwatch.getStandardDeviation());
	}
}
