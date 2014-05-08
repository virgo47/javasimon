package org.javasimon;

import org.javasimon.callback.CompositeCallback;

import java.util.Collection;

/**
 * {@link Manager} implementation that supports {@link #enable()} and {@link #disable()} and switches between
 * backing {@link EnabledManager} and {@link DisabledManager} accordingly.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class SwitchingManager implements Manager {

	private Manager enabled = new EnabledManager();

	private Manager disabled = new DisabledManager();

	private Manager manager = enabled;

	@Override
	public Simon getRootSimon() {
		return manager.getRootSimon();
	}

	@Override
	public Simon getSimon(String name) {
		return manager.getSimon(name);
	}

	@Override
	public Counter getCounter(String name) {
		return manager.getCounter(name);
	}

	@Override
	public Stopwatch getStopwatch(String name) {
		return manager.getStopwatch(name);
	}

	@Override
	public Collection<String> getSimonNames() {
		return manager.getSimonNames();
	}

	@Override
	public Collection<Simon> getSimons(SimonFilter simonFilter) {
		return manager.getSimons(simonFilter);
	}

	@Override
	public void destroySimon(String name) {
		manager.destroySimon(name);
	}

	@Override
	public void clear() {
		manager.clear();
	}

	@Override
	public CompositeCallback callback() {
		return manager.callback();
	}

	@Override
	public ManagerConfiguration configuration() {
		return manager.configuration();
	}

	/** Enables the Simon Manager. Enabled manager provides real Simons. */
	@Override
	public void enable() {
		manager = enabled;
	}

	/** Disables the Simon Manager. Disabled manager provides null Simons that actually do nothing. */
	@Override
	public void disable() {
		manager = disabled;
	}

	/**
	 * Returns true if the Java Simon API is enabled.
	 *
	 * @return true if the API is enabled
	 */
	@Override
	public boolean isEnabled() {
		return manager == enabled;
	}

	@Override
	public void message(String message) {
		manager.message(message);
	}

	@Override
	public void warning(String warning, Exception cause) {
		manager.warning(warning, cause);
	}

	@Override
	public long milliTime() {
		return manager.milliTime();
	}

	@Override
	public long nanoTime() {
		return manager.nanoTime();
	}

	@Override
	public long millisForNano(long nanos) {
		return manager.millisForNano(nanos);
	}

	void purgeIncrementalSimonsOlderThan(long thresholdMs) {
		// Store reference to current manager to avoid check-than-act
		Manager currentManager = manager;

		if (currentManager instanceof EnabledManager) {
			((EnabledManager) currentManager).purgeIncrementalSimonsOlderThan(thresholdMs);
		}
	}
}
