package org.javasimon.jmx;

import org.javasimon.Counter;
import org.javasimon.CounterSample;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class CounterMXBeanImplTest {

	private CounterMXBeanImpl counterMXBean;
	private Counter counter;

	@BeforeMethod
	public void beforeMethod() {
		counter = mock(Counter.class);
		counterMXBean = new CounterMXBeanImpl(counter);
	}

	@Test
	public void testSampleIncrement() {
		String key = "key";
		org.javasimon.CounterSample sample = new CounterSample();
		sample.setCounter(1);
		when(counter.sampleIncrement(key)).thenReturn(sample);

		CounterSample actualSample = counterMXBean.sampleIncrement(key);
		Assert.assertEquals(actualSample.getCounter(), 1);

		verify(counter).sampleIncrement(key);
	}

	@Test
	public void testStopIncrementSampling() {
		String key = "key";
		when(counter.stopIncrementalSampling(key)).thenReturn(true);
		boolean actual = counterMXBean.stopIncrementalSampling(key);
		Assert.assertTrue(actual);

		verify(counter).stopIncrementalSampling(key);
	}
}
