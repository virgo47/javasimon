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
	public void stopwatchStart(Split split) {
		if (rulesAppliesTo(split.getStopwatch(), Event.ALL, Event.START)) {
			callback.stopwatchStart(split);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopwatchStop(Split split) {
		if (rulesAppliesTo(split.getStopwatch(), Event.ALL, Event.STOP)) {
			callback.stopwatchStop(split);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void warning(String warning, Exception cause) {
		if (rulesAppliesTo(null, Event.ALL, Event.WARNING)) {
			callback.warning(warning, cause);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void addRule(Rule.Type type, String ruleText, String pattern, Event... events) {
		Rule rule = new Rule(type, ruleText, new SimonPattern(pattern));
		for (Event event : events) {
			rules.get(event).add(rule);
		}
		if (events.length == 0) {
			rules.get(Event.ALL).add(rule);
		}
	}

	private boolean rulesAppliesTo(Simon simon, Event... events) {
		for (Event event : events) {
			for (Rule rule : rules.get(event)) {
				boolean result = true;
				if (simon != null && rule.getPattern() != null && !rule.getPattern().matches(simon.getName())) {
					result = false;
				}

				if (result && rule.getType().equals(Rule.Type.MUST_NOT)) {
					return false;
				}
				if (result && rule.getType().equals(Rule.Type.SUFFICE)) {
					return true;
				}
			}
		}
		return true;
	}
}