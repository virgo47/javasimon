package org.javasimon.utils;

import org.javasimon.Sample;
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
public class SampleHtmlGeneratorTestNG {
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
		List<Sample> samples = new ArrayList<Sample>();

		for (int i = 0; i < count; i++) {
			final int index = i + 1;
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

			samples.add(sample);
		}

		return samples.toArray(new Sample[count]);
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	private static String resourceAsString(String resourceName) throws IOException, URISyntaxException {
		File file = new File(SampleHtmlGeneratorTestNG.class.getResource(resourceName).toURI());
		byte[] buffer = new byte[(int) file.length()];
		FileInputStream f = new FileInputStream(file);
		f.read(buffer);
		f.close();
		return new String(buffer, Charset.forName("UTF-8"));
	}
}
