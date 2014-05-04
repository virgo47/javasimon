package org.javasimon.examples;

import org.javasimon.StopwatchSample;
import org.javasimon.clock.ClockUtils;
import org.javasimon.utils.BenchmarkUtils;
import org.javasimon.utils.GoogleChartImageGenerator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Measures how long it takes to execute a lot of get-start-stop cycles in heavily multithreaded environment.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 3.1
 */
public final class MultithreadedStopwatchLoad extends Thread {
	public static final int TOTAL_TASK_RUNS = 300000;

	private MultithreadedStopwatchLoad() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 * @throws InterruptedException when sleep is interrupted
	 */
	public static void main(String[] args) throws InterruptedException {
		ExampleUtils.fillManagerWithSimons(100000);
		final ExecutorService executorService = Executors.newFixedThreadPool(1000);
		StopwatchSample[] results = BenchmarkUtils.run(1, 2,
			new BenchmarkUtils.Task("1") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(TOTAL_TASK_RUNS, 1, executorService).execute();
				}
			},
			new BenchmarkUtils.Task("2") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(TOTAL_TASK_RUNS, 2, executorService).execute();
				}
			},
			new BenchmarkUtils.Task("3") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(TOTAL_TASK_RUNS, 3, executorService).execute();
				}
			},
			new BenchmarkUtils.Task("4") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(TOTAL_TASK_RUNS, 4, executorService).execute();
				}
			},
			new BenchmarkUtils.Task("5") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(TOTAL_TASK_RUNS, 5, executorService).execute();
				}
			},
			new BenchmarkUtils.Task("100") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(TOTAL_TASK_RUNS, 100, executorService).execute();
				}
			}
		);
		executorService.shutdown();

		System.out.println("\nGoogle Chart avg:\n" + GoogleChartImageGenerator.barChart(
			results, "Multithreaded test", ClockUtils.NANOS_IN_MILLIS, "ms", false));
	}
}

