package org.javasimon.jmx;

/**
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Mar 6, 2009
 */
public interface CounterMXBean extends SimonSuperMXBean {
	/**
	 * Increments the counter by one.
	 */
	void increase();

	/**
	 * Decrements the counter by one.
	 */
	void decrease();

	/**
	 * Increments the counter by the specified value.
	 *
	 * @param inc added value
	 */
	void increase(long inc);

	/**
	 * Increments the counter by the specified value.
	 *
	 * @param dec subtracted value
	 */
	void decrease(long dec);

	/**
	 * Sets the value of the counter to specified value.
	 *
	 * @param val new counter value
	 */
	void set(long val);

	CounterSample sample();

	CounterSample sampleAndReset();
}