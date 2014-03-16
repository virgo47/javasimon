package org.javasimon.jmx;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.javasimon.SimonManager;

import org.javasimon.SimonUnitTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * JmxRegistrationCallbackTest.
 *
 * @author Rene Kok
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @author joshcanfiel
 * @since 1.0
 */
public class JmxRegistrationCallbackTest extends SimonUnitTest {

	private static final String DOMAIN = "org.javasimon.jmx.JmxRegistrationCallbackTest";
	private MBeanServer mbs;

	@BeforeMethod
	public void setUp() throws Exception {
		mbs = ManagementFactory.getPlatformMBeanServer();
		SimonManager.callback().addCallback(new JmxRegisterCallback(DOMAIN));
	}

	@Test
	public void managerClearTest() throws MalformedObjectNameException {
		String counterName = "test.1";
		ObjectName counterObjectName = new ObjectName(DOMAIN + ":type=" + SimonInfo.COUNTER + ",name=" + counterName);

		String stopwatchName = "test.2";
		ObjectName stopwatchObjectName = new ObjectName(DOMAIN + ":type=" + SimonInfo.STOPWATCH + ",name=" + stopwatchName);

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
		ObjectName counterObjectName = new ObjectName(DOMAIN + ":type=" + SimonInfo.COUNTER + ",name=" + counterName);

		String stopwatchName = "test.2";
		ObjectName stopwatchObjectName = new ObjectName(DOMAIN + ":type=" + SimonInfo.STOPWATCH + ",name=" + stopwatchName);

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

	@Test
	public void simonMxBeanTest() throws Exception {
		ObjectName name = new ObjectName("whatever:type=anything");
		SimonManagerMXBean simonManagerMXBean = new SimonManagerMXBeanImpl(SimonManager.manager());
		mbs.registerMBean(simonManagerMXBean, name);
		mbs.unregisterMBean(name);
	}
}
