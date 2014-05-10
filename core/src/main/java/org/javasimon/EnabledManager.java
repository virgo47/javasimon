package org.javasimon;

import org.javasimon.callback.CompositeCallback;
import org.javasimon.callback.CompositeCallbackImpl;
import org.javasimon.clock.Clock;
import org.javasimon.utils.SimonUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements fully functional {@link Manager} in the enabled state. Does not support
 * {@link #enable()}/{@link #disable()} - for this use {@link SwitchingManager}.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class EnabledManager implements Manager {

	private UnknownSimon rootSimon;

	private final Map<String, AbstractSimon> allSimons = new ConcurrentHashMap<>();

	private final CompositeCallback callback = new CompositeCallbackImpl();

	private final ManagerConfiguration configuration;

	private final Clock clock;

	/** Creates new enabled manager. */
	public EnabledManager() {
		this(Clock.SYSTEM);
	}

	public EnabledManager(Clock clock) {
		this.clock = clock;
		rootSimon = new UnknownSimon(ROOT_SIMON_NAME, this);
		allSimons.put(ROOT_SIMON_NAME, rootSimon);
		configuration = new ManagerConfiguration(this);
		callback.initialize(this);
	}

	@Override
	public Simon getSimon(String name) {
		return allSimons.get(name);
	}

	@Override
	public synchronized void destroySimon(String name) {
		if (name.equals(ROOT_SIMON_NAME)) {
			throw new SimonException("Root Simon cannot be destroyed!");
		}
		AbstractSimon simon = allSimons.remove(name);
		if (simon.getChildren().size() > 0) {
			replaceUnknownSimon(simon, UnknownSimon.class);
		} else {
			((AbstractSimon) simon.getParent()).replaceChild(simon, null);
		}
		callback.onSimonDestroyed(simon);
	}

	@Override
	public synchronized void clear() {
		allSimons.clear();
		rootSimon = new UnknownSimon(ROOT_SIMON_NAME, this);
		allSimons.put(ROOT_SIMON_NAME, rootSimon);
		callback.onManagerClear();
	}

	@Override
	public Counter getCounter(String name) {
		return (Counter) getOrCreateSimon(name, CounterImpl.class);
	}

	@Override
	public Stopwatch getStopwatch(String name) {
		return (Stopwatch) getOrCreateSimon(name, StopwatchImpl.class);
	}

	@Override
	public Simon getRootSimon() {
		return rootSimon;
	}

	@Override
	public Collection<String> getSimonNames() {
		return Collections.unmodifiableCollection(allSimons.keySet());
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public Collection<Simon> getSimons(SimonFilter simonFilter) {
		if (simonFilter == null) {
			return Collections.unmodifiableCollection((Collection) allSimons.values());
		}
		Collection<Simon> simons = new ArrayList<>();
		for (AbstractSimon simon : allSimons.values()) {
			if (simonFilter.accept(simon)) {
				simons.add(simon);
			}
		}
		return simons;
	}

	private Simon getOrCreateSimon(String name, Class<? extends AbstractSimon> simonClass) {
		if (name == null) {
			// create an "anonymous" Simon - Manager does not care about it anymore
			return instantiateSimon(null, simonClass);
		}
		if (name.equals(ROOT_SIMON_NAME)) {
			throw new SimonException("Root Simon cannot be replaced or recreated!");
		}
		AbstractSimon simon = allSimons.get(name);
		if (simon != null && simonClass.isInstance(simon)) {
			return simon;
		}
		return createOrReplaceUnknownSimon(name, simonClass);
	}

	private synchronized AbstractSimon createOrReplaceUnknownSimon(String name, Class<? extends AbstractSimon> simonClass) {
		// we will rather check the map in synchronized block before we try to create/replace the Simon
		AbstractSimon simon = allSimons.get(name);
		if (simon != null && simonClass.isInstance(simon)) {
			return simon; // the same return like in non-synchronized getOrCreateSimon - you just never know
		}

		if (simon == null) {
			if (name != null && !SimonUtils.checkName(name)) {
				throw new SimonException("Simon name must match following pattern: '" + SimonUtils.NAME_PATTERN.pattern() + "', used name: " + name);
			}
			simon = newSimon(name, simonClass);
		} else if (simon instanceof UnknownSimon) {
			simon = replaceUnknownSimon(simon, simonClass);
		} else {
			throw new SimonException("Simon named '" + name + "' already exists and its type is '" + simon.getClass().getName() + "' while requested type is '" + simonClass.getName() + "'.");
		}
		callback.onSimonCreated(simon);
		return simon;
	}

	// called from synchronized method
	private AbstractSimon replaceUnknownSimon(AbstractSimon simon, Class<? extends AbstractSimon> simonClass) {
		AbstractSimon newSimon = instantiateSimon(simon.getName(), simonClass);
		newSimon.enabled = simon.enabled;

		// fixes parent link and parent's children list
		((AbstractSimon) simon.getParent()).replaceChild(simon, newSimon);

		// fixes children list and all children's parent link
		for (Simon child : simon.getChildren()) {
			newSimon.addChild((AbstractSimon) child);
			((AbstractSimon) child).setParent(newSimon);
		}

		allSimons.put(simon.getName(), newSimon);
		return newSimon;
	}

	// called from synchronized method
	private AbstractSimon newSimon(String name, Class<? extends AbstractSimon> simonClass) {
		AbstractSimon simon = instantiateSimon(name, simonClass);
		if (name != null) {
			addToHierarchy(simon, name);
			SimonConfiguration config = configuration.getConfig(name);
			if (config.getState() != null) {
				simon.setState(config.getState(), false);
			}
			allSimons.put(name, simon);
		}
		return simon;
	}

	private AbstractSimon instantiateSimon(String name, Class<? extends AbstractSimon> simonClass) {
		AbstractSimon simon;
		try {
			Constructor<? extends AbstractSimon> constructor = simonClass.getDeclaredConstructor(String.class, Manager.class);
			simon = constructor.newInstance(name, this);
		} catch (NoSuchMethodException e) {
			throw new SimonException(e);
		} catch (InvocationTargetException e) {
			throw new SimonException(e);
		} catch (IllegalAccessException e) {
			throw new SimonException(e);
		} catch (InstantiationException e) {
			throw new SimonException(e);
		}
		return simon;
	}

	private void addToHierarchy(AbstractSimon simon, String name) {
		int ix = name.lastIndexOf(HIERARCHY_DELIMITER);
		AbstractSimon parent = rootSimon;
		if (ix != -1) {
			String parentName = name.substring(0, ix);
			parent = allSimons.get(parentName);
			if (parent == null) {
				parent = new UnknownSimon(parentName, this);
				addToHierarchy(parent, parentName);
				allSimons.put(parentName, parent);
			}
		}
		parent.addChild(simon);
	}

	@Override
	public CompositeCallback callback() {
		return callback;
	}

	@Override
	public ManagerConfiguration configuration() {
		return configuration;
	}

	/** Throws {@link UnsupportedOperationException}. */
	@Override
	public void enable() {
		throw new UnsupportedOperationException("Only SwitchingManager supports this operation.");
	}

	/** Throws {@link UnsupportedOperationException}. */
	@Override
	public void disable() {
		throw new UnsupportedOperationException("Only SwitchingManager supports this operation.");
	}

	/**
	 * Returns true.
	 *
	 * @return true
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void message(String message) {
		callback.onManagerMessage(message);
	}

	@Override
	public void warning(String warning, Exception cause) {
		callback.onManagerWarning(warning, cause);
	}

	@Override
	public long nanoTime() {
		return clock.nanoTime();
	}

	@Override
	public long milliTime() {
		return clock.milliTime();
	}

	@Override
	public long millisForNano(long nanos) {
		return clock.millisForNano(nanos);
	}

	synchronized void purgeIncrementalSimonsOlderThan(long thresholdMs) {
		for (Simon simon : allSimons.values()) {
			if (simon instanceof AbstractSimon) {
				AbstractSimon abstractSimon = (AbstractSimon) simon;
				abstractSimon.purgeIncrementalSimonsOlderThan(thresholdMs);
			}
		}
	}
}
