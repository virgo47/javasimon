package org.javasimon.javaee;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;

/**
 * Reports significant splits (longer than 5% of the request) and list of all used stopwatches with their split counts.
 * Result is reported through {@link org.javasimon.Manager#message(String)}.
 *
 * @author <a href="mailto:richard.richter@posam.sk">Richard "Virgo" Richter</a>
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
			displaySplitDetails(requestSplit, splits, messageBuilder);
		}

		simonServletFilter.getManager().message(messageBuilder.toString());
	}

	private void displaySplitDetails(Split requestSplit, List<Split> splits, StringBuilder messageBuilder) {
		Map<String, StopwatchInfo> stopwatchInfos = new HashMap<String, StopwatchInfo>();

		processSplitsAndDisplaySignificantOnes(requestSplit, splits, messageBuilder, stopwatchInfos);
		displayStopwatchSplitDistribution(messageBuilder, stopwatchInfos);
	}

	private void displayStopwatchSplitDistribution(StringBuilder messageBuilder, Map<String, StopwatchInfo> stopwatchInfos) {
		messageBuilder.append("\nStopwatch/Split count/total/max for this request (sorted by total descending):");
		Set<StopwatchInfo> sortedInfos = new TreeSet<StopwatchInfo>(stopwatchInfos.values());
		for (StopwatchInfo info : sortedInfos) {
			messageBuilder.append("\n\t").append(info.stopwatch.getName()).append(": ").append(info.splits.size()).
				append("x, total: ").append(SimonUtils.presentNanoTime(info.total)).
				append(", max: ").append(SimonUtils.presentNanoTime(info.maxSplit.runningFor()));
			if (info.stopwatch.getNote() != null) {
				messageBuilder.append(", note: ").append(SimonUtils.compact(info.stopwatch.getNote(), NOTE_OUTPUT_MAX_LEN));
			}
		}
	}

	private void processSplitsAndDisplaySignificantOnes(Split requestSplit, List<Split> splits, StringBuilder messageBuilder, Map<String, StopwatchInfo> stopwatchInfos) {
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

	@Override
	public void setSimonServletFilter(SimonServletFilter simonServletFilter) {
		this.simonServletFilter = simonServletFilter;
	}

	private boolean isSignificantSplit(Split split, Split requestSplit) {
		return split.runningFor() > (requestSplit.runningFor() / 20); // is more than 5%
	}

	// naturally sorted by total time descending
	class StopwatchInfo implements Comparable<StopwatchInfo> {
		Stopwatch stopwatch;
		List<Split> splits = new ArrayList<Split>();
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
