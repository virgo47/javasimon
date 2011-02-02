package org.javasimon.jmx;

import org.javasimon.Simon;
import org.javasimon.Counter;

/**
 * MX Bean representing a particular {@link org.javasimon.Counter}. It is not created
 * by default when JMX is activated - it must be created explicitely.
 * {@link JmxRegisterCallback} can be used to automate this.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Mar 6, 2009
 */
public class CounterMXBeanImpl extends AbstractSimonMXBeanImpl implements CounterMXBean {
	private Counter counter;

	/**
	 * Creates the MX bean for the provided Counter.
	 *
	 * @param counter wrapped Counter
	 */
	protected CounterMXBeanImpl(Counter counter) {
		this.counter = counter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void increase() {
		counter.increase();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void decrease() {
		counter.decrease();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void increase(long inc) {
		counter.increase(inc);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void decrease(long dec) {
		counter.decrease(dec);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void set(long val) {
		counter.set(val);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final CounterSample sample() {
		return new CounterSample((org.javasimon.CounterSample) counter.sample());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final CounterSample sampleAndReset() {
		return new CounterSample((org.javasimon.CounterSample) counter.sampleAndReset());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getType() {
		return SimonInfo.COUNTER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final Simon simon() {
		return counter;
	}
}
