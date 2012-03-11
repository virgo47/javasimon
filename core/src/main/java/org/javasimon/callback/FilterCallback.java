package org.javasimon.callback;

/**
 * FilterCallback extends {@link Callback} adding filtering capabilities. {@link FilterRule}s can be added to
 * the filter callback, these allow selective event propagation to sub-callback(s).
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public interface FilterCallback extends Callback {
	/**
	 * Adds the rule to the filter.
	 *
	 * @param type rule type (must, suffice, must-not)
	 * @param condition further conditions of the rule
	 * @param pattern Simon pattern
	 * @param events event list (empty applies to all, can be omitted)
	 */
	void addRule(FilterRule.Type type, String condition, String pattern, Event... events);
}
