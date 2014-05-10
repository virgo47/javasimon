package org.javasimon.examples.perf;

import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.clock.ClockUtils;
import org.javasimon.examples.ExampleUtils;
import org.javasimon.utils.BenchmarkUtils;
import org.javasimon.utils.GoogleChartImageGenerator;
import org.javasimon.utils.SimonUtils;

/**
 * Compares time to obtain the Simon from the Manager with start/stop method calls.
 * It also measures loop containing get+start/stop construction, so you can get the idea of how fast
 * it is. Generally - it's not necessary to hold the Simon in a class field but if you measure something
 * critical, you can do that and get some time. All the measuring follows after inserting 100000 stopwatches
 * into the SM's internal HashMap (sorted map was much slower). Last run is done with Manager after clear
 * (nearly empty HashMap).
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class ManagerVsStopwatchComparison {

	private static final int LOOP = 10000000;

	private static final String NAME = SimonUtils.generateNameForClass("-stopwatch");

	private ManagerVsStopwatchComparison() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		ExampleUtils.fillManagerWithSimons(100000);
		StopwatchSample[] results = BenchmarkUtils.run(2, 5,
			new BenchmarkUtils.Task("getStopwatch") {
				@Override
				public void perform() throws Exception {
					for (int i = 0; i < LOOP; i++) {
						SimonManager.getStopwatch(NAME);
					}
				}
			},
			new BenchmarkUtils.Task("start-stop") {
				@Override
				public void perform() throws Exception {
					Stopwatch stopwatch = SimonManager.getStopwatch(NAME);
					for (int i = 0; i < LOOP; i++) {
						stopwatch.start().stop();
					}
				}
			},
			new BenchmarkUtils.Task("get-start-stop") {
				@Override
				public void perform() throws Exception {
					SimonManager.getStopwatch(NAME);
					for (int i = 0; i < LOOP; i++) {
						SimonManager.getStopwatch(NAME).start().stop();
					}
				}
			},
			new BenchmarkUtils.Task("clear-get-start-stop") {
				@Override
				public void perform() throws Exception {
					SimonManager.clear(); // has to be recreated (only once though) + all other 100k simons are gone
					SimonManager.getStopwatch(NAME);
					for (int i = 0; i < LOOP; i++) {
						SimonManager.getStopwatch(NAME).start().stop();
					}
				}
			}
		);

		System.out.println("\nGoogle Chart avg:\n" + GoogleChartImageGenerator.barChart(
			results, "10M-loop duration", ClockUtils.NANOS_IN_MILLIS, "ms", false));
	}
}
