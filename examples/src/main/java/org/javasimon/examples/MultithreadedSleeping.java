package org.javasimon.examples;

import java.util.concurrent.CountDownLatch;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;

/**
 * MultithreadedSleeping shows that total stopwatch time can be easily much bigger
 * than total run-time of the application. Typical output of this program is:
 * <pre>
 * Simon: Simon Stopwatch: [org.javasimon.examples.MultithreadedSleeping INHERIT] total 100.0 s, counter 100, max 1.00 s, min 999 ms</pre>
 * This shows that whenever wait (IO, sleep) is measured you can get times
 * multiplied by the number of threads. Another thing you can notice is not
 * perfect precision of the sleep because in theory it should not sleep less than
 * 1 second. Either the sleep or the nanoTime is not perfect.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class MultithreadedSleeping extends Thread {
	private static final int THREADS = 100;
	private static final int SLEEP = 1000;

	// name of the Simon will be the same like the name of this class
	private static final String NAME = SimonUtils.generateNameForClass(null);

	private static final CountDownLatch latch = new CountDownLatch(THREADS);

	private MultithreadedSleeping() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 * @throws InterruptedException when sleep is interrupted
	 */
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Going to run 1s sleep in " + THREADS + " threads...");
		Split realTimeSplit = Split.start();
		for (int i = 0; i < THREADS; i++) {
			new MultithreadedSleeping().start();
		}
		// here we wait for all threads to end
		latch.await();
		System.out.println("Simon: " + SimonManager.getStopwatch(NAME));
		System.out.println("Real time: " + realTimeSplit.stop());
	}

	/**
	 * Run method implementing the code performed by the thread.
	 */
	@Override
	public void run() {
		Stopwatch stopwatch = SimonManager.getStopwatch(NAME);
		Split split = stopwatch.start();
		try {
			sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		split.stop();
		// signal to latch that the thread is finished
		latch.countDown();
	}
}
