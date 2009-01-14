package org.javasimon;

import java.util.Collection;

/**
 * SwitchingManager.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Jan 14, 2009
 */
public final class SwitchingManager implements Manager {
	private Manager enabled = new EnabledManager();

	private Manager disabled = new DisabledManager();

	private Manager manager = enabled;

	/**
	 * {@inheritDoc}
	 */
	public Simon getRootSimon() {
		return manager.getRootSimon();
	}

	/**
	 * {@inheritDoc}
	 */
	public Simon getSimon(String name) {
		return manager.getSimon(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter getCounter(String name) {
		return manager.getCounter(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Stopwatch getStopwatch(String name) {
		return manager.getStopwatch(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Simon getUnknown(String name) {
		return manager.getUnknown(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<String> simonNames() {
		return manager.simonNames();
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroySimon(String name) {
		manager.destroySimon(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		manager.clear();
	}


	/**
	 * Enables the Simon Manager. Enabled manager provides real Simons.
	 */
	public void enable() {
		manager = enabled;
	}

	/**
	 * Disables the Simon Manager. Disabled manager provides null Simons that actually do nothing.
	 */
	public void disable() {
		manager = disabled;
	}

	/**
	 * Returns true if the Java Simon API is enabled.
	 *
	 * @return true if the API is enabled
	 */
	public boolean isEnabled() {
		return manager == enabled;
	}
}
