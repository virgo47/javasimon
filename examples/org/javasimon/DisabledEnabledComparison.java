package org.javasimon;

/**
 * DisabledEnabledComparison.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 5, 2008
 */
public final class DisabledEnabledComparison {
	private static final String TEST1_SIMON_NAME = SimonFactory.generateName("-stopwatch", true);

	private static final int OUTER_LOOP = 100;
	private static final int INNER_LOOP = 100;
	private static final int ROUNDS = 10;

	// Random strings for test method to concatenate
	public static final String[] STRINGS = {"AB", "ZD", "ZK", "SE", "AM", "whatever"};

	private static long disbledSimonFactory = 0;
	private static long enabledSimonFactory = 0;
	private static long disbledTestSimon = 0;
	private static long disbledTopSimon = 0;
	private static long enabledTestSimon = 0;
	private static long pureTest = 0;

	private DisabledEnabledComparison() {
	}

	public static void main(String[] args) {
		long simonMeasurement = 0;
		long simonCounter = 0;

		for (int round = 1; round <= ROUNDS; round++) {
			SimonFactory.reset();

			SimonFactory.disable();
			disbledSimonFactory += performMeasurement();

			SimonFactory.enable();
			enabledSimonFactory += performMeasurement();

			SimonFactory.getSimon(TEST1_SIMON_NAME).disable(false);
			disbledTestSimon += performMeasurement();

			SimonFactory.getRootSimon().disable(true);
			disbledTopSimon += performMeasurement();

			SimonFactory.getSimon(TEST1_SIMON_NAME).enable(false);
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

			simonMeasurement += SimonFactory.getStopwatch(TEST1_SIMON_NAME).getElapsedNanos();
			simonCounter += SimonFactory.getStopwatch(TEST1_SIMON_NAME).getCounter();
			System.out.println("Round " + round + " finished");
		}
		System.out.println("Disabled Simon Factory: " + SimonUtils.presentTime(disbledSimonFactory));
		System.out.println("Enabled Simon Factory: " + SimonUtils.presentTime(enabledSimonFactory));
		System.out.println("Disabled test Simon: " + SimonUtils.presentTime(disbledTestSimon));
		System.out.println("Disabled top Simon: " + SimonUtils.presentTime(disbledTopSimon));
		System.out.println("Explicitly enbaled test Simon: " + SimonUtils.presentTime(enabledTestSimon));
		System.out.println("Pure test method without Simon: " + SimonUtils.presentTime(pureTest));

		System.out.println("Simon measurement: " + SimonUtils.presentTime(simonMeasurement));
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
		SimonStopwatch simon = SimonFactory.getStopwatch(TEST1_SIMON_NAME);
		simon.start();
		StringBuilder builder = null;
		for (int i = 0; i < INNER_LOOP; i++) {
			builder = new StringBuilder();
			for (String s : STRINGS) {
				builder.append(s);
			}
			builder.reverse();
		}
		builder.setLength(0);
		Runtime.getRuntime().gc();
		simon.stop();
	}

	private static void testMethodWithoutSimons() {
		StringBuilder builder = null;
		for (int i = 0; i < INNER_LOOP; i++) {
			builder = new StringBuilder();
			for (String s : STRINGS) {
				builder.append(s);
			}
			builder.reverse();
		}
		builder.setLength(0);
		Runtime.getRuntime().gc();
	}
}
