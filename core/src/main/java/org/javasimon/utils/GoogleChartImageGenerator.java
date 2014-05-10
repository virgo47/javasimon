package org.javasimon.utils;

import org.javasimon.StopwatchSample;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Produces URLs for Google Chart Image API - column type.
 * http://code.google.com/apis/chart/image/
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class GoogleChartImageGenerator {

	private static final int FIXED_WIDTH = 100;
	private static final int BAR_WIDTH = 80;
	private static final int BAR_SPACING = 40;
	private static final int BAR_SPACING_MAX_MIN = 25;
	private static final int IMAGE_HEIGHT = 320;

	private static final String URL_START = "http://chart.apis.google.com/chart?chs=";
	private static final String TYPE_BAR1 = "&cht=bvg&chbh=a,3,";
	private static final String TYPE_BAR2 = "&chco=2d69f9,a6c9fd,d0eeff&chxt=y,x,x";
	private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));

	private static final List<Replacer> REPLACERS = new LinkedList<>();

	private static final int TEN_BASE = 10;
	private StopwatchSample[] samples;
	private String title;
	private double divisor;
	private String unit;
	private boolean showMaxMin;

	private double max = 0;
	private boolean first = true;

	static {
		REPLACERS.add(new Replacer("\\+", "%2b"));
		REPLACERS.add(new Replacer(" ", "+"));
		REPLACERS.add(new Replacer("&", "%26"));
	}

	private GoogleChartImageGenerator(StopwatchSample[] samples, String title, double divisor, String unit, boolean showMaxMin) {
		this.samples = samples;
		this.title = title;
		this.divisor = divisor;
		this.unit = unit;
		this.showMaxMin = showMaxMin;
	}

	/**
	 * Generates Google bar chart URL for the provided samples.
	 *
	 * @param samples stopwatch samples
	 * @param title chart title
	 * @param divisor value divisor. For example: if values are in ns and ms are required,
	 * divisor should be set to 1000000.
	 * @param unit unit shown after values under every bar
	 * @param showMaxMin true if additional datasets for max and min values should be shown
	 * @return URL generating the bar chart
	 */
	public static String barChart(StopwatchSample[] samples, String title, double divisor, String unit, boolean showMaxMin) {
		return new GoogleChartImageGenerator(samples, title, divisor, unit, showMaxMin).process();
	}

	private String process() {
		final StringBuilder result = new StringBuilder(URL_START);
		int setSpacing = BAR_SPACING;
		if (showMaxMin) {
			setSpacing = BAR_SPACING_MAX_MIN;
		}
		result.append(FIXED_WIDTH + BAR_WIDTH * samples.length).append('x').append(IMAGE_HEIGHT).append(TYPE_BAR1)
			.append(setSpacing).append(TYPE_BAR2);
		if (showMaxMin) {
			result.append(",x,x");
		}
		result.append("&chtt=").append(encode(title));
		// 0: is Y axis calculated later
		final StringBuilder simonNamesAxis = new StringBuilder("|1:");
		final StringBuilder meanValuesAxis = new StringBuilder("|2:");
		// following axis values are optional
		final StringBuilder maxValuesAxis = new StringBuilder("|3:");
		final StringBuilder minValuesAxis = new StringBuilder("|4:");
		final StringBuilder meanData = new StringBuilder("&chd=t:");
		final StringBuilder maxData = new StringBuilder("|");
		final StringBuilder minData = new StringBuilder("|");
		for (StopwatchSample sample : samples) {
			if (first) {
				first = false;
			} else {
				meanData.append(',');
				maxData.append(',');
				minData.append(',');
			}
			simonNamesAxis.append('|').append(encode(sample.getName()));
			meanData.append(addValueToAxis(meanValuesAxis, sample.getMean()));
			if (showMaxMin) {
				maxData.append(addValueToAxis(maxValuesAxis, sample.getMax()));
				minData.append(addValueToAxis(minValuesAxis, sample.getMin()));
			}
		}
		double division = Math.pow(TEN_BASE, Math.floor(Math.log10(max)));
		StringBuilder yAxis = new StringBuilder("&chxl=0:");
		double x = 0;
		for (; x < max + division; x += division) {
			yAxis.append('|').append(Double.valueOf(x).longValue());
		}
		result.append("&chxr=2,0,").append(Double.valueOf(x - division).longValue());
		result.append("&chds=0,").append(Double.valueOf(x - division).longValue());
		result.append(yAxis).append(simonNamesAxis).append(meanValuesAxis);
		if (showMaxMin) {
			result.append(maxValuesAxis).append(minValuesAxis);
		}
		result.append(meanData);
		if (showMaxMin) {
			result.append(maxData).append(minData);
		}
		result.append("&chdl=avg").append(showMaxMin ? "|max|min" : "").append("&.png");
		return result.toString();
	}

	private String addValueToAxis(StringBuilder axis, double value) {
		value = value / divisor;
		if (value > max) {
			max = value;
		}

		String formattedValue = NUMBER_FORMAT.format(value);
		axis.append('|').append(formattedValue).append("+").append(unit);
		return formattedValue;
	}

	private static String encode(String s) {
		for (final Replacer replacer : REPLACERS) {
			s = replacer.process(s);
		}
		return s;
	}
}
