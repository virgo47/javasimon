package org.javasimon.utils;

import org.javasimon.Counter;
import org.javasimon.Simon;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class SimonUtilsAggregationTests {

	private Stopwatch hierarchyRoot;

	@BeforeTest
	public void beforeTest() {
		StopwatchSample sample = new StopwatchSample();
		sample.setTotal(1);

		hierarchyRoot = createMockStopwatch("root", sample);
		Stopwatch child1 = createMockStopwatch("child1", sample);
		Stopwatch child2 = createMockStopwatch("child2", sample);
		Stopwatch child11 = createMockStopwatch("child11", sample);

		Counter counter = createMockCounter("counter");

		when(hierarchyRoot.getChildren()).thenReturn(Arrays.asList(child1, child2, counter));
		when(child1.getChildren()).thenReturn(Arrays.asList(child11, counter));
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
		StopwatchAggregate aggregate = SimonUtils.calculateStopwatchAggregate(hierarchyRoot);
		Assert.assertEquals(aggregate.getTotal(), 4);
	}

	@Test
	public void testFilteredAggregationWithAcceptAllFilter() {
		SimonFilter acceptAll = new SimonFilter() {
			@Override
			public boolean accept(Simon simon) {
				return true;
			}
		};

		StopwatchAggregate aggregate = SimonUtils.calculateStopwatchAggregate(hierarchyRoot, acceptAll);
		Assert.assertEquals(aggregate.getTotal(), 4);
	}

	@Test
	public void testFilteredAggregationWithRejectAllFilter() {
		SimonFilter rejectAll = new SimonFilter() {
			@Override
			public boolean accept(Simon simon) {
				return false;
			}
		};

		StopwatchAggregate aggregate = SimonUtils.calculateStopwatchAggregate(hierarchyRoot, rejectAll);
		Assert.assertEquals(aggregate.getTotal(), 0);
	}

	@Test
	public void testFilteredAggregationWithFilterThatRejectsRoot() {
		SimonFilter rejectRoot = new SimonFilter() {
			@Override
			public boolean accept(Simon simon) {
				return !simon.getName().equals("root");
			}
		};

		StopwatchAggregate aggregate = SimonUtils.calculateStopwatchAggregate(hierarchyRoot, rejectRoot);
		Assert.assertEquals(aggregate.getTotal(), 0);
	}

	@Test
	public void testFilteredAggregationWithFilterThatSubtree() {
		SimonFilter rejectSubtree = new SimonFilter() {
			@Override
			public boolean accept(Simon simon) {
				return !simon.getName().equals("child1");
			}
		};

		StopwatchAggregate aggregate = SimonUtils.calculateStopwatchAggregate(hierarchyRoot, rejectSubtree);
		Assert.assertEquals(aggregate.getTotal(), 2);
	}

	@Test
	public void testAggregationWithNullFilter() {
		StopwatchAggregate aggregate = SimonUtils.calculateStopwatchAggregate(hierarchyRoot, null);
		Assert.assertEquals(aggregate.getTotal(), 4);
	}

	private Counter createMockCounter(String name) {
		Counter counter = mock(Counter.class);
		when(counter.getChildren()).thenReturn(Collections.<Simon>emptyList());
		when(counter.getName()).thenReturn(name);

		return counter;
	}

	private Stopwatch createMockStopwatch(String name, StopwatchSample sample) {
		Stopwatch stopwatch = mock(Stopwatch.class);
		when(stopwatch.sample()).thenReturn(sample);
		when(stopwatch.getChildren()).thenReturn(Collections.<Simon>emptyList());
		when(stopwatch.getName()).thenReturn(name);

		return stopwatch;
	}
}
