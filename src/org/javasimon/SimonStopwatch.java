package org.javasimon;

/**
 * Simon.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public interface SimonStopwatch extends Simon {
	void addTime(long ns);

	void start();

	void stop();

	long getTotal();

	long getCounter();

	long getMax();

	long getMin();
}
