package org.javasimon.utils;

import org.javasimon.Simon;
import org.javasimon.Stopwatch;

import java.util.Set;

/**
 * GoogleChartGenerator is utility class producing URLs for Google Chart API. Charts (URLs) will
 * probably need additional adjustment, but most of the work will be done by the tool.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class GoogleChartGenerator {
	private static final int ONE_BAR_WIDTH = 100;

	private GoogleChartGenerator() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Generates Google bar chart URL for last values of all collected Simons.
	 *
	 * @param collector data collector
	 * @param title chart title
	 * @param divisor value divisor. For example: if values are in ns and ms are required,
	 * divisor should be set to 1000000.
	 * @param unit unit shown after values under every bar
	 * @return URL generating the bar chart
	 */
	public static String barChart(AbstractDataCollector collector, String title, double divisor, String unit) {
		Set<Simon> simons = collector.getSimons();

		final StringBuilder result = new StringBuilder("<html>\n" +
			"  <head>\n" +
			"    <script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>\n" +
			"    <script type=\"text/javascript\">\n" +
			"      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});\n" +
			"      google.setOnLoadCallback(drawChart);\n" +
			"      function drawChart() {\n" +
			"        var data = new google.visualization.DataTable();\n");
		result.append("        data.addColumn('string', 'stopwatch');\n" +
			"        data.addColumn('number', 'avg');\n" +
			"        data.addColumn('number', 'max');\n" +
			"        data.addColumn('number', 'min');\n" +
			"        data.addRows(" + simons.size() + ");\n");

		int rowIndex = 0;
		for (Simon simon : simons) {
			Stopwatch stopwatch = (Stopwatch) simon;

			result.append("        data.setValue(").append(rowIndex).append(", 0, '")
				.append(stopwatch.getNote()).append("');\n")
				.append("        data.setValue(").append(rowIndex).append(", 1, ")
				.append(stopwatch.getMean() / divisor).append(");\n")
				.append("        data.setValue(").append(rowIndex).append(", 2, ")
				.append(stopwatch.getMax() / divisor).append(");\n")
				.append("        data.setValue(").append(rowIndex).append(", 3, ")
				.append(stopwatch.getMin() / divisor).append(");\n");
			rowIndex++;
		}

		result.append("        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));\n" +
			"        chart.draw(data, {width: " + ONE_BAR_WIDTH * (1 + simons.size()) + ", height: 600, title: '" + title + "',\n" +
			"                          hAxis: {title: 'stopwatch', titleTextStyle: {color: 'red'}}\n" +
			// TODO invisible haxis name + is it possible to insert values under axis?
			"                         });\n" +
			"      }\n" +
			"    </script>\n" +
			"  </head>\n" +
			"\n" +
			"  <body>\n" +
			"    <div id=\"chart_div\"></div>\n" +
			"  </body>\n" +
			"</html>");
		return result.toString();
	}
}
