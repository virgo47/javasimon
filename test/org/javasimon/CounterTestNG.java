package org.javasimon;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * CounterTestNG.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Mar 6, 2009
 */
public final class CounterTestNG {
	private static final String COUNTER_NAME = "org.javasimon.test-counter";

	@BeforeMethod
	public void resetAndEnable() {
		SimonManager.clear();
		SimonManager.enable();
	}

	@Test
	public void basicTest() throws InterruptedException {
		Counter counter = SimonManager.getCounter(COUNTER_NAME);
		Assert.assertEquals(counter.getCounter(), 0);
		Assert.assertEquals(counter.getIncrementSum(), 0);
		Assert.assertEquals(counter.getDecrementSum(), 0);
		Assert.assertEquals(counter.getMax(), Long.MIN_VALUE);
		Assert.assertEquals(counter.getMin(), Long.MAX_VALUE);
		Assert.assertEquals(counter.getFirstUsage(), 0);
		Assert.assertEquals(counter.getLastUsage(), 0);
		Assert.assertEquals(counter.getMaxTimestamp(), 0);
		Assert.assertEquals(counter.getMinTimestamp(), 0);

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

		Thread.sleep(20); // just to assure system ms are changed
		counter.decrease();
		Assert.assertEquals(counter.getCounter(), 0);
		Assert.assertEquals(counter.getIncrementSum(), 1);
		Assert.assertEquals(counter.getDecrementSum(), 1);
		Assert.assertEquals(counter.getMax(), 1);
		Assert.assertEquals(counter.getMin(), 0);
		Assert.assertTrue(counter.getMaxTimestamp() != counter.getLastUsage());
		Assert.assertTrue(counter.getMaxTimestamp() != counter.getMinTimestamp());
		Assert.assertEquals(counter.getMinTimestamp(), counter.getLastUsage());
	}

	@Test
	public void arbitraryStartValue() {
		Counter counter = SimonManager.getCounter(COUNTER_NAME);
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
	}
}
