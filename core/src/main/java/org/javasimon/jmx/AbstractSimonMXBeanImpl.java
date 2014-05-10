package org.javasimon.jmx;

import org.javasimon.Simon;
import org.javasimon.SimonState;
import org.javasimon.utils.SimonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Common functionality for JMX bean for a single Simon that corresponds to AbstractSimon in the core package.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public abstract class AbstractSimonMXBeanImpl implements SimonSuperMXBean {

	@Override
	public final String getName() {
		return simon().getName();
	}

	@Override
	public final String getParentName() {
		if (simon().getParent() != null) {
			return simon().getParent().getName();
		}
		return null;
	}

	@Override
	public final List<String> getChildrenNames() {
		List<String> names = new ArrayList<>();
		for (Simon child : simon().getChildren()) {
			names.add(child.getName());
		}
		return names;
	}

	@Override
	public final String getState() {
		return simon().getState().name();
	}

	@Override
	public final void setState(String state, boolean overrule) {
		simon().setState(SimonState.valueOf(state), overrule);
	}

	@Override
	public final boolean isEnabled() {
		return simon().isEnabled();
	}

	@Override
	public final String getNote() {
		return simon().getNote();
	}

	@Override
	public final void setNote(String note) {
		simon().setNote(note);
	}

	@Override
	public final long getFirstUsage() {
		return simon().getFirstUsage();
	}

	@Override
	public String getFirstUsageAsString() {
		return SimonUtils.presentTimestamp(getFirstUsage());
	}

	@Override
	public final long getLastUsage() {
		return simon().getLastUsage();
	}

	@Override
	public final String getLastUsageAsString() {
		return SimonUtils.presentTimestamp(getLastUsage());
	}

	/**
	 * Returns the wrapped Simon.
	 *
	 * @return wrapped Simon
	 */
	protected abstract Simon simon();
}
