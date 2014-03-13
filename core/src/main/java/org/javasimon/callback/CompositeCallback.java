package org.javasimon.callback;

import java.util.List;

/**
 * Composite {@link Callback} can hold more callbacks which allows to form callback trees where events are passed to
 * sub-callbacks. Events can be filtered using {@link FilterCallback} which may be set up to pass only specific
 * events under certain circumstances.
 * This can be configured via Manager configuration facility. (Configuration part is still rather WIP.)
 * <p/>
 * Callback tree has no correlation with Simon tree in the {@link org.javasimon.Manager}).
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 3.2
 */
public interface CompositeCallback extends Callback {

	/**
	 * Returns the list of all child-callbacks.
	 *
	 * @return children list
	 */
	List<Callback> callbacks();

	/**
	 * Adds another callback as a child to this callback.
	 *
	 * @param callback added callback
	 */
	void addCallback(Callback callback);

	/**
	 * Removes specified callback from this callback.
	 *
	 * @param callback removed child-callback
	 */
	void removeCallback(Callback callback);

	/** Removes all callbacks from this callback. */
	void removeAllCallbacks();
}
