package org.javasimon;

import java.util.List;

/**
 * Callback processes various events of the Java Simon API. Callbacks can be
 * structured into the tree using {@link CompositeCallback} implementation.
 * Partial callbacks can be easily implemented extending the {@link org.javasimon.CallbackSkeleton}
 * class that already implements all methods as empty. Callbacks can be configured
 * via Manager configuration facility.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Jan 22, 2009
 */
public interface Callback {
	/**
	 * Returns the list of all child-callbacks. Implemented in {@link org.javasimon.CompositeCallback}.
	 *
	 * @return children list
	 */
	List<Callback> callbacks();

	/**
	 * Adds another callback as a child to this callback. Implemented in {@link org.javasimon.CompositeCallback}.
	 *
	 * @param callback added callback
	 */
	void addCallback(Callback callback);

	/**
	 * Removes specified callback from this callback. Implemented in {@link org.javasimon.CompositeCallback}.
	 *
	 * @param callback removed child-callback
	 */
	void removeCallback(Callback callback);

	/**
	 * Stopwatch start event.
	 *
	 * @param split started Split
	 */
	void stopwatchStart(Split split);

	/**
	 * Stopwatch stop event.
	 *
	 * @param split stopped Split
	 */
	void stopwatchStop(Split split);

	/**
	 * Warning event containing warning and/or cause. Either cause or warning (or both) should be provided
	 * otherwise the method does nothing.
	 *
	 * @param warning arbitrary warning message
	 * @param cause exception causing this warning
	 */
	void warning(String warning, Exception cause);

	/**
	 * Simon reset event.
	 *
	 * @param simon reset Simon
	 */
	void reset(Simon simon);

	/**
	 * Stopwatch add time event.
	 *
	 * @param stopwatch modified Stopwatch
	 * @param ns added split time in ns
	 */
	void stopwatchAdd(Stopwatch stopwatch, long ns);

	/**
	 * Counter decrease event.
	 *
	 * @param counter modified Counter
	 * @param dec decrement amount
	 */
	void counterDecrease(Counter counter, long dec);

	/**
	 * Counter increase event.
	 *
	 * @param counter modified Counter
	 * @param inc increment amount
	 */
	void counterIncrease(Counter counter, long inc);

	/**
	 * Counter set event.
	 *
	 * @param counter modified Counter
	 * @param val new value
	 */
	void counterSet(Counter counter, long val);

	enum Event {
		ALL,
		RESET,
		STOPWATCH_START,
		STOPWATCH_STOP,
		STOPWATCH_ADD,
		COUNTER_INCREASE,
		COUNTER_DECREASE,
		COUNTER_SET,
		WARNING
	}
}
