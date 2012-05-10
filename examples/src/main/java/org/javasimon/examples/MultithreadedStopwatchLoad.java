package org.javasimon.examples;

import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.utils.BenchmarkUtils;
import org.javasimon.utils.GoogleChartImageGenerator;
import org.javasimon.utils.SimonUtils;

import java.util.concurrent.CountDownLatch;

/**
 * Measures how long it takes to execute a lot of get-start-stop cycles in heavily multithreaded environment.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 3.1
 */
public final class MultithreadedStopwatchLoad extends Thread {
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
		StopwatchSample[] results = BenchmarkUtils.run(1, 2,
			new BenchmarkUtils.Task("1") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(1).execute();
				}
			},
			new BenchmarkUtils.Task("2") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(2).execute();
				}
			},
			new BenchmarkUtils.Task("10") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(10).execute();
				}
			},
			new BenchmarkUtils.Task("100") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(100).execute();
				}
			},
			new BenchmarkUtils.Task("1000") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(1000).execute();
				}
			}
		);

		System.out.println("\nGoogle Chart avg:\n" + GoogleChartImageGenerator.barChart(
			results, "Multithreaded test", SimonUtils.NANOS_IN_MILLIS, "ms", false));
	}
}

/**
 * Class executes test-case for a selected number of threads. Total number of stopwatches is
 * always the same (roughly, not counting division errors), one loop count changes accordingly.
 */
class MultithreadedTester extends Thread {
	// name of the Simon will be the same like the name of this class
	private static final String NAME = SimonUtils.generateName();

	private static final int TOTAL = 10000000;

	private final int threads;
	private final int loop;
	private final CountDownLatch latch;

	MultithreadedTester(int threads) {
		System.out.println("Creating Multithreaded test for " + threads + " threads");
		this.threads = threads;
		this.loop = TOTAL / threads;
		latch = new CountDownLatch(threads);
	}

	void execute() throws InterruptedException {
		Stopwatch stopwatch = SimonManager.getStopwatch(NAME);
		for (int i = 1; i <= threads; i++) {
			new TestThread().start();
			if (i % 500 == 0) {
				System.out.println("Created thread: " + i +
					" (already executed loops " + stopwatch.getCounter() +
					", currently active " + stopwatch.getActive() + ")");
			}
		}
		System.out.println("All threads created (already executed loops " + stopwatch.getCounter() +
			", currently active " + stopwatch.getActive() + ")");
		// here we wait for all threads to end
		latch.await();
		System.out.println("All threads finished: " + stopwatch.sample());
	}

	class TestThread extends Thread {
		/**
		 * Run method implementing the code performed by the thread.
		 */
		@Override
		public void run() {
			for (int i = 0; i < loop; i++) {
				SimonManager.getStopwatch(NAME).start().stop();
			}
			// signal to latch that the thread is finished
			latch.countDown();
		}
	}
}