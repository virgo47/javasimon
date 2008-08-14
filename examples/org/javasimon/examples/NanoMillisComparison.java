package org.javasimon.examples;

import org.javasimon.Stopwatch;
import org.javasimon.SimonFactory;
import org.javasimon.Simon;
import org.javasimon.examples.utils.*;

/**
 * Various timer calles compared along with stopwatch start/stop calls.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 7, 2008
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

	public static void main(String[] args) {
		DataCollector collector = null;
		for (int round = 1; round <= 5; round++) {
			System.out.println("\nRound: " + round);
			emptyTest();
			millisTest();
			nanoTest();
			msAssignTest();
			nsAssignTest();
			simonUsage();
			if (collector == null) {
				 collector = new DataCollector(empty, millis, nanos, assignMs, assignNs, stopwatch) {
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
		nanos = SimonFactory.getStopwatch("nanos").reset().start();
		for (int i = 0; i < LOOP; i++) {
			System.nanoTime();
		}
		nanos.stop();
		System.out.println("Nanos: " + nanos);
	}

	private static void emptyTest() {
		empty = SimonFactory.getStopwatch("empty").reset().start();
		for (int i = 0; i < LOOP; i++) {
		}
		empty.stop();
		System.out.println("Empty: " + empty);
	}

	private static void millisTest() {
		millis = SimonFactory.getStopwatch("millis").reset().start();
		for (int i = 0; i < LOOP; i++) {
			System.currentTimeMillis();
		}
		System.out.println("Millis: " + millis.stop());
	}

	private static void msAssignTest() {
		assignMs = SimonFactory.getStopwatch("assign-ms").reset().start();
		for (int i = 0; i < LOOP; i++) {
			long ms = System.currentTimeMillis();
		}
		System.out.println("Assign ms: " + assignMs.stop());
	}

	private static void nsAssignTest() {
		assignNs = SimonFactory.getStopwatch("assign-ns").reset().start();
		for (int i = 0; i < LOOP; i++) {
			long ns = System.nanoTime();
		}
		System.out.println("Assign ns: " + assignNs.stop());
	}

	private static void simonUsage() {
		Stopwatch simon = SimonFactory.getStopwatch(null);
		stopwatch = SimonFactory.getStopwatch("stopwatch").reset().start();
		for (int i = 0; i < LOOP; i++) {
			simon.start();
			simon.stop();
		}
		System.out.println("Threadsafe: " + stopwatch.stop());
	}
}