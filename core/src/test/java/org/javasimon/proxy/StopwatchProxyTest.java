package org.javasimon.proxy;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.source.DisabledMonitorSource;
import org.javasimon.source.MonitorSource;
import org.javasimon.utils.SimonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Unit test for StopwatchProxy, AbstractMethodSource, etc.
 *
 * @author gquintana
 */
public class StopwatchProxyTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(StopwatchProxyTest.class);

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
	private MonitorSource<ProxyMethodInvocation<MonitoredInterface>, Stopwatch> disabledStopwatchSource
		= DisabledMonitorSource.get();

	@BeforeMethod
	public void beforeMethod() {
		SimonManager.clear();
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
		MonitoredInterface monitoredProxy = StopwatchProxy.newProxy(monitoredTarget, MonitoredInterface.class);
		doTest(monitoredProxy, true);
	}

	/**
	 * Test with caching monitor source
	 */
	@Test
	public void testCache() {
		MonitoredInterface monitoredProxy = StopwatchProxy.newProxy(monitoredTarget, MonitoredInterface.class, new ProxyStopwatchSource<MonitoredInterface>().cache());
		doTest(monitoredProxy, true);
	}

	/**
	 * Test with disabled monitor source
	 */
	@Test
	public void testDisabled() {
		MonitoredInterface monitoredProxy = StopwatchProxy.newProxy(monitoredTarget, MonitoredInterface.class, disabledStopwatchSource);
		doTest(monitoredProxy, false);
	}

	private long doTestPerformance(MonitoredInterface monitoredInterface, int iterations, boolean enabled) {
		Split split = SimonManager.getStopwatch(getClass().getName() + ".testPerformance").start();
		Stopwatch stopwatch = SimonManager.getStopwatch("org.javasimon.proxy.MonitoredImplementation.welcome");
		stopwatch.reset();
		for (int i = 0; i < iterations; i++) {
			monitoredInterface.welcome("world");
		}
		assertStopwatchCounter("org.javasimon.proxy.MonitoredImplementation.welcome", iterations, enabled);
		LOGGER.info(stopwatch.toString());
		return split.stop().runningFor();
	}

	private void logPerformanceTime(String name, long reference, long measure, int iterations) {
		long delta = (measure - reference) / iterations;
		long ratio = (measure - reference) * 100L / reference;
		LOGGER.info(
			name
				+ " " + SimonUtils.presentNanoTime(measure)
				+ " " + (delta > 0 ? "+" : "") + SimonUtils.presentNanoTime(delta)
				+ " " + (ratio > 0 ? "+" : "") + ratio + "%");
	}

	/**
	 * Performance test
	 */
	@Test
	public void testPerformance() {
		int iterations = 1000000;
//		monitoredTarget.sleepTime=1L;
		// 1) Without proxy
		long implementation = doTestPerformance(monitoredTarget, iterations, false);
		logPerformanceTime("No proxy", implementation, implementation, iterations);

		// 2) With proxy
		MonitoredInterface monitoredProxy = StopwatchProxy.newProxy(monitoredTarget, MonitoredInterface.class);
		long proxy = doTestPerformance(monitoredProxy, iterations, true);
		logPerformanceTime("Proxy", implementation, proxy, iterations);

		// 3) With proxy and disable
		monitoredProxy = StopwatchProxy.newProxy(monitoredTarget, MonitoredInterface.class, disabledStopwatchSource);
		long disabledProxy = doTestPerformance(monitoredProxy, iterations, false);
		logPerformanceTime("Proxy disabled", implementation, disabledProxy, iterations);

		// 4) With proxy and cache
		monitoredProxy = StopwatchProxy.newProxy(monitoredTarget, MonitoredInterface.class, new ProxyStopwatchSource<MonitoredInterface>().cache());
		long cacheProxy = doTestPerformance(monitoredProxy, iterations, true);
		logPerformanceTime("Proxy cached", implementation, cacheProxy, iterations);

		// 5) With proxy, cache and disable
		MonitorSource<ProxyMethodInvocation<MonitoredInterface>, Stopwatch> disabledCachedStopwatchSource = new ProxyStopwatchSource<MonitoredInterface>() {
			@Override
			public boolean isMonitored(ProxyMethodInvocation<MonitoredInterface> location) {
				return false;
			}
		}.cache();
		monitoredProxy = StopwatchProxy.newProxy(monitoredTarget, MonitoredInterface.class, disabledCachedStopwatchSource);
		long cacheDisabledProxy = doTestPerformance(monitoredProxy, iterations, false);
		logPerformanceTime("Proxy cached & disabled", implementation, cacheDisabledProxy, iterations);
	}

	public static void main(String[] args) {
		new StopwatchProxyTest().testPerformance();
	}
}
