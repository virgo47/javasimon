package org.javasimon.examples;

import org.javasimon.Stopwatch;
import org.javasimon.SimonFactory;
import org.javasimon.SimonState;

/**
 * DisabledEnabledComparison.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 5, 2008
 */
public final class DisabledEnabledComparison {
	private static final int LOOP = 10000000;

	private DisabledEnabledComparison() {
	}

	public static void main(String[] args) {
		Stopwatch tested;
		Stopwatch stopwatch;
		int round = 1;
		while (true) {
			System.out.println("\nRound: " + round++);
			SimonFactory.reset();
			SimonFactory.enable();

			stopwatch = SimonFactory.getStopwatch(null);
			tested = SimonFactory.getStopwatch("org.javasimon.stopwatch");
			stopwatch.start();
			for (int i = 0; i < LOOP; i++) {
				tested.start();
				tested.stop();
			}
			stopwatch.stop();
			System.out.println("Enabled start/stop: " + stopwatch);

			stopwatch = SimonFactory.getStopwatch(null);
			tested = SimonFactory.getStopwatch("org.javasimon.stopwatch");
			tested.setState(SimonState.DISABLED, false);
			stopwatch.start();
			for (int i = 0; i < LOOP; i++) {
				tested.start();
				tested.stop();
			}
			stopwatch.stop();
			System.out.println("Disabled start/stop: " + stopwatch);

			stopwatch = SimonFactory.getStopwatch(null);
			SimonFactory.getStopwatch("org.javasimon.stopwatch").setState(SimonState.ENABLED, false);

			stopwatch.start();
			for (int i = 0; i < LOOP; i++) {
				tested = SimonFactory.getStopwatch("org.javasimon.stopwatch").start();
				tested.stop();
			}
			stopwatch.stop();
			System.out.println("Enabled get/start/stop: " + stopwatch);

			stopwatch = SimonFactory.getStopwatch(null);
			SimonFactory.disable();

			stopwatch.start();
			for (int i = 0; i < LOOP; i++) {
				tested = SimonFactory.getStopwatch("org.javasimon.stopwatch").start();
				tested.stop();
			}
			stopwatch.stop();
			System.out.println("Disabled factory get/start/stop: " + stopwatch);
		}
	}
}
