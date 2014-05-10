package org.javasimon;

/**
 * Counter tracks the single integer value and watches its max/min values. It can be used
 * for values starting with 0 - in that case min might not be important if counter does not
 * go bellow 0. Counter can also start from any other arbitrary number that is set after the
 * first change (increment, decrement, set) - this is more typical case for tracking also the
 * min value.
 * <p/>
 * <h3>Initialization</h3>
 * When a counter is created, it is set to 0, but its maximum/minimum values are undefined:
 * <pre>
 * Counter counter = SimonManager.getCounter("com.my.counter");
 * System.out.println("counter = " + counter);</pre>
 * Output is:
 * <pre>
 * counter = Simon Counter: [com.my.counter INHERIT] counter=0, max=undef, min=undef</pre>
 *
 * This behavior allows the counter to be initialized before it is used and its extremes are
 * tracked - first initialization also sets max/min (extreme) values:
 * <pre>
 * Counter counter = SimonManager.getCounter("com.my.counter").set(47);
 * System.out.println("counter = " + counter);</pre>
 * Output is:
 * <pre>
 * counter = Simon Counter: [com.my.counter INHERIT] counter=47, max=47, min=47</pre>
 *
 * <h3>Usage</h3>
 * Typical Counter usage is based on {@link #increase()} and {@link #decrease()} methods when
 * it is possible to track the monitored value - this can be used for example to count users logged
 * in. If the value changes by more than 1 than it is possible to use methods with arguments -
 * {@link #increase(long)} and {@link #decrease(long)}. Finally method {@link #set(long)} is
 * always available to set the counter to the particular value when needed.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public interface Counter extends Simon {

	/**
	 * Increments the counter by one.
	 *
	 * @return this
	 */
	Counter increase();

	/**
	 * Decrements the counter by one.
	 *
	 * @return this
	 */
	Counter decrease();

	/**
	 * Increments the counter by the specified value. Using negative values is possible but may provide
	 * unexpected results - this method updates only incrementSum, it is decreased when negative number is used.
	 * Min and max are updated as expected.
	 *
	 * @param inc added value
	 * @return this
	 */
	Counter increase(long inc);

	/**
	 * Increments the counter by the specified value. Using negative values is possible but may provide
	 * unexpected results - this method updates only decrementSum, it is decreased when negative number is used.
	 * Min and max are updated as expected.
	 *
	 * @param dec subtracted value
	 * @return this
	 */
	Counter decrease(long dec);

	/**
	 * Returns the current value of the counter.
	 *
	 * @return counter value
	 */
	long getCounter();

	/**
	 * Returns minimal value of counter. Updated by {@link #decrease()}, {@link #decrease(long)} and {@link #set(long)}.
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
	 * Returns maximal value of counter. Updated by {@link #increase()}, {@link #increase(long)} and {@link #set(long)}.
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
	 * Returns the sum of all incremented values. If incremented value was negative, sum
	 * is lowered by this value.
	 *
	 * @return sum of all incremented values
	 */
	long getIncrementSum();

	/**
	 * Returns the sum of all decremented values (as a positive number). If decremented value was negative, sum
	 * is lowered by this value.
	 *
	 * @return sum of all decremented values
	 */
	long getDecrementSum();

	@Override
	CounterSample sample();

	CounterSample sampleIncrement(Object key);
}
