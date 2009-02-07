package org.javasimon;

import java.util.Collection;
import java.util.Collections;

/**
 * EmptyCallback implements Callback interface so that it does nothing.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Jan 22, 2009
 */
public class CallbackSkeleton implements Callback {
	/**
	 * Returns empty list.
	 *
	 * @return empty list
	 */
	public Collection<Callback> callbacks() {
		return Collections.emptyList();
	}

	/**
	 * Throws UnsupportedOperationException.
	 *
	 * @param callback ignored
	 */
	public void addCallback(Callback callback) {
		throw new UnsupportedOperationException("Only CompositeCallback implements this method.");
	}

	/**
	 * Throws UnsupportedOperationException.
	 *
	 * @param callback ignored
	 */
	public void removeCallback(Callback callback) {
		throw new UnsupportedOperationException("Only CompositeCallback implements this method.");
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopwatchStart(Split split) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopwatchStop(Split split) {
	}

	/**
	 * Warning and stack trace are print out to the standard output.
	 *
	 * {@inheritDoc}
	 */
	public void warning(String warning, Exception cause) {
		System.out.println("Simon warning: " + warning);
		if (cause != null) {
			cause.printStackTrace();
		}
	}
}
