package org.javasimon.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
/**
 * Produces proxy that wrap and existing class using {@link java.lang.reflect.Proxy} class.
 * This class is used to do lightweight AOP.
 * @author gerald
 * @param <T> Type of the wrapped class
 */
public class DelegatingProxyFactory<T> implements InvocationHandler, Delegating<T> {
	/**
	 * Wrapped class and concrete implementation
	 */
	private final T delegate;
	/**
	 * Main constructor
	 * @param delegate Wrapped class and concrete implementation
	 */
	public DelegatingProxyFactory(T delegate) {
		super();
		this.delegate = delegate;
	}
	/**
	 * {@inheritDoc }
	 */
	public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return invoke(new DelegatingMethodInvocation<T>(delegate, proxy, method, args));
	}
	/**
	 * Method to override in child classes
	 * @param delegatingMethodInvocation Method invocation (arguments, method name, etc.)
	 * @return Method invocation results
	 * @throws Throwable Method invocation raised exception
	 */
	protected Object invoke(DelegatingMethodInvocation<T> delegatingMethodInvocation) throws Throwable {
		return delegatingMethodInvocation.proceed();
	}
	/**
	 * Return Wrapped class and concrete implementation.
	 */
	public T getDelegate() {
		return delegate;
	}
	/**
	 * Create a proxy using given classloader and interfaces
	 * @param classLoader Class loader
	 * @param interfaces Interfaces to implement
	 * @return Proxy
	 */
	public Object newProxy(ClassLoader classLoader, Class<?>... interfaces) {
		return Proxy.newProxyInstance(classLoader, interfaces, this);
	}

	/**
	 * Create a proxy using given classloader and interfaces.
	 * Current thread class loaded is used as default classload.
	 * @param interfaces Interfaces to implement
	 * @return Proxy
	 */
	public Object newProxy(Class<?>... interfaces) {
		return newProxy(Thread.currentThread().getContextClassLoader(), interfaces);
	}
	/**
	 * Create a proxy using given classloader and interfaces
	 * @param classLoader Class loader
	 * @param interfaces Interface to implement
	 * @return Proxy
	 */
	public <X> X newProxy(Class<X> interfaces) {
		return (X) newProxy(new Class[]{interfaces});
	}
}
