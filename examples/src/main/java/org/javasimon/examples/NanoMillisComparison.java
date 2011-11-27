package org.javasimon.examples;

import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.utils.BenchmarkUtils;
import org.javasimon.utils.GoogleChartImageGenerator;
import org.javasimon.utils.SimonUtils;

/**
 * Various timer calles compared along with stopwatch start/stop calls.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class NanoMillisComparison {
	private static final int LOOP = 10000000;

	private NanoMillisComparison() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		StopwatchSample[] results = BenchmarkUtils.run(2, 5,
			new BenchmarkUtils.Task("empty") {
				@Override
				public void perform() throws Exception {
					for (int i = 0; i < LOOP; i++) {
					}
				}
			},
			new BenchmarkUtils.Task("millis") {
				@Override
				public void perform() throws Exception {
					for (int i = 0; i < LOOP; i++) {
						System.currentTimeMillis();
					}
				}
			},
			new BenchmarkUtils.Task("nanos") {
				@Override
				public void perform() throws Exception {
					for (int i = 0; i < LOOP; i++) {
						System.nanoTime();
					}
				}
			},
			new BenchmarkUtils.Task("assign-ms") {
				@Override
				public void perform() throws Exception {
					for (int i = 0; i < LOOP; i++) {
						long ms = System.currentTimeMillis();
					}
				}
			},
			new BenchmarkUtils.Task("assign-ns") {
				@Override
				public void perform() throws Exception {
					for (int i = 0; i < LOOP; i++) {
						long ns = System.nanoTime();
					}
				}
			},
			new BenchmarkUtils.Task("stopwatch") {
				@Override
				public void perform() throws Exception {
					Stopwatch simon = SimonManager.getStopwatch(null);
					for (int i = 0; i < LOOP; i++) {
						simon.start().stop();
					}
				}
			},
			new BenchmarkUtils.Task("sw,manager") {
				@Override
				public void perform() throws Exception {
					for (int i = 0; i < LOOP; i++) {
						SimonManager.getStopwatch("some.name").start().stop();
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