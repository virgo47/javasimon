package org.javasimon.javaee.reqreporter;

/**
 * Reports request to standard output (instead of Manager's message method) and omits less specific Simons from
 * output (for instance JDBC's next).
 *
 * @author <a href="mailto:richard.richter@posam.sk">Richard "Virgo" Richter</a>
 */
@SuppressWarnings("UnusedDeclaration")
public class StandardRequestReporter extends DefaultRequestReporter {
	@Override
	protected void reportMessage(String message) {
		System.out.println(message);
	}

	@SuppressWarnings("RedundantIfStatement")
	@Override
	protected boolean shouldBeAddedStopwatchInfo(StopwatchInfo info) {
		if (isJdbcResultSetNextSimon(info)) {
			return false;
		}
		return true;
	}

	private boolean isJdbcResultSetNextSimon(StopwatchInfo info) {
		return info.stopwatch.getName().contains(".sql.") && info.stopwatch.getName().endsWith(".next");
	}
}
