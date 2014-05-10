package org.javasimon.javaee.reqreporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;

import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.javaee.SimonServletFilter;
import org.javasimon.utils.SimonUtils;

/**
 * Reports significant splits (longer than 5% of the request) and list of all used stopwatches with their split counts.
 * Report is sent through {@link org.javasimon.Manager#message(String)}. Following aspects of the class can be overridden:
 * <ul>
 * <li>Where the report goes - override {@link #reportMessage(String)},</li>
 * <li>what is significant split - override {@link #isSignificantSplit(org.javasimon.Split, org.javasimon.Split)},</li>
 * <li>whether stopwatch info (from stopwatch distribution part) should be included -
 * override {@link #shouldBeAddedStopwatchInfo(DefaultRequestReporter.StopwatchInfo)}.</li>
 * </ul>
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public class DefaultRequestReporter implements RequestReporter {
	private static final int NOTE_OUTPUT_MAX_LEN = 80;

	private SimonServletFilter simonServletFilter;

	public DefaultRequestReporter() {
	}

	@Override
	public void reportRequest(HttpServletRequest request, Split requestSplit, List<Split> splits) {
		StringBuilder messageBuilder = new StringBuilder(
			"Web request is too long (" + SimonUtils.presentNanoTime(requestSplit.runningFor()) +
				") [" + requestSplit.getStopwatch().getNote() + "]");

		if (splits.size() > 0) {
			buildSplitDetails(requestSplit, splits, messageBuilder);
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

	private void buildSplitDetails(Split requestSplit, List<Split> splits, StringBuilder messageBuilder) {
		Map<String, StopwatchInfo> stopwatchInfos = new HashMap<>();

		processSplitsAndAddSignificantOnes(requestSplit, splits, messageBuilder, stopwatchInfos);
		addStopwatchSplitDistribution(messageBuilder, stopwatchInfos);
	}

	private void processSplitsAndAddSignificantOnes(Split requestSplit, List<Split> splits, StringBuilder messageBuilder, Map<String, StopwatchInfo> stopwatchInfos) {
		for (Split split : splits) {
			StopwatchInfo stopwatchInfo = stopwatchInfos.get(split.getStopwatch().getName());
			if (stopwatchInfo == null) {
				stopwatchInfo = new StopwatchInfo(split.getStopwatch());
				stopwatchInfos.put(split.getStopwatch().getName(), stopwatchInfo);
			}
			stopwatchInfo.addSplit(split);

			if (isSignificantSplit(split, requestSplit)) {
				messageBuilder.append("\n\t").append(split.getStopwatch().getName()).append(": ").
					append(SimonUtils.presentNanoTime(split.runningFor()));
			}
		}
	}

	/**
	 * Can be overridden to decide whether {@link Split} is considered significant to be reported in the first part of the output.
	 * By default all Splits with time over 5% of total request time are significant. This includes overlapping splits too, so more than
	 * 20 splits can be reported.
	 *
	 * @param split tested Split
	 * @param requestSplit Split for the whole HTTP request
	 * @return true, if tested Split is significant
	 */
	protected boolean isSignificantSplit(Split split, Split requestSplit) {
		return split.runningFor() > (requestSplit.runningFor() / 20); // is more than 5%
	}

	private void addStopwatchSplitDistribution(StringBuilder messageBuilder, Map<String, StopwatchInfo> stopwatchInfos) {
		messageBuilder.append("\nStopwatch/Split count/total/max for this request (sorted by total descending):");
		Set<StopwatchInfo> sortedInfos = new TreeSet<>(stopwatchInfos.values());
		for (StopwatchInfo info : sortedInfos) {
			if (shouldBeAddedStopwatchInfo(info)) {
				addStopwatchInfo(messageBuilder, info);
			}
		}
	}

	/**
	 * Decides whether stopwatch info should be included in the report - by default all are included.
	 *
	 * @param info stopwatch info contains list of all splits, max split and total time of splits for the reported request
	 * @return true, if the stopatch info should be reported
	 */
	@SuppressWarnings("UnusedParameters")
	protected boolean shouldBeAddedStopwatchInfo(StopwatchInfo info) {
		return true;
	}

	private void addStopwatchInfo(StringBuilder messageBuilder, StopwatchInfo info) {
		messageBuilder.append("\n\t").append(info.stopwatch.getName()).append(": ").append(info.splits.size()).
			append("x, total: ").append(SimonUtils.presentNanoTime(info.total)).
			append(", max: ").append(SimonUtils.presentNanoTime(info.maxSplit.runningFor()));
		if (info.stopwatch.getNote() != null) {
			messageBuilder.append(", note: ").append(SimonUtils.compact(info.stopwatch.getNote(), NOTE_OUTPUT_MAX_LEN));
		}
	}

	@Override
	public void setSimonServletFilter(SimonServletFilter simonServletFilter) {
		this.simonServletFilter = simonServletFilter;
	}

	// naturally sorted by total time descending
	protected class StopwatchInfo implements Comparable<StopwatchInfo> {
		Stopwatch stopwatch;
		List<Split> splits = new ArrayList<>();
		Split maxSplit;
		long total;

		StopwatchInfo(Stopwatch stopwatch) {
			this.stopwatch = stopwatch;
		}

		@Override
		public int compareTo(StopwatchInfo o) {
			return total < o.total ? 1 : total == o.total ? 0 : -1;
		}

		public void addSplit(Split split) {
			splits.add(split);
			long runningFor = split.runningFor();
			if (maxSplit == null || runningFor > maxSplit.runningFor()) {
				maxSplit = split;
			}
			total += runningFor;
		}
	}
}
