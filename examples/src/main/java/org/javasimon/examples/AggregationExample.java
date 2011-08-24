package org.javasimon.examples;

import org.javasimon.*;
import org.javasimon.utils.SimonUtils;

import java.util.List;
import java.util.Random;

/**
 * Example shows how to aggregate results from multiple Stopwatch. This feature is not exactly
 * built-in and this example shows how to work around it using the hierarchy.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public class AggregationExample {
	private static final String STOPWATCH_PARENT = "org.javasimon.examples.aggregation";
	private static final int STOPWATCH_COUNT = 4;

	private static final int ITERATIONS = 100;
	private static final int ITERATION_MS_MAX = 50;

	private static Random random = new Random();

	/**
	 * Entry point to the Aggregation Example.
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		for (int i = 0; i < ITERATIONS; i++) {
			Split split = SimonManager.getStopwatch(STOPWATCH_PARENT + Manager.HIERARCHY_DELIMITER + random.nextInt(STOPWATCH_COUNT)).start();
			try {
				Thread.sleep(random.nextInt(ITERATION_MS_MAX));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			split.stop();
		}
		// this is "null" Simon, but you don't care - just take all the children and do the stuff you want
		Simon parent = SimonManager.getSimon(STOPWATCH_PARENT);
		List<Simon> children = parent.getChildren();
		System.out.println("parent children count = " + children.size());

		long totalSum = 0;
		for (Simon child : children) {
			System.out.println(child);
			// do whatever you need to do here...
			totalSum += ((Stopwatch) child).getTotal();
		}
		System.out.println("totalSum = " + SimonUtils.presentNanoTime(totalSum));
	}
}
