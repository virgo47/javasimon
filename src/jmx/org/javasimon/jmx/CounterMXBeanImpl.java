package org.javasimon.jmx;

import org.javasimon.Simon;
import org.javasimon.Counter;

/**
 * Counter MXBean implementation.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Mar 6, 2009
 */
public class CounterMXBeanImpl extends AbstractSimonMXBeanImpl implements CounterMXBean {
	private Counter counter;

	protected CounterMXBeanImpl(Counter counter) {
		this.counter = counter;
	}

	public final void increase() {
		counter.increase();
	}

	public final void decrease() {
		counter.decrease();
	}

	public final void increase(long inc) {
		counter.increase(inc);
	}

	public final void decrease(long dec) {
		counter.decrease(dec);
	}

	public final void set(long val) {
		counter.set(val);
	}

	@Override
	public CounterSample sample() {
		return new CounterSample((org.javasimon.CounterSample) counter.sample());
	}

	@Override
	public CounterSample sampleAndReset() {
		return new CounterSample((org.javasimon.CounterSample) counter.sampleAndReset());
	}

	public final String getType() {
		return "Counter";
	}

	protected Simon simon() {
		return counter;
	}
}
