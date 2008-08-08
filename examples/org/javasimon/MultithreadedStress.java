package org.javasimon;

import java.util.concurrent.CountDownLatch;

/**
 * MultithreadedStress.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 6, 2008
 */
public final class MultithreadedStress extends Thread {
	private static final int THREADS = 1000;
	private static final int LOOP = 10000;

	private static final String NAME = SimonFactory.generateName(null, false);

	private static CountDownLatch latch;

	private static boolean withYield = false;

	public static void main(String[] args) throws InterruptedException {
		// warmup
		test();

		withYield = true;
		SimonFactory.getStopwatch(NAME).reset();
		test();
		System.out.println("With yield: " + SimonFactory.getStopwatch(NAME));

		withYield = false;
		SimonFactory.getStopwatch(NAME).reset();
		test();
		System.out.println("Without yield: " + SimonFactory.getStopwatch(NAME));
	}

	private static void test() throws InterruptedException {
		SimonStopwatch simon = new SimonStopwatchImpl(null);
		simon.start();
		latch = new CountDownLatch(THREADS);
		for (int i = 0; i < THREADS; i++) {
			new MultithreadedStress().start();
		}
		latch.await();
		simon.stop();
		System.out.println("Test simon: " + simon);
	}

	public void run() {
		SimonStopwatch stopwatch = SimonFactory.getStopwatch(NAME);
		for (int i = 0; i < LOOP; i++) {
			stopwatch.start();
			if (withYield) {
				yield();
			}
			stopwatch.stop();
		}
		latch.countDown();
	}
}