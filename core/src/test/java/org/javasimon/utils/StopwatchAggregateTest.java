package org.javasimon.utils;

import org.javasimon.SimonUnitTest;
import org.javasimon.StopwatchSample;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class StopwatchAggregateTest extends SimonUnitTest {

	private StopwatchAggregate aggregate;

	@BeforeMethod
	public void beforeMethod() {
		aggregate = new StopwatchAggregate();
	}

	@Test
	public void testInitValues() {
		Assert.assertEquals(aggregate.getTotal(), 0);
		Assert.assertEquals(aggregate.getCounter(), 0);
		Assert.assertEquals(aggregate.getMin(), Long.MAX_VALUE);
		Assert.assertEquals(aggregate.getMax(), 0);
		Assert.assertEquals(aggregate.getMinTimestamp(), 0);
		Assert.assertEquals(aggregate.getMaxTimestamp(), 0);
		Assert.assertEquals(aggregate.getActive(), 0);
		Assert.assertEquals(aggregate.getMaxActive(), 0);
		Assert.assertEquals(aggregate.getMaxActiveTimestamp(), 0);
	}

	@Test
	public void testTotalIsIncremented() {
		StopwatchSample sample = new StopwatchSample();

		sample.setTotal(5);
		aggregate.addSample(sample);
		Assert.assertEquals(aggregate.getTotal(), 5);

		sample.setTotal(15);
		aggregate.addSample(sample);
		Assert.assertEquals(aggregate.getTotal(), 20);
	}

	@Test
	public void testCounterIsIncremented() {
		StopwatchSample sample = new StopwatchSample();

		sample.setCounter(1);
		aggregate.addSample(sample);
		Assert.assertEquals(aggregate.getCounter(), 1);

		sample.setCounter(2);
		aggregate.addSample(sample);
		Assert.assertEquals(aggregate.getCounter(), 3);
	}

	@Test
	public void testMinIsUpdated() {
		StopwatchSample sample = new StopwatchSample();

		sample.setMin(40);
		sample.setMinTimestamp(100);
		aggregate.addSample(sample);
		Assert.assertEquals(aggregate.getMin(), 40);
		Assert.assertEquals(aggregate.getMinTimestamp(), 100);

		sample.setMin(10);
		sample.setMinTimestamp(200);
		aggregate.addSample(sample);
		Assert.assertEquals(aggregate.getMin(), 10);
		Assert.assertEquals(aggregate.getMinTimestamp(), 200);

		sample.setMin(20);
		sample.setMinTimestamp(500);
		aggregate.addSample(sample);
		Assert.assertEquals(aggregate.getMin(), 10);
		Assert.assertEquals(aggregate.getMinTimestamp(), 200);
	}

	@Test
	public void testMaxIsUpdated() {
		StopwatchSample sample = new StopwatchSample();

		sample.setMax(10);
		sample.setMaxTimestamp(100);
		aggregate.addSample(sample);
		Assert.assertEquals(aggregate.getMax(), 10);
		Assert.assertEquals(aggregate.getMaxTimestamp(), 100);

		sample.setMax(40);
		sample.setMaxTimestamp(200);
		aggregate.addSample(sample);
		Assert.assertEquals(aggregate.getMax(), 40);
		Assert.assertEquals(aggregate.getMaxTimestamp(), 200);

		sample.setMax(20);
		sample.setMaxTimestamp(500);
		aggregate.addSample(sample);
		Assert.assertEquals(aggregate.getMax(), 40);
		Assert.assertEquals(aggregate.getMaxTimestamp(), 200);
	}

	@Test
	public void testActiveIsUpdated() {
		StopwatchSample sample = new StopwatchSample();

		sample.setActive(2);
		aggregate.addSample(sample);
		Assert.assertEquals(aggregate.getActive(), 2);

		sample.setActive(4);
		aggregate.addSample(sample);
		Assert.assertEquals(aggregate.getActive(), 6);
	}

	@Test
	public void testMaxActiveIsUpdated() {
		StopwatchSample sample = new StopwatchSample();

		sample.setMaxActive(10);
		sample.setMaxActiveTimestamp(100);
		aggregate.addSample(sample);
		Assert.assertEquals(aggregate.getMaxActive(), 10);
		Assert.assertEquals(aggregate.getMaxActiveTimestamp(), 100);

		sample.setMaxActive(40);
		sample.setMaxActiveTimestamp(200);
		aggregate.addSample(sample);
		Assert.assertEquals(aggregate.getMaxActive(), 40);
		Assert.assertEquals(aggregate.getMaxActiveTimestamp(), 200);

		sample.setMaxActive(20);
		sample.setMaxActiveTimestamp(300);
		aggregate.addSample(sample);
		Assert.assertEquals(aggregate.getMaxActive(), 40);
		Assert.assertEquals(aggregate.getMaxActiveTimestamp(), 200);
	}

	@Test
	public void testNewlyCreatedAggregatesAreEqual() {
		Assert.assertEquals(aggregate, new StopwatchAggregate());
	}

	@Test
	public void testAggregatesWithSameChangesAreEqual() {
		StopwatchSample sample = new StopwatchSample();
		sample.setTotal(1);
		sample.setCounter(2);
		sample.setMin(3);
		sample.setMax(4);
		sample.setMinTimestamp(100);
		sample.setMaxTimestamp(200);
		sample.setActive(5);
		sample.setMaxActive(6);
		sample.setMaxActiveTimestamp(300);

		StopwatchAggregate otherAggregate = new StopwatchAggregate();
		aggregate.addSample(sample);
		otherAggregate.addSample(sample);

		Assert.assertEquals(aggregate, otherAggregate);
	}
}
