package org.javasimon.utils;

import org.javasimon.Counter;
import org.javasimon.Simon;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * OtherTestNG.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class SimonUtilsTest {
	@DataProvider(name = "test-data-utils")
	public Object[][] testDataUtilsProvider() {
		return new Object[][] {
			{1, "1 ns"},
			{47, "47 ns"},
			{999, "999 ns"},
			{1047, "1.05 us"},
			{11047, "11.0 us"},
			{11000, "11.0 us"},
			{141047, "141 us"},
			{942141047, "942 ms"},
			{942141047666L, "942 s"},
			{10942141047666L, "10942 s"},
		};
	}

	@Test(dataProvider = "test-data-utils")
	public void testUtilPresentNanoTime(long ns, String nsString) throws InterruptedException {
		Assert.assertEquals(SimonUtils.presentNanoTime(ns), nsString);
	}

	@Test
	public void testStringCompact() {
		Assert.assertEquals(SimonUtils.compact("asdfadfasd;a 345tw", 4), "as...w");
		Assert.assertEquals(SimonUtils.compact("asdfadfasd;a 345tw", 7), "asd...w");
		Assert.assertEquals(SimonUtils.compact("asdfadfasd;a 345tw", 12), "asdfad...5tw");
		Assert.assertEquals(SimonUtils.compact("asdfadfasd;a 345tw", 17), "asdfadfa... 345tw");
		Assert.assertEquals(SimonUtils.compact("asdfadfasd;a 345tw", 18), "asdfadfasd;a 345tw");
		Assert.assertEquals(SimonUtils.compact("asdfadfasd;a 345tw", 30), "asdfadfasd;a 345tw");
	}

    @Test
    public void testAggregateSingleStopwatch() {
        StopwatchSample sample = new StopwatchSample();
        sample.setTotal(1);

        Stopwatch stopwatch = createMockStopwatch(sample);

        StopwatchAggregate aggregate = SimonUtils.calculateAggregate(stopwatch);
        Assert.assertEquals(aggregate.getTotal(), 1);
    }

    @Test
    public void testAggregateOnStopwatchHierarchy() {
        StopwatchSample sample = new StopwatchSample();
        sample.setTotal(1);

        Stopwatch root = createMockStopwatch(sample);
        Stopwatch child1 = createMockStopwatch(sample);
        Stopwatch child2 = createMockStopwatch(sample);
        Stopwatch child11 = createMockStopwatch(sample);

        Counter counter = createMockCounter();

        when(root.getChildren()).thenReturn(Arrays.asList(child1, child2, counter));
        when(child1.getChildren()).thenReturn(Arrays.asList(child11, counter));

        StopwatchAggregate aggregate = SimonUtils.calculateAggregate(root);
        Assert.assertEquals(aggregate.getTotal(), 4);
    }

    private Counter createMockCounter() {
        Counter counter = mock(Counter.class);
        when(counter.getChildren()).thenReturn(Collections.<Simon>emptyList());

        return counter;
    }

    private Stopwatch createMockStopwatch(StopwatchSample sample) {
        Stopwatch stopwatch = mock(Stopwatch.class);
        when(stopwatch.sample()).thenReturn(sample);
        when(stopwatch.getChildren()).thenReturn(Collections.<Simon>emptyList());

        return stopwatch;
    }

}
