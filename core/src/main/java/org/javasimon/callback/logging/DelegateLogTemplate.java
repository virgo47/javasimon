package org.javasimon.callback.logging;

/**
 * Base class for log template which delegates part of the work to a concrete log template.
 *
 * @author gquintana
 */
public abstract class DelegateLogTemplate<C> extends LogTemplate<C> {

	/** Delegate log template. */
	private final LogTemplate<C> delegate;

	/**
	 * Constructor with delegate log template.
	 *
	 * @param delegate delegate log template
	 */
	public DelegateLogTemplate(LogTemplate<C> delegate) {
		this.delegate = delegate;
	}

	/**
	 * Get delegate log template.
	 *
	 * @return Delegate log template
	 */
	public LogTemplate getDelegate() {
		return delegate;
	}

	protected boolean isEnabled(C context) {
		return delegate.isEnabled(context);
	}

	protected void log(String message) {
		delegate.log(message);
	}
}
