package org.javasimon.examples;

import org.javasimon.Stopwatch;
import org.javasimon.SimonManager;
import org.javasimon.SimonState;
import org.javasimon.Split;

/**
 * DisabledEnabledComparison.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class DisabledEnabledComparison {
	private static final int LOOP = 1000000;

	private DisabledEnabledComparison() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		Stopwatch tested;
		Stopwatch stopwatch;
		int round = 1;
		while (true) {
			System.out.println("\nRound: " + round++);
			SimonManager.clear();
			SimonManager.enable();

			stopwatch = SimonManager.getStopwatch(null);
			tested = SimonManager.getStopwatch("org.javasimon.stopwatch");
			Split split = stopwatch.start();
			for (int i = 0; i < LOOP; i++) {
				tested.start().stop();
			}
			split.stop();
			System.out.println("Enabled start/stop: " + stopwatch);

			stopwatch = SimonManager.getStopwatch(null);
			tested = SimonManager.getStopwatch("org.javasimon.stopwatch");
			tested.setState(SimonState.DISABLED, false);
			split = stopwatch.start();
			for (int i = 0; i < LOOP; i++) {
				tested.start().stop();
			}
			split.stop();
			System.out.println("Disabled start/stop: " + stopwatch);

			stopwatch = SimonManager.getStopwatch(null);
			SimonManager.getStopwatch("org.javasimon.stopwatch").setState(SimonState.ENABLED, false);

			split = stopwatch.start();
			for (int i = 0; i < LOOP; i++) {
				SimonManager.getStopwatch("org.javasimon.stopwatch").start().stop();
			}
			split.stop();
			System.out.println("Enabled get/start/stop: " + stopwatch);

			stopwatch = SimonManager.getStopwatch(null);
			SimonManager.disable();

			split = stopwatch.start();
			for (int i = 0; i < LOOP; i++) {
				SimonManager.getStopwatch("org.javasimon.stopwatch").start().stop();
			}
			split.stop();
			System.out.println("Disabled manager get/start/stop: " + stopwatch);
		}
	}
}
