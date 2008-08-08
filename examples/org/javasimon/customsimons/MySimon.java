package org.javasimon.customsimons;

/**
 * MySimon only measures time and it's not threadsafe. It has one start method
 * and one stop method that returns the delta. No cumulative state is stored.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 7, 2008
 */
public interface MySimon {
	void start();
	long stop();
}
