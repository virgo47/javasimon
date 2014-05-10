package org.javasimon.callback;

import org.javasimon.Counter;
import org.javasimon.CounterSample;
import org.javasimon.Manager;
import org.javasimon.Simon;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;

import java.util.HashMap;
import java.util.Map;

/**
 * Callback processes various events of the Java Simon API and is used as an extension point of the API.
 * Callbacks can be registered with the {@link org.javasimon.Manager} using its {@link CompositeCallback}
 * that can be obtained by calling {@link org.javasimon.Manager#callback()}. After adding the callback
 * into the main composite callback (or anywhere lower into the callback tree) by calling
 * {@link CompositeCallback#addCallback(Callback)} all events are propagated to all Callbacks (unless filtered
 * using {@link FilterCallback}). Methods called on various events are named {@code onEventXY} with type of the source
 * clearly mentioned in the name (Manager, Simon, Stopwatch, Counter).
 * <p/>
 * Callbacks can be configured via Manager configuration facility. (Configuration part is still rather WIP.)
 * <p/>
 * Callback can have a lifecycle supported with methods {@link #initialize(Manager)} and {@link #cleanup()}.
 * Callback is initialized when it is attached to the manager (anywhere in the callback tree) and
 * deinitialized when the callback is removed from the callback tree.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public interface Callback {

	/** Lifecycle method called when the callback is added to a manager. */
	void initialize(Manager manager);

	/**
	 * Lifecycle method called when the callback is removed from the manager. It should implement
	 * any necessary cleanup or resources - e.g. release JDBC connection if one was used for Callback
	 * functionality.
	 * <p/>
	 * <b>It is important to realize that this method is not guaranteed to be called for all callbacks, only for
	 * callbacks removed from Manager's callback tree.</b>
	 */
	void cleanup();

	/**
	 * Stopwatch start event. <b>Duration of all callbacks is included into the split time!</b>
	 * {@link StopwatchSample} valid for the moment after the start is provided because the callback
	 * is executed out of synchronized block.
	 * It is guaranteed that {@link org.javasimon.Split#getStopwatch()} will not return {@code null}.
	 *
	 * @param split started Split
	 */
	void onStopwatchStart(Split split);

	/**
	 * Stopwatch stop event. This action is executed after the split time is calculated and does not
	 * affect the measuring. {@link StopwatchSample} valid for the moment after the stop is provided
	 * because the callback is executed out of synchronized block.
	 * It is guaranteed that {@link org.javasimon.Split#getStopwatch()} will not return {@code null}.
	 *
	 * @param split stopped Split
	 * @param sample stopwatch sampled after the stop
	 */
	void onStopwatchStop(Split split, StopwatchSample sample);

	/**
	 * Stopwatch add split event. {@link StopwatchSample} valid for the moment after the add is provided
	 * because the callback is executed out of synchronized block.
	 * It is guaranteed that {@link org.javasimon.Split#getStopwatch()} will not return {@code null}.
	 *
	 * @param stopwatch modified Stopwatch
	 * @param split added split object
	 * @param sample stopwatch sampled after the add
	 * @since 3.1
	 */
	void onStopwatchAdd(Stopwatch stopwatch, Split split, StopwatchSample sample);

	/**
	 * Counter decrease event. {@link CounterSample} valid for the moment after the operation is provided
	 * because the callback is executed out of synchronized block.
	 *
	 * @param counter modified Counter
	 * @param dec decrement amount
	 * @param sample counter sampled after the operation
	 */
	void onCounterDecrease(Counter counter, long dec, CounterSample sample);

	/**
	 * Counter increase event. {@link CounterSample} valid for the moment after the operation is provided
	 * because the callback is executed out of synchronized block.
	 *
	 * @param counter modified Counter
	 * @param inc increment amount
	 * @param sample counter sampled after the operation
	 */
	void onCounterIncrease(Counter counter, long inc, CounterSample sample);

	/**
	 * Counter set event. {@link CounterSample} valid for the moment after the operation is provided
	 * because the callback is executed out of synchronized block.
	 *
	 * @param counter modified Counter
	 * @param val new value
	 * @param sample counter sampled after the operation
	 */
	void onCounterSet(Counter counter, long val, CounterSample sample);

	/**
	 * Simon created event is called when Simon is successfully created by the Manager.
	 * <b>Runs within the block synchronized on the Manager</b> - this results in high consistency, but it also
	 * means that <b>implementations of this method should not take much time.</b>
	 *
	 * @param simon created Simon
	 */
	void onSimonCreated(Simon simon);

	/**
	 * Simon destroyed event is called when Simon is successfully destroyed by the Manager.
	 * <b>Runs within the block synchronized on the Manager</b> - this results in high consistency, but it also
	 * means that <b>implementations of this method should not take much time.</b>
	 *
	 * @param simon destroyed Simon
	 */
	void onSimonDestroyed(Simon simon);

	/**
	 * Event called when the manager is cleared.
	 * <b>Runs within the block synchronized on the Manager</b> - this results in high consistency, but it also
	 * means that <b>implementations of this method should not take much time.</b>
	 */
	void onManagerClear();

	/**
	 * Message event is used to propagate arbitrary messages from the manager, or it can
	 * be used by the other Callback methods internally.
	 *
	 * @param message message text
	 */
	void onManagerMessage(String message);

	/**
	 * Warning event containing warning and/or cause.
	 *
	 * @param warning arbitrary warning message - can be {@code null}, unless concrete implementation states otherwise
	 * @param cause exception causing this warning - can be {@code null}, unless concrete implementation states otherwise
	 */
	void onManagerWarning(String warning, Exception cause);

	/**
	 * Enumeration of all supported callback actions. {@link #ALL} is meta-action usable in
	 * configurations meaning that the configuration entry applies to all actions (any action).
	 * In callback configuration action names in lowercase and with _ replaced for - can be
	 * used (e.g. "counter-increase" instead for {@link #COUNTER_INCREASE}.
	 * <p/>
	 * Event codes are used for configuration purposes instead of enum literals.
	 */
	enum Event {
		/** Meta-action designating all actions (or any action in rules). */
		ALL("all"),

		/** Start of the stopwatch. */
		STOPWATCH_START("start"),

		/** Stop of the stopwatch. */
		STOPWATCH_STOP("stop"),

		/** Adding value to the stopwatch. */
		STOPWATCH_ADD("add"),

		/** Counter increased. */
		COUNTER_INCREASE("increase"),

		/** Counter decreased. */
		COUNTER_DECREASE("decrease"),

		/** Counter set to arbitrary value. */
		COUNTER_SET("set"),

		/** Creation of a Simon. */
		CREATED("created"),

		/** Removing of a Simon. */
		DESTROYED("destroyed"),

		/** Clearing of the manager. */
		MANAGER_CLEAR("clearManager"),

		/** Event producing arbitrary message. */
		MESSAGE("message"),

		/** Warning related to the manager. */
		WARNING("warning");

		private static Map<String, Event> codeValues = new HashMap<>();

		private String code;

		static {
			for (Event value : values()) {
				codeValues.put(value.code, value);
			}
		}

		/**
		 * Constructor of the event with its code.
		 *
		 * @param code code of the event
		 */
		Event(String code) {
			this.code = code;
		}

		/**
		 * Returns event for String code used in XML configuration.
		 *
		 * @param code String code
		 * @return Event object
		 */
		public static Event forCode(String code) {
			return codeValues.get(code);
		}
	}
}
