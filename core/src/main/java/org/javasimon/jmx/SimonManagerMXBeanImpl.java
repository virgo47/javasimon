package org.javasimon.jmx;

import java.util.Arrays;
import java.util.Collection;

import org.javasimon.*;
import org.javasimon.utils.SimonUtils;

/**
 * Simon MXBean implementation. See {@link SimonManagerMXBean} for interface
 * documentation.
 * Most methods are implemented by calling the {@link Manager}.
 *
 * @author Radovan Sninsky
 * @see Manager
 * @see Simon
 * @see SimonUtils
 * @since 2.0
 */
public class SimonManagerMXBeanImpl implements SimonManagerMXBean {
	private Manager manager;

	/**
	 * MXBean constructor.
	 *
	 * @param manager instance of {@link Manager}, typically {@code SimonManager.manager()}.
	 */
	public SimonManagerMXBeanImpl(Manager manager) {
		this.manager = manager;
	}

	@Override
	public final void enable() {
		manager.enable();
	}

	@Override
	public final void disable() {
		manager.disable();
	}

	@Override
	public final boolean isEnabled() {
		return manager.isEnabled();
	}

	@Override
	public final String[] getSimonNames() {
		Collection<String> simonNames = manager.getSimonNames();
		return simonNames.toArray(new String[simonNames.size()]);
	}

	@Override
	public final String[] getSimonNamesOrdered() {
		String[] simonNames = getSimonNames();
		Arrays.sort(simonNames);
		return simonNames;
	}

	@Override
	public final String getType(String name) {
		Simon s = manager.getSimon(name);
		return s != null ? s instanceof Stopwatch ? SimonInfo.STOPWATCH : SimonInfo.COUNTER : null;
	}

	@Override
	public final SimonInfo[] getSimonInfos() {
		String[] simonNames = getSimonNamesOrdered();
		SimonInfo[] simonInfo = new SimonInfo[simonNames.length];
		int i = 0;
		for (String name : simonNames) {
			Simon s = manager.getSimon(name);
			simonInfo[i++] = new SimonInfo(name, s instanceof Stopwatch ? SimonInfo.STOPWATCH :
				s instanceof Counter ? SimonInfo.COUNTER : SimonInfo.UNKNOWN);
		}
		return simonInfo;
	}

	@Override
	public final void clearManager() {
		manager.clear();
	}

	@Override
	public final void enableSimon(String name) {
		manager.getSimon(name).setState(SimonState.ENABLED, false);
	}

	@Override
	public final void disableSimon(String name) {
		manager.getSimon(name).setState(SimonState.DISABLED, false);
	}

	@Override
	public final void inheritState(String name) {
		manager.getSimon(name).setState(SimonState.INHERIT, false);
	}

	@Override
	public final CounterSample getCounterSample(String name) {
		Simon s = manager.getSimon(name);
		if (s != null && s instanceof Counter) {
			return new CounterSample((org.javasimon.CounterSample) s.sample());
		}
		return null;
	}

	@Override
	public final StopwatchSample getStopwatchSample(String name) {
		Simon s = manager.getSimon(name);
		if (s != null && s instanceof Stopwatch) {
			return new StopwatchSample((org.javasimon.StopwatchSample) s.sample());
		}
		return null;
	}

	@Override
	public final CounterSample getCounterSampleAndReset(String name) {
		Simon s = manager.getSimon(name);
		if (s != null && s instanceof Counter) {
			return new CounterSample((org.javasimon.CounterSample) s.sampleAndReset());
		}
		return null;
	}

	@Override
	public final StopwatchSample getStopwatchSampleAndReset(String name) {
		Simon s = manager.getSimon(name);
		if (s != null && s instanceof Stopwatch) {
			return new StopwatchSample((org.javasimon.StopwatchSample) s.sampleAndReset());
		}
		return null;
	}

	@Override
	public final void printSimonTree() {
		System.out.println(SimonUtils.simonTreeString(manager.getRootSimon()));
	}

	@Override
	public final void reset(String name) {
		manager.getSimon(name).reset();
	}
}
