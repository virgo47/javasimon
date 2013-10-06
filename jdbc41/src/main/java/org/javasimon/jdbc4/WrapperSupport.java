package org.javasimon.jdbc4;

import java.sql.SQLException;
import java.sql.Wrapper;

/**
 * Helper class for implementing {@link Wrapper} on wrappers.
 *
 * @param <D> delegate type
 * @author gquintana
 */
public final class WrapperSupport<D extends Wrapper> implements Wrapper {
	/**
	 * Delegate instance.
	 */
	private final D delegate;

	/**
	 * Interface implemented by delegate.
	 */
	private final Class<D> delegateType;

	public WrapperSupport(D delegate, Class<D> delegateType) {
		this.delegate = delegate;
		this.delegateType = delegateType;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return delegateType.equals(iface) || delegate.isWrapperFor(iface);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		if (delegateType.equals(iface)) {
			return delegate.isWrapperFor(iface) ? delegate.unwrap(iface) : iface.cast(delegate);
		} else {
			return delegate.unwrap(iface);
		}
	}
}
