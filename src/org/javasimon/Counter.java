package org.javasimon;

/**
 * Counter tracks the single integer value and watches its max/min values. It can be used
 * for values starting with 0 - in that case min might not be important if counter does not
 * go bellow 0. Counter can also start from any other arbitrary number that is set after the
 * first change (increment, decrement, set) - this is more typical case for tracking also the
 * min value.
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
