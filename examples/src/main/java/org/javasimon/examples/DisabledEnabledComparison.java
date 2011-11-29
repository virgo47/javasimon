package org.javasimon.examples;

import org.javasimon.*;
import org.javasimon.utils.BenchmarkUtils;
import org.javasimon.utils.GoogleChartImageGenerator;
import org.javasimon.utils.SimonUtils;

/**
 * Compares start-stop cycle of an enabled and disabled Stopwatch and get-start-stop cycle on an enabled and disabled Manager.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class DisabledEnabledComparison {
	private static final int LOOP = 10000000;

	private DisabledEnabledComparison() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		StopwatchSample[] results = BenchmarkUtils.run(2, 5,
			new BenchmarkUtils.Task("enabled") {
				@Override
				public void perform() throws Exception {
					SimonManager.clear();
					SimonManager.enable();
					Stopwatch tested = SimonManager.getStopwatch("org.javasimon.stopwatch");
					for (int i = 0; i < LOOP; i++) {
						tested.start().stop();
					}
				}
			},
			new BenchmarkUtils.Task("disabled") {
				@Override
				public void perform() throws Exception {
					SimonManager.clear();
					SimonManager.enable();
					Stopwatch tested = SimonManager.getStopwatch("org.javasimon.stopwatch");
					tested.setState(SimonState.DISABLED, false);
					for (int i = 0; i < LOOP; i++) {
						tested.start().stop();
					}
				}
			},
			new BenchmarkUtils.Task("mgr-enabled") {
				@Override
				public void perform() throws Exception {
					SimonManager.clear();
					SimonManager.enable();
					for (int i = 0; i < LOOP; i++) {
						SimonManager.getStopwatch("org.javasimon.stopwatch").start().stop();
					}
				}
			},
			new BenchmarkUtils.Task("mgr-disabled") {
				@Override
				public void perform() throws Exception {
					SimonManager.clear();
					SimonManager.disable();
					for (int i = 0; i < LOOP; i++) {
						SimonManager.getStopwatch("org.javasimon.stopwatch").start().stop();
					}
				}
			}
		);

		System.out.println("\nGoogle Chart avg:\n" + GoogleChartImageGenerator.barChart(
			results, "10M-loop duration", SimonUtils.NANOS_IN_MILLIS, "ms", false));
		System.out.println("\nGoogle Chart avg/max/min:\n" + GoogleChartImageGenerator.barChart(
			results, "10M-loop duration", SimonUtils.NANOS_IN_MILLIS, "ms", true));
	}
}
