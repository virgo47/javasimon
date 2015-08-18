package org.javasimon.console;

import java.util.Iterator;

import org.javasimon.Manager;
import org.javasimon.callback.Callback;
import org.javasimon.callback.CompositeCallback;
import org.javasimon.proxy.Delegating;

/**
 * Simon callback helper class
 *
 * @author gquintana
 */
public class SimonCallbacks {

	/**
	 * Get the first callback of given type in manager
	 *
	 * @param manager Simon manager containing callbacks
	 * @param callbackType Expected callback type
	 * @return Callback or null if not any
	 */
	public static <T extends Callback> T getCallbackByType(Manager manager, Class<T> callbackType) {
		return getCallbackByType(manager.callback().callbacks(), callbackType);
	}

	/**
	 * Search callback by type in list of callbacks
	 *
	 * @param callbacks List of callback
	 * @param callbackType Callback type
	 * @return Callback matching type
	 */
	private static <T extends Callback> T getCallbackByType(Iterable<Callback> callbacks, Class<T> callbackType) {
		T foundCallback = null;
		Iterator<Callback> callbackIterator = callbacks.iterator();
		while (foundCallback == null && callbackIterator.hasNext()) {
			Callback callback = callbackIterator.next();
			// Remove callback wrappers
			while ((callback instanceof Delegating) && (!callbackType.isInstance(callback))) {
				callback = ((Delegating<Callback>) callback).getDelegate();
			}
			if (callbackType.isInstance(callback)) {
				// Callback found
				foundCallback = callbackType.cast(callback);
			} else if (callback instanceof CompositeCallback) {
				// Visit the composite callback
				foundCallback = getCallbackByType(((CompositeCallback) callback).callbacks(), callbackType);
			}
		}
		return foundCallback;
	}
}
