package org.javasimon;

import org.javasimon.utils.SimonUtils;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * EnabledManager implements methods called from SimonManager if the Simon API is enabled.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 16, 2008
 */
public class EnabledManager implements Manager {
	private final Map<String, AbstractSimon> allSimons = new HashMap<String, AbstractSimon>();

	private UnknownSimon rootSimon;

	public EnabledManager() {
		rootSimon = new UnknownSimon(ROOT_SIMON_NAME);
		allSimons.put(ROOT_SIMON_NAME, rootSimon);
	}

	/**
	 * {@inheritDoc}
	 */
	public Simon getSimon(String name) {
		return allSimons.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void destroySimon(String name) {
		if (name.equals(ROOT_SIMON_NAME)) {
			throw new SimonException("Root Simon cannot be destroyed!");
		}
		AbstractSimon simon = allSimons.remove(name);
		if (simon.getChildren().size() > 0) {
			replaceSimon(simon, UnknownSimon.class);
		} else {
			((AbstractSimon) simon.getParent()).replaceChild(simon, null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void clear() {
		allSimons.clear();
		rootSimon = new UnknownSimon(ROOT_SIMON_NAME);
		allSimons.put(ROOT_SIMON_NAME, rootSimon);
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter getCounter(String name) {
		return (Counter) getOrCreateSimon(name, CounterImpl.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Stopwatch getStopwatch(String name) {
		return (Stopwatch) getOrCreateSimon(name, StopwatchImpl.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public UnknownSimon getUnknown(String name) {
		return (UnknownSimon) getOrCreateSimon(name, UnknownSimon.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Simon getRootSimon() {
		return rootSimon;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<String> simonNames() {
		return allSimons.keySet();
	}

	// name can be null in case of "anonymous" Simons
	private synchronized Simon getOrCreateSimon(String name, Class<? extends AbstractSimon> simonClass) {
		AbstractSimon simon = null;
		if (name != null) {
			if (name.equals(ROOT_SIMON_NAME)) {
				throw new SimonException("Root Simon cannot be replaced or recreated!");
			}
			simon = allSimons.get(name);
		}
		if (simon == null) {
			if (name != null && !SimonUtils.checkName(name)) {
				throw new SimonException("Simon name must match following pattern: '" + SimonUtils.NAME_PATTERN.pattern() + '\'');
			}
			simon = newSimon(name, simonClass);
		} else if (simon instanceof UnknownSimon) {
			simon = replaceSimon(simon, simonClass);
		} else {
			if (!(simonClass.isInstance(simon))) {
				throw new SimonException("Simon named '" + name + "' already exists and its type is '" + simon.getClass().getName() + "' while requested type is '" + simonClass.getName() + "'.");
			}
		}
		return simon;
	}

	private AbstractSimon replaceSimon(AbstractSimon simon, Class<? extends AbstractSimon> simonClass) {
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

	private AbstractSimon newSimon(String name, Class<? extends AbstractSimon> simonClass) {
		AbstractSimon simon = instantiateSimon(name, simonClass);
		if (name != null) {
			addToHierarchy(simon, name);
		}
		return simon;
	}

	private AbstractSimon instantiateSimon(String name, Class<? extends AbstractSimon> simonClass) {
		AbstractSimon simon;
		try {
			Constructor<? extends AbstractSimon> constructor = simonClass.getDeclaredConstructor(String.class);
			simon = constructor.newInstance(name);
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
		allSimons.put(name, simon);
		int ix = name.lastIndexOf(HIERARCHY_DELIMITER);
		AbstractSimon parent = rootSimon;
		if (ix != -1) {
			String parentName = name.substring(0, ix);
			parent = allSimons.get(parentName);
			if (parent == null) {
				parent = new UnknownSimon(parentName);
				addToHierarchy(parent, parentName);
			}
		}
		parent.addChild(simon);
	}
}
