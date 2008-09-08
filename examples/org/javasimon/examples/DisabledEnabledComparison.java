package org.javasimon.examples;

import org.javasimon.Stopwatch;
import org.javasimon.SimonManager;
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
			SimonManager.reset();
			SimonManager.enable();

			stopwatch = SimonManager.getStopwatch(null);
			tested = SimonManager.getStopwatch("org.javasimon.stopwatch");
			stopwatch.start();
			for (int i = 0; i < LOOP; i++) {
				tested.start();
				tested.stop();
			}
			stopwatch.stop();
			System.out.println("Enabled start/stop: " + stopwatch);

			stopwatch = SimonManager.getStopwatch(null);
			tested = SimonManager.getStopwatch("org.javasimon.stopwatch");
			tested.setState(SimonState.DISABLED, false);
			stopwatch.start();
			for (int i = 0; i < LOOP; i++) {
				tested.start();
				tested.stop();
			}
			stopwatch.stop();
			System.out.println("Disabled start/stop: " + stopwatch);

			stopwatch = SimonManager.getStopwatch(null);
			SimonManager.getStopwatch("org.javasimon.stopwatch").setState(SimonState.ENABLED, false);

			stopwatch.start();
			for (int i = 0; i < LOOP; i++) {
				tested = SimonManager.getStopwatch("org.javasimon.stopwatch").start();
				tested.stop();
			}
			stopwatch.stop();
			System.out.println("Enabled get/start/stop: " + stopwatch);

			stopwatch = SimonManager.getStopwatch(null);
			SimonManager.disable();

			stopwatch.start();
			for (int i = 0; i < LOOP; i++) {
				tested = SimonManager.getStopwatch("org.javasimon.stopwatch").start();
				tested.stop();
			}
			stopwatch.stop();
			System.out.println("Disabled manager get/start/stop: " + stopwatch);
		}
	}
}
