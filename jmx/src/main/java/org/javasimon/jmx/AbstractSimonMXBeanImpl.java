package org.javasimon.jmx;

import org.javasimon.Simon;
import org.javasimon.SimonState;

import java.util.List;
import java.util.ArrayList;

/**
 * SimonSuperMXBeanImpl.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Mar 6, 2009
 */
public abstract class AbstractSimonMXBeanImpl implements SimonSuperMXBean {
	public final String getName() {
		return simon().getName();
	}

	public final String getParentName() {
		if (simon().getParent() != null) {
			return simon().getParent().getName();
		}
		return null;
	}

	public final List<String> getChildrenNames() {
		List<String> names = new ArrayList<String>();
		for (Simon child : simon().getChildren()) {
			names.add(child.getName());
		}
		return names;
	}

	public final String getState() {
		return simon().getState().name();
	}

	public final void setState(String state, boolean overrule) {
		simon().setState(SimonState.valueOf(state), overrule);
	}

	public final boolean isEnabled() {
		return simon().isEnabled();
	}

	public final void reset() {
		simon().reset();
	}

	public final String getNote() {
		return simon().getNote();
	}

	public final void setNote(String note) {
		simon().setNote(note);
	}

	public final long getFirstUsage() {
		return simon().getFirstUsage();
	}

	public final long getLastUsage() {
		return simon().getLastUsage();
	}

	protected abstract Simon simon();
}
