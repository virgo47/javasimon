package org.javasimon.examples;

import com.jamonapi.MonitorFactory;
import com.jamonapi.Monitor;

import java.util.concurrent.CountDownLatch;

import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.StatProcessorType;

/**
 * Compares Simon and Jamon performance in a heavy-multithreaded environment. Creates 1000 threads
 * and calls 1000 times start/stop for both APIs. You can check that hit count is the same for both
 * API, but Simon should be faster (even on Windows XP) and - thanks to ns - more precise.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 6, 2008
 */
public final class MultithreadJamonComparison {
	private static final int THREADS = 1000;
	private static final int LOOP = 1000;

	private static final String NAME = SimonManager.generateName(null, false);

	private static CountDownLatch latch;

	private MultithreadJamonComparison() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) throws InterruptedException {
		SimonManager.getStopwatch(NAME).setStatProcessor(StatProcessorType.BASIC.create());
		int round = 1;
		while (true) {
			System.out.println("\nRound: " + round++);
			Stopwatch simon = SimonManager.getStopwatch(null).start();
			latch = new CountDownLatch(THREADS);
			for (int i = 0; i < THREADS; i++) {
				new SimonMultithreadedStress().start();
			}
			latch.await();
			simon.stop();
			System.out.println("Result: " + SimonManager.getStopwatch(NAME));
			System.out.println("Test Simon: " + simon);

			simon = SimonManager.getStopwatch(null).start();
			latch = new CountDownLatch(THREADS);
			for (int i = 0; i < THREADS; i++) {
				new JamonMultithreadedStress().start();
			}
			latch.await();
			simon.stop();
			System.out.println("Result: " + MonitorFactory.getTimeMonitor("jamon-stopwatch"));
			System.out.println("Test Jamon: " + simon);
		}
	}

	private static class SimonMultithreadedStress extends Thread {
		public void run() {
			Stopwatch stopwatch = SimonManager.getStopwatch(NAME);
			for (int i = 0; i < LOOP; i++) {
				stopwatch.start();
				stopwatch.stop();
			}
			latch.countDown();
		}
	}

	private static class JamonMultithreadedStress extends Thread {
		public void run() {
			Monitor stopwatch = MonitorFactory.getTimeMonitor("jamon-stopwatch");
			for (int i = 0; i < LOOP; i++) {
				stopwatch.start();
				stopwatch.stop();
			}
			latch.countDown();
		}
	}
}