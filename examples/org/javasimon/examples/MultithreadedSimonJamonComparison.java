package org.javasimon.examples;

import com.jamonapi.MonitorFactory;
import com.jamonapi.Monitor;

import java.util.concurrent.CountDownLatch;

import org.javasimon.SimonFactory;
import org.javasimon.Stopwatch;

/**
 * MultithreadedStress.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 6, 2008
 */
public final class MultithreadedSimonJamonComparison {
	private static final int THREADS = 1000;
	private static final int LOOP = 1000;

	private static final String NAME = SimonFactory.generateName(null, false);

	private static CountDownLatch latch;

	public static void main(String[] args) throws InterruptedException {
		int round = 1;
		while (true) {
			System.out.println("\nRound: " + round);
			Stopwatch simon = SimonFactory.getStopwatch(null).start();
			latch = new CountDownLatch(THREADS);
			for (int i = 0; i < THREADS; i++) {
				new SimonMultithreadedStress().start();
			}
			latch.await();
			simon.stop();
			System.out.println("Result: " + SimonFactory.getStopwatch(NAME));
			System.out.println("Test Simon: " + simon);

			simon = SimonFactory.getStopwatch(null).start();
			latch = new CountDownLatch(THREADS);
			for (int i = 0; i < THREADS; i++) {
				new JamonMultithreadedStress().start();
			}
			latch.await();
			simon.stop();
			System.out.println("Result: " + MonitorFactory.getTimeMonitor("jamon-stopwatch"));
			System.out.println("Test Jamon: " + simon);

			round++;
		}
	}

	private static class SimonMultithreadedStress extends Thread {
		public void run() {
			Stopwatch stopwatch = SimonFactory.getStopwatch(NAME);
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