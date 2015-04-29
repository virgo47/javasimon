package org.javasimon.javaee.reqreporter;

import java.util.ArrayList;
import java.util.List;

import org.javasimon.Split;
import org.javasimon.Stopwatch;

/**
 * Contains cummulated information about about single {@link Stopwatch} with all its reported {@link Split}s.
 * Naturally comparable by total time descending.
 */
public class ReporterStopwatchInfo implements Comparable<ReporterStopwatchInfo> {
	Stopwatch stopwatch;
	List<Split> splits = new ArrayList<>();
	Split maxSplit;
	long total;

	ReporterStopwatchInfo(Stopwatch stopwatch) {
		this.stopwatch = stopwatch;
	}

	@Override
	public int compareTo(ReporterStopwatchInfo o) {
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
