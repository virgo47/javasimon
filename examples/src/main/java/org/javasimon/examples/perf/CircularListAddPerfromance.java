package org.javasimon.examples.perf;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.callback.lastsplits.CircularList;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author gquintana
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class CircularListAddPerfromance {

	public static final int ITERATIONS = 1000000;

	public static void main(String... args) {
		List<Integer> circularList = new CircularList<>(10);
		Stopwatch stopwatch = SimonManager.getStopwatch(CircularListAddPerfromance.class.getName() + ".testAddPerformance");
		Split split = stopwatch.start();
		for (int i = 0; i < ITERATIONS; i++) {
			circularList.add(i);
		}
		long circular = split.stop().runningFor();
		LinkedList<Integer> linkedList = new LinkedList<>();
		split = stopwatch.start();
		for (int i = 0; i < ITERATIONS; i++) {
			linkedList.add(i);
			if (linkedList.size() > 10) {
				linkedList.removeFirst();
			}
		}
		long linked = split.stop().runningFor();
		System.out.println("Circular " + circular + " /Linked " + linked + " " + ((linked - circular) * 100 / circular) + "%");
	}
}
