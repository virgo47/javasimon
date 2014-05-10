package org.javasimon.console;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.javasimon.Manager;
import org.javasimon.Simon;
import org.javasimon.SimonPattern;

/**
 * Helper class to navigate through Simons.
 *
 * @author gquintana
 */
public class SimonVisitors {

	private SimonVisitors() {
	}

	/**
	 * Visit simons as a list.
	 * Calls {@link  Manager#getSimons(org.javasimon.SimonFilter)} method
	 * then Simons are sorted by name and filtered by type
	 * finally the visitor is called on each of them.
	 *
	 * @param manager Simon manager
	 * @param pattern Pattern
	 * @param types set of Simon types
	 * @param visitor Visitor
	 * @throws IOException
	 */
	public static void visitList(Manager manager, String pattern, Set<SimonType> types, SimonVisitor visitor) throws IOException {
		List<Simon> simons = new ArrayList<>(manager.getSimons(SimonPattern.create(pattern)));
		Collections.sort(simons, new Comparator<Simon>() {
			public int compare(Simon s1, Simon s2) {
				return s1.getName().compareTo(s2.getName());
			}
		});
		for (Simon simon : simons) {
			SimonType lType = SimonTypeFactory.getValueFromInstance(simon);
			if (types == null || types.contains(lType)) {
				visitor.visit(simon);
			}
		}
	}

	/**
	 * Visit Simons recursively as a tree starting from the specified name or root Simon if the name is {@code null}.
	 *
	 * @param manager Simon manager
	 * @param visitor Visitor
	 * @throws IOException
	 */
	public static void visitTree(Manager manager, String rootName, SimonVisitor visitor) throws IOException {
		Simon root = rootName == null ? manager.getRootSimon() : manager.getSimon(rootName);
		visitTree(root, visitor);
	}

	/**
	 * Visit Simons recursively as a tree starting from the specified Simon.
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
