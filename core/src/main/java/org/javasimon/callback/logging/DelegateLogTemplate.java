package org.javasimon.callback.logging;

/**
 * Base class for log template which delegates part of the work to a concrete log template.
 * @author gquintana
 */
public abstract class DelegateLogTemplate<C> extends LogTemplate<C> {
	/**
	 * Delegate log template
	 */
	private final LogTemplate delegate;
	/**
	 * Constructor
	 * @param delegate Delegate log template 
	 */
	public DelegateLogTemplate(LogTemplate delegate) {
		this.delegate = delegate;
	}
	/**
	 * Get delegate log template
	 * @return Delegate log template
	 */
	public LogTemplate getDelegate() {
		return delegate;
	}
	
	/**
	 * {@inheritDoc }
	 */
	public boolean isEnabled(C context) {
		return delegate.isEnabled(context);
	}
	/**
	 * {@inheritDoc }
	 */
	public void log(String message) {
		delegate.log(message);
	}
}
