package org.javasimon.examples;

import java.util.List;
import java.util.Random;

import org.javasimon.Manager;
import org.javasimon.Simon;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;

/**
 * Example shows how to aggregate results from multiple Stopwatch. This feature is not exactly
 * built-in and this example shows how to work around it using the hierarchy.
 *
 * TODO: Should be supported by API in 3.3 or later version (not so simple to make it right and easy).
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class AggregationExample {
	private static final String STOPWATCH_PARENT = "org.javasimon.examples.aggregation";
	private static final int STOPWATCH_COUNT = 4;

	private static final int ITERATIONS = 100;

	private static Random random = new Random();

	/**
	 * Entry point to the Aggregation Example.
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		for (int i = 0; i < ITERATIONS; i++) {
			Split split = SimonManager.getStopwatch(STOPWATCH_PARENT + Manager.HIERARCHY_DELIMITER + random.nextInt(STOPWATCH_COUNT)).start();
			ExampleUtils.waitRandomlySquared(30);
			split.stop();
		}
		// this is "null" Simon, but you don't care - just take all the children and do the stuff you want
		Simon parent = SimonManager.getSimon(STOPWATCH_PARENT);
		List<Simon> children = parent.getChildren();
		System.out.println("parent.children count = " + children.size());

		long totalSum = 0;
		for (Simon child : children) {
			System.out.println(child);
			// do whatever you need to do here...
			totalSum += ((Stopwatch) child).getTotal();
		}
		System.out.println("totalSum = " + SimonUtils.presentNanoTime(totalSum));
	}
}
