package org.javasimon.console;

import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;

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
//        Split split=stopwatch.start();
//        try {
//            Thread.sleep(sleep);
//        } catch (InterruptedException e) {
//        } finally {
//            split.stop();
//        }
		stopwatch.addTime(sleep * SimonUtils.NANOS_IN_MILLIS);
	}

	private static void initStopwatches() {
		addTime("A", 100);
		addTime("B", 200);
		addTime("C", 300);
		addTime("A", 200);
		addTime("A", 300);
		addTime("B", 100);
	}

	public static void initialize() {
		initialized = true;
		SimonManager.clear();
		initStopwatches();
	}
}
