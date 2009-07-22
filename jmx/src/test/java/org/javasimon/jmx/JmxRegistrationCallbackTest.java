package org.javasimon.jmx;

import org.javasimon.SimonManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.management.ObjectName;
import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

/**
 * JmxRegistrationCallbackTest.
 *
 * @author Rene Kok
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @version $Revision$ $Date$
 * @created 21.7.2009 10:07:06
 * @since 1.0
 */
public class JmxRegistrationCallbackTest {
	private static final String OBJECT_NAME = "whatever:type=anything";

	@BeforeMethod
	public void setUp() throws Exception {
		SimonManager.clear();

		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = new ObjectName(OBJECT_NAME);
		SimonMXBean simon = new SimonMXBeanImpl(SimonManager.manager());
		mbs.registerMBean(simon, name);

		SimonManager.callback().addCallback(new JmxRegisterCallback());
	}

	@Test
	public void managerClearTest() {
		SimonManager.getCounter("test.1");
		SimonManager.getStopwatch("test.2");
		SimonManager.clear();
	}

	@AfterMethod
	public void tearDown() throws Exception {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = new ObjectName(OBJECT_NAME);
		mbs.unregisterMBean(name);
	}
}
