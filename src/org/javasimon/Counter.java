package org.javasimon;

/**
 * Counter tracks the single integer value and watches its max/min values. It can be used
 * for values starting with 0 - in that case min might not be important if counter does not
 * go bellow 0. Counter can also start from any other arbitrary number that is set after the
 * first change (increment, decrement, set) - this is more typical case for tracking also the
 * min value.
 *
 * <h3>Initialization</h3>
 * When a counter is created, it is set to 0, but its maximum/minimum values are undefined:
 * <pre>
 * Counter counter = SimonManager.getCounter("com.my.counter");
 * System.out.println("counter = " + counter);</pre>
 * Output is:
 * <pre>
 * counter = Simon Counter: [com.my.counter INHERIT/stats=NULL] counter=0, max=undef, min=undef</pre>
 *
 * This behavior allows the counter to be initialized - this also sets max/min values:
 * <pre>
 * Counter counter = SimonManager.getCounter("com.my.counter").set(47);
 * System.out.println("counter = " + counter);</pre>
 * Output is:
 * <pre>
 * counter = Simon Counter: [com.my.counter INHERIT/stats=NULL] counter=47, max=47, min=47</pre>
 *
 * <h3>Usage</h3>
 * Typical Counter usage is based on {@link #increment()} and {@link #decrement()} methods when
 * it is possible to track the monitored value - this can be used for example to count users logged
 * in. If the value changes by more than 1 than it is possible to use methods with arguments -
 * {@link #increment(long)} and {@link #decrement(long)}. Finally method {@link #set(long)} is
 * always available to set the counter to the particular value when needed.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public interface Counter extends Simon {
	/**
	 * Increments the counter by one.
	 *
	 * @return this
	 */
	Counter increment();

	/**
	 * Decrements the counter by one.
	 *
	 * @return this
	 */
	Counter decrement();

	/**
	 * Increments the counter by the specified value.
	 *
	 * @param inc added value
	 * @return this
	 */
	Counter increment(long inc);

	/**
	 * Increments the counter by the specified value.
	 *
	 * @param dec subtracted value
	 * @return this
	 */
	Counter decrement(long dec);

	/**
	 * Returns the current value of the counter.
	 *
	 * @return counter value
	 */
	long getCounter();

	/**
	 * Returns minimal value of counter.
	 *
	 * @return maximal reached value
	 */
	long getMin();

	/**
	 * Returns ms timestamp when the min value was reached.
	 *
	 * @return ms timestamp of the min value decremented
	 */
	long getMinTimestamp();

	/**
	 * Returns maximal value of counter.
	 *
	 * @return maximal reached value
	 */
	long getMax();

	/**
	 * Returns ms timestamp when the max value was reached.
	 *
	 * @return ms timestamp of the max value incremented
	 */
	long getMaxTimestamp();

	/**
	 * Sets the value of the counter to specified value.
	 *
	 * @param val new counter value
	 * @return this
	 */
	Counter set(long val);

	/**
	 * {@inheritDoc}
	 */
	Counter reset();

	/**
	 * Returns the sum of all incremented values. If incremented value was negative, sum
	 * is lowered by this value.
	 *
	 * @return sum of all incremented values
	 */
	long getIncrementSum();

	/**
	 * Returns the sum of all decremented values (as a positive number). If decremented value was negative, sum
	 * is raised by this value.
	 *
	 * @return sum of all decremented values
	 */
	long getDecrementSum();
}
