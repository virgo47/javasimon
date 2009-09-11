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
		Stopwatch stopwatch = SimonManager.getStopwatch(STOPWATCH_NAME);
		long split = stopwatch.start().stop();
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
		Split split = stopwatch.start();
		Assert.assertEquals(stopwatch.getFirstUsage(), stopwatch.getLastUsage());
		split.stop();
		Assert.assertTrue(stopwatch.getFirstUsage() <= stopwatch.getLastUsage());
		Thread.sleep(20);
		stopwatch.addTime(0);
		Assert.assertTrue(stopwatch.getFirstUsage() < stopwatch.getLastUsage());
	}

	@Test
	public void resetTest() throws Exception {
		Stopwatch stopwatch = SimonManager.getStopwatch(STOPWATCH_NAME);
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
		StopwatchSample sample = (StopwatchSample) stopwatch.sample();
		Assert.assertEquals(sample.getCounter(), 1);
		Assert.assertEquals(sample.getTotal(), 100);
		Assert.assertEquals(sample.getMean(), 100d);
		Assert.assertEquals(sample.getStandardDeviation(), 0d);
		Assert.assertEquals(sample.getVariance(), 0d);
		Assert.assertEquals(sample.getVarianceN(), 0d);

		stopwatch.reset();
		Assert.assertEquals(stopwatch.getTotal(), 0);
		Assert.assertEquals(stopwatch.getMax(), 0);
		Assert.assertEquals(stopwatch.getMin(), Long.MAX_VALUE);
		Assert.assertEquals(stopwatch.getMaxTimestamp(), 0);
		Assert.assertEquals(stopwatch.getMinTimestamp(), 0);
		Assert.assertTrue(stopwatch.getLastUsage() >= ts); // usages are NOT clear!
		Assert.assertTrue(stopwatch.getFirstUsage() >= ts);
		Assert.assertEquals(stopwatch.getCounter(), 0);
		sample = (StopwatchSample) stopwatch.sample();
		Assert.assertEquals(sample.getCounter(), 0);
		Assert.assertEquals(sample.getTotal(), 0);
		Assert.assertEquals(sample.getMean(), 0d);
		Assert.assertEquals(sample.getStandardDeviation(), 0d);
		Assert.assertEquals(sample.getVariance(), 0d);
		Assert.assertEquals(sample.getVarianceN(), 0d);
	}

	@Test
	public void disableEnableInsideSplit() throws Exception {
		Stopwatch stopwatch = SimonManager.getStopwatch(STOPWATCH_NAME);
		Split split = stopwatch.start();
		Assert.assertEquals(stopwatch.getActive(), 1);
		stopwatch.setState(SimonState.DISABLED, false);
		Assert.assertEquals(stopwatch.getActive(), 1);
		split.stop();
		Assert.assertEquals(stopwatch.getActive(), 0);
		Assert.assertTrue(stopwatch.getTotal() > 0);
		Assert.assertEquals(stopwatch.getCounter(), 1);
		// split started on enabled stopwatch does have an effect
		long total = stopwatch.getTotal();
		long counter = stopwatch.getCounter();

		split = stopwatch.start();
		Assert.assertEquals(stopwatch.getActive(), 0);
		stopwatch.setState(SimonState.ENABLED, false);
		Assert.assertEquals(stopwatch.getActive(), 0);
		split.stop();
		Assert.assertEquals(stopwatch.getActive(), 0);
		// there is no change because this split was started on disabled stopwatch
		Assert.assertEquals(stopwatch.getTotal(), total);
		Assert.assertEquals(stopwatch.getCounter(), counter);
	}

	@Test
	public void issue10NPEInSplitToString() {
		Stopwatch stopwatch = SimonManager.getStopwatch(STOPWATCH_NAME);
		SimonManager.getStopwatch("org").setState(SimonState.DISABLED, true);
		Split split = stopwatch.start();
		split.stop();
		split.toString();
	}
}
