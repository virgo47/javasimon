package org.javasimon.callback.logging;

/**
 *
 * @author gquintana
 */
public final class DisabledLogTemplate<C> extends LogTemplate<C> {
	public boolean isEnabled(C context) {
		return false;
	}
	public void log(String message) {
		// Do nothing
	}
	private static final DisabledLogTemplate INSTANCE=new DisabledLogTemplate();
	public static <C> DisabledLogTemplate<C> getInstance() {
		return (DisabledLogTemplate<C>) INSTANCE;
	}
}
