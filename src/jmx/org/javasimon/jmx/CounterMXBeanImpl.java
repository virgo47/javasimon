package org.javasimon.jmx;

import org.javasimon.Simon;
import org.javasimon.Counter;

/**
 * Counter MXBean implementation.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Mar 6, 2009
 */
public class CounterMXBeanImpl extends SimonSuperMXBeanImpl implements CounterMXBean {
	private Counter counter;

	protected CounterMXBeanImpl(Counter counter) {
		this.counter = counter;
	}

	public void increase() {
		counter.increase();
	}

	public void decrease() {
		counter.decrease();
	}

	public void increase(long inc) {
		counter.increase(inc);
	}

	public void decrease(long dec) {
		counter.decrease(dec);
	}

	public void set(long val) {
		counter.set(val);
	}

	public CounterSample sample() {
		return new CounterSample((org.javasimon.CounterSample) counter.sample());
	}

	public CounterSample sampleAndReset() {
		return new CounterSample((org.javasimon.CounterSample) counter.sampleAndReset());
	}

	public String getType() {
		return "Counter";
	}

	protected Simon simon() {
		return counter;
	}
}
