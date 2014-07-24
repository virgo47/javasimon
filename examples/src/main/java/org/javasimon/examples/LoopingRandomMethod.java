package org.javasimon.examples;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

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
			try (Split ignored = SimonManager.getStopwatch("stopwatch").start()) {
				ExampleUtils.waitRandomlySquared(50);
			}
			System.out.println("Stopwatch after round " + i + ": " + stopwatch);
		}
		System.out.println("stopwatch.sample() = " + stopwatch.sample());
	}
}
