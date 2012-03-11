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
public final class CompositeCallbackImpl implements CompositeCallback {
	private List<Callback> callbacks = new CopyOnWriteArrayList<Callback>();

	private boolean initialized; // should also indicate whether this callback is joined to manager

	/**
	 * Returns the list of all child-callbacks.
	 *
	 * @return children list
	 */
	@Override
	public List<Callback> callbacks() {
		return callbacks;
	}

	/**
	 * Adds another callback as a child to this callback.
	 *
	 * @param callback added callback
	 */
	@Override
	public void addCallback(Callback callback) {
		if (initialized) {
			callback.initialize();
		}
		callbacks.add(callback);
	}

	/**
	 * Removes specified callback from this callback, properly cleans up the removed callback.
	 *
	 * @param callback removed child-callback
	 */
	@Override
	public void removeCallback(Callback callback) {
		callbacks.remove(callback);
		if (initialized) {
			callback.cleanup();
		}
	}

	/**
	 * Removes specified callback from this callback, properly cleans up all the removed callbacks.
	 */
	@Override
	public void removeAllCallbacks() {
		for (Callback callback : callbacks) {
			removeCallback(callback);
		}
	}

	/**
	 * Calls initialize on all children.
	 */
	@Override
	public void initialize() {
		initialized = true;
		for (Callback callback : callbacks) {
			try {
				callback.initialize();
			} catch (Exception e) {
				onManagerWarning("Callback initialization error", e);
			}
		}
	}

	/**
	 * Calls deactivate on all children.
	 */
	@Override
	public void cleanup() {
		initialized = false;
		for (Callback callback : callbacks) {
			try {
				callback.cleanup();
			} catch (Exception e) {
				onManagerWarning("Callback cleanup error", e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSimonReset(Simon simon) {
		for (Callback callback : callbacks) {
			callback.onSimonReset(simon);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStopwatchAdd(Stopwatch stopwatch, long ns) {
		for (Callback callback : callbacks) {
			callback.onStopwatchAdd(stopwatch, ns);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStopwatchAdd(Stopwatch stopwatch, Split split) {
		for (Callback callback : callbacks) {
			callback.onStopwatchAdd(stopwatch, split);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStopwatchStart(Split split) {
		for (Callback callback : callbacks) {
			callback.onStopwatchStart(split);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStopwatchStop(Split split) {
		for (Callback callback : callbacks) {
			callback.onStopwatchStop(split);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCounterDecrease(Counter counter, long dec) {
		for (Callback callback : callbacks) {
			callback.onCounterDecrease(counter, dec);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCounterIncrease(Counter counter, long inc) {
		for (Callback callback : callbacks) {
			callback.onCounterIncrease(counter, inc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCounterSet(Counter counter, long val) {
		for (Callback callback : callbacks) {
			callback.onCounterSet(counter, val);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSimonCreated(Simon simon) {
		for (Callback callback : callbacks) {
			callback.onSimonCreated(simon);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSimonDestroyed(Simon simon) {
		for (Callback callback : callbacks) {
			callback.onSimonDestroyed(simon);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onManagerClear() {
		for (Callback callback : callbacks) {
			callback.onManagerClear();
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onManagerMessage(String message) {
		for (Callback callback : callbacks) {
			callback.onManagerMessage(message);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onManagerWarning(String warning, Exception cause) {
		for (Callback callback : callbacks) {
			callback.onManagerWarning(warning, cause);
		}
	}
}