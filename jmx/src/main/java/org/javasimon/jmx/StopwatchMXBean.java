package org.javasimon.jmx;

/**
 * StopwatchMXBean.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Mar 6, 2009
 */
public interface StopwatchMXBean extends SimonSuperMXBean {
	/**
	 * Adds split time in nanoseconds to total time of the stopwatch.
	 *
	 * @param ns split time
	 */
	void addTime(long ns);

	/**
	 * Returns value of the last added split - wheter it was added directly or with stop method.
	 *
	 * @return value of the last added split
	 */
	long getLast();

	/**
	 * {@inheritDoc}
	 */
	@Override
	StopwatchSample sample();

	/**
	 * {@inheritDoc}
	 */
	@Override
	StopwatchSample sampleAndReset();
}
