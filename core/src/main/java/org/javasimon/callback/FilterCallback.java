package org.javasimon.callback;

/**
 * FilterCallback extends Callback with filtering capabilities. Filter callback
 * adds {@link FilterRule}s that allow selective event propagation to children.
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
