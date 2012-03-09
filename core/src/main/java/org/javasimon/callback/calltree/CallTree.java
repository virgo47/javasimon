package org.javasimon.callback.calltree;

import java.util.LinkedList;
import org.javasimon.Split;

/**
 * Call tree contains the root call tree node and the current call stack
 * @author gquintana
 */
public class CallTree {
        /**
         * Call stack is the path (made of tree nodes) from root tree node
         * to current tree node
         */
	private final LinkedList<CallTreeNode> callStack=new LinkedList<CallTreeNode>();
        /**
         * When stopwatch is started, a new tree node is added to parent
         * tree node and pushed on the call stack. 
         * As a result, child tree node becomes the current tree node
	 * @return Current (child) tree node
         */
	public CallTreeNode onStopwatchStart(Split split) {
		final String name=split.getStopwatch().getName();
		CallTreeNode currentNode;
		if (callStack.isEmpty()) {
			// Root tree node
			currentNode=new CallTreeNode(name);
			onRootStopwatchStart(currentNode, split);
		} else {
			// Child node
			currentNode=callStack.getLast().getOrAddChild(name);
		}
		callStack.addLast(currentNode);
		return currentNode;
	}
        /**
         * When stopwatch is stopped, the the split is added to current tree node
         * and this tree node is popped from call stack.
         * As a result, parent tree node becomes current tree node
	 * @return Current (child) tree node
         */
	public CallTreeNode onStopwatchStop(Split split) {
		CallTreeNode currentNode=callStack.removeLast();
		currentNode.addSplit(split);
		if (callStack.isEmpty()) {
			onRootStopwatchStop(currentNode, split);
		}
		return currentNode;
	}
	/**
	 * When stopwatch is started, and the root tree node is pushed into
	 * the call stack, this method is called.
	 * Does nothing but can be overriden for custom needs.
	 * 
	 * @param rootNode Root tree node
	 * @param split Root split
	 */
	public void onRootStopwatchStart(CallTreeNode rootNode, Split split) {
	}
	
        /**
         * When stopwatched is stopped, and root tree node is popped from 
         * call stack, this method is called. 
	 * Does nothing but can be overriden for custom needs, such as logging, storing...
         */
	protected void onRootStopwatchStop(CallTreeNode callTreeNode,Split split) {
	}
}
