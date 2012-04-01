package org.javasimon.proxy;

import java.lang.reflect.Method;

/**
 * Proxy method invocation
 * @author gquintana
 */
public class ProxyMethodInvocation<T> {
	/**
	 * Target (real) object
	 */
	private final T target;
	/**
	 * Proxy
	 */
	private final Object proxy;
	/**
	 * Method
	 */
	private final Method method;
	/**
	 * Invocation arguments
	 */
	private final Object[] args;

	public ProxyMethodInvocation(T target,Object proxy, Method method, Object... args) {
		this.target = target;
		this.proxy = proxy;
		this.method = method;
		this.args = args;
	}

	public Object[] getArgs() {
		return args;
	}

	public Method getMethod() {
		return method;
	}

	public Object getProxy() {
		return proxy;
	}
	
	public T getTarget() {
		return target;
	}
	public Method getTargetMethod() throws NoSuchMethodException {
		return target.getClass().getMethod(method.getName(), method.getParameterTypes());
	}
	public Object proceed() throws Throwable {
		return method.invoke(target, args);
	}
    
}
