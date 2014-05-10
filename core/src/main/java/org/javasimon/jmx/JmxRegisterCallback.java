package org.javasimon.jmx;

import org.javasimon.Counter;
import org.javasimon.Manager;
import org.javasimon.Simon;
import org.javasimon.Stopwatch;
import org.javasimon.callback.CallbackSkeleton;

import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Callback that registers MXBeans for Simons after their creation. It is
 * advisable to register the callback as soon as possible otherwise MX Beans
 * for some Simons may not be created. Class can be extended in order to
 * override {@link #constructObjectName(Simon)}.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public class JmxRegisterCallback extends CallbackSkeleton {

	/** Domain part of the JMX object name - protected for subclasses. */
	protected String domain;

	/** MBean server instance specified for this callback (or default platform one) - protected for subclasses. */
	protected MBeanServer mBeanServer;

	/** Names of all beans registered for separate Simons */
	private Set<String> registeredNames = new HashSet<>();

	/** Whether all existing Simons from Manager should be registered after callback is added to a Manager. */
	private boolean registerExisting;

	private Manager manager;

	/**
	 * Default constructor uses default MBeanServer.
	 *
	 * @param domain domain part of the object name
	 */
	public JmxRegisterCallback(String domain) {
		this(ManagementFactory.getPlatformMBeanServer(), domain);
	}

	/**
	 * Constructor using specific MBeanServer.
	 *
	 * @param mBeanServer specific MBeanServer
	 * @param domain domain part of the object name
	 */
	public JmxRegisterCallback(MBeanServer mBeanServer, String domain) {
		assert domain != null && !(domain.isEmpty());
		this.mBeanServer = mBeanServer;
		this.domain = domain;
	}

	@Override
	public synchronized void initialize(Manager manager) {
		if (this.manager != null) {
			throw new IllegalStateException("Callback was already initialized");
		}
		this.manager = manager;
		if (registerExisting) {
			for (Simon simon : manager.getSimons(null)) {
				register(simon);
			}
		}
	}

	/**
	 * After Simon is created respective MX bean is registered for it according to its type.
	 *
	 * @param simon created Simon
	 */
	@Override
	public final void onSimonCreated(Simon simon) {
		if (simon.getName() == null) {
			return;
		}
		register(simon);
	}

	/**
	 * When the Simon is destroyed, its MX bean is unregistered.
	 *
	 * @param simon destroyed Simon
	 */
	@Override
	public final void onSimonDestroyed(Simon simon) {
		String name = constructObjectName(simon);
		unregisterSimon(name);
	}

	private synchronized void unregisterSimon(String name) {
		try {
			ObjectName objectName = new ObjectName(name);
			mBeanServer.unregisterMBean(objectName);
			registeredNames.remove(name);
			onManagerMessage("Unregistered Simon with the name: " + objectName);
		} catch (JMException e) {
			onManagerWarning("JMX unregistration failed for: " + name, e);
		}
	}

	/** When the manager is cleared, all MX beans for its Simons are unregistered. */
	@Override
	public final void onManagerClear() {
		unregisterAllSimons();
	}

	/** Unregister all previously registered Simons. */
	private synchronized void unregisterAllSimons() {
		Iterator<String> namesIter = registeredNames.iterator();
		while (namesIter.hasNext()) {
			String name = namesIter.next();
			try {
				ObjectName objectName = new ObjectName(name);
				mBeanServer.unregisterMBean(objectName);
				// I have to use iterator.remove() - hence no common method for clearManager() and simonDestroyed(simon)
				namesIter.remove();
				onManagerMessage("Unregistered Simon with the name: " + objectName);
			} catch (JMException e) {
				onManagerWarning("JMX unregistration failed for: " + name, e);
			}
		}
	}

	/** Stop operation and clear all registered beans. Callback should be removed from Simon Manager. */
	public void cleanup() {
		unregisterAllSimons();
	}

	/**
	 * Method registering Simon MX Bean - can not be overridden, but can be used in subclasses.
	 *
	 * @param simon Simon MX Bean to be registered
	 * @since 3.1
	 */
	protected final void register(Simon simon) {
		Object mBean = constructObject(simon);
		String name = constructObjectName(simon);
		registerSimonBean(mBean, name);
	}

	private synchronized void registerSimonBean(Object simonBean, String name) {
		if (simonBean != null && name != null) {
			try {
				ObjectName objectName = new ObjectName(name);
				if (mBeanServer.isRegistered(objectName)) {
					mBeanServer.unregisterMBean(objectName);
				} else {
					registeredNames.add(name);
				}
				mBeanServer.registerMBean(simonBean, objectName);
				onManagerMessage("Simon registered under the name: " + objectName);
			} catch (JMException e) {
				onManagerWarning("JMX registration failed for: " + name, e);
				registeredNames.remove(name);
			}
		}
	}

	/**
	 * Constructs JMX object from Simon object. Method can be overridden.
	 *
	 * @param simon Simon object
	 * @return JMX object (=MBean) representing the Simon
	 */
	protected SimonSuperMXBean constructObject(Simon simon) {
		SimonSuperMXBean simonMxBean;
		if (simon instanceof Counter) {
			simonMxBean = new CounterMXBeanImpl((Counter) simon);
		} else if (simon instanceof Stopwatch) {
			simonMxBean = new StopwatchMXBeanImpl((Stopwatch) simon);
		} else {
			onManagerWarning("Unknown type of Simon! " + simon, null);
			simonMxBean = null;
		}
		return simonMxBean;
	}

	/**
	 * Constructs JMX object name from Simon object. Method can be overridden.
	 *
	 * @param simon Simon object
	 * @return object name in String form
	 */
	protected String constructObjectName(Simon simon) {
		return domain + ":type=" + simonType(simon) + ",name=" + simon.getName();
	}

	/**
	 * Returns type of the simon as defined in {@link SimonInfo#COUNTER},
	 * {@link SimonInfo#STOPWATCH} or {@link SimonInfo#UNKNOWN}.
	 *
	 * @param simon Simon object
	 * @return type of the Simon as String
	 */
	protected String simonType(Simon simon) {
		String type = SimonInfo.UNKNOWN;
		if (simon instanceof Counter) {
			type = SimonInfo.COUNTER;
		} else if (simon instanceof Stopwatch) {
			type = SimonInfo.STOPWATCH;
		}
		return type;
	}

	String getDomain() {
		return domain;
	}

	MBeanServer getBeanServer() {
		return mBeanServer;
	}

	/**
	 * If set to true before initialization callback registers all Simons already existing in the Manager
	 * during initialization.
	 *
	 * @param registerExisting true if all already existing simons should be registered
	 */
	public void setRegisterExisting(boolean registerExisting) {
		this.registerExisting = registerExisting;
	}
}
