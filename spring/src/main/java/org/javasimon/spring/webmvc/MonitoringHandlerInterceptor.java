package org.javasimon.spring.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.source.StopwatchSource;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring MVC interceptor monitors time spent in handlers (usually controllers)
 * and views. Spring configuration:
 * <pre>{@literal
 * <mvc:interceptors>
 *    <bean class="org.javasimon.spring.webmvc.MonitoringHandlerInterceptor"/>
 * </mvc:interceptors>
 * }</pre>
 *
 * @author gquintana
 * @since Spring 3.1
 */
public class MonitoringHandlerInterceptor implements HandlerInterceptor {
	/**
	 * Current thread running split, if any.
	 */
	private final ThreadLocal<HandlerLocation> threadLocation = new ThreadLocal<>();

	/**
	 * Stopwatch source.
	 */
	private StopwatchSource<HandlerLocation> stopwatchSource;

	/**
	 * Constructor with stopwatch source.
	 *
	 * @param stopwatchSource Stopwatch source
	 */
	public MonitoringHandlerInterceptor(StopwatchSource<HandlerLocation> stopwatchSource) {
		this.stopwatchSource = stopwatchSource;
	}

	/**
	 * Constructor with simon manager and default stopwatch source.
	 *
	 * @param manager Manager manager
	 */
	public MonitoringHandlerInterceptor(Manager manager) {
		stopwatchSource = new HandlerStopwatchSource(manager);
	}

	/**
	 * Default constructor: default stopwatch source, default manager.
	 */
	public MonitoringHandlerInterceptor() {
		stopwatchSource = new HandlerStopwatchSource(SimonManager.manager());
	}

	/**
	 * Start stopwatch for given name and thread.
	 *
	 * @return Running split
	 */
	protected final Split startStopwatch(HandlerLocation location) {
		Split split = stopwatchSource.start(location);
		location.setSplit(split);
		return split;
	}

	/**
	 * Stop current thread stopwatch (if any).
	 *
	 * @return Stopped split
	 */
	protected final Split stopStopwatch() {
		HandlerLocation location = threadLocation.get();
		Split split = null;
		if (location != null) {
			split = location.getSplit();
			split.stop();
			location.setSplit(null);
		}
		return split;
	}

	/**
	 * Invoked before controller.
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		final HandlerLocation location = new HandlerLocation(request, handler, HandlerStep.CONTROLLER);
		threadLocation.set(location);

		// Start controller stopwatch
		startStopwatch(location);
		return true;
	}

	/**
	 * Invoked between controller and view.
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
		// Stop controller stopwatch
		stopStopwatch();

		HandlerLocation location = threadLocation.get();
		location.setStep(HandlerStep.VIEW);

		// Start view stopwatch
		startStopwatch(location);
	}

	/**
	 * Invoked after view.
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		// Stop view stopwatch
		stopStopwatch();

		// Remove location
		threadLocation.remove();
	}
}
