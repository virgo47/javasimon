package org.javasimon;

import org.javasimon.clock.TestClock;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for {@link Counter}.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class CounterTest extends SimonUnitTest {

	@Test
	public void newCounter() throws InterruptedException {
		Counter counter = SimonManager.getCounter(null);
		Assert.assertEquals(counter.getCounter(), 0);
		Assert.assertEquals(counter.getIncrementSum(), 0);
		Assert.assertEquals(counter.getDecrementSum(), 0);
		Assert.assertEquals(counter.getMax(), Long.MIN_VALUE);
		Assert.assertEquals(counter.getMin(), Long.MAX_VALUE);
		Assert.assertEquals(counter.getFirstUsage(), 0);
		Assert.assertEquals(counter.getLastUsage(), 0);
		Assert.assertEquals(counter.getMaxTimestamp(), 0);
		Assert.assertEquals(counter.getMinTimestamp(), 0);
		assertCounterAndSampleAreEqual(counter);
	}

	@Test
	public void increase() throws InterruptedException {
		Counter counter = SimonManager.getCounter(null);
		counter.increase();
		Assert.assertEquals(counter.getCounter(), 1);
		Assert.assertEquals(counter.getIncrementSum(), 1);
		Assert.assertEquals(counter.getDecrementSum(), 0);
		Assert.assertEquals(counter.getMax(), 1);
		Assert.assertEquals(counter.getMin(), Long.MAX_VALUE);
		Assert.assertTrue(counter.getFirstUsage() != 0);
		Assert.assertEquals(counter.getFirstUsage(), counter.getLastUsage());
		Assert.assertEquals(counter.getMaxTimestamp(), counter.getLastUsage());
		Assert.assertEquals(counter.getMinTimestamp(), 0);
		assertCounterAndSampleAreEqual(counter);
	}

	@Test
	public void decrease() {
		Counter counter = SimonManager.getCounter(null);
		counter.decrease();
		Assert.assertEquals(counter.getCounter(), -1);
		Assert.assertEquals(counter.getIncrementSum(), 0);
		Assert.assertEquals(counter.getDecrementSum(), 1);
		Assert.assertEquals(counter.getMax(), Long.MIN_VALUE);
		Assert.assertEquals(counter.getMin(), -1);
		Assert.assertTrue(counter.getFirstUsage() != 0);
		Assert.assertEquals(counter.getFirstUsage(), counter.getLastUsage());
		Assert.assertEquals(counter.getMaxTimestamp(), 0);
		Assert.assertEquals(counter.getMinTimestamp(), counter.getLastUsage());
		assertCounterAndSampleAreEqual(counter);
	}

	@Test
	public void increaseDecrease() throws InterruptedException {
		TestClock testClock = new TestClock();
		EnabledManager manager = new EnabledManager(testClock);
		Counter counter = manager.getCounter(null);

		testClock.setMillisNanosFollow(10);
		counter.increase();

		testClock.setMillisNanosFollow(30);
		counter.decrease();

		Assert.assertEquals(counter.getCounter(), 0);
		Assert.assertEquals(counter.getIncrementSum(), 1);
		Assert.assertEquals(counter.getDecrementSum(), 1);
		Assert.assertEquals(counter.getMax(), 1);
		Assert.assertEquals(counter.getMin(), 0);
		Assert.assertTrue(counter.getMaxTimestamp() != counter.getLastUsage());
		Assert.assertTrue(counter.getMaxTimestamp() != counter.getMinTimestamp());
		Assert.assertEquals(counter.getMinTimestamp(), counter.getLastUsage());
		assertCounterAndSampleAreEqual(counter);
	}

	@Test
	public void negativeIncrease() throws InterruptedException {
		Counter counter = SimonManager.getCounter(null);
		counter.increase(-3);
		Assert.assertEquals(counter.getCounter(), -3);
		Assert.assertEquals(counter.getIncrementSum(), -3);
		Assert.assertEquals(counter.getDecrementSum(), 0);
		Assert.assertEquals(counter.getMax(), Long.MIN_VALUE);
		Assert.assertEquals(counter.getMin(), -3);
		assertCounterAndSampleAreEqual(counter);
	}

	@Test
	public void arbitraryStartValue() {
		Counter counter = SimonManager.getCounter(null);
		counter.set(47);
		Assert.assertEquals(counter.getCounter(), 47);
		Assert.assertEquals(counter.getIncrementSum(), 0);
		Assert.assertEquals(counter.getDecrementSum(), 0);
		Assert.assertEquals(counter.getMax(), 47);
		Assert.assertEquals(counter.getMin(), 47);
		Assert.assertTrue(counter.getFirstUsage() != 0);
		Assert.assertEquals(counter.getFirstUsage(), counter.getLastUsage());
		Assert.assertEquals(counter.getMaxTimestamp(), counter.getLastUsage());
		Assert.assertEquals(counter.getMinTimestamp(), counter.getLastUsage());
		assertCounterAndSampleAreEqual(counter);
	}

	@Test
	public void disabledCounter() {
		Counter counter = SimonManager.getCounter(null);
		counter.setState(SimonState.DISABLED, false);
		assertZeroSample(counter.sample());
		counter.set(47);
		counter.increase();
		counter.increase(3);
		counter.decrease();
		counter.decrease(2);
		assertZeroSample(counter.sample());

		counter.setState(SimonState.ENABLED, false);
		counter.increase();
		Assert.assertEquals(counter.getCounter(), 1);
		counter.setState(SimonState.DISABLED, false);
		// disabled mean no change, but reading works just fine
		counter.increase();
		Assert.assertEquals(counter.getCounter(), 1);
		Assert.assertEquals(counter.sample().getCounter(), 1);
	}

	@Test
	public void sampling() {
		Counter counter = SimonManager.getCounter(null);
		counter.increase();
		assertCounterAndSampleAreEqual(counter, counter.sampleIncrement(""));
		// no change, zero increment
		assertZeroSample(counter.sampleIncrement(""));

		counter.increase();
		assertIncrementalSampleAfterIncrease(counter.sampleIncrement(""));

		// another increase produces the same incremental sample
		counter.increase();
		assertIncrementalSampleAfterIncrease(counter.sampleIncrement(""));

		// after key is removed, next incremental sample equals normal sample
		Assert.assertTrue(counter.stopIncrementalSampling(""));
		counter.increase();
		assertCounterAndSampleAreEqual(counter, counter.sampleIncrement(""));
		assertCounterAndSampleAreEqual(counter);

		// check of return value for nonexistent increment key
		Assert.assertFalse(counter.stopIncrementalSampling("nonexistent"));
	}

	private void assertIncrementalSampleAfterIncrease(CounterSample sampleIncrement) {
		Assert.assertEquals(sampleIncrement.getCounter(), 1);
		Assert.assertEquals(sampleIncrement.getMax(), 1);
		Assert.assertEquals(sampleIncrement.getMin(), Long.MAX_VALUE);
		Assert.assertNotEquals(sampleIncrement.getMaxTimestamp(), 0);
		Assert.assertEquals(sampleIncrement.getMinTimestamp(), 0);
		Assert.assertEquals(sampleIncrement.getIncrementSum(), 1);
		Assert.assertEquals(sampleIncrement.getDecrementSum(), 0);
	}

	private void assertZeroSample(CounterSample sample) {
		Assert.assertEquals(sample.getCounter(), 0);
		Assert.assertEquals(sample.getMax(), Long.MIN_VALUE);
		Assert.assertEquals(sample.getMin(), Long.MAX_VALUE);
		Assert.assertEquals(sample.getMaxTimestamp(), 0);
		Assert.assertEquals(sample.getMinTimestamp(), 0);
		Assert.assertEquals(sample.getIncrementSum(), 0);
		Assert.assertEquals(sample.getDecrementSum(), 0);
	}

	private void assertCounterAndSampleAreEqual(Counter counter) {
		assertCounterAndSampleAreEqual(counter, counter.sample());
	}

	private void assertCounterAndSampleAreEqual(Counter counter, CounterSample sample) {
		Assert.assertEquals(sample.getCounter(), counter.getCounter());
		Assert.assertEquals(sample.getIncrementSum(), counter.getIncrementSum());
		Assert.assertEquals(sample.getDecrementSum(), counter.getDecrementSum());
		Assert.assertEquals(sample.getMax(), counter.getMax());
		Assert.assertEquals(sample.getMin(), counter.getMin());
		Assert.assertEquals(sample.getFirstUsage(), counter.getFirstUsage());
		Assert.assertEquals(sample.getLastUsage(), counter.getLastUsage());
		Assert.assertEquals(sample.getMaxTimestamp(), counter.getMaxTimestamp());
		Assert.assertEquals(sample.getMinTimestamp(), counter.getMinTimestamp());
	}
}
