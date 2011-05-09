package org.javasimon.examples;

import org.javasimon.SimonManager;
import org.javasimon.utils.SimonUtils;
import org.javasimon.Stopwatch;
import org.javasimon.Split;

/**
 * Compares time to obtain the Simon from the Manager with start/stop method calls.
 * It also measures loop containing get+start/stop construction, so you can get the idea of how fast
 * it is. Generally - it's not necessary to hold the Simon in a class field but if you measure something
 * critical, you can do that and get some time. All the measuring follows after inserting 100000 stopwatches
 * into the SM's internal HashMap (sorted map was much slower). Last run is done with SM after reset
 * (nearly empty HashMap).
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class ManagerVsStopwatchComparison {
	private static final int LOOP = 10000000;

	private static final String NAME = SimonUtils.generateName("-stopwatch", false);

	private ManagerVsStopwatchComparison() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		String name = NAME;
		for (int i = 0; i < 100000; i++) {
			name = Long.toHexString((long) (Math.random() * Long.MAX_VALUE));
			SimonManager.getStopwatch(name);
		}
		System.out.println("Manager initialized");

		// warmup
		Stopwatch tested = SimonManager.getStopwatch(name);
		getStartStopTest(name);
		System.out.println("Warm-up complete");

		Stopwatch stopwatch = SimonManager.getStopwatch(null);
		Split split = stopwatch.start();
		getStopwatchTest(name);
		System.out.println("\ngetStopwatch: " + SimonUtils.presentNanoTime(split.stop()));

		tested.reset();
		split = stopwatch.reset().start();
		startStopTest(tested);
		System.out.println("\nstart/stop: " + SimonUtils.presentNanoTime(split.stop()));
		System.out.println("Stopwatch: " + tested);

		tested.reset();
		split = stopwatch.reset().start();
		getStartStopTest(name);
		System.out.println("\nget+start/stop: " + SimonUtils.presentNanoTime(split.stop()));
		System.out.println("Stopwatch: " + tested);

		SimonManager.clear();
		tested = SimonManager.getStopwatch(name); // after clear you have to get it from SM again to recreate it
		tested.reset();
		split = stopwatch.reset().start();
		getStartStopTest(name);
		System.out.println("\nget+start/stop after SM clear: " + SimonUtils.presentNanoTime(split.stop()));
		System.out.println("Stopwatch: " + tested);
	}

	private static void getStopwatchTest(String name) {
		for (int i = 0; i < LOOP; i++) {
			SimonManager.getStopwatch(name);
		}
	}

	private static void startStopTest(Stopwatch stopwatch) {
		for (int i = 0; i < LOOP; i++) {
			stopwatch.start().stop();
		}
	}

	private static void getStartStopTest(String name) {
		for (int i = 0; i < LOOP; i++) {
			SimonManager.getStopwatch(name).start().stop();
		}
	}
}
