package org.javasimon.examples.jmx;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.jmx.JmxReporter;

import java.util.Random;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class JmxReporterExample {
	public static void main(String... args) {
		// Create JMX reporter
		JmxReporter reporter = JmxReporter.forDefaultManager()
				// Separate JMX beans will be registered for each Simon
				.registerSimons(true)
				// Finish configuration
				.build();

		// Register beans
		reporter.start();

		// "testStopwatch" can be accessed through JMX bean for manager and
		// through separate beans for this particular Simon
		Stopwatch stopwatch = SimonManager.manager().getStopwatch("testStopwatch");

		// Generate some metrics data that can be accessed through JMX
		Random random = new Random();
		while (true) {
			Split split = stopwatch.start();
			try {
				Thread.sleep(random.nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			split.stop();
		}
	}
}
