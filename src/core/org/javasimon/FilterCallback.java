package org.javasimon;

/**
 * FilterCallback extends Callback with filtering capabilities. Filter callback
 * adds filtering rules that allow selective event propagation to children.
 * <p/>
 * Rule can be one of the following types:
 * <ul>
 * <li>must - rule MUST be true and following rules are checked
 * <li>suffice - if this rule is true the filter passes the event to children
 * otherwise next rules are checked
 * <li>must-not - if this rule is true the filter ignores the event, otherwise
 * next rules are checked
 * </ul>
 * As the order is important not all MUST rules must pass if there is any
 * satisfied SUFFICE rule before.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Feb 7, 2009
 */
public interface FilterCallback extends Callback {
	/**
	 * Adds the rule to the filter.
	 *
	 * @param type rule type (must, suffice, must-not)
	 * @param ruleText specific rule for a particular event(s)
	 * @param pattern Simon pattern
	 * @param events event list (empty applies to all, can be omitted)
	 */
	void addRule(Rule.Type type, String ruleText, String pattern, Event... events);

	class Rule {
		public enum Type {
			MUST, SUFFICE, MUST_NOT
		}

		private Type type;
		private String condition;
		private SimonPattern pattern;

		Rule(Type type, String condition, SimonPattern pattern) {
			this.type = type;
			this.condition = condition;
			this.pattern = pattern;
		}

		public Type getType() {
			return type;
		}

		public String getCondition() {
			return condition;
		}

		public SimonPattern getPattern() {
			return pattern;
		}
	}
}
