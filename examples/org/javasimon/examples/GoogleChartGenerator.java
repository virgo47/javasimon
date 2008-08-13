package org.javasimon.examples;

import org.javasimon.Simon;

import java.util.List;
import java.text.DecimalFormat;

/**
 * GoogleChartGenerator.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 13, 2008
 */
public final class GoogleChartGenerator {
	private static final String URL_START = "http://chart.apis.google.com/chart?chs=400x300";
	private static final String TYPE_BAR = "&cht=bvg&chbh=32,10,60&chco=4d89f9,c6d9fd&chxt=x,x,y";
	private static final DecimalFormat nf = new DecimalFormat("0.00");

	public static String barChart(DataCollector collector, String title, double divisor, String unit) {
		StringBuilder sb = new StringBuilder(URL_START).append(TYPE_BAR);
		sb.append("&chtt=").append(encode(title));
		StringBuilder x0 = new StringBuilder("&chxl=0:");
		StringBuilder x1 = new StringBuilder("|1:");
		StringBuilder data = new StringBuilder("&chd=t:");
		double max = 0;
		boolean first = true;
		for (Simon simon : collector.getSimons()) {
			if (first) {
				first = false;
			} else {
				data.append(',');
			}
			List<Double> values = collector.valuesFor(simon);
			double lastValue = values.get(values.size() - 1) / divisor;
			x0.append('|').append(encode(simon.getName()));
			String formattedValue = nf.format(lastValue);
			x1.append('|').append(formattedValue).append("+").append(unit);
			if (lastValue > max) {
				max = lastValue;
			}
			data.append(formattedValue);
		}
		double division = Math.pow(10, Math.floor(Math.log10(max)));
		StringBuilder x2 = new StringBuilder("|2:");
		double x = 0;
		for (; x < max + division; x += division) {
			x2.append('|').append(Double.valueOf(x).longValue());
		}
		sb.append("&chxr=2,0,").append(Double.valueOf(x - division).longValue());
		sb.append("&chds=0,").append(Double.valueOf(x - division).longValue());
		sb.append(x0).append(x1).append(x2).append(data);
		sb.append("&.png");
		return sb.toString();
	}

	private static String encode(String title) {
		title = title.replaceAll("\\+", "%2b");
		title = title.replaceAll(" ", "+");
		title = title.replaceAll("&", "%26");
		return title;
	}
}
