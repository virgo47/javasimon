/**
 * {@link MonitorSource} is the interface used as function to transform execution context (method invocation,
 * HTTP request...) into a Simon. Following subtypes are provided:
 * <ul>
 * <li>{@link CacheMonitorSource} - abstract implementation which acts as a cache for excutation context to Simon conversion.</li>
 * <li>{@link DisabledMonitorSource} - implementation to disable monitoring at interceptor level(produces null Simons).</li>
 * <li>{@code HttpStopwatchSource} (in javaee module) - used by servlet filter to get the Simon associated with a HTTP request.</li>
 * <li>{@link AbstractMethodStopwatchSource} - abstract used for method invocation contexts:
 * <ul>
 * <li>{@code SpringStopwatchSource} (in spring module) - used by the Spring AOP interceptor to get Stopwatch names from
 * {@link org.javasimon.aop.Monitored} interfaces;</li>
 * <li>{@code MethodStopwatchSource} (in javee module) - used by the JavaEE interceptor to get Stopwatch names
 * from EJBs/CDI beans;</li>
 * <li>{@link org.javasimon.proxy.ProxyStopwatchSource} - used by the Proxy to get Stopwatch names.</li>
 * </ul></li>
 * </ul></li>
 * </ul>
 */
package org.javasimon.source;