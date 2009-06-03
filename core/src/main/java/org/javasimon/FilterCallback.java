package org.javasimon;

import net.java.dev.eval.Expression;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;

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
	 * @param condition further conditions of the rule
	 * @param pattern Simon pattern
	 * @param events event list (empty applies to all, can be omitted)
	 */
	void addRule(Rule.Type type, String condition, String pattern, Event... events);

	/**
	 * Represents filtering rule.
	 */
	class Rule {
		/**
		 * Enumeration of rule types that determines the evaluation of mutliple rules in a chain.
		 */
		public enum Type {
			/**
			 * Rule must pass and next rule is consulted.
			 */
			MUST,

			/**
			 * If the rule passes the whole filter passes and no other rule is consulted. If the rule
			 * doesn't pass next rule is consulted.
			 */
			SUFFICE,

			/**
			 * Rule must not pass. If the rule passes the whole filter doesn't pass. If it fails next
			 * rule is checked.
			 */
			MUST_NOT
		}

		/**
		 * Name of the rule variable for last split time in ns.
		 */
		public static final String VAR_SPLIT = "split";

		/**
		 * Name of the rule variable for number of concurrently active splits of a particular Simon.
		 */
		public static final String VAR_ACTIVE = "active";

		/**
		 * Name of the rule variable for maximal number of concurrently active splits.
		 */
		public static final String VAR_MAX_ACTIVE = "maxactive";

		/**
		 * Name of the rule variable for current value of the counter.
		 */
		public static final String VAR_COUNTER = "counter";

		/**
		 * Name of the rule variable for maximal value of the Simon (stopwatch in ns, counter without unit).
		 */
		public static final String VAR_MAX = "max";

		/**
		 * Name of the rule variable for minimal value of the Simon (stopwatch in ns, counter without unit).
		 */
		public static final String VAR_MIN = "min";

		/**
		 * Name of the rule variable for total split time.
		 */
		public static final String VAR_TOTAL = "total";

		/**
		 * Name of the rule variable for increment or decrement value.
		 */
		public static final String VAR_VALUE = "value";

		private Type type;
		private String condition;
		private Expression expression;
		private SimonPattern pattern;

		/**
		 * Creates the rule with a specified type, condition and pattern. Rule can have a condition
		 * and/or a pattern. Pattern is not relevant for some callback operations (for example "warning").
		 * Both condition and pattern are optional and can be null.
		 *
		 * @param type rule type determining the role of the rule in the chain of the filter
		 * @param condition additional conditional expression that must be true
		 * @param pattern Simon pattern that must match
		 */
		Rule(Type type, String condition, SimonPattern pattern) {
			this.type = type;
			this.condition = condition;
			if (condition != null) {
				expression = new Expression(condition.toLowerCase());
			}
			this.pattern = pattern;
		}

		/**
		 * Returns the type of this rule.
		 *
		 * @return type of this rule
		 */
		public Type getType() {
			return type;
		}

		/**
		 * Returns the additional condition of this rule. Values from the affected Simon can be checked and compared.
		 *
		 * @return additional condition of this rule
		 */
		public String getCondition() {
			return condition;
		}

		/**
		 * Retruns the Simon pattern of this rule.
		 *
		 * @return Simon pattern of this rule
		 */
		public SimonPattern getPattern() {
			return pattern;
		}

		/**
		 * Checks the Simon and optional parameters against the condition specified for a rule.
		 *
		 * @param simon related Simon
		 * @param params optional parameters, e.g. value that is added to a Counter
		 * @return true if no condition is specified or the condition is satisfied, otherwise false
		 */
		boolean checkCondition(Simon simon, Object... params) {
			if (condition == null) {
				return true;
			}
			if (simon instanceof Stopwatch) {
				return checkStopwtach((Stopwatch) simon, params);
			}
			if (simon instanceof Counter) {
				return checkCounter((Counter) simon, params);
			}
			return true;
		}

		private boolean checkCounter(Counter counter, Object... params) {
			Map<String, BigDecimal> vars = new HashMap<String, BigDecimal>();
			processParams(vars, params);
			vars.put(VAR_COUNTER, BigDecimal.valueOf(counter.getCounter()));
			vars.put(VAR_MAX, BigDecimal.valueOf(counter.getMax()));
			vars.put(VAR_MIN, BigDecimal.valueOf(counter.getMin()));
			return eval(vars);
		}

		private boolean checkStopwtach(Stopwatch stopwatch, Object... params) {
			Map<String, BigDecimal> vars = new HashMap<String, BigDecimal>();
			processParams(vars, params);
			vars.put(VAR_ACTIVE, BigDecimal.valueOf(stopwatch.getActive()));
			vars.put(VAR_COUNTER, BigDecimal.valueOf(stopwatch.getCounter()));
			vars.put(VAR_MAX, BigDecimal.valueOf(stopwatch.getMax()));
			vars.put(VAR_MIN, BigDecimal.valueOf(stopwatch.getMin()));
			vars.put(VAR_MAX_ACTIVE, BigDecimal.valueOf(stopwatch.getMaxActive()));
			vars.put(VAR_TOTAL, BigDecimal.valueOf(stopwatch.getTotal()));
			return eval(vars);
		}

		private void processParams(Map<String, BigDecimal> vars, Object... params) {
			for (Object param : params) {
				if (param instanceof Split) {
					vars.put(VAR_SPLIT, BigDecimal.valueOf(((Split) param).runningFor()));
				} else if (param instanceof Long) {
					vars.put(VAR_VALUE, BigDecimal.valueOf((Long) param));
				}
			}
		}

		private boolean eval(Map<String, BigDecimal> vars) {
			return expression.eval(vars).equals(BigDecimal.ONE);
		}
	}
}
