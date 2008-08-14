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
	 * Sets the state of the Somon. You have to specify whether you want to propagate the change
	 * and overrule states of all sub-simons which effectively sets the same state to the whole
	 * subtree. If you don't overrule, some simons (with their subtrees) will not be affected
	 * by this change.
	 *
	 * @param state a new state.
	 * @param overrrule specifies whether this change is forced to the whole subtree.
	 */
	void setState(SimonState state, boolean overrrule);

	/**
	 * Returns true, if the Simon is enabled or if the enabled state is inherited.
	 *
	 * @return true, if the Simon is effectively enabled
	 */
	boolean isEnabled();

	/**
	 * Resets the Simon - concrete values depends on the type and the implementation.
	 *
	 * @return returns this
	 */
	Simon reset();

	/**
	 * Returns statistics processor assigned to the Simon. StatProcessor extends Simon
	 * with providing more statistic information about measured data (observations).
	 *
	 * @return statistics processor
	 */
	StatProcessor getStatProcessor();

	/**
	 * Sets statistics processor assigned to the Simon. StatProcessor extends Simon
	 * with providing more statistic information about measured data (observations).
	 *
	 * @param statProcessor statistics processor
	 */
	void setStatProcessor(StatProcessor statProcessor);

	/**
	 * Returns note for the Simon. Note allows you to add additional information in human
	 * readable form to the Simon.
	 *
	 * @return note for the Simon.
	 */
	String getNote();

	/**
	 * Sets note for the Simon. Note allows you to add additional information in human
	 * readable form to the Simon.
	 *
	 * @param note note for the Simon.
	 */
	void setNote(String note);
}
