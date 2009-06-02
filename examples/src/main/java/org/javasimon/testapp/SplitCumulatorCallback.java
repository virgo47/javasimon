package org.javasimon.testapp;

import org.javasimon.CallbackSkeleton;
import org.javasimon.Split;

import java.util.List;
import java.util.ArrayList;

/**
 * This callback accumulates some selected splits when they are stopped for further analysis.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Jun 2, 2009
 */
public final class SplitCumulatorCallback extends CallbackSkeleton {
	public static final int MAX_SPLITS = 100;

	private final ThreadLocal<List<Split>> splits = new ThreadLocal<List<Split>>();

	private String controller;

	/**
	 * Initializes thread local list for splits which instructs the callback to accumulate splits
	 * for this thread.
	 */
	public void orderSplits() {
		splits.set(new ArrayList<Split>());
	}

	/**
	 * Returns list of splits and removes the thread local list which effectively ends the accumulation
	 * process.
	 *
	 * @return accumulated split since the previous {@link #orderSplits}
	 */
	public List<Split> getSplits() {
		List<Split> splitList = splits.get();
		splits.remove();
		return splitList;
	}

	/**
	 * If controller is set this method "orders splits".
	 *
	 * @param split used to check the name
	 */
	public void stopwatchStart(Split split) {
		if (controller != null && split.getStopwatch().getName().equals(controller)) {
			orderSplits();
		}
	}

	/**
	 * Adds the split into the thread local list - if the list was initialized ({@link #orderSplits()}.
	 * If split of the controller Stopwatch is stopped results are printed (without that split).
	 *
	 * @param split stopped split that is going to be accumulated
	 */
	public void stopwatchStop(Split split) {
		List<Split> splitList = splits.get();
		if (controller != null && split.getStopwatch().getName().equals(controller)) {
			System.out.println("Splits: " + getSplits());
		} else if (splitList != null) {
			splitList.add(split);
		}
	}

	/**
	 * Sets the name of the controlling Stopwatch that automaticaly triggers orderSplits.
	 *
	 * @param controller name of the controlling Stopwatch
	 */
	public void setController(String controller) {
		this.controller = controller;
	}
}
