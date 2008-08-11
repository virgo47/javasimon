package org.javasimon.examples;

import org.javasimon.SimonFactory;
import org.javasimon.Stopwatch;

import java.util.concurrent.CountDownLatch;

/**
 * MultithreadSleeping.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 6, 2008
 */
public final class MultithreadedSleeping extends Thread {
	private static final int THREADS = 100;
	private static final int SLEEP = 1000;

	private static final String NAME = SimonFactory.generateName(null, false);

	private static CountDownLatch latch = new CountDownLatch(THREADS);

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < THREADS; i++) {
			new MultithreadedSleeping().start();
		}
		latch.await();
		System.out.println("Simon: " + SimonFactory.getStopwatch(NAME));
	}

	public void run() {
		Stopwatch stopwatch = SimonFactory.getStopwatch(NAME);
		stopwatch.start();
		try {
			sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stopwatch.stop();
		latch.countDown();
	}
}
