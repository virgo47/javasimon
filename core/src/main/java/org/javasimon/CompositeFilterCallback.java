package org.javasimon;

import java.util.List;
import java.util.Map;
import java.util.EnumMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This callback combines Composite and Filter behavior. Filter can be configured
 * via {@link #addRule(org.javasimon.FilterCallback.Rule.Type, String, String, org.javasimon.Callback.Event[])}
 * method and if the rule is satisfied the event is propagated to all
 * children callbacks added via {@link #addCallback(Callback)}.
 * Filter without any rules does not propagate events. Any number of global
 * rules and per event rules can be added. Event rules have higher priority.
 * Rules are checked in the order they were added to the filter.
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
 * @created Jan 22, 2009
 */
public final class CompositeFilterCallback implements FilterCallback {
	private CompositeCallback callback = new CompositeCallback();

	private Map<Event, List<Rule>> rules;

	/**
	 * Constructs composite filter callback.
	 */
	public CompositeFilterCallback() {
		rules = new EnumMap<Event, List<Rule>>(Event.class);
		for (Event event : Event.values()) {
			rules.put(event, new CopyOnWriteArrayList<Rule>());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Callback> callbacks() {
		return callback.callbacks();
	}

	/**
	 * {@inheritDoc}
	 */
	public void addCallback(Callback callback) {
		this.callback.addCallback(callback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeCallback(Callback callback) {
		this.callback.removeCallback(callback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void initialize() {
		callback.initialize();
	}

	/**
	 * {@inheritDoc}
	 */
	public void deactivate() {
		callback.deactivate();
	}

	/**
	 * {@inheritDoc}
	 */
	public void reset(Simon simon) {
		if (rulesAppliesTo(simon, Event.RESET)) {
			callback.reset(simon);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopwatchAdd(Stopwatch stopwatch, long ns) {
		if (rulesAppliesTo(stopwatch, Event.STOPWATCH_ADD, ns)) {
			callback.stopwatchAdd(stopwatch, ns);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopwatchStart(Split split) {
		if (rulesAppliesTo(split.getStopwatch(), Event.STOPWATCH_START, split)) {
			callback.stopwatchStart(split);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopwatchStop(Split split) {
		if (rulesAppliesTo(split.getStopwatch(), Event.STOPWATCH_STOP, split)) {
			callback.stopwatchStop(split);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void counterDecrease(Counter counter, long dec) {
		if (rulesAppliesTo(counter, Event.COUNTER_DECREASE, dec)) {
			callback.counterDecrease(counter, dec);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void counterIncrease(Counter counter, long inc) {
		if (rulesAppliesTo(counter, Event.COUNTER_INCREASE, inc)) {
			callback.counterDecrease(counter, inc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void counterSet(Counter counter, long val) {
		if (rulesAppliesTo(counter, Event.COUNTER_SET, val)) {
			callback.counterDecrease(counter, val);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void simonCreated(Simon simon) {
		if (rulesAppliesTo(simon, Event.CREATED)) {
			callback.simonCreated(simon);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void simonDestroyed(Simon simon) {
		if (rulesAppliesTo(simon, Event.DESTROYED)) {
			callback.simonDestroyed(simon);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		if (rulesAppliesTo(null, Event.CLEAR)) {
			callback.clear();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void message(String message) {
		if (rulesAppliesTo(null, Event.MESSAGE, message)) {
			callback.message(message);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void warning(String warning, Exception cause) {
		if (rulesAppliesTo(null, Event.WARNING, cause)) {
			callback.warning(warning, cause);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void addRule(Rule.Type type, String ruleText, String pattern, Event... events) {
		SimonPattern simonPattern = null;
		if (pattern != null) {
			simonPattern = new SimonPattern(pattern);
		}
		Rule rule = new Rule(type, ruleText, simonPattern);
		for (Event event : events) {
			if (event != null) {
				rules.get(event).add(rule);
			}
		}
		if (events.length == 0) {
			rules.get(Event.ALL).add(rule);
		}
	}

	private boolean rulesAppliesTo(Simon simon, Event checkedEvent, Object... params) {
		for (Event event : new Event[]{checkedEvent, Event.ALL}) {
			for (Rule rule : rules.get(event)) {
				boolean result = patternAndConditionCheck(simon, rule, params);

				if (!result && rule.getType().equals(Rule.Type.MUST)) { // fast fail on MUST condition
					return false;
				}
				if (result && rule.getType().equals(Rule.Type.MUST_NOT)) { // fast fail on MUST NOT condition
					return false;
				}
				if (result && rule.getType().equals(Rule.Type.SUFFICE)) { // fast success on SUFFICE condition
					return true;
				}
			}
		}
		return true;
	}

	private boolean patternAndConditionCheck(Simon simon, Rule rule, Object... params) {
		boolean result = true;
		if (simon != null && rule.getPattern() != null && !rule.getPattern().matches(simon.getName())) {
			result = false;
		}
		if (result && !rule.checkCondition(simon, params)) {
			result = false;
		}
		return result;
	}
}