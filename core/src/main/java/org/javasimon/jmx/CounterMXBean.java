package org.javasimon.jmx;

/**
 * Interface for MX Bean representing a particular {@link org.javasimon.Counter}.
 * It is not created by default when JMX is activated - it must be created explicitely.
 * {@link JmxRegisterCallback} can be used to automate this.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public interface CounterMXBean extends SimonSuperMXBean {
	/**
	 * Increments the counter by one.
	 *
	 * @see org.javasimon.Counter#increase()
	 */
	void increase();

	/**
	 * Decrements the counter by one.
	 *
	 * @see org.javasimon.Counter#decrease()
	 */
	void decrease();

	/**
	 * Increments the counter by the specified value.
	 *
	 * @param inc added value
	 * @see org.javasimon.Counter#increase(long)
	 */
	void increase(long inc);

	/**
	 * Increments the counter by the specified value.
	 *
	 * @param dec subtracted value
	 * @see org.javasimon.Counter#decrease(long)
	 */
	void decrease(long dec);

	/**
	 * Returns the current value of the counter.
	 *
	 * @return counter value
	 * @since 3.3
	 * @see org.javasimon.Counter#getCounter()
	 */
	long getCounter();

	/**
	 * Returns minimal value of counter.
	 *
	 * @return maximal reached value
	 * @since 3.3
	 * @see org.javasimon.Counter#getMin()
	 */
	long getMin();

	/**
	 * Returns ms timestamp when the min value was reached.
	 *
	 * @return ms timestamp of the min value decremented
	 * @since 3.3
	 * @see org.javasimon.Counter#getMinTimestamp()
	 */
	long getMinTimestamp();

	/**
	 * Returns maximal value of counter.
	 *
	 * @return maximal reached value
	 * @since 3.3
	 * @see org.javasimon.Counter#getMax()
	 */
	long getMax();

	/**
	 * Returns ms timestamp when the max value was reached.
	 *
	 * @return ms timestamp of the max value incremented
	 * @since 3.3
	 * @see org.javasimon.Counter#getMaxTimestamp()
	 */
	long getMaxTimestamp();

	/**
	 * Sets the value of the counter to specified value.
	 *
	 * @param val new counter value
	 * @see org.javasimon.Counter#set(long)
	 */
	void set(long val);

	/**
	 * Returns the sum of all incremented values. If incremented value was negative, sum
	 * is lowered by this value.
	 *
	 * @return sum of all incremented values
	 * @since 3.3
	 * @see org.javasimon.Counter#getIncrementSum()
	 */
	long getIncrementSum();

	/**
	 * Returns the sum of all decremented values (as a positive number). If decremented value was negative, sum
	 * is lowered by this value.
	 *
	 * @return sum of all decremented values
	 * @since 3.3
	 * @see org.javasimon.Counter#getDecrementSum()
	 */
	long getDecrementSum();

	@Override
	CounterSample sample();

	@Override
	CounterSample sampleAndReset();
}