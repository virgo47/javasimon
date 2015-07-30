package org.javasimon.proxy;

/**
 * Interface indicating that an object is wrapping another object.
 *
 * @param <T> Wrapped type
 * @author gerald
 */
public interface Delegating<T> {

	/**
	 * Get wrapped object.
	 *
	 * @return Wrapped object
	 */
	T getDelegate();
}
