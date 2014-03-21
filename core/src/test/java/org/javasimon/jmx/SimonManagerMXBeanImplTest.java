package org.javasimon.jmx;

import org.javasimon.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Unit test for {@link SimonManagerMXBeanImpl}.
 *
 * @author gerald
 */
public class SimonManagerMXBeanImplTest {

	private static final String COUNTER_PATTERN = "*.counter.aa*";
	private static final String STOPWATCH_PATTERN = "*.stopwatch.aa*";

	private SimonManagerMXBeanImpl managerMXBean;
	private Manager manager;
	private Counter counterA;
	private Counter counterB;
	private Stopwatch stopwatchA;
	private Stopwatch stopwatchB;

	@BeforeMethod
	public void beforeMethod() throws Exception {
		manager = mock(Manager.class);
		managerMXBean = new SimonManagerMXBeanImpl(manager);

		counterA = createCounter("base.counter.aaa", 1);
		counterB = createCounter("base.counter.bbb", 2);

		stopwatchA = createStopwatch("base.stopwatch.aaa", 1);
		stopwatchB = createStopwatch("base.stopwatch.bbb", 2);

		when(manager.getSimons(SimonPattern.createForCounter(null)))
				.thenReturn(Arrays.<Simon>asList(counterA, counterB));
		when(manager.getSimons(SimonPattern.createForStopwatch(null)))
				.thenReturn(Arrays.<Simon>asList(stopwatchA, stopwatchB));
		when(manager.getSimons(SimonPattern.createForCounter(COUNTER_PATTERN)))
				.thenReturn(Arrays.<Simon>asList(counterA));
		when(manager.getSimons(SimonPattern.createForStopwatch(STOPWATCH_PATTERN)))
				.thenReturn(Arrays.<Simon>asList(stopwatchA));
		when(manager.getSimons(null))
				.thenReturn(Arrays.asList(counterA, counterB, stopwatchA, stopwatchB));
	}

	private Counter createCounter(String name, long counterVal) {
		Counter counter = mock(Counter.class);
		when(counter.sample()).thenReturn(counterSample(counterVal));
		when(manager.getCounter(name)).thenReturn(counter);
		when(manager.getSimon(name)).thenReturn(counter);
		return counter;
	}

	private org.javasimon.CounterSample counterSample(long counterVal) {
		org.javasimon.CounterSample counterSample = new org.javasimon.CounterSample();
		counterSample.setCounter(counterVal);
		return counterSample;
	}

	private Stopwatch createStopwatch(String name, long counter) {
		Stopwatch stopwatch = mock(Stopwatch.class);
		when(stopwatch.sample()).thenReturn(stopwatchSample(counter));
		when(manager.getStopwatch(name)).thenReturn(stopwatch);
		when(manager.getSimon(name)).thenReturn(stopwatch);
		return stopwatch;
	}

	private org.javasimon.StopwatchSample stopwatchSample(long counter) {
		org.javasimon.StopwatchSample sample = new org.javasimon.StopwatchSample();
		sample.setCounter(counter);
		return sample;
	}

	/** Test {@link SimonManagerMXBeanImpl#getCounterSamples(java.lang.String)}. */
	@Test
	public void testGetCounterSamples() {
		List<CounterSample> samples = managerMXBean.getCounterSamples();
		assertEquals(samples.size(), 2);

		verify(counterA).sample();
		verify(counterB).sample();
	}

	@Test
	public void testGetCounterSamplesForPattern() {
		List<CounterSample> samples = managerMXBean.getCounterSamples(COUNTER_PATTERN);

		verify(counterA).sample();
		verify(counterB, times(0)).sample();

		assertEquals(samples.size(), 1);
		assertEquals(samples.get(0).getCounter(), 1);
	}

	/** Test {@link SimonManagerMXBeanImpl#getStopwatchSamples(java.lang.String)}. */
	@Test
	public void testGetStopwatchSamples() throws InterruptedException {
		List<StopwatchSample> samples = managerMXBean.getStopwatchSamples();
		assertEquals(samples.size(), 2);

		verify(stopwatchA).sample();
		verify(stopwatchB).sample();
	}

	@Test
	public void testGetStopwatchSamplesForPattern() {
		List<StopwatchSample> samples = managerMXBean.getStopwatchSamples(STOPWATCH_PATTERN);

		verify(stopwatchA).sample();
		verify(stopwatchB, times(0)).sample();

		assertEquals(samples.size(), 1);
		assertEquals(samples.get(0).getCounter(), 1);
	}

	@Test
	public void testGetIncrementNonExistingCounter() {
		CounterSample sample = managerMXBean.getIncrementCounterSample("nonExistingCounter", "key");
		assertNull(sample);
	}

	@Test
	public void testGetIncrementNotCounter() {
		CounterSample sample = managerMXBean.getIncrementCounterSample("base.stopwatch.aaa", "key");
		assertNull(sample);
	}

	@Test
	public void testGetIncrementCounterSampleWithExistingSample() {
		String key = "key";
		when(counterA.sampleIncrement(key)).thenReturn(counterSample(2));
		CounterSample sample = managerMXBean.getIncrementCounterSample("base.counter.aaa", key);
		assertEquals(sample.getCounter(), 2);

		verify(counterA).sampleIncrement(key);
	}

	@Test
	public void testGetIncrementCounterSamplesWithExistingSample() {
		String key = "key";
		when(counterA.sampleIncrement(key)).thenReturn(counterSample(2));
		List<CounterSample> samples = managerMXBean.getIncrementCounterSamples(COUNTER_PATTERN, key);
		assertEquals(samples.size(), 1);
		assertEquals(samples.get(0).getCounter(), 2);

		verify(counterA).sampleIncrement(key);
	}

	@Test
	public void testGetIncrementCounterSamplesWithoutPattern() {
		String key = "key";
		when(counterA.sampleIncrement(key)).thenReturn(counterSample(3));
		when(counterB.sampleIncrement(key)).thenReturn(counterSample(4));
		List<CounterSample> samples = managerMXBean.getIncrementCounterSamples(key);
		assertEquals(samples.size(), 2);
		assertEquals(samples.get(0).getCounter(), 3);
		assertEquals(samples.get(1).getCounter(), 4);

		verify(counterA).sampleIncrement(key);
		verify(counterB).sampleIncrement(key);
	}

	@Test
	public void testGetIncrementStopwatchSampleWithExistingSample() {
		String key = "key";
		when(stopwatchA.sampleIncrement(key)).thenReturn(stopwatchSample(2));
		StopwatchSample sample = managerMXBean.getIncrementStopwatchSample("base.stopwatch.aaa", key);
		assertEquals(sample.getCounter(), 2);

		verify(stopwatchA).sampleIncrement(key);
	}

	@Test
	public void testGetIncrementNonExistingStopwatch() {
		StopwatchSample sample = managerMXBean.getIncrementStopwatchSample("nonExistingStopwatch", "key");
		assertNull(sample);
	}

	@Test
	public void testGetIncrementNotStopwatch() {
		StopwatchSample sample = managerMXBean.getIncrementStopwatchSample("base.counter.aaa", "key");
		assertNull(sample);
	}

	@Test
	public void testGetIncrementStopwatchSamplesWithExistingSample() {
		String key = "key";
		when(stopwatchA.sampleIncrement(key)).thenReturn(stopwatchSample(2));
		List<StopwatchSample> samples = managerMXBean.getIncrementStopwatchSamples(STOPWATCH_PATTERN, key);
		assertEquals(samples.size(), 1);
		assertEquals(samples.get(0).getCounter(), 2);

		verify(stopwatchA).sampleIncrement(key);
	}

	@Test
	public void testGetIncrementStopwatchSamplesWithoutPattern() {
		String key = "key";
		when(stopwatchA.sampleIncrement(key)).thenReturn(stopwatchSample(3));
		when(stopwatchB.sampleIncrement(key)).thenReturn(stopwatchSample(4));
		List<StopwatchSample> samples = managerMXBean.getIncrementStopwatchSamples(key);
		assertEquals(samples.size(), 2);
		assertEquals(samples.get(0).getCounter(), 3);
		assertEquals(samples.get(1).getCounter(), 4);

		verify(stopwatchA).sampleIncrement(key);
		verify(stopwatchB).sampleIncrement(key);
	}
}
