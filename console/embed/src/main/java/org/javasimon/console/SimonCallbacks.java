package org.javasimon.console;

import org.javasimon.Manager;
import org.javasimon.callback.Callback;

/**
 * Simon callback helper class
 * @author gquintana
 */
public class SimonCallbacks {
	/**
	 * Get the first callback of given type in manager
	 * @param <T>
	 * @param manager Simon manager containing callbacks
	 * @param callbackType Expected callback type
	 * @return Callback or null if not any
	 */
	public static <T extends Callback> T getCallbackByType(Manager manager,Class<T> callbackType) {
		for(Callback callback:manager.callback().callbacks()) {
			if (callbackType.isInstance(callback)) {
				return callbackType.cast(callback);
			}
		}
		return null;
	}
}
