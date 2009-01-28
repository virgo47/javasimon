package org.javasimon;

import java.util.Collection;

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
	Collection<Callback> callbacks();

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
}
