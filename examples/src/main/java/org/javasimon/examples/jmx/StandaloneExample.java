package org.javasimon.examples.jmx;

import java.lang.management.ManagementFactory;
import javax.management.JMException;
import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.javasimon.Counter;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.jmx.SimonManagerMXBean;
import org.javasimon.jmx.SimonManagerMXBeanImpl;

/**
 * This example shows simplest possible way how to retrieve data from Simon through JMX.
 *
 * @author Radovan Sninsky
 * @since 2.0
 */
public class StandaloneExample {

	/**
	 * Register Simon MXBean into platform MBeanServer under
	 * {@code org.javasimon.jmx.example:type=Simon} name.
	 *
	 * @return registered SimonManagerMXBean implementation object
	 */
	private static SimonManagerMXBean register() {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName name = new ObjectName("org.javasimon.jmx.example:type=Simon");
			if (mbs.isRegistered(name)) {
				mbs.unregisterMBean(name);
			}
			SimonManagerMXBean simonManagerMXBean = new SimonManagerMXBeanImpl(SimonManager.manager());
			mbs.registerMBean(simonManagerMXBean, name);
			System.out.println("SimonManagerMXBean registerd under name: "+name);
			return simonManagerMXBean;
		} catch (JMException e) {
			System.out.println("SimonManagerMXBean registration failed!\n"+e);
		}
		return null;
	}

	/**
	 * Unregister Simon MXBean from platform MBeanServer under
	 * {@code org.javasimon.jmx.example:type=Simon} name.
	 */
	private static void unregister() {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName name = new ObjectName("org.javasimon.jmx.example:type=Simon");
			if (mbs.isRegistered(name)) {
				mbs.unregisterMBean(name);
			}
			System.out.println("SimonManagerMXBean was unregisterd");
		} catch (JMException e) {
			System.out.println("SimonManagerMXBean unregistration failed!\n"+e);
		}
	}

	/**
	 * Entry point of the example application.
	 *
	 * @param args command line arguments
	 * @throws MalformedObjectNameException probably never
	 */
	public static void main(String[] args) throws MalformedObjectNameException {
		// We create some example Simons
		Stopwatch stopwatch = SimonManager.getStopwatch("org.javasimon.jmx.example1");
		Counter counter = SimonManager.getCounter("org.javasimon.jmx.example2");

		// Do little measurement for stopwatch Simon ...
		Split s = stopwatch.start();
		try { Thread.sleep(632); } catch (InterruptedException e) { /* do nothing */ }
		s.stop();

		// ... and few usage of counter Simon.
		counter.increase(52);
		counter.decrease(12);
		counter.increase(18);

		// Than, we register provided SimonManagerMXBean.
		// SimonManagerMXBean is part of javasimon, but register method is client responsibility
		// so look to it for more detail.
		register();

		// The following is client part of code.
		// Because, this is same VM, we don't need to create JMXConnector object and
		// than obtain MBeanServerConnection. Platform MBeanServer is a MBeanServerConnection
		// already, so it's used.
		// So we use JMX.newMXBeanProxy method to make JMX create new client proxy of SimonManagerMXBean
		// for us.
		SimonManagerMXBean simonManagerMXBean = JMX.newMXBeanProxy(ManagementFactory.getPlatformMBeanServer(),
			new ObjectName("org.javasimon.jmx.example:type=Simon"), SimonManagerMXBean.class);

		// Now, we can freely retrieve smples for counter and stopwatch example Simons
		System.out.println("counter = " + simonManagerMXBean.getCounterSample("org.javasimon.jmx.example2"));
		System.out.println("stopwatch = " + simonManagerMXBean.getStopwatchSample("org.javasimon.jmx.example1"));

		// Aftera all, it's good manner to clean up, so we unregister MXBean
		unregister();
	}
}
