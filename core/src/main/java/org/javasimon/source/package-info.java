/**
 * {@link org.javasimon.source.MonitorSource} is the interface used as function to transform execution context (method invocation,
 * HTTP request...) into a Simon. Following subtypes are provided:
 * <ul>
 * <li>{@link org.javasimon.source.CachedMonitorSource} - abstract implementation which acts as a cache for excutation context to Simon conversion.</li>
 * <li>{@link org.javasimon.source.DisabledMonitorSource} - implementation to disable monitoring at interceptor level (produces null Simons).</li>
 * <li>{@code org.javasimon.javaee.HttpStopwatchSource} (in javaee module) - used by servlet filter to get the Simon associated with a HTTP request.</li>
 * <li>{@link org.javasimon.source.AbstractMethodStopwatchSource} - abstract used for method invocation contexts:
 * <ul>
 * <li>{@code org.javasimon.spring.SpringStopwatchSource} (in spring module) - used by the Spring AOP interceptor to get Stopwatch names from
 * {@link org.javasimon.aop.Monitored} interfaces;</li>
 * <li>{@code org.javasimon.source.MethodStopwatchSource} (in javee module) - used by the JavaEE interceptor to get Stopwatch names
 * from EJBs/CDI beans;</li>
 * <li>{@link org.javasimon.proxy.ProxyStopwatchSource} - used by the Proxy to get Stopwatch names.</li>
 * </ul></li>
 * </ul>
 */
package org.javasimon.source;