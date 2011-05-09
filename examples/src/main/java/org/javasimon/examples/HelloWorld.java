package org.javasimon.examples;

import org.javasimon.Stopwatch;
import org.javasimon.SimonManager;
import org.javasimon.Split;

/**
 * HelloWorld is the most basic example of Stopwatch usage. You can show this
 * even to managers - it's that easy. :-) Hello world line shows that stopwatch
 * doesn't contain any results yet, these are added after stop method.
 *
 * You can experiment with this example, try to put start/stop into the loop and
 * check total time, or whatever.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class HelloWorld {
	private HelloWorld() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		Stopwatch stopwatch = SimonManager.getStopwatch("org.javasimon.examples.HelloWorld-stopwatch");

		Split split = stopwatch.start();
		System.out.println("Hello world, " + stopwatch);
		split.stop();

		System.out.println("Result: " + stopwatch);
	}
}
