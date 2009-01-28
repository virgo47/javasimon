package org.javasimon.jmx;

import org.javasimon.Stopwatch;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Counter;

import javax.management.ObjectName;
import javax.management.JMException;
import javax.management.MBeanServer;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * Trieda Example.
 *
 * @author <a href="mailto:radovan.sninsky@siemens.com">Radovan Sninsky</a>
 * @version $ Revision $ $ Date $
 * @created 26.1.2009 9:57:39
 * @since 1.0
 */
public class Example {

	private static void waitForEnterPressed() {
		try {
			System.out.println("\nPress <Enter> to continue...");
			System.in.read();
		} catch (IOException e) { /* do nothing */ }
	}

	private static SimonMXBeanImpl register() {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName name = new ObjectName("org.javasimon.jmx.example:type=Simon");
			if (mbs.isRegistered(name)) {
				mbs.unregisterMBean(name);
			}
			SimonMXBeanImpl simon = new SimonMXBeanImpl(SimonManager.manager());
			mbs.registerMBean(simon, name);
			System.out.println("SimonMXBean registerd under name: "+name);
			return simon;
		} catch (JMException e) {
			System.out.println("SimonMXBean registration failed!\n"+e);
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
			System.out.println("SimonMXBean unregistration failed!\n"+e);
		}
	}


	/**
	 * Entry point of the example application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		Stopwatch stopwatch = SimonManager.getStopwatch("org.javasimon.jmx.example1");
		Counter counter = SimonManager.getCounter("org.javasimon.jmx.example2");

		Split s = stopwatch.start();
		try { Thread.sleep(632); } catch (InterruptedException e) { /* do nothing */ }
		s.stop();

		counter.increment(52);
		counter.decrement(12);
		counter.increment(18);

		register();

		waitForEnterPressed();
		unregister();
	}
}
