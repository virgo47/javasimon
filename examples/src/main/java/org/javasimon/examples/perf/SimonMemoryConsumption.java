package org.javasimon.examples.perf;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

public class SimonMemoryConsumption {

	private static long baseline;
	private static int count;

	public static void main(String[] args) {

		reportAbsolute("EMPTY");
		count = 100000;
		Stopwatch[] stopwatches = new Stopwatch[count];

		setBaseLine("BASE-LINE");

		for (int i = 0; i < stopwatches.length; i++) {
			stopwatches[i] = SimonManager.getStopwatch(null);
		}

		report("100K stopwatches");

		for (int i = 0; i < stopwatches.length; i++) {
			stopwatches[i] = SimonManager.getStopwatch(null);
		}

		report("100K stopwatches again");

		for (Stopwatch stopwatch : stopwatches) {
			stopwatch.start().stop();
		}
		report("after 100K start/stop");
		for (Stopwatch stopwatch : stopwatches) {
			something(stopwatch);
		}
		report("after 100K (2 keys each)");

		System.out.println("array-len = " + stopwatches.length);
		report("after array.length");
	}

	private static void report(final String what) {
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		memoryMXBean.gc();
		long used = memoryMXBean.getHeapMemoryUsage().getUsed();
		long delta = used - baseline;
		System.out.println(what + ": " + delta + " (total=" + used + "), " + (delta / count) + "/unit");
	}

	private static void reportAbsolute(final String what) {
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		memoryMXBean.gc();
		memoryMXBean.gc();
		System.out.println(what + ": " + memoryMXBean.getHeapMemoryUsage().getUsed());
	}

	private static void setBaseLine(final String what) {
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		memoryMXBean.gc();
		memoryMXBean.gc();
		baseline = memoryMXBean.getHeapMemoryUsage().getUsed();
		System.out.println(what + ": " + baseline);
	}

	private static void something(Stopwatch stopwatch) {
		stopwatch.sample();
		stopwatch.addSplit(Split.create(55));
		stopwatch.sampleIncrement("1");

		stopwatch.addSplit(Split.create(47));
		stopwatch.sampleIncrement("1");

		stopwatch.addSplit(Split.create(33));
		stopwatch.sampleIncrement("1");
		stopwatch.sampleIncrement("2");

		stopwatch.addSplit(Split.create(100));
		stopwatch.sampleIncrement("1");
		stopwatch.sampleIncrement("2");
		stopwatch.sampleIncrement("3");

		stopwatch.stopIncrementalSampling("1");
		stopwatch.addSplit(Split.create(12));
	}
}
