package org.javasimon.examples.perf;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.proxy.DelegatingMethodInvocation;
import org.javasimon.proxy.ProxyStopwatchSource;
import org.javasimon.proxy.StopwatchProxyFactory;
import org.javasimon.source.DisabledStopwatchSource;
import org.javasimon.source.StopwatchSource;
import org.javasimon.utils.SimonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gquintana
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class StopwatchProxyPerformance {

	private static final Logger LOGGER = LoggerFactory.getLogger(StopwatchProxyPerformance.class);
	public static final int ITERATIONS = 1000000;

	public interface MonitoredInterface {
		String welcome(String name);
	}

	public static class MonitoredImplementation implements MonitoredInterface {
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

	private static MonitoredImplementation monitoredTarget = new MonitoredImplementation();
	private static StopwatchSource<DelegatingMethodInvocation<MonitoredInterface>> disabledStopwatchSource
		= DisabledStopwatchSource.get();

	private static MonitoredInterface newMonitoredProxy(StopwatchSource<DelegatingMethodInvocation<MonitoredInterface>> stopwatchSource) {
		return new StopwatchProxyFactory<>(monitoredTarget, stopwatchSource).newProxy(MonitoredInterface.class);
	}

	private static MonitoredInterface newMonitoredProxy() {
		return new StopwatchProxyFactory<MonitoredInterface>(monitoredTarget).newProxy(MonitoredInterface.class);
	}

	public static void main(String... args) {
		// 1) Without proxy
		long implementation = doTestPerformance(monitoredTarget, ITERATIONS);
		logPerformanceTime("No proxy", implementation, implementation, ITERATIONS);

		// 2) With proxy
		MonitoredInterface monitoredProxy = newMonitoredProxy();
		long proxy = doTestPerformance(monitoredProxy, ITERATIONS);
		logPerformanceTime("Proxy", implementation, proxy, ITERATIONS);

		// 3) With proxy and disable
		monitoredProxy = newMonitoredProxy(disabledStopwatchSource);
		long disabledProxy = doTestPerformance(monitoredProxy, ITERATIONS);
		logPerformanceTime("Proxy disabled", implementation, disabledProxy, ITERATIONS);

		// 4) With proxy and cache
		monitoredProxy = newMonitoredProxy(new ProxyStopwatchSource<MonitoredInterface>().cache());
		long cacheProxy = doTestPerformance(monitoredProxy, ITERATIONS);
		logPerformanceTime("Proxy cached", implementation, cacheProxy, ITERATIONS);

		// 5) With proxy, cache and disable
		StopwatchSource<DelegatingMethodInvocation<MonitoredInterface>> disabledCachedStopwatchSource = new ProxyStopwatchSource<MonitoredInterface>() {
			@Override
			public boolean isMonitored(DelegatingMethodInvocation<MonitoredInterface> location) {
				return false;
			}
		}.cache();
		monitoredProxy = newMonitoredProxy(disabledCachedStopwatchSource);
		long cacheDisabledProxy = doTestPerformance(monitoredProxy, ITERATIONS);
		logPerformanceTime("Proxy cached & disabled", implementation, cacheDisabledProxy, ITERATIONS);
	}

	private static long doTestPerformance(MonitoredInterface monitoredInterface, int iterations) {
		Stopwatch stopwatch = SimonManager.getStopwatch(StopwatchProxyPerformance.class.getName() + ".testPerformance");
		Split split = stopwatch.start();

//		stopwatch.reset();
		for (int i = 0; i < iterations; i++) {
			monitoredInterface.welcome("world");
		}

		LOGGER.info(stopwatch.toString());
		return split.stop().runningFor();
	}

	private static void logPerformanceTime(String name, long reference, long measure, int iterations) {
		long delta = (measure - reference) / iterations;
		long ratio = (measure - reference) * 100L / reference;
		LOGGER.info(name +
			" " + SimonUtils.presentNanoTime(measure) +
			" " + (delta > 0 ? "+" : "") + SimonUtils.presentNanoTime(delta) +
			" " + (ratio > 0 ? "+" : "") + ratio + "%");
	}
}
