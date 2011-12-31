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
	/**
	 * Wrapped {@link Stopwatch} instance - protected for subclasses.
	 */
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
	public final void addTime(long ns) {
		stopwatch.addTime(ns);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final long getLast() {
		return stopwatch.getLast();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getLastAsString() {
		return SimonUtils.presentNanoTime(getLast());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final StopwatchSample sample() {
		return new StopwatchSample(stopwatch.sample());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final StopwatchSample sampleAndReset() {
		return new StopwatchSample(stopwatch.sampleAndReset());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getType() {
		return SimonInfo.STOPWATCH;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final Simon simon() {
		return stopwatch;
	}
}
