package org.javasimon.jmx;

import org.javasimon.Stopwatch;
import org.javasimon.Simon;
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
	protected Stopwatch stopwatch;

	/**
	 * Creates the MX bean for the provided Stopwatch.
	 *
	 * @param stopwatch wrapped Stopwatch
	 */
	public StopwatchMXBeanImpl(Stopwatch stopwatch) {
		this.stopwatch = stopwatch;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addTime(long ns) {
		stopwatch.addTime(ns);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLast() {
		return stopwatch.getLast();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLastAsString() {
		return SimonUtils.presentNanoTime(getLast());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StopwatchSample sample() {
		return new StopwatchSample(stopwatch.sample());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StopwatchSample sampleAndReset() {
		return new StopwatchSample(stopwatch.sampleAndReset());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Simon simon() {
		return stopwatch;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getType() {
		return SimonInfo.STOPWATCH;
	}
}
