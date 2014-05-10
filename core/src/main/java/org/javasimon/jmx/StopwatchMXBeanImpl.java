package org.javasimon.jmx;

import org.javasimon.Simon;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;

/**
 * MX Bean representing a particular {@link org.javasimon.Stopwatch}. It is not created
 * by default when JMX is activated - it must be created explicitely.
 * {@link JmxRegisterCallback} can be used to automate this.
 * <p/>
 * Class can be subclassed to override default behavior if desired, {@link #stopwatch} is declared protected for this reason.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public class StopwatchMXBeanImpl extends AbstractSimonMXBeanImpl implements StopwatchMXBean {

	/** Wrapped {@link Stopwatch} instance - protected for subclasses. */
	protected Stopwatch stopwatch;

	/**
	 * Creates the MX bean for the provided Stopwatch.
	 *
	 * @param stopwatch wrapped Stopwatch
	 */
	public StopwatchMXBeanImpl(Stopwatch stopwatch) {
		this.stopwatch = stopwatch;
	}

	@Override
	public long getTotal() {
		return stopwatch.getTotal();
	}

	@Override
	public final long getLast() {
		return stopwatch.getLast();
	}

	@Override
	public final String getLastAsString() {
		return SimonUtils.presentNanoTime(getLast());
	}

	@Override
	public long getCounter() {
		return stopwatch.getCounter();
	}

	@Override
	public long getMax() {
		return stopwatch.getMax();
	}

	@Override
	public long getMin() {
		return stopwatch.getMin();
	}

	@Override
	public long getMaxTimestamp() {
		return stopwatch.getMaxTimestamp();
	}

	@Override
	public long getMinTimestamp() {
		return stopwatch.getMinTimestamp();
	}

	@Override
	public long getActive() {
		return stopwatch.getActive();
	}

	@Override
	public long getMaxActive() {
		return stopwatch.getMaxActive();
	}

	@Override
	public long getMaxActiveTimestamp() {
		return stopwatch.getMaxActiveTimestamp();
	}

	@Override
	public double getMean() {
		return stopwatch.getMean();
	}

	@Override
	public double getStandardDeviation() {
		return stopwatch.getStandardDeviation();
	}

	@Override
	public double getVariance() {
		return stopwatch.getVariance();
	}

	@Override
	public double getVarianceN() {
		return stopwatch.getVarianceN();
	}

	@Override
	public final StopwatchSample sample() {
		return new StopwatchSample(stopwatch.sample());
	}

	@Override
	public StopwatchSample sampleIncrement(String key) {
		return new StopwatchSample(stopwatch.sampleIncrement(key));
	}

	@Override
	public final String getType() {
		return SimonInfo.STOPWATCH;
	}

	@Override
	public boolean stopIncrementalSampling(String key) {
		return stopwatch.stopIncrementalSampling(key);
	}

	@Override
	protected final Simon simon() {
		return stopwatch;
	}
}
