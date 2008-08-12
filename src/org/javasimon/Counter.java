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

	long getMin();

	long getMax();

	Counter reset();
}
