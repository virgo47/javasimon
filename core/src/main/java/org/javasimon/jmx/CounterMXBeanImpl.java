package org.javasimon.jmx;

import org.javasimon.Simon;
import org.javasimon.Counter;

/**
 * MX Bean representing a particular {@link org.javasimon.Counter}. It is not created
 * by default when JMX is activated - it must be created explicitely.
 * {@link JmxRegisterCallback} can be used to automate this.
 * <p/>
 * Class can be subclassed to override default behavior if desired, {@link #counter} is declared protected for this reason.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public class CounterMXBeanImpl extends AbstractSimonMXBeanImpl implements CounterMXBean {
	protected Counter counter;

	/**
	 * Creates the MX bean for the provided Counter.
	 *
	 * @param counter wrapped Counter
	 */
	public CounterMXBeanImpl(Counter counter) {
		this.counter = counter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void increase() {
		counter.increase();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decrease() {
		counter.decrease();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void increase(long inc) {
		counter.increase(inc);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decrease(long dec) {
		counter.decrease(dec);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(long val) {
		counter.set(val);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CounterSample sample() {
		return new CounterSample(counter.sample());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CounterSample sampleAndReset() {
		return new CounterSample(counter.sampleAndReset());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getType() {
		return SimonInfo.COUNTER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Simon simon() {
		return counter;
	}
}
