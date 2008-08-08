package org.javasimon;

/**
 * DisabledEnabledComparison.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 5, 2008
 */
public final class DisabledEnabledComparison {
	private static final int LOOP = 10000000;
	private static final int ROUNDS = 10;

	private DisabledEnabledComparison() {
	}

	public static void main(String[] args) {
		for (int round = 1; round <= ROUNDS; round++) {
			SimonFactory.reset();
			SimonFactory.enable();
			SimonStopwatch stopwatch = new SimonStopwatchSimple(null);
//			SimonStopwatch tested = new SimonStopwatchSimple(null);

			stopwatch.start();
			for (int i = 0; i < LOOP; i++) {
				SimonStopwatch tested = SimonFactory.getStopwatch("org.javasimon.stopwatch");
				tested.start();
				tested.stop();
			}
			stopwatch.stop();
			System.out.println("Enabled: " + stopwatch);

			stopwatch = new SimonStopwatchSimple(null);
//			tested = new SimonStopwatchSimple(null).getDisabledDecorator();

			SimonFactory.disable();
			stopwatch.start();
			for (int i = 0; i < LOOP; i++) {
				SimonStopwatch tested = SimonFactory.getStopwatch("org.javasimon.stopwatch");
				tested.start();
				tested.stop();
			}
			stopwatch.stop();
			System.out.println("Disabled: " + stopwatch);
		}
	}
}
