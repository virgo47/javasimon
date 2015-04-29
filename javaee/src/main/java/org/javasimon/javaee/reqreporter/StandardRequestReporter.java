package org.javasimon.javaee.reqreporter;

/**
 * Reports request to standard output (instead of Manager's message method) and omits less specific Simons from
 * output (for instance JDBC's next).
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
@SuppressWarnings("UnusedDeclaration")
public class StandardRequestReporter extends DefaultRequestReporter {
	@Override
	protected void reportMessage(String message) {
		System.out.println(message);
	}

	@Override
	protected boolean shouldBeAddedStopwatchInfo(ReporterStopwatchInfo info) {
		return !(isJdbcResultSetNextSimon(info));
	}

	private boolean isJdbcResultSetNextSimon(ReporterStopwatchInfo info) {
		return info.stopwatch.getName().contains(".sql.") && info.stopwatch.getName().endsWith(".next");
	}
}
