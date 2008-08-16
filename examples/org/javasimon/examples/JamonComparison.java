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
		int round = 1;
		while (true) {
			System.out.println("\nRound: " + round++);
			jamonTest();
			simonTest();
		}
	}

	private static void simonTest() {
		Stopwatch stopwatch = SimonFactory.getStopwatch("bu");
		stopwatch.reset();

		long ns = System.nanoTime();
		for (int i = 0; i < OUTER_LOOP; i++) {
//			stay();
			stopwatch.start();
//			stay();
			stopwatch.stop();
		}
		ns = System.nanoTime() - ns;

		System.out.println("Simon Total: " + SimonUtils.presentNanoTime(stopwatch.getTotal()) +
			" max: " + SimonUtils.presentNanoTime(stopwatch.getMax()) + " min: " + SimonUtils.presentNanoTime(stopwatch.getMin()) +
			" real: " + SimonUtils.presentNanoTime(ns));
	}

	private static void jamonTest() {
		MonitorFactory.enable();
		Monitor monitor = MonitorFactory.getTimeMonitor("bu");
		monitor.reset();

		long ns = System.nanoTime();
		for (int i = 0; i < OUTER_LOOP; i++) {
//			stay();
			monitor.start();
//			stay();
			monitor.stop();
		}
		ns = System.nanoTime() - ns;

		System.out.println("Jamon Total: " + monitor.getTotal() + " max: " + monitor.getMax() + " min: " + monitor.getMin() + " real: " + SimonUtils.presentNanoTime(ns));
	}

	private static void stay() {
		for (int j = 0; j < INNER_LOOP; j++) {
			j++;
			j--;
		}
	}
}
