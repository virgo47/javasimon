package org.javasimon.examples.jmx;

import org.javasimon.Stopwatch;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Counter;
import org.javasimon.jmx.SimonMXBeanImpl;
import org.javasimon.jmx.SimonMXBean;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * This example shows simplest possible way how to retrieve data from javasimons
 * through jmx. Of course, this is not real apllication of javasimon jmx feature
 * becouse whole game is played in same playground - virtual machine, but it's
 * useful for studying purposes. Code is also commented
 *
 * @author Radovan Sninsky
 * @version $Revision $ $Date $
 * @created 26.1.2009 9:57:39
 * @since 2
 */
public class StandaloneExample {

	/**
	 * Register Simon MXBean into platform MBeanServer under
	 * {@code org.javasimon.jmx.example:type=Simon} name.
	 *
	 * @return registered SimonMXBean implementation object
	 */
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
			System.out.println("SimonMXBean was unregisterd");
		} catch (JMException e) {
			System.out.println("SimonMXBean unregistration failed!\n"+e);
		}
	}

	/**
	 * Entry point of the example application.
	 *
	 * @param args command line arguments
	 * @throws MalformedObjectNameException probably never
	 */
	public static void main(String[] args) throws MalformedObjectNameException {
		// We create some example simons
		Stopwatch stopwatch = SimonManager.getStopwatch("org.javasimon.jmx.example1");
		Counter counter = SimonManager.getCounter("org.javasimon.jmx.example2");

		// Do little measurement for stopwatch simon ...
		Split s = stopwatch.start();
		try { Thread.sleep(632); } catch (InterruptedException e) { /* do nothing */ }
		s.stop();

		// ... and few usage of counter simon.
		counter.increase(52);
		counter.decrease(12);
		counter.increase(18);

		// Than, we register provided SimonMXBean.
		// SimonMXBean is part of javasimon, but register method is client responsibility
		// so look to it for more detail.
		register();

		// The following is client part of code.
		// Becouse, this is same VM, we don't need to create JMXConnector object and
		// than obtain MBeanServerConnection. Platform MBeanServer is a MBeanServerConnection
		// already, so it's used.
		// So we use JMX.newMXBeanProxy method to make JMX create new client proxy of SimonMXBean
		// for us.
		SimonMXBean simon = JMX.newMXBeanProxy(ManagementFactory.getPlatformMBeanServer(),
			new ObjectName("org.javasimon.jmx.example:type=Simon"), SimonMXBean.class);

		// Now, we can freely retrieve smples for counter and stopwatch example simons
		System.out.println("counter = " + simon.getCounterSample("org.javasimon.jmx.example2"));
		System.out.println("stopwatch = " + simon.getStopwatchSample("org.javasimon.jmx.example1"));

		// Aftera all, it's good manner to clean up, so we unregister MXBean
		unregister();
	}
}
