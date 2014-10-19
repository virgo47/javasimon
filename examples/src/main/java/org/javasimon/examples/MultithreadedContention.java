package org.javasimon.examples;

import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.clock.SimonUnit;
import org.javasimon.utils.BenchmarkUtils;
import org.javasimon.utils.GoogleChartImageGenerator;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Measures nothing with a single stopwatch with varying thread count. Results
 * should be close to 0, but that is hard to expect. :-)
 */
public class MultithreadedContention {

	public static final int TOTAL_TASK_RUNS = 2000000;
	public static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

	private MultithreadedContention() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 * @throws InterruptedException when sleep is interrupted
	 */
	public static void main(String[] args) throws InterruptedException {
		final ExecutorService executorService = Executors.newFixedThreadPool(1000);
		BenchmarkUtils.run(1, 2,
			// low contention - single thread
			new BenchmarkUtils.Task("1") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(TOTAL_TASK_RUNS, 1, executorService).execute();
				}
			},
			// medium contention
			new BenchmarkUtils.Task("CPU") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(TOTAL_TASK_RUNS, AVAILABLE_PROCESSORS, executorService).execute();
				}
			},
			// High contention
			new BenchmarkUtils.Task("400") {
				@Override
				public void perform() throws Exception {
					new MultithreadedTester(TOTAL_TASK_RUNS, 400, executorService).execute();
				}
			}
		);
		executorService.shutdown();

		// we're not using Benchmark results here this time, so we have to extract them on our own
		StopwatchSample sampleLo = SimonManager.getStopwatch("MT-1").sample();
		StopwatchSample sampleCpu = SimonManager.getStopwatch("MT-" + AVAILABLE_PROCESSORS).sample();
		StopwatchSample sampleHi = SimonManager.getStopwatch("MT-400").sample();

		System.out.println("\nsampleLo = " + sampleLo);
		System.out.println("sampleCpu = " + sampleCpu);
		System.out.println("sampleHi = " + sampleHi);

		System.out.println("\nGoogle Chart avg:\n" +
			GoogleChartImageGenerator.barChart("MultithreadedContention", SimonUnit.NANOSECOND,
				sampleLo,
				sampleCpu,
				sampleHi));
		System.out.println("\nGoogle Chart avg/min/max:\n" +
			GoogleChartImageGenerator.barChart("MultithreadedContention", SimonUnit.NANOSECOND, true,
				sampleLo,
				sampleCpu,
				sampleHi));
	}

	/**
	 * Class executes test-case for a selected number of threads. Total number of stopwatches is
	 * always the same (roughly, not counting division errors), loop count for a thread changes accordingly
	 * to the number of threads.
	 */
	private static class MultithreadedTester extends Thread {

		// name of the Simon will be the same like the name of this class
		private final String name;

		private final int threads;
		private final int loop;
		private ExecutorService executorService;
		private final Runnable task = new TestTask();

		private final CountDownLatch latch;
		private final CyclicBarrier barrier;

		/**
		 * Creates the tester specifying total number of task runs, number of threads and thread pool
		 * (optional, can be {@code null}).
		 *
		 * @param taskRuns total number of task runs
		 * @param threads number of threads executing part of the run count
		 * @param executorService service used to execute tasks, can be {@code null}
		 */
		private MultithreadedTester(int taskRuns, int threads, ExecutorService executorService) {
			System.out.println("Creating Multithreaded test for " + threads + " threads");
			this.threads = threads;
			this.loop = taskRuns / threads;
			this.executorService = executorService;
			name = "MT-" + threads;

			latch = new CountDownLatch(threads);
			barrier = new CyclicBarrier(threads + 1);
		}

		void execute() throws InterruptedException {
			Stopwatch stopwatch = SimonManager.getStopwatch(name);
			for (int i = 1; i <= threads; i++) {
				startTask();
			}
			System.out.println("Threads created, ready... starting");
			try {
				barrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
			// here we wait for all threads to end
			latch.await();
			System.out.println("All threads finished: " + stopwatch.sample());
		}

		private void startTask() {
			if (executorService == null) {
				new Thread(task).start();
			} else {
				executorService.submit(task);
			}
		}

		class TestTask implements Runnable {
			/** Run method implementing the code performed by the thread. */
			@Override
			public void run() {
				try {
					barrier.await();
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
				Stopwatch stopwatch = SimonManager.getStopwatch(name);
				for (int i = 0; i < loop; i++) {
					stopwatch.start().stop();
				}
				// signal to latch that the thread is finished
				latch.countDown();
			}
		}
	}
}
