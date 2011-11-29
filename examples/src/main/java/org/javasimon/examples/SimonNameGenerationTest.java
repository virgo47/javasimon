package org.javasimon.examples;

import org.javasimon.*;
import org.javasimon.utils.BenchmarkUtils;
import org.javasimon.utils.GoogleChartImageGenerator;
import org.javasimon.utils.SimonUtils;

/**
 * Compares get/start/stop with a static name and the same cycle with a name generated every time.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class SimonNameGenerationTest {
	private static final int LOOP = 200000;

	private static final String NAME = SimonUtils.generateNameForClass("-stopwatch");

	private SimonNameGenerationTest() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		StopwatchSample[] results = BenchmarkUtils.run(2, 5,
			new BenchmarkUtils.Task("get-start-stop") {
				@Override
				public void perform() throws Exception {
					for (int i = 0; i < LOOP; i++) {
						SimonManager.getStopwatch(NAME).start().stop();
					}
				}
			},
			new BenchmarkUtils.Task("gen-get-s-s") {
				@Override
				public void perform() throws Exception {
					for (int i = 0; i < LOOP; i++) {
						SimonManager.getStopwatch(SimonUtils.generateNameForClass("-stopwatch")).start().stop();
					}
				}
			},
			new BenchmarkUtils.Task("generate") {
				@Override
				public void perform() throws Exception {
					for (int i = 0; i < LOOP; i++) {
						SimonUtils.generateName();
					}
				}
			},
			new BenchmarkUtils.Task("get-stacktrace") {
				@Override
				public void perform() throws Exception {
					for (int i = 0; i < LOOP; i++) {
						Thread.currentThread().getStackTrace();
					}
				}
			}
		);

		System.out.println("\nGoogle Chart avg:\n" + GoogleChartImageGenerator.barChart(
			results, "200k-loop duration", SimonUtils.NANOS_IN_MILLIS, "ms", false));
		System.out.println("\nGoogle Chart avg/max/min:\n" + GoogleChartImageGenerator.barChart(
			results, "200k-loop duration", SimonUtils.NANOS_IN_MILLIS, "ms", true));
	}
}