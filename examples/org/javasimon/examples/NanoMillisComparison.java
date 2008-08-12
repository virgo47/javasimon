package org.javasimon.examples;

import org.javasimon.Stopwatch;
import org.javasimon.SimonFactory;

/**
 * JamonComparison.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 7, 2008
 */
public final class NanoMillisComparison {
	private static final int LOOP = 10000000;

	private NanoMillisComparison() {
	}

	public static void main(String[] args) {
		int round = 1;
		while (true) {
			System.out.println("\nRound: " + round++);
			emptyTest();
			millisTest();
			nanoTest();
			msAssignTest();
			nsAssignTest();
			simonStart();
			simonSimpleStart();
		}
	}

	private static void nanoTest() {
		Stopwatch stopwatch = SimonFactory.getStopwatch("nanos").reset().start();
		for (int i = 0; i < LOOP; i++) {
			System.nanoTime();
		}
		System.out.println("Nanos: " + stopwatch.stop());
	}

	private static void emptyTest() {
		Stopwatch stopwatch = SimonFactory.getStopwatch("empty").reset().start();
		for (int i = 0; i < LOOP; i++) {
		}
		System.out.println("Empty: " + stopwatch.stop());
	}

	private static void millisTest() {
		Stopwatch stopwatch = SimonFactory.getStopwatch("millis").reset().start();
		for (int i = 0; i < LOOP; i++) {
			System.currentTimeMillis();
		}
		System.out.println("Millis: " + stopwatch.stop());
	}

	private static void msAssignTest() {
		Stopwatch stopwatch = SimonFactory.getStopwatch("assign-ms").reset().start();
		for (int i = 0; i < LOOP; i++) {
			long ms = System.currentTimeMillis();
		}
		System.out.println("Assign ms: " + stopwatch.stop());
	}

	private static void nsAssignTest() {
		Stopwatch stopwatch = SimonFactory.getStopwatch("assign-ns").reset().start();
		for (int i = 0; i < LOOP; i++) {
			long ns = System.nanoTime();
		}
		System.out.println("Assign ns: " + stopwatch.stop());
	}

	private static void simonStart() {
		Stopwatch simon = SimonFactory.getStopwatch(null);
		Stopwatch stopwatch = SimonFactory.getStopwatch("simonstart").reset().start();
		for (int i = 0; i < LOOP; i++) {
			simon.start();
		}
		System.out.println("Threadsafe start: " + stopwatch.stop());
	}

	private static void simonSimpleStart() {
		Stopwatch simon = SimonFactory.getSimpleStopwatch(null);
		Stopwatch stopwatch = SimonFactory.getStopwatch("simplestart").reset().start();
		for (int i = 0; i < LOOP; i++) {
			simon.start();
		}
		System.out.println("Unsafe start: " + stopwatch.stop());
	}
}