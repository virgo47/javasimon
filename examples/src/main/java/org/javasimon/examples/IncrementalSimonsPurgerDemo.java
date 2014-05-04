package org.javasimon.examples;

import org.javasimon.IncrementalSimonsPurger;
import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * This demos shows how IncrementalSimonsPurger class can be used to periodically
 * remove old incremental keys. If incremental sampling key was not used for a long
 * period of time it will be removed by IncrementalSimonsPurger.
 * <p/>
 * Output of this demo shows how IncrementalSimonsPurger removes incremental Simons
 * if they were not used for some period of time. IncrementalSimonsPurger logs a message
 * with DEBUG level when purging is performed.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class IncrementalSimonsPurgerDemo {

	private static final int SLEEP_TIME = 2500;
	private static final long PURGING_PERIOD = 5;
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	public static void main(String... args) throws Exception {
		Manager manager = SimonManager.manager();

		// Create and start incremental Simons purger
		IncrementalSimonsPurger incrementalSimonsPurger = new IncrementalSimonsPurger(manager);
		incrementalSimonsPurger.start(PURGING_PERIOD, TIME_UNIT);

		Stopwatch stopwatch = manager.getStopwatch("demo.stopwatch");

		// Attempt to create random usage
		Random random = new Random();
		while (true) {

			// If an incremental Simons for a specified key exist in current Stopwatch
			// this statement will print incremental Sample with no Simon name:
			// e.g. StopwatchSample{total=1.37 s, counter=2, max=908 ms, ...}
			// if the key does not exist or was deleted this statement will print "continuous" sample
			// for the Stopwatch including its name:
			// e.g. StopwatchSample{name=demo.stopwatch, total=121 s, counter=97, ... }
			System.out.println(stopwatch.sampleIncrement("key1"));
			Split split = stopwatch.start();
			Thread.sleep(random.nextInt(SLEEP_TIME));
			split.stop();

			System.out.println(stopwatch.sampleIncrement("key2"));
			split = stopwatch.start();
			Thread.sleep(random.nextInt(SLEEP_TIME));
			split.stop();
		}
	}
}
