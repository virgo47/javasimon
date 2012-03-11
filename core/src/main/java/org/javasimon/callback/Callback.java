package org.javasimon.callback;

import org.javasimon.Counter;
import org.javasimon.Simon;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

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
 * Callback can have a lifecycle supported with methods {@link #initialize()} and {@link #cleanup()}.
 * Callback is initialized when it is attached to the manager (anywhere in the callback tree) and
 * deinitialized when the callback is removed from the callback tree.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public interface Callback {
	/**
	 * Lifecycle method called when the callback is added to a manager.
	 */
	void initialize();

	/**
	 * Lifecycle method called when the callback is removed from the manager. It should implement
	 * any necessary cleanup or resources - e.g. release JDBC connection if one was used for Callback
	 * functionality.
	 */
	void cleanup();

	/**
	 * Stopwatch start event. <b>Duration of this processing is included into the split time!</b>
	 *
	 * @param split started Split
	 */
	void onStopwatchStart(Split split);

	/**
	 * Stopwatch stop event. This action is executed after the split time is calculated and does not
	 * affect the measuring.
	 *
	 * @param split stopped Split
	 */
	void onStopwatchStop(Split split);

	/**
	 * Simon reset event.
	 *
	 * @param simon reset Simon
	 */
	void onSimonReset(Simon simon);

	/**
	 * Stopwatch add time event.
	 *
	 * @param stopwatch modified Stopwatch
	 * @param ns added split time in ns
	 */
	void onStopwatchAdd(Stopwatch stopwatch, long ns);

	/**
	 * Stopwatch add split event.
	 *
	 * @param stopwatch modified Stopwatch
	 * @param split added split object
	 * @since 3.1
	 */
	void onStopwatchAdd(Stopwatch stopwatch, Split split);

	/**
	 * Counter decrease event.
	 *
	 * @param counter modified Counter
	 * @param dec decrement amount
	 */
	void onCounterDecrease(Counter counter, long dec);

	/**
	 * Counter increase event.
	 *
	 * @param counter modified Counter
	 * @param inc increment amount
	 */
	void onCounterIncrease(Counter counter, long inc);

	/**
	 * Counter set event.
	 *
	 * @param counter modified Counter
	 * @param val new value
	 */
	void onCounterSet(Counter counter, long val);

	/**
	 * Simon created event is called when Simon is successfully created by the Manager.
	 *
	 * @param simon created Simon
	 */
	void onSimonCreated(Simon simon);

	/**
	 * Simon destroyed event is called when Simon is successfully destroyed by the Manager.
	 *
	 * @param simon destroyed Simon
	 */
	void onSimonDestroyed(Simon simon);

	/**
	 * Event called when the manager is cleared.
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
	 * @param warning arbitrary warning message
	 * @param cause exception causing this warning
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
		/**
		 * Meta-action designating all actions (or any action in rules).
		 */
		ALL("all"),

		/**
		 * Reset of the Simon.
		 */
		RESET("reset"),

		/**
		 * Start of the stopwatch.
		 */
		STOPWATCH_START("start"),

		/**
		 * Stop of the stopwatch.
		 */
		STOPWATCH_STOP("stop"),

		/**
		 * Adding value to the stopwatch.
		 */
		STOPWATCH_ADD("add"),

		/**
		 * Counter increased.
		 */
		COUNTER_INCREASE("increase"),

		/**
		 * Counter decreased.
		 */
		COUNTER_DECREASE("decrease"),

		/**
		 * Counter set to arbitrary value.
		 */
		COUNTER_SET("set"),

		/**
		 * Creation of a Simon.
		 */
		CREATED("created"),

		/**
		 * Removing of a Simon.
		 */
		DESTROYED("destroyed"),

		/**
		 * Clearing of the manager.
		 */
		CLEAR("clear"),

		/**
		 * Event producing arbitrary message.
		 */
		MESSAGE("message"),

		/**
		 * Warning related to the manager.
		 */
		WARNING("warning");

		private static Map<String, Event> codeValues = new HashMap<String, Event>();

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
