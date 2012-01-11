package org.javasimon.callback;

import org.javasimon.Counter;
import org.javasimon.Simon;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Composite callbacks holds child-callbacks and delegates any operations to all of them.
 * It implements {@link #callbacks()}, {@link #addCallback(Callback)} and {@link #removeCallback(Callback)}.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class CompositeCallback implements Callback {
	private List<Callback> callbacks = new CopyOnWriteArrayList<Callback>();

	private boolean initialized; // should also indicate whether this callback is joined to manager

	/**
	 * Returns the list of all child-callbacks.
	 *
	 * @return children list
	 */
	public List<Callback> callbacks() {
		return callbacks;
	}

	/**
	 * Adds another callback as a child to this callback.
	 *
	 * @param callback added callback
	 */
	public void addCallback(Callback callback) {
		if (initialized) {
			callback.initialize();
		}
		callbacks.add(callback);
	}

	/**
	 * Removes specified callback from this callback.
	 *
	 * @param callback removed child-callback
	 */
	public void removeCallback(Callback callback) {
		callbacks.remove(callback);
		if (initialized) {
			callback.cleanup();
		}
	}

	/**
	 * Calls initialize on all children.
	 */
	public void initialize() {
		initialized = true;
		for (Callback c : callbacks) {
			try {
				c.initialize();
			} catch (Exception e) {
				onManagerWarning("Initialization error", e);
			}
		}
	}

	/**
	 * Calls deactivate on all children.
	 */
	public void cleanup() {
		initialized = false;
		for (Callback c : callbacks) {
			try {
				c.cleanup();
			} catch (Exception e) {
				onManagerWarning("Deactivation error", e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void onSimonReset(Simon simon) {
		for (Callback c : callbacks) {
			c.onSimonReset(simon);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void onStopwatchAdd(Stopwatch stopwatch, long ns) {
		for (Callback c : callbacks) {
			c.onStopwatchAdd(stopwatch, ns);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void onStopwatchAdd(Stopwatch stopwatch, Split split) {
		for (Callback c : callbacks) {
			c.onStopwatchAdd(stopwatch, split);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void onStopwatchStart(Split split) {
		for (Callback c : callbacks) {
			c.onStopwatchStart(split);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void onStopwatchStop(Split split) {
		for (Callback c : callbacks) {
			c.onStopwatchStop(split);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void onCounterDecrease(Counter counter, long dec) {
		for (Callback c : callbacks) {
			c.onCounterDecrease(counter, dec);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void onCounterIncrease(Counter counter, long inc) {
		for (Callback c : callbacks) {
			c.onCounterIncrease(counter, inc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void onCounterSet(Counter counter, long val) {
		for (Callback c : callbacks) {
			c.onCounterSet(counter, val);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void onSimonCreated(Simon simon) {
		for (Callback c : callbacks) {
			c.onSimonCreated(simon);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void onSimonDestroyed(Simon simon) {
		for (Callback c : callbacks) {
			c.onSimonDestroyed(simon);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void onManagerClear() {
		for (Callback c : callbacks) {
			c.onManagerClear();
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public void onManagerMessage(String message) {
		for (Callback c : callbacks) {
			c.onManagerMessage(message);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void onManagerWarning(String warning, Exception cause) {
		for (Callback c : callbacks) {
			c.onManagerWarning(warning, cause);
		}
	}
}