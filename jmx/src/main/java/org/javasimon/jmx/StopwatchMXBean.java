package org.javasimon.jmx;

/**
 * Interface for MX Bean representing a particular {@link org.javasimon.Stopwatch}. It is not created
 * by default when JMX is activated - it must be created explicitely.
 * {@link JmxRegisterCallback} can be used to automate this.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
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
	 * Returns value of the last added split as formatted string.
	 *
	 * @return value of the last added split as string
	 */
	String getLastAsString();

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
