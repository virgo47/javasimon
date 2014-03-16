package org.javasimon;

import org.javasimon.utils.SimonUtils;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for {@link Stopwatch}.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class StopwatchTest {

	@BeforeMethod
	public void resetAndEnable() {
		SimonManager.clear();
		SimonManager.enable();
	}

	@Test
	public void basicStopwatchTest() {
		Stopwatch stopwatch = SimonManager.getStopwatch(null);
		long split = stopwatch.start().stop().runningFor();
		Assert.assertTrue(stopwatch.getTotal() >= 0);
		Assert.assertEquals(stopwatch.getTotal(), split);
		Assert.assertEquals(stopwatch.getCounter(), 1);
		Assert.assertEquals(stopwatch.getMax(), stopwatch.getTotal());
		Assert.assertEquals(stopwatch.getMin(), stopwatch.getTotal());
	}

	@Test
	public void usagesTest() throws Exception {
		Stopwatch stopwatch = SimonManager.getStopwatch(null);
		stopwatch.reset();
		Assert.assertEquals(stopwatch.getFirstUsage(), 0);
		Assert.assertEquals(stopwatch.getLastUsage(), 0);
		Split split = stopwatch.start();
		Assert.assertEquals(stopwatch.getFirstUsage(), stopwatch.getLastUsage());
		split.stop();
		Assert.assertTrue(stopwatch.getFirstUsage() <= stopwatch.getLastUsage());
		Thread.sleep(20);
		stopwatch.addSplit(Split.create(0));
		Assert.assertTrue(stopwatch.getFirstUsage() < stopwatch.getLastUsage());
	}

	@Test
	public void resetTest() throws Exception {
		// with raw current millis this test is unstable - this is not a problem in real-life situations though
		// point is to check that timestamps are set, not that they are set off by 1 ms or so
		long ts = SimonUtils.millisForNano(System.currentTimeMillis());
		Stopwatch stopwatch = SimonManager.getStopwatch(null);
		stopwatch.reset();
		stopwatch.addSplit(Split.create(100));
		Assert.assertEquals(stopwatch.getTotal(), 100);
		Assert.assertEquals(stopwatch.getMax(), 100);
		Assert.assertEquals(stopwatch.getMin(), 100);
		long maxTimestamp = stopwatch.getMaxTimestamp();
		Assert.assertTrue(maxTimestamp >= ts, "maxTimestamp=" + maxTimestamp + ", ts=" + ts);
		Assert.assertEquals(stopwatch.getMinTimestamp(), maxTimestamp);
		Assert.assertEquals(stopwatch.getLastUsage(), maxTimestamp);
		Assert.assertEquals(stopwatch.getFirstUsage(), maxTimestamp);
		Assert.assertEquals(stopwatch.getCounter(), 1);
		StopwatchSample sample = stopwatch.sample();
		Assert.assertEquals(sample.getName(), stopwatch.getName());
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
		sample = stopwatch.sample();
		Assert.assertEquals(sample.getCounter(), 0);
		Assert.assertEquals(sample.getTotal(), 0);
		Assert.assertEquals(sample.getMean(), 0d);
		Assert.assertEquals(sample.getStandardDeviation(), Double.NaN);
		Assert.assertEquals(sample.getVariance(), Double.NaN);
		Assert.assertEquals(sample.getVarianceN(), Double.NaN);
	}

	@Test
	public void disableEnableInsideSplit() throws Exception {
		Stopwatch stopwatch = SimonManager.getStopwatch(null);
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
}
