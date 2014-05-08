package org.javasimon.examples.jmx;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.jmx.JmxReporter;

import java.util.Random;

/**
 * {@link JmxReporter} allows for quick set up of {@link org.javasimon.jmx.SimonManagerMXBean} and
 * optionally for {@link org.javasimon.jmx.JmxRegisterCallback} via fluent API, as well as for
 * easy unregistration of all related MXBeans.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class JmxReporterExample {

	public static void main(String... args) throws InterruptedException {
		Stopwatch existingStopwatch = SimonManager.getStopwatch("any.previously.ExistingStopwatch");
		// Create JMX reporter
		JmxReporter reporter = JmxReporter.forDefaultManager()
			.registerSimons() // add MBean for every Simon
			.registerExistingSimons() // register also already existing ones (ExistingStopwatch in this case)
			.start(); // this performs actual MXBean registration + JmxRegisterCallback is added to manager

		// "testStopwatch" can be accessed through JMX bean for manager and
		// through separate beans for this particular Simon
		Stopwatch stopwatch = SimonManager.manager().getStopwatch("testStopwatch");

		// Generate some metrics data that can be accessed through JMX
		Random random = new Random();
		// loop will finish when ExistingStopwatch is set to disabled
		// go to jconsole/jvisualvm, find MBean for ExistingStopwatch, go to Operations and call setState(DISABLED,true)
		while (existingStopwatch.isEnabled()) {
			Split split = stopwatch.start();
			try {
				Thread.sleep(random.nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			split.stop();
		}

		reporter.stop();
		// this is here to give user some time to check, that all MBeans were unregistered
		Thread.sleep(10000);
	}
}
