package org.javasimon.utils;

import org.javasimon.CounterSample;
import org.javasimon.Sample;
import org.javasimon.SimonUnitTest;
import org.javasimon.StopwatchSample;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test of {@link SampleHtmlGenerator}.
 */
public class SampleHtmlGeneratorTest extends SimonUnitTest {

	@Test
	public void testSamplesToHtmlTable() throws Exception {
		SampleHtmlGenerator.setLineSeparator("\r\n"); // in case it runs on Unix/Linux
		assertCountSampleToHtmlTable(0);
		assertCountSampleToHtmlTable(1);
		assertCountSampleToHtmlTable(3);
	}

	private void assertCountSampleToHtmlTable(int count) throws IOException, URISyntaxException {
		String resourceName = this.getClass().getSimpleName() + "-" + count + "sample.html";
		String expected = resourceAsString(resourceName);

		String tableHtml = SampleHtmlGenerator.generate(makeStopwatchSamples(count));
		Assert.assertEquals(tableHtml, expected);
	}

	private static Sample[] makeStopwatchSamples(int count) {
		List<Sample> samples = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			createSample(samples, i);
		}

		return samples.toArray(new Sample[count]);
	}

	private static void createSample(List<Sample> samples, int i) {
		final int index = i + 1;
		Sample sample;
		if (index % 2 == 0) {
			sample = createCounterSample(index);
		} else {
			sample = createStopwatchSample(index);
		}

		samples.add(sample);
	}

	private static Sample createCounterSample(int index) {
		CounterSample sample = new CounterSample();
		sample.setName("counter" + index);
		sample.setCounter(555);
		sample.setMax(600);
		sample.setIncrementSum(1000);
		sample.setDecrementSum(13);
		sample.setMin(Long.MIN_VALUE);
		return sample;
	}

	private static Sample createStopwatchSample(int index) {
		StopwatchSample sample = new StopwatchSample();
		sample.setName("sample" + index);
		sample.setActive(index);
		sample.setCounter(2 * index);
		sample.setMin(5000 * index);
		sample.setMinTimestamp(5000 * index);
		sample.setMax(15000 * index);
		sample.setMaxTimestamp(15000 * index);
		sample.setMean(10000 * index);
		sample.setTotal(20000 * index);
		return sample;
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	private static String resourceAsString(String resourceName) throws IOException, URISyntaxException {
		File file = new File(SampleHtmlGeneratorTest.class.getResource(resourceName).toURI());
		byte[] buffer = new byte[(int) file.length()];
		FileInputStream f = new FileInputStream(file);
		f.read(buffer);
		f.close();
		return new String(buffer, Charset.forName("UTF-8"));
	}
}
