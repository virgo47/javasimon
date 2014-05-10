package org.javasimon.callback.calltree;

import static org.javasimon.callback.logging.LogTemplates.toSLF4J;
import static org.javasimon.callback.logging.LogTemplates.whenSplitLongerThanMilliseconds;

import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.callback.logging.LogTemplate;
import org.javasimon.callback.logging.SplitThresholdLogTemplate;

/**
 * Callback which logs the call tree when the main call is bigger than specified threshold.
 * This callback can give good results only if interceptors/filters have been
 * placed a different level of the application (web/business/data tiers for
 * instance).
 * <p/>
 * Call tree looks like this:
 * <pre>
 * org.javasimon.web.Controller.execute 123ms
 * 	org.javasimon.business.FirstService.work 75ms, 75%
 * 		org.javasimon.data.FirstDAO.findAll 50 ms, 82%
 * 		org.javasimon.data.SecondDAO.findByRelation 20ms, 10%, 3
 * 	org.javasimon.business.SecondService.do 10ms, 5%
 * </pre>
 *
 * @author gquintana
 * @see CallTree
 * @since 3.2
 */
public class CallTreeCallback extends CallbackSkeleton {

	/** Call tree of current thread. */
	private final ThreadLocal<CallTree> threadCallTree = new ThreadLocal<>();

	/** Log template used for printing call tree. */
	private LogTemplate<Split> callTreeLogTemplate;

	/** Simon attribute name used to store last significant call tree. */
	public static final String ATTR_NAME_LAST = "lastCallTree";

	/** Duration threshold used to trigger logging and remembering. */
	private Long logThreshold;

	/** Default constructor. */
	public CallTreeCallback() {
		initLogThreshold(500L);
	}

	/**
	 * Constructor with logging duration threshold.
	 *
	 * @param threshold Threshold
	 */
	public CallTreeCallback(long threshold) {
		initLogThreshold(threshold);
	}

	/**
	 * Constructor with log template.
	 *
	 * @param callTreeLogTemplate Log template
	 */
	public CallTreeCallback(LogTemplate<Split> callTreeLogTemplate) {
		this.callTreeLogTemplate = callTreeLogTemplate;
	}

	/** Configures {@link #callTreeLogTemplate} with a {@link SplitThresholdLogTemplate}. */
	private void initLogThreshold(Long threshold) {
		this.logThreshold = threshold;
		final LogTemplate<Split> toLogger = toSLF4J(getClass().getName(), "debug");
		if (threshold == null) {
			callTreeLogTemplate = toLogger;
		} else {
			callTreeLogTemplate = whenSplitLongerThanMilliseconds(toLogger, threshold);
		}
	}

	/** Returns log threshold when {@link #callTreeLogTemplate} is a {@link SplitThresholdLogTemplate}. */
	public Long getLogThreshold() {
		return logThreshold;
	}

	/**
	 * Sets log threshold.
	 * Configure {@link #callTreeLogTemplate} with a {@link SplitThresholdLogTemplate}.
	 */
	public void setLogThreshold(Long logThreshold) {
		initLogThreshold(logThreshold);
	}

	/**
	 * Returns call tree for current thread.
	 *
	 * @return Thread call tree
	 */
	private CallTree getCallTree() {
		return threadCallTree.get();
	}

	/**
	 * Initializes the call tree for current thread.
	 *
	 * @return Created call tree
	 */
	private CallTree initCallTree() {
		final CallTree callTree = new CallTree(logThreshold) {
			@Override
			protected void onRootStopwatchStop(CallTreeNode rootNode, Split split) {
				CallTreeCallback.this.onRootStopwatchStop(this, split);
			}
		};
		threadCallTree.set(callTree);
		return callTree;
	}

	/** Removes call tree for current thread. */
	private void removeCallTree() {
		threadCallTree.remove();
	}

	@Override
	public void onStopwatchStart(Split split) {
		CallTree callTree = getCallTree();
		if (callTree == null) {
			// New tree root
			callTree = initCallTree();
		}
		callTree.onStopwatchStart(split);
	}

	@Override
	public void onStopwatchStop(Split split, StopwatchSample sample) {
		getCallTree().onStopwatchStop(split);
	}

	/**
	 * When stopwatch corresponding to root tree node is stopped, this method is called.
	 * Logs call tree when split is longer than threshold.
	 *
	 * @param callTree call tree to log
	 * @param split stopped split
	 */
	public void onRootStopwatchStop(CallTree callTree, Split split) {
		callTreeLogTemplate.log(split, callTree);
		if (logThreshold != null && split.runningFor() > logThreshold) {
			split.getStopwatch().setAttribute(ATTR_NAME_LAST, callTree);
		}
		removeCallTree();
	}

	/**
	 * Returns last call tree stored in stopwatch attributes.
	 *
	 * @param stopwatch Stopwatch
	 * @return Last call tree or {@code null} if any
	 */
	public static CallTree getLastCallTree(Stopwatch stopwatch) {
		return (CallTree) stopwatch.getAttribute(ATTR_NAME_LAST);
	}
}
