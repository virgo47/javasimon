package org.javasimon.jmx;

import org.javasimon.Sample;

import java.util.List;

/**
 * Interface with common methods for JMX beans for a signle Simon that corresponds
 * to AbstractSimon in the core package.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public interface SimonSuperMXBean {
	/**
	 * Returns Simon name. While Simon names can be {@code null} for anonymous
	 * Simons, such Simons are never registered hence this method never returns
	 * {@code null}.
	 *
	 * @return name of the Simon
	 */
	String getName();

	/**
	 * Returns name of the parent Simon.
	 *
	 * @return name of the parent Simon
	 */
	String getParentName();

	/**
	 * Returns list of children names.
	 *
	 * @return list of children names
	 */
	List<String> getChildrenNames();

	/**
	 * Returns state of the Simon that can be ENABLED, DISABLED or INHERITED. State is returned
	 * as a String that matches values of the {@link org.javasimon.SimonState} enumeration.
	 *
	 * @return state of the Simon as a String
	 */
	String getState();

	/**
	 * Sets the state of the Simon. It must be specified whether to propagate the change
	 * and overrule states of all sub-simons which effectively sets the same state to the whole
	 * subtree. If subtree is not overruled, some Simons (with their subtrees) may not be affected
	 * by this change.
	 *
	 * @param state a new state as a valid String for {@link org.javasimon.SimonState#valueOf(String)}
	 * @param overrule specifies whether this change is forced to the whole subtree.
	 */
	void setState(String state, boolean overrule);

	/**
	 * Returns true, if the Simon is enabled or if the enabled state is inherited.
	 *
	 * @return true, if the Simon is effectively enabled
	 */
	boolean isEnabled();

	/**
	 * Resets the Simon, its usages and stat processor - concrete values depend
	 * on the type and the implementation.
	 */
	void reset();

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
	 * Returns nicely formatted timestamp of the first usage of this Simon.
	 *
	 * @return formatted date and time of the first usage
	 */
	String getFirstUsageAsString();

	/**
	 * Returns ms timestamp of the last usage of this Simon. First and last usage
	 * are updated when monitor performs the measuring (start/stop/count/etc). They
	 * are not updated when values are obtained from the monitor.
	 *
	 * @return ms timestamp of the last usage
	 */
	long getLastUsage();

	/**
	 * Returns nicely formatted timestamp of the last usage of this Simon.
	 *
	 * @return formatted date and time of the last usage
	 */
	String getLastUsageAsString();

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

	/**
	 * Returns Simon type used as a property in the {@link javax.management.ObjectName}.
	 *
	 * @return Simon type
	 */
	String getType();
}