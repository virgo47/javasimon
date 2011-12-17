package org.javasimon.examples.jmx.custom;

import org.javasimon.Stopwatch;
import org.javasimon.jmx.StopwatchMXBeanImpl;

/**
 * Customized Stopwatch JMX bean displaying values in milliseconds
 *
 * @author <a href="mailto:gerald.quintana@gmail.com">Gerald Quintana</a>
 */
public class CustomStopwatchMXBeanImpl extends StopwatchMXBeanImpl implements CustomStopwatchMXBean {

	public CustomStopwatchMXBeanImpl(Stopwatch stopwatch) {
		super(stopwatch);
	}

	private double nanosToMillis(double nanos) {
		return nanos / 1000000d;
	}

	private long nanosToMillis(long nanos) {
		return nanos / 1000000L;
	}

	@Override
	public double getMean() {
		return nanosToMillis(stopwatch.getMean());
	}

	@Override
	public long getMin() {
		return nanosToMillis(stopwatch.getMin());
	}

	@Override
	public long getMax() {
		return nanosToMillis(stopwatch.getMax());
	}

	@Override
	public long getTotal() {
		return nanosToMillis(stopwatch.getTotal());
	}

	@Override
	public long getLast() {
		return nanosToMillis(stopwatch.getLast());
	}

	@Override
	public long getCounter() {
		return stopwatch.getCounter();
	}

	@Override
	public double getStandardDeviation() {
		return nanosToMillis(stopwatch.getStandardDeviation());
	}

}
