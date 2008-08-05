package org.javasimon;

import java.util.List;

/**
 * DisabledSimon is empty simon with some unsupported operations returned if the whole
 * Simon API is disabled. Returned name is null, isEnabled returns false, simon action methods
 * do nothing or return zero (like disabled decorator) and the rest throw an exception.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 5, 2008
 */
public final class DisabledSimon extends DisabledDecorator {
	public static final DisabledSimon INSTANCE = new DisabledSimon();

	private DisabledSimon() {
		super(null);
	}

	public Simon getParent() {
		throw new UnsupportedOperationException("Not suppported on Simon substitute for disabled API");
	}

	public List<Simon> getChildren() {
		throw new UnsupportedOperationException("Not suppported on Simon substitute for disabled API");
	}

	public void addChild(Simon simon) {
		throw new UnsupportedOperationException("Not suppported on Simon substitute for disabled API");
	}

	public String getName() {
		return null;
	}

	public SimonState getState() {
		throw new UnsupportedOperationException("Not suppported on Simon substitute for disabled API");
	}

	public void enable() {
		throw new UnsupportedOperationException("Not suppported on Simon substitute for disabled API");
	}

	public void disable() {
		throw new UnsupportedOperationException("Not suppported on Simon substitute for disabled API");
	}

	public void inheritState() {
		throw new UnsupportedOperationException("Not suppported on Simon substitute for disabled API");
	}

	public void setSubtreeToInherit() {
		throw new UnsupportedOperationException("Not suppported on Simon substitute for disabled API");
	}

	public boolean isEnabled() {
		return false;
	}
}
