package org.javasimon;

/**
 * FactoryVsStopwatchComparison.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 6, 2008
 */
public final class FactoryVsStopwatchComparison {
	private static final int LOOP = 10000000;

	private static final String NAME = SimonFactory.generateName("-stopwatch", false);

	private FactoryVsStopwatchComparison() {
	}

	public static void main(String[] args) {
		// warmup
		factoryTest();

		long ns = System.nanoTime();
		factoryTest();
		System.out.println("SimonFactory.getStopwatch(): " + SimonUtils.presentTime(System.nanoTime() - ns));

		SimonStopwatch stopwatch = SimonFactory.getStopwatch(NAME);
		stopwatchTest(stopwatch);

		ns = System.nanoTime();
		stopwatchTest(stopwatch);
		System.out.println("Stopwatch start/stop: " + SimonUtils.presentTime(System.nanoTime() - ns));
		System.out.println("Stopwatch: " + stopwatch);
	}

	private static void stopwatchTest(SimonStopwatch stopwatch) {
		for (int i = 0; i < LOOP; i++) {
			stopwatch.start();
			stopwatch.stop();
		}
	}

	private static void factoryTest() {
		for (int i = 0; i < LOOP; i++) {
			SimonFactory.getStopwatch(NAME);
		}
	}
}
