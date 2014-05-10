package org.javasimon.proxy;

import org.javasimon.SimonManager;
import org.javasimon.SimonUnitTest;
import org.javasimon.Stopwatch;
import org.javasimon.source.DisabledStopwatchSource;
import org.javasimon.source.StopwatchSource;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Unit test for StopwatchProxy, AbstractMethodSource, etc.
 *
 * @author gquintana
 */
public class StopwatchProxyTest extends SimonUnitTest {

	public interface MonitoredInterface {
		String welcome(String name);
	}

	public class MonitoredImplementation implements MonitoredInterface {
		public long sleepTime = 0;

		public String welcome(String name) {
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException interruptedException) {
					// ignored
				}
			}
			return "Hello " + name + "!";
		}
	}

	private MonitoredImplementation monitoredTarget = new MonitoredImplementation();
	private StopwatchSource<DelegatingMethodInvocation<MonitoredInterface>> disabledStopwatchSource
		= DisabledStopwatchSource.get();

	private MonitoredInterface newMonitoredProxy(StopwatchSource<DelegatingMethodInvocation<MonitoredInterface>> stopwatchSource) {
		return new StopwatchProxyFactory<>(monitoredTarget, stopwatchSource).newProxy(MonitoredInterface.class);
	}

	private MonitoredInterface newMonitoredProxy() {
		return new StopwatchProxyFactory<MonitoredInterface>(monitoredTarget).newProxy(MonitoredInterface.class);
	}

	private void assertStopwatchCounter(String name, long counter, boolean enabled) {
		if (enabled) {
			Stopwatch stopwatch = (Stopwatch) SimonManager.getSimon(name);
			assertNotNull(stopwatch);
			assertEquals(stopwatch.getCounter(), counter);
		}
	}

	private void doTest(MonitoredInterface monitoredProxy, boolean enabled) {
		String message = monitoredProxy.welcome("world");
		assertEquals(message, "Hello world!");
		assertStopwatchCounter("org.javasimon.proxy.MonitoredImplementation.welcome", 1L, enabled);
		message = monitoredProxy.welcome("Simon");
		assertEquals(message, "Hello Simon!");
		assertStopwatchCounter("org.javasimon.proxy.MonitoredImplementation.welcome", 2L, enabled);
	}

	/**
	 * Normal test
	 */
	@Test
	public void testMain() {
		MonitoredInterface monitoredProxy = newMonitoredProxy();
		doTest(monitoredProxy, true);
	}

	/**
	 * Test with caching monitor source
	 */
	@Test
	public void testCache() {
		MonitoredInterface monitoredProxy = newMonitoredProxy(new ProxyStopwatchSource<MonitoredInterface>().cache());
		doTest(monitoredProxy, true);
	}

	/**
	 * Test with disabled monitor source
	 */
	@Test
	public void testDisabled() {
		MonitoredInterface monitoredProxy = newMonitoredProxy(disabledStopwatchSource);
		doTest(monitoredProxy, false);
	}
}
