package org.javasimon.utils;

import org.javasimon.CounterSample;
import org.javasimon.SimonUnitTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public final class CounterAggregateTest extends SimonUnitTest {

	private CounterAggregate counterAggregate;

	@BeforeMethod
	public void beforeMethod() {
		counterAggregate = new CounterAggregate();
	}

	@Test
	public void testInitValues() {
		Assert.assertEquals(counterAggregate.getCounter(), 0);
		Assert.assertEquals(counterAggregate.getIncrementSum(), 0);
		Assert.assertEquals(counterAggregate.getDecrementSum(), 0);
		Assert.assertEquals(counterAggregate.getMin(), Long.MAX_VALUE);
		Assert.assertEquals(counterAggregate.getMax(), Long.MIN_VALUE);
		Assert.assertEquals(counterAggregate.getMinTimestamp(), 0);
		Assert.assertEquals(counterAggregate.getMaxTimestamp(), 0);
	}

	@Test
	public void testCounterIsAdded() {
		CounterSample sample = new CounterSample();

		sample.setCounter(1);
		counterAggregate.addSample(sample);
		Assert.assertEquals(counterAggregate.getCounter(), 1);

		sample.setCounter(2);
		counterAggregate.addSample(sample);
		Assert.assertEquals(counterAggregate.getCounter(), 3);
	}

	@Test
	public void testIncrementSumIsAdded() {
		CounterSample sample = new CounterSample();

		sample.setIncrementSum(1);
		counterAggregate.addSample(sample);
		Assert.assertEquals(counterAggregate.getIncrementSum(), 1);

		sample.setIncrementSum(2);
		counterAggregate.addSample(sample);
		Assert.assertEquals(counterAggregate.getIncrementSum(), 3);
	}

	@Test
	public void testDecrementSumIsAdded() {
		CounterSample sample = new CounterSample();

		sample.setDecrementSum(1);
		counterAggregate.addSample(sample);
		Assert.assertEquals(counterAggregate.getDecrementSum(), 1);

		sample.setDecrementSum(2);
		counterAggregate.addSample(sample);
		Assert.assertEquals(counterAggregate.getDecrementSum(), 3);
	}

	@Test
	public void testMinValueIsUpdated() {
		CounterSample sample = new CounterSample();

		sample.setMin(40);
		sample.setMinTimestamp(100);
		counterAggregate.addSample(sample);
		Assert.assertEquals(counterAggregate.getMin(), 40);
		Assert.assertEquals(counterAggregate.getMinTimestamp(), 100);

		sample.setMin(10);
		sample.setMinTimestamp(200);
		counterAggregate.addSample(sample);
		Assert.assertEquals(counterAggregate.getMin(), 10);
		Assert.assertEquals(counterAggregate.getMinTimestamp(), 200);

		sample.setMin(20);
		sample.setMinTimestamp(300);
		counterAggregate.addSample(sample);
		Assert.assertEquals(counterAggregate.getMin(), 10);
		Assert.assertEquals(counterAggregate.getMinTimestamp(), 200);
	}

	@Test
	public void testMaxValueIsUpdated() {
		CounterSample sample = new CounterSample();

		sample.setMax(10);
		sample.setMaxTimestamp(100);
		counterAggregate.addSample(sample);
		Assert.assertEquals(counterAggregate.getMax(), 10);
		Assert.assertEquals(counterAggregate.getMaxTimestamp(), 100);

		sample.setMax(40);
		sample.setMaxTimestamp(200);
		counterAggregate.addSample(sample);
		Assert.assertEquals(counterAggregate.getMax(), 40);
		Assert.assertEquals(counterAggregate.getMaxTimestamp(), 200);

		sample.setMax(20);
		sample.setMaxTimestamp(300);
		counterAggregate.addSample(sample);
		Assert.assertEquals(counterAggregate.getMax(), 40);
		Assert.assertEquals(counterAggregate.getMaxTimestamp(), 200);
	}

	@Test
	public void testNewlyCreatedAggregatesAreEqual() {
		Assert.assertEquals(counterAggregate, new CounterAggregate());
	}

	@Test
	public void testAggregatesWithSameChangesAreEqual() {
		CounterSample sample = new CounterSample();
		sample.setCounter(8);
		sample.setIncrementSum(10);
		sample.setDecrementSum(2);
		sample.setMin(-2);
		sample.setMax(10);
		sample.setMinTimestamp(100);
		sample.setMaxTimestamp(200);

		CounterAggregate otherAggregate = new CounterAggregate();
		counterAggregate.addSample(sample);
		otherAggregate.addSample(sample);

		Assert.assertEquals(counterAggregate, otherAggregate);
	}
}
