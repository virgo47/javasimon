package org.javasimon.jmx;

import org.javasimon.*;
import org.javasimon.utils.SimonUtils;

import java.util.Collection;

/**
 * Simon MXBean implementation. See {@link org.javasimon.jmx.SimonMXBean} for interface
 * documentation.
 * Most methods are implemented by calling the {@link Manager}.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 15.7.2008 23:19:50
 * @since 2
 * @see Manager
 * @see Simon
 * @see SimonUtils
 */
public class SimonMXBeanImpl implements SimonMXBean {

	private Manager manager;

	/**
	 * MXBean constructor.
	 *
	 * @param manager instance of {@link Manager}, typically {@code SimonManager.manager()}.
	 */
	public SimonMXBeanImpl(Manager manager) {
		this.manager = manager;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void enable() {
		manager.enable();
	}

	/**
	 * {@inheritDoc}
	 */
	public final void disable() {
		manager.disable();
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean isEnabled() {
		return manager.isEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	public final String[] getSimonNames() {
		return manager.simonNames().toArray(new String[manager.simonNames().size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	public final String getType(String name) {
		Simon s = manager.getSimon(name);
		return s != null ? s instanceof Stopwatch ? SimonInfo.STOPWATCH : SimonInfo.COUNTER : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public final SimonInfo[] getSimonInfos() {
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

	/**
	 * {@inheritDoc}
	 */
	public final void clear() {
		manager.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public final void enableSimon(String name) {
		manager.getSimon(name).setState(SimonState.ENABLED, false);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void disableSimon(String name) {
		manager.getSimon(name).setState(SimonState.DISABLED, false);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void inheritState(String name) {
		manager.getSimon(name).setState(SimonState.INHERIT, false);
	}

	/**
	 * {@inheritDoc}
	 */
	public final CounterSample getCounterSample(String name) {
		Simon s = manager.getSimon(name);
		if (s != null && s instanceof Counter) {
			return new CounterSample((org.javasimon.CounterSample)s.sample());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public final StopwatchSample getStopwatchSample(String name) {
		Simon s = manager.getSimon(name);
		if (s != null && s instanceof Stopwatch) {
			return new StopwatchSample((org.javasimon.StopwatchSample)s.sample());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void printSimonTree() {
		System.out.println(SimonUtils.simonTreeString(manager.getRootSimon()));
	}
}
