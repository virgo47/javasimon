package org.javasimon;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * StopwatchTest.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 11, 2008
 */
public final class StopwatchTestNG {
	private static final String STOPWATCH_NAME = "org.javasimon.test-stopwatch";

	@BeforeMethod
	public void resetAndEnable() {
		SimonManager.clear();
		SimonManager.enable();
	}

	@Test
	public void basicStopwatchTest() {
		Stopwatch stopwatch = SimonManager.getStopwatch(STOPWATCH_NAME).start();
		long split = stopwatch.stop();
		Assert.assertTrue(stopwatch.getTotal() >= 0);
		Assert.assertEquals(stopwatch.getTotal(), split);
		Assert.assertEquals(stopwatch.getCounter(), 1);
		Assert.assertEquals(stopwatch.getMax(), stopwatch.getTotal());
		Assert.assertEquals(stopwatch.getMin(), stopwatch.getTotal());
	}

	@Test
	public void usagesTest() throws Exception {
		Stopwatch stopwatch = SimonManager.getStopwatch(STOPWATCH_NAME);
		stopwatch.reset();
		Assert.assertEquals(stopwatch.getFirstUsage(), 0);
		Assert.assertEquals(stopwatch.getLastUsage(), 0);
		stopwatch.start();
		Assert.assertEquals(stopwatch.getFirstUsage(), stopwatch.getLastUsage());
		stopwatch.stop();
		Assert.assertTrue(stopwatch.getFirstUsage() <= stopwatch.getLastUsage());
		Thread.sleep(20);
		stopwatch.addTime(0);
		Assert.assertTrue(stopwatch.getFirstUsage() < stopwatch.getLastUsage());
	}

	@Test
	public void resetTest() throws Exception {
		Stopwatch stopwatch = SimonManager.getStopwatch(STOPWATCH_NAME);
		stopwatch.setStatProcessor(StatProcessorType.BASIC.create());
		stopwatch.reset();
		long ts = System.currentTimeMillis();
		stopwatch.addTime(100);
		Assert.assertEquals(stopwatch.getTotal(), 100);
		Assert.assertEquals(stopwatch.getMax(), 100);
		Assert.assertEquals(stopwatch.getMin(), 100);
		Assert.assertTrue(stopwatch.getMaxTimestamp() >= ts);
		Assert.assertTrue(stopwatch.getMinTimestamp() >= ts);
		Assert.assertTrue(stopwatch.getLastUsage() >= ts);
		Assert.assertTrue(stopwatch.getFirstUsage() >= ts);
		Assert.assertEquals(stopwatch.getCounter(), 1);
		Assert.assertEquals(stopwatch.getStatProcessor().getCount(), 1);
		Assert.assertEquals(stopwatch.getStatProcessor().getSum(), 100d);
		Assert.assertEquals(stopwatch.getStatProcessor().getMean(), 100d);
		Assert.assertEquals(stopwatch.getStatProcessor().getStandardDeviation(), 0d);
		stopwatch.reset();
		Assert.assertEquals(stopwatch.getTotal(), 0);
		Assert.assertEquals(stopwatch.getMax(), 0);
		Assert.assertEquals(stopwatch.getMin(), Long.MAX_VALUE);
		Assert.assertEquals(stopwatch.getMaxTimestamp(), 0);
		Assert.assertEquals(stopwatch.getMinTimestamp(), 0);
		Assert.assertTrue(stopwatch.getLastUsage() >= ts); // usages are NOT clear!
		Assert.assertTrue(stopwatch.getFirstUsage() >= ts);
		Assert.assertEquals(stopwatch.getCounter(), 0);
		Assert.assertEquals(stopwatch.getStatProcessor().getCount(), 0);
		Assert.assertEquals(stopwatch.getStatProcessor().getSum(), 0d);
		Assert.assertEquals(stopwatch.getStatProcessor().getMean(), 0d);
		Assert.assertEquals(stopwatch.getStatProcessor().getStandardDeviation(), 0d);
	}
}
