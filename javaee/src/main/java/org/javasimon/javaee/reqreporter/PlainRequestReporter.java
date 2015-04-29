package org.javasimon.javaee.reqreporter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.javasimon.Split;
import org.javasimon.javaee.SimonServletFilter;
import org.javasimon.utils.SimonUtils;

/**
 * Reports the request with split lenght and list of all splits.
 * Report is sent through {@link org.javasimon.Manager#message(String)}. Following aspects of the class can be overridden:
 * <ul>
 * <li>Where the report goes - override {@link #reportMessage(String)},</li>
 * <li>whether split should be included - override {@link #shouldBeAddedSplit(org.javasimon.Split)}.</li>
 * </ul>
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
@SuppressWarnings("UnusedDeclaration")
public class PlainRequestReporter implements RequestReporter {
	private SimonServletFilter simonServletFilter;

	public PlainRequestReporter() {
	}

	@Override
	public void reportRequest(HttpServletRequest request, Split requestSplit, List<Split> splits) {
		StringBuilder messageBuilder = new StringBuilder(
			"Web request is too long (" + SimonUtils.presentNanoTime(requestSplit.runningFor()) +
				") [" + requestSplit.getStopwatch().getNote() + "]");

		for (Split split : splits) {
			if (shouldBeAddedSplit(split)) {
				messageBuilder.append("\n\t").append(split.getStopwatch().getName()).append(": ").
					append(SimonUtils.presentNanoTime(split.runningFor()));
			}
		}

		reportMessage(messageBuilder.toString());
	}

	/**
	 * Reports the prepared message through the method {@link org.javasimon.Manager#message(String)} - can be overridden
	 * to emit the message to log/console/etc.
	 *
	 * @param message prepared message with report
	 */
	protected void reportMessage(String message) {
		simonServletFilter.getManager().message(message);
	}

	/**
	 * Decides whether split should be included in the report - by default all are included.
	 *
	 * @param split tested Split
	 * @return true, if the split should be reported in the list
	 */
	@SuppressWarnings("UnusedParameters")
	protected boolean shouldBeAddedSplit(Split split) {
		return true;
	}

	@Override
	public void setSimonServletFilter(SimonServletFilter simonServletFilter) {
		this.simonServletFilter = simonServletFilter;
	}
}
