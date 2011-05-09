package org.javasimon.examples;

import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.Split;

/**
 * Simple example of the measuring a method in a loop. Method takes random time to finish.
 */
public final class LoopingRandomMethod {
	/**
	 * Entry point to the Example.
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		Stopwatch stopwatch = SimonManager.getStopwatch("stopwatch");
		for (int i = 1; i <= 10; i++) {
			strangeMethod();
			System.out.println("Stopwatch after round " + i + ": " + stopwatch);
		}
		System.out.println("stopwatch.sample() = " + stopwatch.sample());
	}

	/**
	 * Method that lasts randomly from ~0 to ~2500 ms.
	 */
	private static void strangeMethod() {
		Split split = SimonManager.getStopwatch("stopwatch").start();
		long random = (long) (Math.random() * 50);
		try {
			Thread.sleep(random * random);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		split.stop();
	}
}
