package org.javasimon.jmx;

import org.javasimon.Counter;
import org.javasimon.Simon;

/**
 * MX Bean representing a particular {@link org.javasimon.Counter}. It is not created
 * by default when JMX is activated - it must be created explicitly.
 * {@link JmxRegisterCallback} can be used to automate this.
 * <p/>
 * Class can be subclassed to override default behavior if desired, {@link #counter} is declared protected for this reason.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public class CounterMXBeanImpl extends AbstractSimonMXBeanImpl implements CounterMXBean {

	/** Wrapped {@link Counter} instance - protected for subclasses. */
	protected Counter counter;

	/**
	 * Creates the MX bean for the provided Counter.
	 *
	 * @param counter wrapped Counter
	 */
	public CounterMXBeanImpl(Counter counter) {
		this.counter = counter;
	}

	@Override
	public final void increase() {
		counter.increase();
	}

	@Override
	public final void decrease() {
		counter.decrease();
	}

	@Override
	public final void increase(long inc) {
		counter.increase(inc);
	}

	@Override
	public final void decrease(long dec) {
		counter.decrease(dec);
	}

	@Override
	public long getMaxTimestamp() {
		return counter.getMaxTimestamp();
	}

	@Override
	public long getMax() {
		return counter.getMax();
	}

	@Override
	public long getMinTimestamp() {
		return counter.getMinTimestamp();
	}

	@Override
	public long getMin() {
		return counter.getMin();
	}

	@Override
	public long getCounter() {
		return counter.getCounter();
	}

	@Override
	public final void set(long val) {
		counter.set(val);
	}

	@Override
	public long getIncrementSum() {
		return counter.getIncrementSum();
	}

	@Override
	public long getDecrementSum() {
		return counter.getDecrementSum();
	}

	@Override
	public final CounterSample sample() {
		return new CounterSample(counter.sample());
	}

	@Override
	public CounterSample sampleIncrement(String key) {
		return new CounterSample(counter.sampleIncrement(key));
	}

	@Override
	public final String getType() {
		return SimonInfo.COUNTER;
	}

	@Override
	public boolean stopIncrementalSampling(String key) {
		return counter.stopIncrementalSampling(key);
	}

	@Override
	protected final Simon simon() {
		return counter;
	}
}
