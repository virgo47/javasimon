package org.javasimon.console;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.javasimon.Manager;
import org.javasimon.Simon;
import org.javasimon.SimonPattern;

/**
 * Helper class to navigate through Simons.
 *
 * @author gquintana
 */
public class SimonVisitors {

	/**
	 * Hidden constructor.
	 */
	private SimonVisitors() {
	}

	/**
	 * Visit simons as a list.
	 * Calls {@link  Manager#getSimons(org.javasimon.SimonPattern)} method
	 * then Simons are sorted by name and filtered by type
	 * finally the visitor is called on each of them.
	 *
	 * @param manager Simon manager
	 * @param pattern Pattern
	 * @param type Type
	 * @param visitor Visitor
	 * @throws IOException
	 */
	public static void visitList(Manager manager, String pattern, SimonType type, SimonVisitor visitor) throws IOException {
		List<Simon> simons = new ArrayList<Simon>(manager.getSimons(SimonPattern.create(pattern)));
		Collections.sort(simons, new Comparator<Simon>() {

			public int compare(Simon s1, Simon s2) {
				return s1.getName().compareTo(s2.getName());

			}
		});
		for (Simon simon : simons) {
			SimonType lType = SimonType.getValueFromInstance(simon);
			if (type == null || type == lType) {
				visitor.visit(simon);
			}
		}
	}

	/**
	 * Visit simons as a tree.
	 * Calls {@link  Manager#getRootSimon() } method
	 * finally the visitor is recursively called on each of them
	 *
	 * @param manager Simon manager
	 * @param visitor Visitor
	 * @throws IOException
	 */
	public static void visitTree(Manager manager, SimonVisitor visitor) throws IOException {
		visitTree(manager.getRootSimon(), visitor);
	}

	/**
	 * Visit simons as a tree.
	 * Calls The visitor is recursively called on each of them
	 *
	 * @param simon Parent simon
	 * @param visitor Visitor
	 * @throws IOException
	 */
	public static void visitTree(Simon simon, SimonVisitor visitor) throws IOException {
		visitor.visit(simon);
		for (Simon childSimon : simon.getChildren()) {
			visitTree(childSimon, visitor);
		}
	}
}
