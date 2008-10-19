package org.javasimon.examples;

import org.javasimon.SimonManager;
import org.javasimon.utils.SimonUtils;
import org.javasimon.Stopwatch;

/**
 * Compares get/start/stop with static name with the same cycle with name generated every time.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 6, 2008
 */
public final class SimonNameGenerationTest {
	private static final int LOOP = 100000;

	private static final String NAME = SimonManager.generateName("-stopwatch", false);

	private SimonNameGenerationTest() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		// warmup
		Stopwatch tested = SimonManager.getStopwatch(NAME);
		getStartStopTest();
		getStartStopGenerateWithSuffixTest();
		System.out.println("Warm-up complete");

		Stopwatch stopwatch = SimonManager.getStopwatch(null).start();
		tested.reset();
		getStartStopTest();
		System.out.println("\nget+start/stop: " + SimonUtils.presentNanoTime(stopwatch.stop()));
		System.out.println("Stopwatch: " + tested);

		tested.reset();
		stopwatch.reset().start();
		getStartStopGenerateWithSuffixTest();
		System.out.println("\nget generated+start/stop: " + SimonUtils.presentNanoTime(stopwatch.stop()));
		System.out.println("Stopwatch: " + tested);
	}

	private static void getStartStopTest() {
		for (int i = 0; i < LOOP; i++) {
			Stopwatch stopwatch = SimonManager.getStopwatch(NAME).start();
			stopwatch.stop();
		}
	}

	private static void getStartStopGenerateWithSuffixTest() {
		for (int i = 0; i < LOOP; i++) {
			Stopwatch stopwatch = SimonManager.getStopwatch(SimonManager.generateName("-stopwatch", false)).start();
			stopwatch.stop();
		}
	}
}