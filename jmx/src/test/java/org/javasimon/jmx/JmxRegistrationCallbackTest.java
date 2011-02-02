package org.javasimon.jmx;

import org.javasimon.SimonManager;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import java.lang.management.ManagementFactory;

/**
 * JmxRegistrationCallbackTest.
 *
 * @author Rene Kok
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @author joshcanfiel
 * @version $Revision$ $Date$
 * @created 21.7.2009 10:07:06
 * @since 1.0
 */
public class JmxRegistrationCallbackTest {
	private static final String OBJECT_NAME = "whatever:type=anything";
	private MBeanServer mbs;

	@BeforeMethod
	public void setUp() throws Exception {
		SimonManager.clear();

		mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = new ObjectName(OBJECT_NAME);
		SimonMXBean simon = new SimonMXBeanImpl(SimonManager.manager());
		mbs.registerMBean(simon, name);

		SimonManager.callback().addCallback(new JmxRegisterCallback());
	}

	@Test
	public void managerClearTest() throws MalformedObjectNameException {
		String counterName = "test.1";
		ObjectName counterObjectName = new ObjectName(counterName + ":type=" + SimonInfo.COUNTER);

		String stopwatchName = "test.2";
		ObjectName stopwatchObjectName = new ObjectName(stopwatchName + ":type=" + SimonInfo.STOPWATCH);

		Assert.assertFalse(mbs.isRegistered(counterObjectName));
		Assert.assertFalse(mbs.isRegistered(stopwatchObjectName));

		SimonManager.getCounter(counterName);
		Assert.assertTrue(mbs.isRegistered(counterObjectName));

		SimonManager.getStopwatch(stopwatchName);
		Assert.assertTrue(mbs.isRegistered(stopwatchObjectName));

		SimonManager.clear();
		Assert.assertFalse(mbs.isRegistered(counterObjectName));
		Assert.assertFalse(mbs.isRegistered(stopwatchObjectName));
	}

	@Test
	public void destroySimonTest() throws MalformedObjectNameException {
		String counterName = "test.1";
		ObjectName counterObjectName = new ObjectName(counterName + ":type=" + SimonInfo.COUNTER);

		String stopwatchName = "test.2";
		ObjectName stopwatchObjectName = new ObjectName(stopwatchName + ":type=" + SimonInfo.STOPWATCH);

		Assert.assertFalse(mbs.isRegistered(counterObjectName));
		Assert.assertFalse(mbs.isRegistered(stopwatchObjectName));

		SimonManager.getCounter(counterName);
		Assert.assertTrue(mbs.isRegistered(counterObjectName));
		SimonManager.destroySimon(counterName);
		Assert.assertFalse(mbs.isRegistered(counterObjectName));

		SimonManager.getStopwatch(stopwatchName);
		Assert.assertTrue(mbs.isRegistered(stopwatchObjectName));
		SimonManager.destroySimon(stopwatchName);
		Assert.assertFalse(mbs.isRegistered(stopwatchObjectName));
	}

	@AfterMethod
	public void tearDown() throws Exception {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = new ObjectName(OBJECT_NAME);
		mbs.unregisterMBean(name);
	}
}
