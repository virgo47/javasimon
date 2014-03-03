package org.javasimon.aggregation;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.jmx.SimonManagerMXBean;
import org.javasimon.jmx.SimonManagerMXBeanImpl;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class DemoServer {
	public static void main(String... args) {

		//System.setProperty("com.sun.management.jmxremote", "");
		//System.setProperty("com.sun.management.jmxremote.port", "1099");
		//System.setProperty("com.sun.management.jmxremote.local.only", "false");
		//System.setProperty("com.sun.management.jmxremote.authenticate", "false");
		//System.setProperty("com.sun.management.jmxremote.ssl", "false");

		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName name = new ObjectName("org.javasimon.jmx.example:type=Simon");
			if (mbs.isRegistered(name)) {
				mbs.unregisterMBean(name);
			}
			SimonManagerMXBean simonManagerMXBean = new SimonManagerMXBeanImpl(SimonManager.manager());
			mbs.registerMBean(simonManagerMXBean, name);
			System.out.println("SimonManagerMXBean registerd under name: "+name);

		} catch (JMException e) {
			System.out.println("SimonManagerMXBean registration failed!\n"+e);
		}


		Stopwatch stopwatch1 = SimonManager.getStopwatch("org.javasimon.jmx.example1");
		Stopwatch stopwatch2 = SimonManager.getStopwatch("org.javasimon.jmx.example2");

		while (true) {
			Split s1 = stopwatch1.start();
			try { Thread.sleep(632); } catch (InterruptedException e) { /* do nothing */ }
			s1.stop();

			Split s2 = stopwatch2.start();
			try { Thread.sleep(851); } catch (InterruptedException e) { /* do nothing */ }
			s2.stop();
		}
	}

}
