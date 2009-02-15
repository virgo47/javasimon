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

	class Rule {
		public enum Type {
			MUST, SUFFICE, MUST_NOT
		}

		public static final String VAR_SPLIT = "split";
		public static final String VAR_ACTIVE = "active";
		public static final String VAR_MAX_ACTIVE = "maxactive";
		public static final String VAR_COUNTER = "counter";
		public static final String VAR_MAX = "max";
		public static final String VAR_MIN = "min";
		public static final String VAR_TOTAL = "total";

		private Type type;
		private String condition;
		private Expression expression;
		private SimonPattern pattern;

		Rule(Type type, String condition, SimonPattern pattern) {
			this.type = type;
			this.condition = condition;
			if (condition != null) {
				expression = new Expression(condition.toLowerCase());
			}
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

		boolean checkCondition(Simon simon, Split split) {
			if (condition == null) {
				return true;
			}
			if (simon instanceof Stopwatch) {
				return checkStopwtach((Stopwatch) simon, split);
			}
			return true;
		}

		private boolean checkStopwtach(Stopwatch stopwatch, Split split) {
			Map<String, BigDecimal> vars = new HashMap<String, BigDecimal>();
			if (split != null) {
				vars.put(VAR_SPLIT, BigDecimal.valueOf(split.runningFor()));
			}
			vars.put(VAR_ACTIVE, BigDecimal.valueOf(stopwatch.getActive()));
			vars.put(VAR_COUNTER, BigDecimal.valueOf(stopwatch.getCounter()));
			vars.put(VAR_MAX, BigDecimal.valueOf(stopwatch.getMax()));
			vars.put(VAR_MIN, BigDecimal.valueOf(stopwatch.getMin()));
			vars.put(VAR_MAX_ACTIVE, BigDecimal.valueOf(stopwatch.getMaxActive()));
			vars.put(VAR_TOTAL, BigDecimal.valueOf(stopwatch.getTotal()));
			return eval(vars);
		}

		private boolean eval(Map<String, BigDecimal> vars) {
			return expression.eval(vars).equals(BigDecimal.ONE);
		}
	}
}
