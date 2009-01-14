package org.javasimon.examples;

import org.javasimon.SimonManager;
import org.javasimon.SimonState;
import org.javasimon.utils.SimonUtils;
import org.javasimon.Split;

/**
 * DisabledEnabledComparison.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 5, 2008
 */
public final class DisabledEnabledSemiRealComparison {
	private static final String TEST1_SIMON_NAME = SimonUtils.generateName("-stopwatch", true);

	private static final int OUTER_LOOP = 100;
	private static final int INNER_LOOP = 100;
	private static final int ROUNDS = 10;

	// Random strings for test method to concatenate
	private static final String[] STRINGS = {"AB", "ZD", "ZK", "SE", "AM", "whatever"};

	// Total nanosecond times
	private static long disbledSimonManager = 0;
	private static long enabledSimonManager = 0;
	private static long disbledTestSimon = 0;
	private static long disbledTopSimon = 0;
	private static long enabledTestSimon = 0;
	private static long pureTest = 0;

	private DisabledEnabledSemiRealComparison() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		long simonMeasurement = 0;
		long simonCounter = 0;

		for (int round = 1; round <= ROUNDS; round++) {
			SimonManager.clear();

			SimonManager.disable();
			disbledSimonManager += performMeasurement();

			SimonManager.enable();
			enabledSimonManager += performMeasurement();

			SimonManager.getSimon(TEST1_SIMON_NAME).setState(SimonState.DISABLED, false);
			disbledTestSimon += performMeasurement();

			SimonManager.getRootSimon().setState(SimonState.DISABLED, true);
			disbledTopSimon += performMeasurement();

			SimonManager.getSimon(TEST1_SIMON_NAME).setState(SimonState.ENABLED, false);
			enabledTestSimon += performMeasurement();

			// The same like measurement but without Simons
			for (int i = 0; i < OUTER_LOOP; i++) {
				testMethodWithoutSimons();
			}
			long ns = System.nanoTime();
			for (int i = 0; i < OUTER_LOOP; i++) {
				testMethodWithoutSimons();
			}
			pureTest += System.nanoTime() - ns;

			simonMeasurement += SimonManager.getStopwatch(TEST1_SIMON_NAME).getTotal();
			simonCounter += SimonManager.getStopwatch(TEST1_SIMON_NAME).getCounter();
			System.out.println("Round " + round + " finished");
		}
		System.out.println("Disabled SimonManager: " + SimonUtils.presentNanoTime(disbledSimonManager));
		System.out.println("Enabled SimonManager: " + SimonUtils.presentNanoTime(enabledSimonManager));
		System.out.println("Disabled test Simon: " + SimonUtils.presentNanoTime(disbledTestSimon));
		System.out.println("Disabled top Simon: " + SimonUtils.presentNanoTime(disbledTopSimon));
		System.out.println("Explicitly enbaled test Simon: " + SimonUtils.presentNanoTime(enabledTestSimon));
		System.out.println("Pure test method without Simon: " + SimonUtils.presentNanoTime(pureTest));

		System.out.println("Simon measurement: " + SimonUtils.presentNanoTime(simonMeasurement));
		System.out.println("Simon counter: " + simonCounter);
	}

	private static long performMeasurement() {
		for (int i = 0; i < OUTER_LOOP; i++) {
			testMethod();
		}
		long ns = System.nanoTime();
		for (int i = 0; i < OUTER_LOOP; i++) {
			testMethod();
		}
		return System.nanoTime() - ns;
	}

	private static void testMethod() {
		Split split = SimonManager.getStopwatch(TEST1_SIMON_NAME).start();
		StringBuilder builder;
		for (int i = 0; i < INNER_LOOP; i++) {
			builder = new StringBuilder();
			for (String s : STRINGS) {
				builder.append(s);
			}
			builder.reverse();
		}
		Runtime.getRuntime().gc();
		split.stop();
	}

	private static void testMethodWithoutSimons() {
		StringBuilder builder;
		for (int i = 0; i < INNER_LOOP; i++) {
			builder = new StringBuilder();
			for (String s : STRINGS) {
				builder.append(s);
			}
			builder.reverse();
		}
		Runtime.getRuntime().gc();
	}
}