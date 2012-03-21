package org.javasimon.callback.calltree;

import org.javasimon.Split;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.callback.logging.LogTemplate;
import static org.javasimon.callback.logging.LogTemplates.*;
import org.javasimon.callback.logging.SplitThresholdLogTemplate;

/**
 * Callback which logs a call tree when a the main call is bigger than a
 * threshold.
 * This callback can give good results only if interceptors/filters have been
 * placed a different level of the application (web/business/data tiers for 
 * instance).
 *
 * Call tree looks like this:
 * <pre>
 * org.javasimon.web.Controller.execute 123ms
 * 	org.javasimon.business.FirstService.work 75ms, 75%
 * 		org.javasimon.data.FirstDAO.findAll 50 ms, 82%
 * 		org.javasimon.data.SecondDAO.findByRelation 20ms, 10%, 3
 * 	org.javasimon.business.SecodeService.do 10ms, 5%
 * </pre>
 *
 * See {@see CallTree} for more information.
 * @author gquintana
 * @since 3.2.0
 */
public class CallTreeCallback extends CallbackSkeleton {

	/**
	 * Call tree of current thread
	 */
	private final ThreadLocal<CallTree> threadCallTree = new ThreadLocal<CallTree>();
	/**
	 * Log template used for printing call tree
	 */
	private LogTemplate<Split> callTreeLogTemplate;

	/**
	 * Default constructor
	 */
	public CallTreeCallback() {
		initLogThreshold(500L);
	}
	/**
	 * Constructor with logging duration threshold
	 *
	 * @param logThreshold Threshold
	 */
	public CallTreeCallback(long logThreshold) {
		initLogThreshold(logThreshold);
	}
	/**
	 * Constructor with log template
	 *
	 * @param callTreeLogTemplate Log template
	 */
	public CallTreeCallback(LogTemplate<Split> callTreeLogTemplate) {
		this.callTreeLogTemplate=callTreeLogTemplate;
	}

	/**
	 * Configure {@link #callTreeLogTemplate} with a {@link SplitThresholdLogTemplate}.
	 */
	private void initLogThreshold(Long threshold) {
		LogTemplate<Split> toLogger=toSLF4J(getClass().getName(), "debug");
		if (threshold==null) {
			callTreeLogTemplate = toLogger;
		} else {
			callTreeLogTemplate=whenSplitLongerThanMilliseconds(toLogger, threshold);		
		}
	}
	/**
	 * Get log threshold when {@link #callTreeLogTemplate} is a {@link SplitThresholdLogTemplate}.
	 */
	public Long getLogThreshold() {
		Long threshold=null;
		if (callTreeLogTemplate instanceof SplitThresholdLogTemplate) {
			threshold=((SplitThresholdLogTemplate) callTreeLogTemplate).getThreshold();
		}
		return threshold;
	}
	/**
	 * Set log threshold.
	 * Configure {@link #callTreeLogTemplate} with a {@link SplitThresholdLogTemplate}.
	 */
	public void setLogThreshold(Long logThreshold) {
		initLogThreshold(logThreshold);
	}

	/**
	 * Get call tree for current thread
	 *
	 * @return Thread call tree
	 */
	private CallTree getCallTree() {
		return threadCallTree.get();
	}

	/**
	 * Initialize the call tree for current thread
	 *
	 * @return Created call tree
	 */
	private CallTree initCallTree() {
		final CallTree callTree = new CallTree() {
			@Override
			protected void onRootStopwatchStop(CallTreeNode rootNode, Split split) {
				CallTreeCallback.this.onRootStopwatchStop(this, split);
			}
		};
		threadCallTree.set(callTree);
		return callTree;
	}

	/**
	 * Remove call tree for current thread
	 */
	private void removeCallTree() {
		threadCallTree.remove();
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	public void onStopwatchStart(Split split) {
		CallTree callTree = getCallTree();
		if (callTree == null) {
			// New tree root
			callTree = initCallTree();
		}
		callTree.onStopwatchStart(split);
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	public void onStopwatchStop(Split split) {
		getCallTree().onStopwatchStop(split);
	}

	/**
	 * When stopwatch corresponding to root tree node is stopped, this
	 * method is called.
	 * Logs call tree when split is longer than threshold
	 *
	 * @param rootNode Root tree node
	 * @param split Final split
	 */
	public void onRootStopwatchStop(CallTree callTree, Split split) {
		callTreeLogTemplate.log(split, callTree);
		removeCallTree();
	}
}
