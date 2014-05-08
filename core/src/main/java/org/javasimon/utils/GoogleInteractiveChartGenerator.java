package org.javasimon.utils;

import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;

/**
 * Generates HTML page that generates JavaScript interactive graph based on Google Charts.
 * http://code.google.com/apis/chart/
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class GoogleInteractiveChartGenerator {
	private static final int ONE_BAR_WIDTH = 100;

	private GoogleInteractiveChartGenerator() {
		throw new AssertionError();
	}

	/**
	 * Generates Google bar chart HTML5 source code for the provided samples.
	 *
	 * @param samples stopwatch samples
	 * @param title chart title
	 * @param divisor value divisor. For example: if values are in ns and ms are required,
	 * divisor should be set to 1000000.
	 * @param unit unit shown after values under every bar - TODO currently not used
	 * @return HTML5 source code displaying the chart
	 */
	public static String barChart(StopwatchSample[] samples, String title, double divisor, String unit) {
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
			"        data.addRows(").append(samples.length).append(");\n");

		int rowIndex = 0;
		for (StopwatchSample sample : samples) {
			Stopwatch stopwatch = (Stopwatch) sample;

			result.append("        data.setValue(").append(rowIndex).append(", 0, '")
				.append(stopwatch.getName()).append("');\n")
				.append("        data.setValue(").append(rowIndex).append(", 1, ")
				.append(stopwatch.getMean() / divisor).append(");\n")
				.append("        data.setValue(").append(rowIndex).append(", 2, ")
				.append(stopwatch.getMax() / divisor).append(");\n")
				.append("        data.setValue(").append(rowIndex).append(", 3, ")
				.append(stopwatch.getMin() / divisor).append(");\n");
			rowIndex++;
		}

		result.append("        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));\n" +
			"        chart.draw(data, {width: ").
			append(ONE_BAR_WIDTH * (1 + samples.length)).
			append(", height: 600, title: '").
			append(title).
			append("',\n                          hAxis: {title: 'stopwatch', titleTextStyle: {color: 'red'}}\n" +
				"                         });\n      }\n" +
				"    </script>\n" + "  </head>\n\n  <body>\n    <div id=\"chart_div\"></div>\n  </body>\n</html>");
		return result.toString();
	}
}
