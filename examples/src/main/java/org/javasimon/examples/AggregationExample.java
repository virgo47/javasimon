package org.javasimon.examples;

import org.javasimon.Manager;
import org.javasimon.Simon;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;

import java.util.List;
import java.util.Random;

/**
 * Example shows how to aggregate results from multiple Stopwatch. Before version 3.5 one has to work around it,
 * now it is supported with various {@link org.javasimon.utils.SimonUtils} methods.
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
		System.out.println("Wait for it... (~10s or more)");
		for (int i = 0; i < ITERATIONS; i++) {
			Split split = SimonManager.getStopwatch(STOPWATCH_PARENT + Manager.HIERARCHY_DELIMITER + random.nextInt(STOPWATCH_COUNT)).start();
			ExampleUtils.waitRandomlySquared(20);
			split.stop();
		}
		// this is "null" Simon, but you don't care - just take all the children and do the stuff you want
		Simon parent = SimonManager.getSimon(STOPWATCH_PARENT);
		List<Simon> children = parent.getChildren();
		System.out.println("parent.children count = " + children.size() + '\n');

		// programmatic example - not counting parent itself, because here it is not used
		long totalSum = 0;
		for (Simon child : children) {
			System.out.println(child);
			// do whatever you need to do here...
			totalSum += ((Stopwatch) child).getTotal();
		}
		System.out.println("Programmatic totalSum = " + SimonUtils.presentNanoTime(totalSum));

		// using utils available @since 3.5
		System.out.println("\nSimonUtils.calculateStopwatchAggregate(parent) = " + SimonUtils.calculateStopwatchAggregate(parent));
	}
}
