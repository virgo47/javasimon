package org.javasimon.console;

import org.javasimon.Counter;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.clock.ClockUtils;

/**
 * Generates Simon Data for unit testing
 *
 * @author gquintana
 */
public class SimonData {
	@SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
	private static boolean initialized = false;

	private static void addTime(String name, long sleep) {
		Stopwatch stopwatch = SimonManager.manager().getStopwatch(name);
		stopwatch.addSplit(Split.create(sleep * ClockUtils.NANOS_IN_MILLIS));
	}

	private static void initStopwatches() {
		addTime("A", 100);
		addTime("B", 200);
		addTime("C", 300);
		addTime("A", 200);
		addTime("A", 300);
		addTime("B", 100);
		addCounter("X", 1L);
		addCounter("X", 4L);
		addCounter("X", 2L);
	}

	private static void addCounter(String name, long value) {
		Counter counter = SimonManager.manager().getCounter(name);
		counter.set(value);
	}

	public static void initialize() {
		initialized = true;
		SimonManager.clear();
		initStopwatches();
	}
}
