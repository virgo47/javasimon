package org.javasimon;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Composite callbacks holds child-callbacks and delegates any operations to all of them.
 * It implements {@link #callbacks()}, {@link #addCallback(Callback)} and {@link #removeCallback(Callback)}.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Jan 22, 2009
 */
public final class CompositeCallback implements Callback {
	private List<Callback> callbacks = new CopyOnWriteArrayList<Callback>();

	/**
	 * Returns the list of all child-callbacks.
	 *
	 * @return children list
	 */
	public Collection<Callback> callbacks() {
		return callbacks;
	}

	/**
	 * Adds another callback as a child to this callback.
	 *
	 * @param callback added callback
	 */
	public void addCallback(Callback callback) {
		callbacks.add(callback);
	}

	/**
	 * Removes specified callback from this callback.
	 *
	 * @param callback removed child-callback
	 */
	public void removeCallback(Callback callback) {
		callbacks.remove(callback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopwatchStart(Split split) {
		for (Callback c : callbacks) {
			c.stopwatchStart(split);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopwatchStop(Split split) {
		for (Callback c : callbacks) {
			c.stopwatchStop(split);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void warning(String warning, Exception cause) {
		for (Callback c : callbacks) {
			c.warning(warning, cause);
		}
	}
}