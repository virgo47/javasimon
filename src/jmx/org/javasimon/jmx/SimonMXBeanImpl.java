package org.javasimon.jmx;

import org.javasimon.*;
import org.javasimon.utils.SimonUtils;

import java.util.Collection;

/**
 * Simon MXBean implementation. See {@link org.javasimon.jmx.SimonMXBean} for interface
 * documentation.
 * Most methods are implemented by calling the Manager.
 *
 * @author Radovan Sninsky
 * @version $Revision: 275 $ $Date: 2008-12-07 12:07:45 +0100 (Ne, 07 dec 2008) $
 * @created 15.7.2008 23:19:50
 * @since 2
 * @see Manager
 * @see Simon
 * @see SimonUtils
 */
public class SimonMXBeanImpl implements SimonMXBean {
	private Manager manager;

	public SimonMXBeanImpl(Manager manager) {
		this.manager = manager;
	}

	public void enable() {
		manager.enable();
	}

	public void disable() {
		manager.disable();
	}

	public boolean isEnabled() {
		return manager.isEnabled();
	}

	public String[] getSimonNames() {
		return manager.simonNames().toArray(new String[manager.simonNames().size()]);
	}

	public String getType(String name) {
		Simon s = manager.getSimon(name);
		return s != null ? s instanceof Stopwatch ? SimonInfo.STOPWATCH : SimonInfo.COUNTER : null;
	}

	public SimonInfo[] getSimonInfos() {
		Collection<String> sn = manager.simonNames();
		SimonInfo[] si = new SimonInfo[sn.size()];
		int i=0;
		for (String name : sn) {
			Simon s = manager.getSimon(name);
			si[i++] = new SimonInfo(name, s instanceof Stopwatch ? SimonInfo.STOPWATCH :
				s instanceof Counter ? SimonInfo.COUNTER : SimonInfo.UNKNOWN);
		}
		return si;
	}

	public void clear() {
		manager.clear();
	}

	public void enableSimon(String name) {
		manager.getSimon(name).setState(SimonState.ENABLED, false);
	}

	public void disableSimon(String name) {
		manager.getSimon(name).setState(SimonState.DISABLED, false);
	}

	public CounterSample getCounterSample(String name) {
		Simon s = manager.getSimon(name);
		if (s != null && s instanceof Counter) {
			return new CounterSample((org.javasimon.CounterSample)s.sample());
		}
		return null;
	}

	public StopwatchSample getStopwatchSample(String name) {
		Simon s = manager.getSimon(name);
		if (s != null && s instanceof Stopwatch) {
			return new StopwatchSample((org.javasimon.StopwatchSample)s.sample());
		}
		return null;
	}

	public void printSimonTree() {
		System.out.println(SimonUtils.simonTreeString(manager.getRootSimon()));
	}
}
