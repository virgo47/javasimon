package org.javasimon.callback;

import org.javasimon.Counter;
import org.javasimon.CounterSample;
import org.javasimon.Manager;
import org.javasimon.Simon;
import org.javasimon.SimonPattern;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.script.ScriptException;

/**
 * This callback combines Composite and Filter behavior. Filter can be configured
 * via {@link #addRule(FilterRule.Type, String, String, Callback.Event...)}
 * method and if the rule is satisfied the event is propagated to all
 * children callbacks added via {@link #addCallback(Callback)}. XML facility for configuration
 * is provided via {@link org.javasimon.ManagerConfiguration#readConfig(java.io.Reader)}.
 * <p/>
 * Filter without any rules does not propagate events (default DENY behavior).
 * Any number of global rules (for {@link Callback.Event#ALL}) and per event rules can be added.
 * Event rules have higher priority and if the filter passes on event rules, global rules are not consulted.
 * Rules are checked in the order they were added to the filter.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @see FilterRule
 */
public final class CompositeFilterCallback implements FilterCallback, CompositeCallback {

	private CompositeCallbackImpl callback = new CompositeCallbackImpl();

	private Map<Event, List<FilterRule>> rules;

	/** Constructs composite filter callback. */
	public CompositeFilterCallback() {
		rules = new EnumMap<>(Event.class);
		for (Event event : Event.values()) {
			rules.put(event, new CopyOnWriteArrayList<FilterRule>());
		}
	}

	@Override
	public List<Callback> callbacks() {
		return callback.callbacks();
	}

	@Override
	public void addCallback(Callback callback) {
		this.callback.addCallback(callback);
	}

	@Override
	public void removeCallback(Callback callback) {
		this.callback.removeCallback(callback);
	}

	@Override
	public void removeAllCallbacks() {
		this.callback.removeAllCallbacks();
	}

	@Override
	public void initialize(Manager manager) {
		callback.initialize(manager);
	}

	@Override
	public void cleanup() {
		callback.cleanup();
	}

	@Override
	public void onStopwatchAdd(Stopwatch stopwatch, Split split, StopwatchSample sample) {
		if (rulesApplyTo(stopwatch, Event.STOPWATCH_ADD, split.runningFor())) {
			callback.onStopwatchAdd(stopwatch, split, sample);
		}
	}

	@Override
	public void onStopwatchStart(Split split) {
		Stopwatch stopwatch = split.getStopwatch();
		if (stopwatch != null && rulesApplyTo(stopwatch, Event.STOPWATCH_START, split)) {
			callback.onStopwatchStart(split);
		}
	}

	@Override
	public void onStopwatchStop(Split split, StopwatchSample sample) {
		Stopwatch stopwatch = split.getStopwatch();
		if (stopwatch != null && rulesApplyTo(stopwatch, Event.STOPWATCH_STOP, split)) {
			callback.onStopwatchStop(split, sample);
		}
	}

	@Override
	public void onCounterDecrease(Counter counter, long dec, CounterSample sample) {
		if (rulesApplyTo(counter, Event.COUNTER_DECREASE, dec)) {
			callback.onCounterDecrease(counter, dec, sample);
		}
	}

	@Override
	public void onCounterIncrease(Counter counter, long inc, CounterSample sample) {
		if (rulesApplyTo(counter, Event.COUNTER_INCREASE, inc)) {
			callback.onCounterIncrease(counter, inc, sample);
		}
	}

	@Override
	public void onCounterSet(Counter counter, long val, CounterSample sample) {
		if (rulesApplyTo(counter, Event.COUNTER_SET, val)) {
			callback.onCounterSet(counter, val, sample);
		}
	}

	@Override
	public void onSimonCreated(Simon simon) {
		if (rulesApplyTo(simon, Event.CREATED)) {
			callback.onSimonCreated(simon);
		}
	}

	@Override
	public void onSimonDestroyed(Simon simon) {
		if (rulesApplyTo(simon, Event.DESTROYED)) {
			callback.onSimonDestroyed(simon);
		}
	}

	@Override
	public void onManagerClear() {
		if (rulesApplyTo(null, Event.MANAGER_CLEAR)) {
			callback.onManagerClear();
		}
	}

	@Override
	public void onManagerMessage(String message) {
		if (rulesApplyTo(null, Event.MESSAGE, message)) {
			callback.onManagerMessage(message);
		}
	}

	@Override
	public void onManagerWarning(String warning, Exception cause) {
		if (rulesApplyTo(null, Event.WARNING, cause)) {
			callback.onManagerWarning(warning, cause);
		}
	}

	@Override
	public void addRule(FilterRule.Type type, String condition, String pattern, Event... events) {
		SimonPattern simonPattern = SimonPattern.create(pattern);
		FilterRule rule = new FilterRule(type, condition, simonPattern);
		for (Event event : events) {
			if (event != null) {
				rules.get(event).add(rule);
			}
		}
		if (events.length == 0) {
			rules.get(Event.ALL).add(rule);
		}
	}

	private boolean rulesApplyTo(Simon simon, Event checkedEvent, Object... params) {
		// only if event rules are empty, check rules for ALL as a fallback
		if (rules.get(checkedEvent).size() == 0) {
			return checkRules(simon, Event.ALL, params);
		}
		return checkRules(simon, checkedEvent, params);
	}

	private boolean checkRules(Simon simon, Event event, Object... params) {
		List<FilterRule> rulesForEvent = rules.get(event);
		if (rulesForEvent.size() == 0) { // empty rule list => DENY
			return false;
		}
		boolean allMustSatisfied = false;
		for (FilterRule rule : rulesForEvent) {
			boolean result = false;
			try {
				result = patternAndConditionCheck(simon, rule, params);
			} catch (ScriptException e) {
				onManagerWarning("Script exception while evaluating rule expression", e);
			}

			if (!result && rule.getType().equals(FilterRule.Type.MUST)) { // fast fail on MUST condition
				return false;
			} else if (result && rule.getType().equals(FilterRule.Type.MUST)) { // MUST condition met, let's go on
				allMustSatisfied = true;
			} else if (result && rule.getType().equals(FilterRule.Type.MUST_NOT)) { // fast fail on MUST NOT condition
				return false;
			} else if (!result && rule.getType().equals(FilterRule.Type.MUST_NOT)) { // MUST NOT condition met, go on
				allMustSatisfied = true;
			} else if (result && rule.getType().equals(FilterRule.Type.SUFFICE)) { // fast success on SUFFICE condition
				return true;
			}
		}
		return allMustSatisfied;
	}

	private boolean patternAndConditionCheck(Simon simon, FilterRule rule, Object... params) throws ScriptException {
		//noinspection SimplifiableIfStatement
		if (simon != null && rule.getPattern() != null && !rule.getPattern().accept(simon)) {
			return false;
		}
		return rule.checkCondition(simon, params);
	}
}