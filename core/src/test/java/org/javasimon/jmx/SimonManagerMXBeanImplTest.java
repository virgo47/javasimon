package org.javasimon.jmx;

import java.lang.management.ManagementFactory;
import java.util.List;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.javasimon.Counter;
import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
/**
 * Unit test for {@link SimonManagerMXBeanImpl}
 * @author gerald
 */
public class SimonManagerMXBeanImplTest {
	private final MBeanServer mBeanServer;
	private final Manager manager;
	private final SimonManagerMXBean managerMXBean;

	public SimonManagerMXBeanImplTest() throws Exception {
		manager=SimonManager.manager();
		managerMXBean=new SimonManagerMXBeanImpl(manager);
		mBeanServer = ManagementFactory.getPlatformMBeanServer();
		final ObjectName objectName = new ObjectName("org.javasimon", "type", "SimonManager");
		if (mBeanServer.isRegistered(objectName)) {
			mBeanServer.unregisterMBean(objectName);
		}
		mBeanServer.registerMBean(managerMXBean, objectName);
	}
	
	@BeforeMethod
	public void beforeMethod() throws Exception {
		manager.clear();		
	}
	/**
	 * Test {@link SimonManagerMXBeanImpl#getCounterSamples(java.lang.String) }
	 */
	@Test
	public void testGetCounterSamples() {
		// Prepare some Counters
		Counter aaa=SimonManager.manager().getCounter("base.counter.aaa");
		aaa.increase();aaa.increase();aaa.decrease();
		Counter bbb=SimonManager.manager().getCounter("base.counter.bbb");
		bbb.increase();bbb.decrease();
		Stopwatch sss=SimonManager.getStopwatch("base.stopwatch.sss");
		// Sample Counters
		List<CounterSample> samples=managerMXBean.getCounterSamples();
		assertEquals(samples.size(), 2);
		samples=managerMXBean.getCounterSamples("*.counter.aa*");
		assertEquals(samples.size(), 1);		
		assertEquals(samples.get(0).getIncrementSum(), 2);
		assertEquals(samples.get(0).getDecrementSum(), 1);
	}
	/**
	 * Test {@link SimonManagerMXBeanImpl#getStopwatchSamples(java.lang.String) }
	 */
	@Test
	public void testGetStopwatchSamples() throws InterruptedException {
		// Prepare some Stopwatches
		Stopwatch aaa=SimonManager.manager().getStopwatch("base.stopwatch.aaa");
		Split aaa1=aaa.start();Split aaa2=aaa.start();aaa1.stop();aaa2.stop();
		Stopwatch bbb=SimonManager.manager().getStopwatch("base.stopwatch.bbb");
		Split bbb1=bbb.start(); Thread.sleep(100L);bbb1.stop();
		Counter ccc=SimonManager.getCounter("base.counter.ccc");
		// Sample Counters
		List<StopwatchSample> samples=managerMXBean.getStopwatchSamples();
		assertEquals(samples.size(), 2);
		samples=managerMXBean.getStopwatchSamples("*.stopwatch.aa*");
		assertEquals(samples.size(), 1);
		assertEquals(samples.get(0).getCounter(), 2);
		assertEquals(samples.get(0).getMaxActive(), 2);
	}
}
