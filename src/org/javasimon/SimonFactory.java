package org.javasimon;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * SimonFactory.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public final class SimonFactory {
	public static final String HIERARCHY_DELIMITER = ".";

	private static final String TOP_SIMON_NAME = "";

	private static final TopSimon top = new TopSimon(TOP_SIMON_NAME);

	private static final Map<String, Simon> allSimons = new HashMap<String, Simon>();

	private static boolean enabled = true;

	static {
		reset();
	}

	/**
	 * Returns Simon by its name if it exists.
	 *
	 * @param name name of the Simon
	 * @return simon object
	 */
	public static Simon getSimon(String name) {
		top.start();
		Simon simon = availabilityDecorator(allSimons.get(name));
		top.stop();
		return simon;
	}

	/**
	 * Returns existing SimonCounter or creates new if necessary.
	 *
	 * @param name name of the Counter
	 * @return counter object
	 */
	public static SimonCounter getCounter(String name) {
		top.start();
		Simon simon = getOrCreateSimon(name, SimonCounterImpl.class);
		top.stop();
		return (SimonCounter) simon;
	}

	/**
	 * Returns existing SimonStopwatch or creates new if necessary.
	 *
	 * @param name name of the Stopwatch
	 * @return stopwatch object
	 */
	public static SimonStopwatch getStopwatch(String name) {
		top.start();
		Simon simon = getOrCreateSimon(name, SimonStopwatchImpl.class);
		top.stop();
		return (SimonStopwatch) simon;
	}

	static Simon availabilityDecorator(Simon simon) {
		if (!simon.isEnabled()) {
			simon = new DisabledSimon(simon);
		}
		return simon;
	}

	private static Simon getOrCreateSimon(String name, Class<? extends Simon> simonClass) {
		Simon simon = allSimons.get(name);
		if (simon == null || simon instanceof UnknownSimon) {
			simon = newSimon(name, simonClass);
		} else {
			if (!(simonClass.isInstance(simon))) {
				throw new SimonException("Simon named '" + name + "' already exists and its type is '" + simon.getClass().getName() + "' while requested type is '" + simonClass.getName() + "'.");
			}
		}
		return availabilityDecorator(simon);
	}

	private static Simon newSimon(String name, Class<? extends Simon> simonClass) {
		Simon simon;
		try {
			Constructor<? extends Simon> constructor = simonClass.getConstructor(String.class);
			simon = constructor.newInstance(name);
			addToHierarchy(simon,  name);
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

	private static void addToHierarchy(Simon simon, String name) {
		allSimons.put(name, simon);
		int ix = name.lastIndexOf(HIERARCHY_DELIMITER);
		Simon parent = top;
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

	public static void enable() {
		enabled = true;
	}

	public static void disable() {
		enabled = false;
	}

	public static boolean isEnabled() {
		return enabled;
	}

	public static SimonStopwatch getTop() {
		return (SimonStopwatch) availabilityDecorator(top);
	}

	public static Map<String, Simon> simonMap() {
		return Collections.unmodifiableMap(allSimons);
	}

	public static void reset() {
		allSimons.clear();
		top.reset();
		allSimons.put(TOP_SIMON_NAME, top);
	}

	public static void main(String[] args) {
		check(allSimons.size() == 1);
		getCounter("org.javasimon.test.counter1").increment();
		check(allSimons.size() == 5);

		check(getSimon("org.javasimon.test") instanceof UnknownSimon);
		SimonCounter counter2 = getCounter("org.javasimon.test");
		check(getSimon("org.javasimon.test") instanceof SimonCounter);

		getTop().disable();
		check(!getTop().isEnabled());
		check(!getCounter("org.javasimon.test.counter1").isEnabled());

		getCounter("org.javasimon.test.counter1").enable();
		check(getCounter("org.javasimon.test.counter1").isEnabled());
		check(!getCounter("org.javasimon.test").isEnabled());

		getCounter("org.javasimon.test.counter1").inheritState();
		check(!getCounter("org.javasimon.test.counter1").isEnabled());
		check(!getCounter("org.javasimon.test").isEnabled());

		getCounter("org.javasimon.test.counter1").disable();
		
		check(false);
	}

	private static void check(boolean assertion) {
		if (!assertion) {
			System.out.println("Problem on: " + Thread.currentThread().getStackTrace()[2]);
		}
	}
}
