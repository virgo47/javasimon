package org.javasimon.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.javasimon.Counter;
import org.javasimon.CounterSample;
import org.javasimon.Simon;
import org.javasimon.SimonFilter;
import org.javasimon.SimonUnitTest;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class SimonUtilsAggregationTests extends SimonUnitTest {

	private Stopwatch stopwatchHeirarchy;
	private Counter counterHierarchy;

	private SimonFilter acceptAllFilter = new SimonFilter() {
		@Override
		public boolean accept(Simon simon) {
			return true;
		}
	};

	private SimonFilter rejectAll = new SimonFilter() {
		@Override
		public boolean accept(Simon simon) {
			return false;
		}
	};

	private SimonFilter rejectRoot = new SimonFilter() {
		@Override
		public boolean accept(Simon simon) {
			return !simon.getName().equals("root");
		}
	};

	private SimonFilter rejectSubtree = new SimonFilter() {
		@Override
		public boolean accept(Simon simon) {
			return !simon.getName().equals("child1");
		}
	};

	@BeforeTest
	public void beforeTest() {
		createStopwatchHierarchy();
		createCounterHierarchy();
	}

	private void createStopwatchHierarchy() {
		StopwatchSample sample = new StopwatchSample();
		sample.setTotal(1);

		stopwatchHeirarchy = createMockStopwatch("root", sample);
		Stopwatch child1 = createMockStopwatch("child1", sample);
		Stopwatch child2 = createMockStopwatch("child2", sample);
		Stopwatch child11 = createMockStopwatch("child11", sample);

		Counter counter = createMockCounter("counter", new CounterSample());

		when(stopwatchHeirarchy.getChildren()).thenReturn(Arrays.asList(child1, child2, counter));
		when(child1.getChildren()).thenReturn(Arrays.asList(child11, counter));
	}

	private Counter createMockCounter(String name, CounterSample sample) {
		Counter counter = mock(Counter.class);
		when(counter.getChildren()).thenReturn(Collections.<Simon>emptyList());
		when(counter.getName()).thenReturn(name);
		when(counter.sample()).thenReturn(sample);

		return counter;
	}

	private Stopwatch createMockStopwatch(String name, StopwatchSample sample) {
		Stopwatch stopwatch = mock(Stopwatch.class);
		when(stopwatch.sample()).thenReturn(sample);
		when(stopwatch.getChildren()).thenReturn(Collections.<Simon>emptyList());
		when(stopwatch.getName()).thenReturn(name);

		return stopwatch;
	}

	private void createCounterHierarchy() {
		CounterSample sample = new CounterSample();
		sample.setCounter(1);

		counterHierarchy = createMockCounter("root", sample);
		Counter child1 = createMockCounter("child1", sample);
		Counter child2 = createMockCounter("child2", sample);
		Counter child11 = createMockCounter("child11", sample);

		Stopwatch stopwatch = createMockStopwatch("stopwatch", new StopwatchSample());

		when(counterHierarchy.getChildren()).thenReturn(Arrays.asList(child1, child2, stopwatch));
		when(child1.getChildren()).thenReturn(Arrays.asList(child11, stopwatch));
	}

	@Test
	public void testAggregateSingleStopwatch() {
		StopwatchSample sample = new StopwatchSample();
		sample.setTotal(1);

		Stopwatch stopwatch = createMockStopwatch("stopwatch", sample);

		StopwatchAggregate aggregate = SimonUtils.calculateStopwatchAggregate(stopwatch);
		Assert.assertEquals(aggregate.getTotal(), 1);
	}

	@Test
	public void testAggregateOnStopwatchHierarchy() {
		StopwatchAggregate aggregate = SimonUtils.calculateStopwatchAggregate(stopwatchHeirarchy);
		Assert.assertEquals(aggregate.getTotal(), 4);
	}

	@Test
	public void testFilteredAggregationWithAcceptAllFilter() {
		StopwatchAggregate aggregate = SimonUtils.calculateStopwatchAggregate(stopwatchHeirarchy, acceptAllFilter);
		Assert.assertEquals(aggregate.getTotal(), 4);
	}

	@Test
	public void testFilteredAggregationWithRejectAllFilter() {
		StopwatchAggregate aggregate = SimonUtils.calculateStopwatchAggregate(stopwatchHeirarchy, rejectAll);
		Assert.assertEquals(aggregate.getTotal(), 0);
	}

	@Test
	public void testFilteredAggregationWithFilterThatRejectsRoot() {
		StopwatchAggregate aggregate = SimonUtils.calculateStopwatchAggregate(stopwatchHeirarchy, rejectRoot);
		Assert.assertEquals(aggregate.getTotal(), 0);
	}

	@Test
	public void testFilteredAggregationWithSubtreeFilter() {
		StopwatchAggregate aggregate = SimonUtils.calculateStopwatchAggregate(stopwatchHeirarchy, rejectSubtree);
		Assert.assertEquals(aggregate.getTotal(), 2);
	}

	@Test
	public void testAggregationWithNullFilter() {
		StopwatchAggregate aggregate = SimonUtils.calculateStopwatchAggregate(stopwatchHeirarchy, null);
		Assert.assertEquals(aggregate.getTotal(), 4);
	}

	@Test
	public void testAggregateWithSingleCounter() {
		CounterSample sample = new CounterSample();
		sample.setCounter(1);

		Counter counter = createMockCounter("counter", sample);
		CounterAggregate aggregate = SimonUtils.calculateCounterAggregate(counter);
		Assert.assertEquals(aggregate.getCounter(), 1);
	}

	@Test
	public void testAggregateWithCountersHierarchy() {
		CounterAggregate aggregate = SimonUtils.calculateCounterAggregate(counterHierarchy);
		Assert.assertEquals(aggregate.getCounter(), 4);
	}

	@Test
	public void testFilteredCounterAggregateWithSubtreeFilter() {
		CounterAggregate aggregate = SimonUtils.calculateCounterAggregate(counterHierarchy, rejectSubtree);
		Assert.assertEquals(aggregate.getCounter(), 2);
	}

	@Test
	public void testFilteredCounterAggregateWithRejectAllFilter() {
		CounterAggregate aggregate = SimonUtils.calculateCounterAggregate(counterHierarchy, rejectAll);
		Assert.assertEquals(aggregate.getCounter(), 0);
	}

	@Test
	public void testFilteredCounterAggregateWithRejectRoot() {
		CounterAggregate aggregate = SimonUtils.calculateCounterAggregate(counterHierarchy, rejectRoot);
		Assert.assertEquals(aggregate.getCounter(), 0);
	}
}
