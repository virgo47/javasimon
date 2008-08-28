package org.javasimon.utils;

import org.javasimon.Simon;

import java.util.List;
import java.util.LinkedList;
import java.util.Locale;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * GoogleChartGenerator is utility class producing URLs for Google Chart API. Charts (URLs) will
 * probably need additional adjustment, but most of the work will be done by the tool.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 13, 2008
 */
public final class GoogleChartGenerator {
	private static final String URL_START = "http://chart.apis.google.com/chart?chs=600x300";
	private static final String TYPE_BAR = "&cht=bvg&chbh=32,10,60&chco=4d89f9,c6d9fd&chxt=x,x,y";
	private static final DecimalFormat nf = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.US));

    private static final List<Replacer> REPLACERS = new LinkedList<Replacer>();

    static {
        REPLACERS.add(new Replacer("\\+", "%2b"));
        REPLACERS.add(new Replacer(" ", "+"));
        REPLACERS.add(new Replacer("&", "%26"));
    }

	private GoogleChartGenerator() {
		throw new UnsupportedOperationException();
	}

	public static String barChart(DataCollector collector, String title, double divisor, String unit) {
		final StringBuilder result = new StringBuilder(URL_START).append(TYPE_BAR);
		result.append("&chtt=").append(encode(title));
		final StringBuilder x0 = new StringBuilder("&chxl=0:");
		final StringBuilder x1 = new StringBuilder("|1:");
		final StringBuilder data = new StringBuilder("&chd=t:");
		double max = 0;
		boolean first = true;
		for (final Simon simon : collector.getSimons()) {
			if (first) {
				first = false;
			} else {
				data.append(',');
			}
			final List<Double> values = collector.valuesFor(simon);
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
		result.append("&chxr=2,0,").append(Double.valueOf(x - division).longValue());
		result.append("&chds=0,").append(Double.valueOf(x - division).longValue());
		result.append(x0).append(x1).append(x2).append(data);
		result.append("&.png");
		return result.toString();
	}

	private static String encode(String s) {
        for (final Replacer replacer : REPLACERS) {
            s = replacer.process(s);
        }
		return s;
	}
}
