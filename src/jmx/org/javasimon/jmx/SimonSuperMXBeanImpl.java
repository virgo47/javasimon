package org.javasimon.jmx;

import org.javasimon.Sample;
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
public abstract class SimonSuperMXBeanImpl implements SimonSuperMXBean {
	public String getName() {
		return simon().getName();
	}

	public String getParentName() {
		if (simon().getParent() != null) {
			return simon().getParent().getName();
		}
		return null;
	}

	public List<String> getChildrenNames() {
		List<String> names = new ArrayList<String>();
		for (Simon child : simon().getChildren()) {
			names.add(child.getName());
		}
		return names;
	}

	public String getState() {
		return simon().getState().name();
	}

	public void setState(String state, boolean overrule) {
		simon().setState(SimonState.valueOf(state), overrule);
	}

	public boolean isEnabled() {
		return simon().isEnabled();
	}

	public void reset() {
		simon().reset();
	}

	public String getNote() {
		return simon().getNote();
	}

	public void setNote(String note) {
		simon().setNote(note);
	}

	public long getFirstUsage() {
		return simon().getFirstUsage();
	}

	public long getLastUsage() {
		return simon().getLastUsage();
	}

	public Sample sample() {
		return simon().sample();
	}

	public Sample sampleAndReset() {
		return simon().sampleAndReset();
	}

	protected abstract Simon simon();
}
