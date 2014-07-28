package org.javasimon.examples.jmx;

import org.javasimon.Counter;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.examples.ExampleUtils;
import org.javasimon.jmx.JmxRegisterCallback;

/**
 * JmxCallbackExample demonstrates {@link JmxRegisterCallback} in action. It creates one Counter
 * and one Stopwatch that can be monitored via {@code jconsole} or any other/custom JMX client.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class JmxCallbackExample {
	/**
	 * Entry point to the JMX Callback Example.
	 *
	 * @param args unused
	 * @throws Exception whatever may happen in this crazy world
	 */
	@SuppressWarnings("InfiniteLoopStatement")
	public static void main(String[] args) throws Exception {
		SimonManager.callback().addCallback(new JmxRegisterCallback("org.javasimon.examples.jmx.JmxCallbackExample"));

		Counter counter = SimonManager.getCounter("org.javasimon.examples.jmx.counter");
		Stopwatch stopwatch = SimonManager.getStopwatch("org.javasimon.examples.jmx.stopwatch");
		// these created just to have more stuff in jconsole
		SimonManager.getCounter("org.javasimon.different.counter");
		SimonManager.getStopwatch("org.javasimon.some.other.jmx.stopwatch1");
		SimonManager.getStopwatch("org.javasimon.some.other.jmx.stopwatch2");
		System.out.println("Now open jconsole and check it out...\nWatch org.javasimon.examples.jmx.stopwatch for changes.");
		while (true) {
			counter.increase();
			try (Split ignored = stopwatch.start()) {
				ExampleUtils.waitRandomlySquared(40);
			}
		}
	}
}
