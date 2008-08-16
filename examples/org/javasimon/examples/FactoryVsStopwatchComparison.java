package org.javasimon.examples;

import org.javasimon.SimonFactory;
import org.javasimon.utils.SimonUtils;
import org.javasimon.Stopwatch;

/**
 * FactoryVsStopwatchComparison compares obtaining Simon from the factory with start/stop method calls.
 * It also measures loop containing get+start/stop construction, so you can get the idea of how fast
 * it is. Generally - it's not necessary to hold the Simon in a class field but if you measure something
 * critical, you can do that and get some time.
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
		Stopwatch tested = SimonFactory.getStopwatch(NAME);
		getStopwatchTest();
		startStopTest(tested);
		getStartStopTest();

		Stopwatch stopwatch = SimonFactory.getStopwatch(null).start();
		getStopwatchTest();
		System.out.println("getStopwatch: " + SimonUtils.presentNanoTime(stopwatch.stop()));

		tested.reset();
		stopwatch.reset().start();
		startStopTest(tested);
		System.out.println("start/stop: " + SimonUtils.presentNanoTime(stopwatch.stop()));
		System.out.println("Stopwatch: " + tested);

		tested.reset();
		stopwatch.reset().start();
		getStartStopTest();
		System.out.println("get+start/stop: " + SimonUtils.presentNanoTime(stopwatch.stop()));
		System.out.println("Stopwatch: " + tested);
	}

	private static void getStopwatchTest() {
		for (int i = 0; i < LOOP; i++) {
			SimonFactory.getStopwatch(NAME);
		}
	}

	private static void startStopTest(Stopwatch stopwatch) {
		for (int i = 0; i < LOOP; i++) {
			stopwatch.start();
			stopwatch.stop();
		}
	}

	private static void getStartStopTest() {
		for (int i = 0; i < LOOP; i++) {
			Stopwatch stopwatch = SimonFactory.getStopwatch(NAME).start();
			stopwatch.stop();
		}
	}
}
