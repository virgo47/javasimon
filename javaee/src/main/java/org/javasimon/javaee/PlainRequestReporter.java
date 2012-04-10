package org.javasimon.javaee;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import org.javasimon.Split;
import org.javasimon.utils.SimonUtils;

/**
 * Reports the request with split lenght and list of all splits.
 * Result is reported through {@link org.javasimon.Manager#message(String)}.
 *
 * @author <a href="mailto:richard.richter@posam.sk">Richard "Virgo" Richter</a>
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
			messageBuilder.append("\n\t").append(split.getStopwatch().getName()).append(": ").
				append(SimonUtils.presentNanoTime(split.runningFor()));
		}
		simonServletFilter.getManager().message(messageBuilder.toString());
	}

	@Override
	public void setSimonServletFilter(SimonServletFilter simonServletFilter) {
		this.simonServletFilter = simonServletFilter;
	}
}
