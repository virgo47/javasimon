package org.javasimon.callback;

import org.javasimon.Counter;
import org.javasimon.CounterSample;
import org.javasimon.Manager;
import org.javasimon.Simon;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Composite callbacks holds child-callbacks and delegates any operations to all of them.
 * It implements {@link #callbacks()}, {@link #addCallback(Callback)} and {@link #removeCallback(Callback)}.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class CompositeCallbackImpl implements CompositeCallback {

	private List<Callback> callbacks = new CopyOnWriteArrayList<>();

	private Manager manager; // not null indicates, that this callback is initialized (joined to manager)

	/** Calls initialize on all children. */
	@Override
	public synchronized void initialize(Manager manager) {
		if (this.manager != null) {
			throw new IllegalStateException("Callback was already initialized");
		}
		this.manager = manager;
		for (Callback callback : callbacks) {
			try {
				callback.initialize(manager);
			} catch (Exception e) {
				onManagerWarning("Callback initialization error", e);
			}
		}
	}

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
		if (manager != null) {
			callback.initialize(manager);
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
		if (manager != null) {
			callback.cleanup();
		}
	}

	/** Removes specified callback from this callback, properly cleans up all the removed callbacks. */
	@Override
	public void removeAllCallbacks() {
		for (Callback callback : callbacks) {
			removeCallback(callback);
		}
	}

	/** Calls deactivate on all children. */
	@Override
	public void cleanup() {
		manager = null;
		for (Callback callback : callbacks) {
			try {
				callback.cleanup();
			} catch (Exception e) {
				onManagerWarning("Callback cleanup error", e);
			}
		}
	}

	@Override
	public void onStopwatchAdd(Stopwatch stopwatch, Split split, StopwatchSample sample) {
		for (Callback callback : callbacks) {
			callback.onStopwatchAdd(stopwatch, split, sample);
		}
	}

	@Override
	public void onStopwatchStart(Split split) {
		for (Callback callback : callbacks) {
			callback.onStopwatchStart(split);
		}
	}

	@Override
	public void onStopwatchStop(Split split, StopwatchSample sample) {
		for (Callback callback : callbacks) {
			callback.onStopwatchStop(split, sample);
		}
	}

	@Override
	public void onCounterDecrease(Counter counter, long dec, CounterSample sample) {
		for (Callback callback : callbacks) {
			callback.onCounterDecrease(counter, dec, sample);
		}
	}

	@Override
	public void onCounterIncrease(Counter counter, long inc, CounterSample sample) {
		for (Callback callback : callbacks) {
			callback.onCounterIncrease(counter, inc, sample);
		}
	}

	@Override
	public void onCounterSet(Counter counter, long val, CounterSample sample) {
		for (Callback callback : callbacks) {
			callback.onCounterSet(counter, val, sample);
		}
	}

	@Override
	public void onSimonCreated(Simon simon) {
		for (Callback callback : callbacks) {
			callback.onSimonCreated(simon);
		}
	}

	@Override
	public void onSimonDestroyed(Simon simon) {
		for (Callback callback : callbacks) {
			callback.onSimonDestroyed(simon);
		}
	}

	@Override
	public void onManagerClear() {
		for (Callback callback : callbacks) {
			callback.onManagerClear();
		}
	}

	@Override
	public void onManagerMessage(String message) {
		for (Callback callback : callbacks) {
			callback.onManagerMessage(message);
		}
	}

	@Override
	public void onManagerWarning(String warning, Exception cause) {
		for (Callback callback : callbacks) {
			callback.onManagerWarning(warning, cause);
		}
	}
}