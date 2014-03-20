package org.javasimon.jmx;

import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class StopwatchMXBeanImplTest {
	private StopwatchMXBeanImpl stopwatchMXBean;
	private Stopwatch stopwatch;

	@BeforeMethod
	public void beforeMethod() {
		stopwatch = mock(Stopwatch.class);
		stopwatchMXBean = new StopwatchMXBeanImpl(stopwatch);
	}

	@Test
	public void testSampleIncrement() {
		String key = "key";
		StopwatchSample sample = new StopwatchSample();
		sample.setCounter(1);
		when(stopwatch.sampleIncrement(key)).thenReturn(sample);

		StopwatchSample actualSample = stopwatchMXBean.sampleIncrement(key);
		Assert.assertEquals(actualSample.getCounter(), 1);

		verify(stopwatch).sampleIncrement(key);
	}

	@Test
	public void testStopIncrementSampling() {
		String key = "key";
		when(stopwatch.stopIncrementalSampling(key)).thenReturn(true);
		boolean actual = stopwatchMXBean.stopIncrementalSampling(key);
		Assert.assertTrue(actual);

		verify(stopwatch).stopIncrementalSampling(key);
	}
}
