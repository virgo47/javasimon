package org.javasimon.callback;

import org.javasimon.Counter;
import org.javasimon.Simon;
import org.javasimon.SimonException;
import org.javasimon.SimonPattern;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.utils.Replacer;

import java.math.BigDecimal;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Represents filtering rule that checks whether sub-callbacks will get the event.
 * Rule can be one of the following types:
 * <ul>
 * <li>{@link Type#MUST} - rule MUST be true and following rules are checked
 * <li>{@link Type#SUFFICE} - if this rule is true the filter passes the event to children
 * otherwise next rules are checked
 * <li>{@link Type#MUST_NOT} - if this rule is true the filter ignores the event, otherwise
 * next rules are checked
 * </ul>
 * As the order is important not all MUST rules must pass if there is any satisfied SUFFICE rule before.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 3.1 (previously was {@code FilterCallback.Rule})
 */
public class FilterRule {

	/** Enumeration of rule types that determines the evaluation of multiple rules in a chain. */
	public enum Type {
		/** Rule must pass and next rule is consulted. */
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

	/** Name of the rule variable for last split time in ns (split). */
	public static final String VAR_SPLIT = "split";

	/** Name of the rule variable for number of concurrently active splits of a particular Simon (active). */
	public static final String VAR_ACTIVE = "active";

	/** Name of the rule variable for maximal number of concurrently active splits (maxactive). */
	public static final String VAR_MAX_ACTIVE = "maxactive";

	/** Name of the rule variable for current value of the counter (counter). */
	public static final String VAR_COUNTER = "counter";

	/** Name of the rule variable for maximal value of the Simon - stopwatch in ns, counter without unit (max). */
	public static final String VAR_MAX = "max";

	/** Name of the rule variable for minimal value of the Simon - stopwatch in ns, counter without unit (min). */
	public static final String VAR_MIN = "min";

	/** Name of the rule variable for total split time (total). */
	public static final String VAR_TOTAL = "total";

	/** Name of the rule variable for increment or decrement value (value). */
	public static final String VAR_VALUE = "value";

	private static final ScriptEngine ECMA_SCRIPT_ENGINE = new ScriptEngineManager().getEngineByName("ecmascript");

	private static final Replacer[] CONDITION_REPLACERS = new Replacer[] {
		new Replacer(" lt ", " < "),
		new Replacer(" le ", " <= "),
		new Replacer(" eq ", " == "),
		new Replacer(" ne ", " != "),
		new Replacer(" gt ", " > "),
		new Replacer(" ge ", " >= "),
		new Replacer(" and ", " && "),
		new Replacer(" or ", " || "),
		new Replacer(" not ", " ! "),
		new Replacer("(\\d)s", "$1000000000"),
		new Replacer("(\\d)ms", "$1000000"),
		new Replacer("(\\d)us", "$1000"),
	};

	private Type type;
	private String condition;
	private CompiledScript expression;
	private SimonPattern pattern;

	/**
	 * Creates the rule with a specified type, condition and pattern. Rule can have a condition and/or a pattern.
	 * Pattern is not relevant for manager-level callback operations ({@link Callback#onManagerWarning(String, Exception)}, {@link Callback#onManagerMessage(String)}).
	 * Both condition and pattern are optional and can be null.
	 *
	 * @param type rule type determining the role of the rule in the chain of the filter
	 * @param condition additional conditional expression that must be true
	 * @param pattern Simon pattern that must match
	 */
	public FilterRule(Type type, String condition, SimonPattern pattern) {
		this.type = type;
		this.condition = condition;
		if (condition != null) {
			condition = condition.toLowerCase();
			for (Replacer conditionReplacer : CONDITION_REPLACERS) {
				condition = conditionReplacer.process(condition);
			}
			try {
				expression = ((Compilable) ECMA_SCRIPT_ENGINE).compile(condition);
				Bindings bindings = ECMA_SCRIPT_ENGINE.createBindings();
				bindings.put(VAR_ACTIVE, 0);
				bindings.put(VAR_COUNTER, 0);
				bindings.put(VAR_MAX, 0);
				bindings.put(VAR_MAX_ACTIVE, 0);
				bindings.put(VAR_MIN, 0);
				bindings.put(VAR_SPLIT, 0);
				bindings.put(VAR_TOTAL, 0);
				bindings.put(VAR_VALUE, 0);
				if (!(expression.eval(bindings) instanceof Boolean)) {
					throw new SimonException("Expression '" + condition + "' does not return boolean.");
				}
			} catch (ScriptException e) {
				throw new SimonException(e);
			}
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
	 * Returns the Simon pattern of this rule.
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
	 * @throws javax.script.ScriptException possible exception raised by the expression evaluation
	 */
	public synchronized boolean checkCondition(Simon simon, Object... params) throws ScriptException {
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

	private boolean checkCounter(Counter counter, Object... params) throws ScriptException {
		Bindings bindings = ECMA_SCRIPT_ENGINE.createBindings();
		processParams(bindings, params);
		bindings.put(VAR_COUNTER, counter.getCounter());
		bindings.put(VAR_MAX, counter.getMax());
		bindings.put(VAR_MIN, counter.getMin());
		return eval(bindings);
	}

	private boolean checkStopwtach(Stopwatch stopwatch, Object... params) throws ScriptException {
		Bindings bindings = ECMA_SCRIPT_ENGINE.createBindings();
		processParams(bindings, params);
		bindings.put(VAR_ACTIVE, stopwatch.getActive());
		bindings.put(VAR_COUNTER, stopwatch.getCounter());
		bindings.put(VAR_MAX, stopwatch.getMax());
		bindings.put(VAR_MIN, stopwatch.getMin());
		bindings.put(VAR_MAX_ACTIVE, stopwatch.getMaxActive());
		bindings.put(VAR_TOTAL, stopwatch.getTotal());
		return eval(bindings);
	}

	private void processParams(Bindings bindings, Object... params) {
		for (Object param : params) {
			if (param instanceof Split) {
				bindings.put(VAR_SPLIT, BigDecimal.valueOf(((Split) param).runningFor()));
			} else if (param instanceof Long) {
				bindings.put(VAR_VALUE, BigDecimal.valueOf((Long) param));
			}
		}
	}

	private boolean eval(Bindings bindings) throws ScriptException {
		return (Boolean) expression.eval(bindings);
	}
}
