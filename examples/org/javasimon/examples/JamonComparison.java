package org.javasimon.examples;

import com.jamonapi.MonitorFactory;
import com.jamonapi.Monitor;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;
import org.javasimon.SimonFactory;

/**
 * JamonComparison.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 7, 2008
 */
public final class JamonComparison {
	private static final int OUTER_LOOP = 1000000;
	private static final int INNER_LOOP = 10;

	private JamonComparison() {
	}

	public static void main(String[] args) {
		MonitorFactory.enable();

		int round = 1;
		while (true) {
			System.out.println("\nRound: " + round++);
			jamonTest();
			jamonTest2();
			simonTest();
			simonTest2();
		}
	}

	private static void simonTest() {
		Stopwatch stopwatch = SimonFactory.getStopwatch("bu");
		stopwatch.reset();

		long ns = System.nanoTime();
		for (int i = 0; i < OUTER_LOOP; i++) {
			stay();
			stopwatch.start();
			stay();
			stopwatch.stop();
		}
		ns = System.nanoTime() - ns;

		printResults(ns, stopwatch, "Simon start/stop");
	}

	private static void simonTest2() {
		SimonFactory.getStopwatch("org.javasimon.examples.stopwatch1").reset();

		long ns = System.nanoTime();
		for (int i = 0; i < OUTER_LOOP; i++) {
			stay();
			Stopwatch stopwatch = SimonFactory.getStopwatch("org.javasimon.examples.stopwatch1").start();
			stay();
			stopwatch.stop();
		}
		ns = System.nanoTime() - ns;

		Stopwatch stopwatch = SimonFactory.getStopwatch("org.javasimon.examples.stopwatch1");
		printResults(ns, stopwatch, "Simon get+start/stop");
	}

	private static void printResults(long ns, Stopwatch stopwatch, String title) {
		System.out.println(title + ": " + SimonUtils.presentNanoTime(stopwatch.getTotal()) +
			" max: " + SimonUtils.presentNanoTime(stopwatch.getMax()) + " min: " + SimonUtils.presentNanoTime(stopwatch.getMin()) +
			" real: " + SimonUtils.presentNanoTime(ns));
	}

	private static void jamonTest() {
		Monitor monitor = MonitorFactory.getTimeMonitor("bu");
		monitor.reset();

		long ns = System.nanoTime();
		for (int i = 0; i < OUTER_LOOP; i++) {
			stay();
			monitor.start();
			stay();
			monitor.stop();
		}
		ns = System.nanoTime() - ns;

		System.out.println("Jamon start/stop: " + monitor.getTotal() + " max: " + monitor.getMax() + " min: " + monitor.getMin() + " real: " + SimonUtils.presentNanoTime(ns));
	}

	private static void jamonTest2() {
		Monitor monitor = MonitorFactory.getTimeMonitor("bu");
		monitor.reset();

		long ns = System.nanoTime();
		for (int i = 0; i < OUTER_LOOP; i++) {
			stay();
			monitor = MonitorFactory.start("bu");
			stay();
			monitor.stop();
		}
		ns = System.nanoTime() - ns;

		System.out.println("Jamon get+start/stop: " + monitor.getTotal() + " max: " + monitor.getMax() + " min: " + monitor.getMin() + " real: " + SimonUtils.presentNanoTime(ns));
	}

	private static void stay() {
		for (int j = 0; j < INNER_LOOP; j++) {
			j++;
			j--;
		}
	}
}
