package org.javasimon;

/**
 * SimonCounter.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public interface SimonCounter extends Simon {
	void increment();

	void decrement();

	void increment(long inc);

	void decrement(long inc);

	long getCounter();
}
