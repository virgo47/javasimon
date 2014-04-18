package org.javasimon.aggregation;

import org.javasimon.jmx.StopwatchSample;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ReplaceSamplesAggregatorTest {

	private ReplaceSamplesAggregator replaceSamplesAggregator;

	private static final String MANAGER_ID = "managerId";

	@BeforeMethod
	public void beforeMethod() {
		replaceSamplesAggregator = new ReplaceSamplesAggregator();
	}

	@Test
	public void testGetServerIdsFromNewAggregator() {
		Set<String> ids = replaceSamplesAggregator.getServerIds();
		Assert.assertTrue(ids.isEmpty());
	}

	@Test
	public void testGetServerIdsAfterStopwatchSampleWasAdded() {
		StopwatchSample sample = createStopwatchSample("s1", 1);
		replaceSamplesAggregator.addStopwatchSamples(MANAGER_ID, Arrays.asList(sample));

		Set<String> ids = replaceSamplesAggregator.getServerIds();
		Assert.assertEquals(ids.size(), 1);
		Assert.assertTrue(ids.contains(MANAGER_ID));
	}

	@Test
	public void testAddSingleStopwatchSample() {
		StopwatchSample sample = createStopwatchSample("s1", 1);
		replaceSamplesAggregator.addStopwatchSamples(MANAGER_ID, Arrays.asList(sample));
		List<StopwatchSample> actualSamples = replaceSamplesAggregator.getSamples (MANAGER_ID);
		Assert.assertEquals(actualSamples.size(), 1);
		Assert.assertTrue(actualSamples.contains(sample));
	}

	@Test
	public void testAddSamplesForMultipleManagers() {
		String manager2 = "manager2";

		StopwatchSample sample1 = createStopwatchSample("s1", 1);
		StopwatchSample sample2 = createStopwatchSample("s1", 2);

		replaceSamplesAggregator.addStopwatchSamples(MANAGER_ID, Arrays.asList(sample1));
		replaceSamplesAggregator.addStopwatchSamples(manager2, Arrays.asList(sample2));

		List<StopwatchSample> actualSamples = replaceSamplesAggregator.getSamples (MANAGER_ID);
		Assert.assertEquals(actualSamples.size(), 1);
		Assert.assertTrue(actualSamples.contains(sample1));
	}

	@Test
	public void testReplaceOldSample() {
		StopwatchSample s11 = createStopwatchSample("s1", 1);
		StopwatchSample s12 = createStopwatchSample("s2", 2);

		StopwatchSample s21 = createStopwatchSample("s1", 3);
		StopwatchSample s22 = createStopwatchSample("s2", 4);

		replaceSamplesAggregator.addStopwatchSamples(MANAGER_ID, Arrays.asList(s11, s12));
		replaceSamplesAggregator.addStopwatchSamples(MANAGER_ID, Arrays.asList(s21, s22));

		List<StopwatchSample> actualSamples = replaceSamplesAggregator.getSamples (MANAGER_ID);
		Assert.assertEquals(actualSamples.size(), 2);
		Assert.assertTrue(actualSamples.contains(s21));
		Assert.assertTrue(actualSamples.contains(s22));
	}

	private StopwatchSample createStopwatchSample(String name, int counter) {
		org.javasimon.StopwatchSample sample = new org.javasimon.StopwatchSample();
		sample.setName(name);
		sample.setCounter(counter);

		return new StopwatchSample(sample);
	}
}
