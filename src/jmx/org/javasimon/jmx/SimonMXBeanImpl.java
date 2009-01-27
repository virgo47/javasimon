package org.javasimon.jmx;

import org.javasimon.*;
import org.javasimon.utils.SimonUtils;

import java.util.Collection;

/**
 * Simon MXBean implementation. See {@link org.javasimon.jmx.SimonMXBean} for interface
 * documentation.
 * Most methods are implemented by calling SimonManager.
 *
 * @author Radovan Sninsky
 * @version $Revision: 275 $ $Date: 2008-12-07 12:07:45 +0100 (Ne, 07 dec 2008) $
 * @created 15.7.2008 23:19:50
 * @since 2
 * @see SimonManager
 * @see Simon
 * @see SimonUtils
 */
public class SimonMXBeanImpl implements SimonMXBean {

	public void enable() {
		SimonManager.enable();
	}

	public void disable() {
		SimonManager.disable();
	}

	public boolean isEnabled() {
		return SimonManager.isEnabled();
	}

	public String[] getSimonNames() {
		return SimonManager.simonNames().toArray(new String[SimonManager.simonNames().size()]);
	}

	public String getType(String name) {
		Simon s = SimonManager.getSimon(name);
		return s != null ? s instanceof Stopwatch ? SimonInfo.STOPWATCH : SimonInfo.COUNTER : null;
	}

	public SimonInfo[] getSimonInfos() {
		Collection<String> sn = SimonManager.simonNames();
		SimonInfo[] si = new SimonInfo[sn.size()];
		int i=0;
		for (String name : sn) {
			Simon s = SimonManager.getSimon(name);
			si[i++] = new SimonInfo(name, s instanceof Stopwatch ? SimonInfo.STOPWATCH :
				s instanceof Counter ? SimonInfo.COUNTER : SimonInfo.UNKNOWN);
		}
		return si;
	}

	public void clear() {
		SimonManager.clear();
	}

	public void enableSimon(String name) {
		SimonManager.getSimon(name).setState(SimonState.ENABLED, false);
	}

	public void disableSimon(String name) {
		SimonManager.getSimon(name).setState(SimonState.DISABLED, false);
	}

	public CounterSample getCounterSample(String name) {
		Simon s = SimonManager.getSimon(name);
		if (s != null && s instanceof Counter) {
			return new CounterSample((org.javasimon.CounterSample)s.sample());
		}
		return null;
	}

	public StopwatchSample getStopwatchSample(String name) {
		Simon s = SimonManager.getSimon(name);
		if (s != null && s instanceof Stopwatch) {
			return new StopwatchSample((org.javasimon.StopwatchSample)s.sample());
		}
		return null;
	}

	public void printSimonTree() {
		System.out.println(SimonUtils.simonTreeString(SimonManager.getRootSimon()));
	}
}
