package org.javasimon.examples.jmx;

import org.javasimon.jmx.SimonMXBeanImpl;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.utils.LoggingCallback;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.JMException;
import java.lang.management.ManagementFactory;
import java.util.Random;
import java.util.logging.Level;

/**
 * Simulates task processing system where task processing means time should be smaller than the
 * interval between two tasks are started. Active count should be higher than 1 in many occasions.
 * Run the application with following command (from Java Simon root):
 * <pre>
 * java -cp build/core:build/examples:build/jmx org.javasimon.examples.jmx.JmxTaskProcessingSimulator</pre>
 *
 * It is not possible to graph values via {@code jconsole}, but it is possible to use operation
 * {@code getStopwatchSample} with value {@code org.javasimon.examples.jmx.tasksw} to get to the
 * stopwatch values. 
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class JmxTaskProcessingSimulator {
	/**
	 * Minimal time of the sleep before new task in ms.
	 */
	public static final int NEW_TASK_MIN = 1000;

	/**
	 * Optional time spread between tasks in ms (new task can start between {@link #NEW_TASK_MIN}
	 * and {@code NEW_TASK_MIN&nbsp;+&nbsp;NEW_TASK_SPREAD} ms after previous one).
	 */
	public static final int NEW_TASK_SPREAD = 14000;

	/**
	 * Minimal duration of the task in ms.
	 */
	public static final int TASK_MIN = 500;

	/**
	 * Optional spread added to the minimal duration in ms.
	 */
	public static final int TASK_SPREAD = 11500;

	/**
	 * Name of the task stopwatch.
	 */
	public static final String TASK_STOPWATCH = "org.javasimon.examples.jmx.tasksw";

	private static SimonMXBeanImpl register() {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName name = new ObjectName("org.javasimon.jmx.example:type=Simon");
			if (mbs.isRegistered(name)) {
				mbs.unregisterMBean(name);
			}
			SimonMXBeanImpl simon = new SimonMXBeanImpl(SimonManager.manager());
			mbs.registerMBean(simon, name);
			System.out.println("SimonMXBean registerd under name: " + name);
			return simon;
		} catch (JMException e) {
			System.out.println("SimonMXBean registration failed!\n" + e);
		}
		return null;
	}

	private static void unregister() {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName name = new ObjectName("org.javasimon.jmx.example:type=Simon");
			if (mbs.isRegistered(name)) {
				mbs.unregisterMBean(name);
			}
			System.out.println("SimonMXBean was unregisterd");
		} catch (JMException e) {
			System.out.println("SimonMXBean unregistration failed!\n" + e);
		}
	}

	/**
	 * Entry point to the JMX task processing simulator.
	 *
	 * @param args unused
	 * @throws Exception whatever may happen in this crazy world
	 */
	public static void main(String[] args) throws Exception {
		LoggingCallback loggingCallback = new LoggingCallback();
		loggingCallback.setLevel(Level.INFO);
		SimonManager.callback().addCallback(loggingCallback);
		Random random = new Random();

		register();
		try {
			while (true) {
				final int task = random.nextInt(TASK_SPREAD) + TASK_MIN;
				new Thread() {
					public void run() {
						Split split = SimonManager.getStopwatch(TASK_STOPWATCH).start();
						try {
							Thread.sleep(task);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						split.stop();
					}
				}.start();
				int sleep = random.nextInt(NEW_TASK_SPREAD) + NEW_TASK_MIN;
				Thread.sleep(sleep);
			}
		} finally {
			unregister();
		}
	}
}
