package org.javasimon;

/**
 * SimonCounter.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public interface Counter extends Simon {
	Counter increment();

	Counter decrement();

	Counter increment(long inc);

	Counter decrement(long inc);

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

	Counter reset();

	Counter set(long val);
}
