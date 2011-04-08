package org.javasimon.examples;

import org.javasimon.Stopwatch;
import org.javasimon.SimonManager;
import org.javasimon.Simon;
import org.javasimon.Split;
import org.javasimon.utils.*;

/**
 * Various timer calles compared along with stopwatch start/stop calls.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class NanoMillisComparison {
	private static final int LOOP = 10000000;

	private static Stopwatch nanos;
	private static Stopwatch empty;
	private static Stopwatch millis;
	private static Stopwatch assignMs;
	private static Stopwatch stopwatch;
	private static Stopwatch assignNs;

	private NanoMillisComparison() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		AbstractDataCollector collector = null;
		for (int round = 1; round <= 5; round++) {
			System.out.println("\nRound: " + round);
			emptyTest();
			millisTest();
			nanoTest();
			msAssignTest();
			nsAssignTest();
			simonUsage();
			if (collector == null) {
				 collector = new AbstractDataCollector(empty, millis, nanos, assignMs, assignNs, stopwatch) {
					 public double obtainValue(Simon simon) {
						 return ((Stopwatch) simon).getTotal();
					 }
				 };
			}
			collector.collect();
		}
		System.out.println("\nGoogle Chart:\n" + GoogleChartGenerator.barChart(collector, "10M-loop duration", 1000000, "ms"));
	}

	private static void nanoTest() {
		nanos = SimonManager.getStopwatch("nanos").reset();
		Split split = nanos.start();
		for (int i = 0; i < LOOP; i++) {
			System.nanoTime();
		}
		split.stop();
		System.out.println("Nanos: " + nanos);
	}

	private static void emptyTest() {
		empty = SimonManager.getStopwatch("empty").reset();
		Split split = empty.start();
		for (int i = 0; i < LOOP; i++) {
		}
		split.stop();
		System.out.println("Empty: " + empty);
	}

	private static void millisTest() {
		millis = SimonManager.getStopwatch("millis").reset();
		Split split = millis.start();
		for (int i = 0; i < LOOP; i++) {
			System.currentTimeMillis();
		}
		split.stop();
		System.out.println("Millis: " + millis);
	}

	private static void msAssignTest() {
		assignMs = SimonManager.getStopwatch("assign-ms").reset();
		Split split = assignMs.start();
		for (int i = 0; i < LOOP; i++) {
			long ms = System.currentTimeMillis();
		}
		split.stop();
		System.out.println("Assign ms: " + assignMs);
	}

	private static void nsAssignTest() {
		assignNs = SimonManager.getStopwatch("assign-ns").reset();
		Split split = assignNs.start();
		for (int i = 0; i < LOOP; i++) {
			long ns = System.nanoTime();
		}
		split.stop();
		System.out.println("Assign ns: " + assignNs);
	}

	private static void simonUsage() {
		Stopwatch simon = SimonManager.getStopwatch(null);
		stopwatch = SimonManager.getStopwatch("stopwatch").reset();
		Split split = stopwatch.start();
		for (int i = 0; i < LOOP; i++) {
			simon.start().stop();
		}
		split.stop();
		System.out.println("Threadsafe: " + stopwatch);
	}
}