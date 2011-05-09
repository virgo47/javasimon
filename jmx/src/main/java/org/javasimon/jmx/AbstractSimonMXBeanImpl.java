package org.javasimon.jmx;

import org.javasimon.Simon;
import org.javasimon.SimonState;
import org.javasimon.utils.SimonUtils;

import java.util.List;
import java.util.ArrayList;

/**
 * Common functionality for JMX bean for a signle Simon that corresponds to AbstractSimon
 * in the core package.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public abstract class AbstractSimonMXBeanImpl implements SimonSuperMXBean {
	public final String getName() {
		return simon().getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getParentName() {
		if (simon().getParent() != null) {
			return simon().getParent().getName();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<String> getChildrenNames() {
		List<String> names = new ArrayList<String>();
		for (Simon child : simon().getChildren()) {
			names.add(child.getName());
		}
		return names;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getState() {
		return simon().getState().name();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setState(String state, boolean overrule) {
		simon().setState(SimonState.valueOf(state), overrule);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isEnabled() {
		return simon().isEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void reset() {
		simon().reset();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getNote() {
		return simon().getNote();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setNote(String note) {
		simon().setNote(note);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final long getFirstUsage() {
		return simon().getFirstUsage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFirstUsageAsString() {
		return SimonUtils.presentTimestamp(getFirstUsage());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final long getLastUsage() {
		return simon().getLastUsage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLastUsageAsString() {
		return SimonUtils.presentTimestamp(getLastUsage());
	}

	/**
	 * Returns the wrapped Simon.
	 *
	 * @return wrapped Simon
	 */
	protected abstract Simon simon();
}
