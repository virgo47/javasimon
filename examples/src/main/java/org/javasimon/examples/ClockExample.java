package org.javasimon.examples;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.clock.Clock;

/**
 * This example shows how different Clock sources for Splits affect the measured values.
 * Using {@link Split} with {@link Clock#CPU} is quick way to measure real CPU time (as real as it is for JVM).
 * <b>Obviously - one should not stop such a split in different thread than it was started, because
 * it may be based on timers/counters valid for a single thread.</b>
 */
public class ClockExample {

	public static void main(String[] args) throws InterruptedException {
		String stopwatchName = "stopwatch";

		Split cpuSplit = Split.start(Clock.CPU);
		Split systemSplit = Split.start();

		System.out.println("cpuSplit = " + cpuSplit);
		System.out.println("systemSplit = " + systemSplit);

		for (int loop = 0; loop < 10; loop++) {
			for (int i = 0; i < 1000000; i++) {
				SimonManager.getStopwatch(stopwatchName);
			}
			Thread.sleep(200); // this should cause roughly 200 ms difference on each loop
			System.out.println("\nAfter iteration #" + loop);
			System.out.println("cpuSplit = " + cpuSplit);
			System.out.println("systemSplit = " + systemSplit);
		}
	}
}
