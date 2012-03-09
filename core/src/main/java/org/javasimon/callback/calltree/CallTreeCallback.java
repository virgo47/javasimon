package org.javasimon.callback.calltree;

import org.javasimon.Split;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.utils.SimonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Callback which logs a call tree when a the main call is bigger than a
 * threshold. 
 * 
 * Call tree looks like this:
 * <pre>
 * org.javasimon.web.Controller.execute 123ms
 *	org.javasimon.business.FirstService.work 75ms, 75%
 *		org.javasimon.data.FirstDAO.findAll 50 ms, 82%
 *		org.javasimon.data.SecondDAO.findByRelation 20ms, 10%, 3
 *	org.javasimon.business.SecodeService.do 10ms, 5%
 * </pre>
 *
 * @author gquintana
 */
public class CallTreeCallback extends CallbackSkeleton {

	/**
	 * Call tree of current thread
	 */
	private final ThreadLocal<CallTree> threadCallTree = new ThreadLocal<CallTree>();
	/**
	 * Logger used for printing call tree
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CallTreeCallback.class);
	/**
	 * Duration threshold overwhich call tree is logged
	 */
	private long logThreshold = 500;

	/**
	 * Default constructor
	 */
	public CallTreeCallback() {
	}

	/**
	 * Constructor with logging duration threshold
	 *
	 * @param logThreshold Threshold
	 */
	public CallTreeCallback(long logThreshold) {
		this.logThreshold = logThreshold;
	}

	public long getLogThreshold() {
		return logThreshold;
	}

	public void setLogThreshold(long logThreshold) {
		this.logThreshold = logThreshold;
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
				CallTreeCallback.this.onRootStopwatchStop(rootNode, split);
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
		if (callTree==null) {
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
	public void onRootStopwatchStop(CallTreeNode rootNode, Split split) {
		if (rootNode.getTotal() > logThreshold * SimonUtils.NANOS_IN_MILLIS) {
			LOGGER.warn("Call Tree alert\r\n" + rootNode.toString());
		}
		removeCallTree();
	}
}
