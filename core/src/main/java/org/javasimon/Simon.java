package org.javasimon;

import java.util.List;

/**
 * Simon interface contains common functions related to Simon management - enable/disable and hierarchy.
 * It does not contain any real action method - these are in specific interfaces that describes
 * purpose of the particular type of monitor.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @see Manager
 * @see Counter for Simon counting some events
 * @see Stopwatch for Simon measuring time spans
 */
public interface Simon extends HasAttributes {

	/**
	 * Returns Simon name. Simon name is always fully qualified
	 * and determines also position of the Simon in the monitor hierarchy.
	 * Simon name can be {@code null} for anonymous Simons.
	 *
	 * @return name of the Simon
	 */
	String getName();

	/**
	 * Returns parent Simon.
	 *
	 * @return parent Simon
	 */
	Simon getParent();

	/**
	 * Returns list of children - direct sub-simons.
	 *
	 * @return list of children
	 */
	List<Simon> getChildren();

	/**
	 * Returns state of the Simon that can be enabled, disabled or inherited.
	 *
	 * @return state of the Simon
	 */
	SimonState getState();

	/**
	 * Sets the state of the Simon. It must be specified whether to propagate the change
	 * and overrule states of all sub-simons which effectively sets the same state to the whole
	 * subtree. If subtree is not overruled, some Simons (with their subtrees) may not be affected
	 * by this change.
	 *
	 * @param state a new state.
	 * @param overrule specifies whether this change is forced to the whole subtree.
	 * @throws IllegalArgumentException if {@code state} is {@code null}.
	 */
	void setState(SimonState state, boolean overrule);

	/**
	 * Returns true, if the Simon is enabled or if the enabled state is inherited.
	 *
	 * @return true, if the Simon is effectively enabled
	 */
	boolean isEnabled();

	/**
	 * Resets the Simon values related to the measuring, timestamps and so on - usage timestamps, state,
	 * attributes are not affected. Timestamp of the last reset can be obtained by the method {@link #getLastReset()}.
	 * Reset is performed even for disabled Simons.
	 */
	void reset();

	/**
	 * Returns ms timestamp of the last recent usage of the {@link #reset()} method on the Simon.
	 * Returns 0 if {@code reset} was not called yet. This timestamp is useful for rate measuring
	 * when reset is called on a regular basis - likely via {@link #sampleAndReset()}. While
	 * client code could store the timestamp too it is not necessary with this method.
	 *
	 * @return ms timestamp of the last reset or 0 if reset was not called yet
	 */
	long getLastReset();

	/**
	 * Returns note for the Simon. Note enables Simon with an additional information in human
	 * readable form.
	 *
	 * @return note for the Simon.
	 */
	String getNote();

	/**
	 * Sets note for the Simon. Note enables Simon with an additional information in human
	 * readable form.
	 *
	 * @param note note for the Simon.
	 */
	void setNote(String note);

	/**
	 * Returns ms timestamp of the first usage of this Simon. First and last usage
	 * are updated when monitor performs the measuring (start/stop/count/etc). They
	 * are not updated when values are obtained from the monitor.
	 *
	 * @return ms timestamp of the first usage
	 */
	long getFirstUsage();

	/**
	 * Returns ms timestamp of the last usage of this Simon. First and last usage
	 * are updated when monitor performs the measuring (start/stop/count/etc). They
	 * are not updated when values are obtained from the monitor.
	 *
	 * @return ms timestamp of the last usage
	 */
	long getLastUsage();

	/**
	 * Samples Simon values and returns them in a Java Bean derived from Sample interface.
	 *
	 * @return sample containing all Simon values
	 */
	Sample sample();

	/**
	 * Samples Simon values and returns them in a Java Bean derived from Sample interface
	 * and resets the Simon. Operation is synchronized to assure atomicity.
	 *
	 * @return sample containing all Simon values
	 */
	Sample sampleAndReset();

//	Sample sample(Object key);
}
