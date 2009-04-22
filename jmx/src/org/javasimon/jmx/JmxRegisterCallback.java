package org.javasimon.jmx;

import org.javasimon.*;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.JMException;
import java.util.HashSet;
import java.util.Set;
import java.lang.management.ManagementFactory;

/**
 * JmxRegisterCallback.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Mar 6, 2009
 */
public class JmxRegisterCallback extends CallbackSkeleton {
	MBeanServer mBeanServer;
	Set<String> registeredNames = new HashSet<String>();

	public JmxRegisterCallback() {
		this.mBeanServer = ManagementFactory.getPlatformMBeanServer();
	}

	@Override
	public void simonCreated(Simon simon) {
		if (simon.getName() == null) {
			return;
		}
		if (simon instanceof Counter) {
			register(new CounterMXBeanImpl((Counter) simon));
		} else if (simon instanceof Stopwatch) {
			register(new StopwatchMXBeanImpl((Stopwatch) simon));
		} else {
			warning("Unknown type of Simon! " + simon, null);
		}
	}

	@Override
	public void simonDestroyed(Simon simon) {
		unregister(simon.getName());
	}

	@Override
	public void clear() {
		for (String name : registeredNames) {
			unregister(name);
		}
	}

	private void register(SimonSuperMXBean simonMxBean) {
		String name = simonMxBean.getName() + ":type=" + simonMxBean.getType();
		try {
			ObjectName objectName = new ObjectName(name);
			if (mBeanServer.isRegistered(objectName)) {
				mBeanServer.unregisterMBean(objectName);
			} else {
				registeredNames.add(name);
			}
			mBeanServer.registerMBean(simonMxBean, objectName);
			message("Simon registered under the name: " + objectName);
		} catch (JMException e) {
			warning("JMX registration failed for: " + name, e);
			registeredNames.remove(name);
		}
	}

	private void unregister(String name) {
		try {
			ObjectName objectName = new ObjectName(name);
			mBeanServer.unregisterMBean(objectName);
			registeredNames.remove(name);
			message("Unregistered Simon with the name: " + objectName);
		} catch (JMException e) {
			warning("JMX unregistration failed for: " + name, e);
		}
	}
}
