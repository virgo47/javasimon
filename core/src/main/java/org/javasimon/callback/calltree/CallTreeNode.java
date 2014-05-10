package org.javasimon.callback.calltree;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javasimon.Split;
import org.javasimon.utils.SimonUtils;

/**
 * Call tree node is one Simon one for one call level, all splits for this
 * Simon+Level(+Thread) tuple are kept for later analysis.
 * Simon name is unique within parent tree node. Said differently a tree node can
 * not have two children with same name.
 *
 * @author gquintana
 * @since 3.2
 */
public class CallTreeNode {
	/**
	 * Name, used as a key.
	 */
	private final String name;

	/**
	 * Splits.
	 * Size defaults to 1, because most of the time there are no loops.
	 */
	private final List<Split> splits = new ArrayList<>(1);

	/**
	 * Child tree nodes.
	 */
	private Map<String, CallTreeNode> children;

	/**
	 * Parent tree node. {@code null} for root tree node.
	 */
	private CallTreeNode parent;

	/**
	 * Main constructor.
	 *
	 * @param name Simon name
	 */
	public CallTreeNode(String name) {
		this.name = name;
	}

	/**
	 * Returns Simon name.
	 *
	 * @return Name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Adds a split to the current tree node.
	 * In case of loops, child nodes can have many splits.
	 *
	 * @param split Split
	 */
	public void addSplit(Split split) {
		splits.add(split);
	}

	/**
	 * Returns the number of splits in this node.
	 *
	 * @return Split count
	 */
	public int getSplitCount() {
		return splits.size();
	}

	/**
	 * Returns the total time of splits using {@link org.javasimon.Split#runningFor()}.
	 *
	 * @return total time of splits
	 */
	public long getTotal() {
		long total = 0;
		for (Split split : splits) {
			total += split.runningFor();
		}
		return total;
	}

	/**
	 * Returns the part of time spent in this node compared to parent.
	 *
	 * @return Percent time
	 */
	public Integer getPercent() {
		Integer percent;
		if (parent == null) {
			percent = null;
		} else {
			percent = (int) (getTotal() * 100L / getParent().getTotal());
		}
		return percent;
	}

	/**
	 * Adds a child to this tree node.
	 *
	 * @param name Child Simon name
	 * @return Created child node
	 */
	public CallTreeNode addChild(String name) {
		if (children == null) {
			children = new HashMap<>();
		}
		CallTreeNode child = new CallTreeNode(name);
		children.put(name, child);
		child.parent = this;
		return child;
	}

	/**
	 * Returns the child node by Simon name.
	 *
	 * @param name Simon name
	 * @return Child corresponding to given name, or null if any
	 */
	public CallTreeNode getChild(String name) {
		return (children == null) ? null : children.get(name);
	}

	/**
	 * Returns all child nodes.
	 *
	 * @return children
	 */
	public Collection<CallTreeNode> getChildren() {
		return children == null ? Collections.<CallTreeNode>emptyList() : children.values();
	}

	/**
	 * Returns a child node with given name or creates it if it does not exists.
	 *
	 * @param name Simon name
	 * @return Child node
	 */
	public CallTreeNode getOrAddChild(String name) {
		CallTreeNode child = getChild(name);
		if (child == null) {
			child = addChild(name);
		}
		return child;
	}

	/**
	 * Returns parent tree node.
	 *
	 * @return Parent tree node
	 */
	public CallTreeNode getParent() {
		return parent;
	}

	/**
	 * Recursively prints this tree node to given print writer.
	 *
	 * @param printWriter Output print writer
	 * @param prefix Line prefix (used internally for indentation)
	 * @param parentTotal Duration of parent node (used to compute duration ratio for child nodes), null for root nodes
	 */
	private void print(PrintWriter printWriter, String prefix, Long parentTotal) {
		long total = getTotal();
		printWriter.print(prefix);
		printWriter.print(name);
		printWriter.print(' ');
		if (parentTotal != null && parentTotal != 0L) {
			printWriter.print(total * 100 / parentTotal);
			printWriter.print("%, ");
		}
		printWriter.print(SimonUtils.presentNanoTime(total));
		long counter = getSplitCount();
		if (counter > 1) {
			printWriter.print(", ");
			printWriter.print(counter);
		}
		printWriter.println();
		for (CallTreeNode child : getChildren()) {
			child.print(printWriter, prefix + "\t", total);
		}
	}

	/**
	 * Recursively prints this tree node to given print writer.
	 *
	 * @param printWriter Output print writer
	 */
	public void print(PrintWriter printWriter) {
		print(printWriter, "", null);
	}

	/**
	 * Returns a string representing the tree from this tree node, visiting recursively this tree branch.
	 *
	 * @return String
	 */
	@Override
	public String toString() {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		print(printWriter);
		return stringWriter.toString();
	}
}
