package org.javasimon;

/**
 * Simon.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public interface Stopwatch extends Simon {
	Stopwatch addTime(long ns);

	Stopwatch start();

	long stop();

	long getTotal();

	long getCounter();

	long getMax();

	long getMin();

	Stopwatch reset();
}
