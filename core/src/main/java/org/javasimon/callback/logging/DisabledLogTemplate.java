package org.javasimon.callback.logging;

/**
 * @author gquintana
 */
public final class DisabledLogTemplate<C> extends LogTemplate<C> {

	protected boolean isEnabled(C context) {
		return false;
	}

	protected void log(String message) {
		// Do nothing
	}

	private static final DisabledLogTemplate INSTANCE = new DisabledLogTemplate();

	public static <C> DisabledLogTemplate<C> getInstance() {
		//noinspection unchecked
		return INSTANCE;
	}
}
