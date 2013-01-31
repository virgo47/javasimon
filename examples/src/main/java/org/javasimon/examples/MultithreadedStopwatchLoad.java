package org.javasimon.examples;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.javasimon.StopwatchSample;
import org.javasimon.utils.BenchmarkUtils;
import org.javasimon.utils.GoogleChartImageGenerator;
import org.javasimon.utils.SimonUtils;

/**
 * Measures how long it takes to execute a lot of get-start-stop cycles in heavily multithreaded environment.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 3.1
 */
public final class MultithreadedStopwatchLoad extends Thread {
	public static final int TOTAL_TASK_RUNS = 10000000;

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
			new BenchmarkUtils.Task("10") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(TOTAL_TASK_RUNS, 10, executorService).execute();
				}
			},
			new BenchmarkUtils.Task("100") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(TOTAL_TASK_RUNS, 100, executorService).execute();
				}
			},
			new BenchmarkUtils.Task("1000") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(TOTAL_TASK_RUNS, 1000, executorService).execute();
				}
			},
			new BenchmarkUtils.Task("1000thr") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(TOTAL_TASK_RUNS, 1000, null).execute();
				}
			}
		);
		executorService.shutdown();

		System.out.println("\nGoogle Chart avg:\n" + GoogleChartImageGenerator.barChart(
			results, "Multithreaded test", SimonUtils.NANOS_IN_MILLIS, "ms", false));
	}
}

