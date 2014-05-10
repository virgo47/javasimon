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
	 * Returns Simon's {@link Manager}.
	 *
	 * @return Simon's {@link Manager}
	 * @since 3.5
	 */
	Manager getManager();

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
	 * Samples increment in Simon values since the previous call of this method with the
	 * same key. When the method is called the first time for the key, current values
	 * are returned (same like from {@link #sample()}. Any subsequent calls with the key
	 * provide increments.
	 * <p/>
	 * Clients can use any sampling key (any Object) which enables safe access to their own increments.
	 * Using String does not guarantee this as any client can potentially guess the key. This
	 * may or may not be an issue.
	 *
	 * @param key sampling key used to access incremental sample
	 * @return {@link org.javasimon.Sample} with value increments
	 * @since 3.5
	 */
	Sample sampleIncrement(Object key);

	/**
	 * Stops incremental sampling for the key, removing any internal information for the key.
	 * Next call to {@link #sampleIncrement(Object)} for the key will be considered the first
	 * call for the key.
	 *
	 * @param key sampling key used to access incremental sample
	 * @return true if incremental information for the key existed, false otherwise
	 * @since 3.5
	 */
	boolean stopIncrementalSampling(Object key);
}
