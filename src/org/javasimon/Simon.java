package org.javasimon;

import java.util.List;

/**
 * Simon interface contains common functions related to Simon management - enable/disable and hierarchy.
 * It does not contain any real action method - these are in specific interfaces that describes
 * purpose of the particular type of monitors.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public interface Simon {
	/**
	 * Returns parent simon.
	 *
	 * @return parent simon
	 */
	Simon getParent();

	/**
	 * Returns list of children - direct sub-simons.
	 *
	 * @return list of children
	 */
	List<Simon> getChildren();

	/**
	 * Returns Simon name. Simon name is always fully qualified
	 * and determines also position of the Simon in the monitor hierarchy.
	 *
	 * @return name of the Simon
	 */
	String getName();

	/**
	 * Returns state of the Simon that can be enabled, disabled or ihnerited.
	 *
	 * @return state of the Simon
	 */
	SimonState getState();

	/**
	 * Enables this Simon. You have to specify whether you want to reset states of all sub-simons
	 * to inherit which effectively enables the whole subtree. If you don't reset the state
	 * some simons (with their subtree) will not be affected by this change.
	 *
	 * @param resetSubtree if true, the whole subtree will be set to inherit (thus enabled),
	 * if false, some explicitely set monitors (with their subtree) will not be affected by
	 * this change
	 */
	void enable(boolean resetSubtree);

	/**
	 * Disables this Simon. You have to specify whether you want to reset states of all sub-simons
	 * to inherit which effectively disables the whole subtree. If you don't reset the state
	 * some simons (with their subtree) will not be affected by this change.
	 *
	 * @param resetSubtree if true, the whole subtree will be set to inherit (thus disabled),
	 * if false, some explicitely set monitors (with their subtree) will not be affected by
	 * this change
	 */
	void disable(boolean resetSubtree);

	/**
	 * Lets this Simon to inherit the state. You have to specify whether you want to reset
	 * states of all sub-simons to inherit the state as well.
	 *
	 * @param resetSubtree if true, the whole subtree will be set to inherit; if false,
	 * some explicitely set monitors (with their subtree) will not be affected by
	 * this change
	 */
	void inheritState(boolean resetSubtree);

	/**
	 * Returns true, if the Simon is enabled or if the enabled state is inherited.
	 *
	 * @return true, if the Simon is effectively enabled
	 */
	boolean isEnabled();

	/**
	 * Resets the Simon - concrete values depends on the type and the implementation.
	 */
	void reset();
}
