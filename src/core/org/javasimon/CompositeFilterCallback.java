package org.javasimon;

import java.util.List;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This callback combines Composite and Filter behavior. Filter can be configured
 * via {@link #addRule(org.javasimon.FilterCallback.Rule.Type, org.javasimon.Callback.Event[], String, String)}
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

	private List<Rule> rules = new CopyOnWriteArrayList<Rule>();

	/**
	 * {@inheritDoc}
	 */
	public Collection<Callback> callbacks() {
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
		if (rulesAppliesTo(split.getStopwatch())) {
			callback.stopwatchStart(split);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopwatchStop(Split split) {
		if (rulesAppliesTo(split.getStopwatch())) {
			callback.stopwatchStop(split);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void warning(String warning, Exception cause) {
		callback.warning(warning, cause);
	}

	public void addRule(Rule.Type type, Event[] events, String ruleText, String pattern) {
		Rule rule = new Rule(type, ruleText, new SimonPattern(pattern));
		if (events == null) {
			rules.add(rule);
			return;
		}
		for (Event event : events) {
			// TODO
		}
	}

	private boolean rulesAppliesTo(Simon simon) {
		for (Rule rule : rules) {
			boolean result = true;
			if (rule.getPattern() != null && !rule.getPattern().matches(simon.getName())) {
				result = false;
			}

			if (result && rule.getType().equals(Rule.Type.MUST_NOT)) {
				return false;
			}
			if (result && rule.getType().equals(Rule.Type.SUFFICE)) {
				return true;
			}
		}
		return false;
	}
}