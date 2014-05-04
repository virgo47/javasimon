package org.javasimon.examples;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.utils.SimonUtils;

/**
 * Class executes test-case for a selected number of threads. Total number of stopwatches is
 * always the same (roughly, not counting division errors), loop count for a thread changes accordingly
 * to the number of threads.
 */
public class MultithreadedTester extends Thread {

	// name of the Simon will be the same like the name of this class
	private static final String NAME = SimonUtils.generateName();

	private final int threads;
	private final int loop;
	private ExecutorService executorService;

	private final Runnable task = new TestTask();

	private final CountDownLatch latch;

	/**
	 * Creates the tester specifying total number of task runs, number of threads and thread pool
	 * (optional, can be {@code null}).
	 *
	 * @param taskRuns total number of task runs
	 * @param threads number of threads executing part of the run count
	 * @param executorService service used to execute tasks, can be {@code null}
	 */
	public MultithreadedTester(int taskRuns, int threads, ExecutorService executorService) {
		System.out.println("Creating Multithreaded test for " + threads + " threads");
		this.threads = threads;
		this.loop = taskRuns / threads;
		this.executorService = executorService;

		latch = new CountDownLatch(threads);
	}

	void execute() throws InterruptedException {
		Stopwatch stopwatch = SimonManager.getStopwatch(NAME);
		for (int i = 1; i <= threads; i++) {
			startTask();
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
			for (int i = 0; i < loop; i++) {
				Stopwatch stopwatch = SimonManager.getStopwatch(NAME);
				Split split = stopwatch.start();
				StopwatchSample sample = stopwatch.sample();
				//noinspection ResultOfMethodCallIgnored
				sample.toString();
				split.stop();
			}
			// signal to latch that the thread is finished
			latch.countDown();
		}
	}
}
