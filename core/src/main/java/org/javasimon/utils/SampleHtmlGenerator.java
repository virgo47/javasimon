package org.javasimon.utils;

import org.javasimon.CounterSample;
import org.javasimon.Sample;
import org.javasimon.StopwatchSample;

import java.util.Arrays;

/**
 * Generates HTML table for the provided {@link org.javasimon.Sample}s. Lines are separated by the default line separator
 * (current value of {@code System.getProperty("line.separator")}).
 */
@SuppressWarnings("JavaDoc")
public class SampleHtmlGenerator {

	private static String lineSeparator = System.getProperty("line.separator");

	private final Iterable<Sample> samples;
	private final StringBuilder builder;

	private SampleHtmlGenerator(Iterable<Sample> samples) {
		this.samples = samples;
		builder = new StringBuilder();
	}

	/**
	 * Generates HTML table for the provided {@link Sample}s.
	 *
	 * @param samples vararg array of {@link Sample}s
	 * @return HTML output for the samples
	 */
	public static String generate(Sample... samples) {
		return new SampleHtmlGenerator(Arrays.asList(samples)).toHtmlTable();
	}

	/**
	 * Generates HTML table for the provided {@link Sample}s.
	 *
	 * @param samples iterable of {@link Sample}s
	 * @return HTML output for the samples
	 */
	public static String generate(Iterable<Sample> samples) {
		return new SampleHtmlGenerator(samples).toHtmlTable();
	}

	/**
	 * Generates HTML table for the provided {@link Sample}s.
	 *
	 * @return HTML output for the samples
	 */
	private String toHtmlTable() {
		buildHeader();
		for (Sample sample : this.samples) {
			buildRowForSample(sample);
		}
		builder.append("</table>");
		return builder.toString();
	}

	private void buildHeader() {
		builder.append("<table class=\"javasimon-samples-table\">").append(lineSeparator);
		indent(1).append("<tr>").append(lineSeparator);
		indent(2).append("<th>Name</th>").append(lineSeparator);
		indent(2).append("<th>Active</th>").append(lineSeparator);
		indent(2).append("<th>Counter</th>").append(lineSeparator);
		indent(2).append("<th>Min</th>").append(lineSeparator);
		indent(2).append("<th>Max</th>").append(lineSeparator);
		indent(2).append("<th>Mean</th>").append(lineSeparator);
		indent(2).append("<th>Total</th>").append(lineSeparator);
		indent(1).append("</tr>").append(lineSeparator);
	}

	private void buildRowForSample(Sample sample) {
		indent(1).append("<tr>").append(lineSeparator);
		indent(2).append("<td>").append(sample.getName()).append("</td>").append(lineSeparator);

		if (sample instanceof StopwatchSample) {
			StopwatchSample stopwatchSample = (StopwatchSample) sample;

			indent(2).append("<td>").append(stopwatchSample.getActive()).append("</td>").append(lineSeparator);
			indent(2).append("<td>").append(stopwatchSample.getCounter()).append("</td>").append(lineSeparator);
			indent(2).append("<td>").append(SimonUtils.presentNanoTime(stopwatchSample.getMin())).append("</td>").append(lineSeparator);
			indent(2).append("<td>").append(SimonUtils.presentNanoTime(stopwatchSample.getMax())).append("</td>").append(lineSeparator);
			indent(2).append("<td>").append(SimonUtils.presentNanoTime(stopwatchSample.getMean())).append("</td>").append(lineSeparator);
			indent(2).append("<td>").append(SimonUtils.presentNanoTime(stopwatchSample.getTotal())).append("</td>").append(lineSeparator);
		} else {
			CounterSample counterSample = (CounterSample) sample;
			indent(2).append("<td>-</td>").append(lineSeparator);
			indent(2).append("<td>").append(counterSample.getCounter()).append("</td>").append(lineSeparator);
			indent(2).append("<td>").append(SimonUtils.presentMinMaxCount(counterSample.getMin())).append("</td>").append(lineSeparator);
			indent(2).append("<td>").append(SimonUtils.presentMinMaxCount(counterSample.getMax())).append("</td>").append(lineSeparator);
			indent(2).append("<td>-</td>").append(lineSeparator);
			indent(2).append("<td>").append("+").append(SimonUtils.presentMinMaxCount(counterSample.getIncrementSum())).
				append("/-").append(SimonUtils.presentMinMaxCount(counterSample.getDecrementSum())).append("</td>").append(lineSeparator);
		}
		indent(1).append("</tr>").append(lineSeparator);
	}

	private StringBuilder indent(int count) {
		for (int i = 0; i < count; i++) {
			builder.append("    ");
		}
		return builder;
	}

	/**
	 * Sets the line separator to desired string. Default value is {@code System.getProperty("line.separator")}.
	 *
	 * @param newLineSeparator new line separator string
	 */
	public static void setLineSeparator(String newLineSeparator) {
		lineSeparator = newLineSeparator;
	}
}
