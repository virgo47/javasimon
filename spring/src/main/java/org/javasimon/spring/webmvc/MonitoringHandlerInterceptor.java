package org.javasimon.spring.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring MVC interceptor monitors time spent in handlers (usually controllers)
 * and views.
 * Spring configuration:
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
	 * Prefix used for Simon names
	 */
	private String prefix = "org.javasimon.mvc";

	/**
	 * Suffix used for Simon names of Controllers
	 */
	private String controllerSuffix = "ctrl";

	/**
	 * Suffix used for Simon names of Views
	 */
	private String viewSuffix = "view";

	/**
	 * Spring MVC request processing step
	 */
	public enum Step {
		CONTROLLER, VIEW
	}

	/**
	 * Current thread running split, if any
	 */
	private final ThreadLocal<Split> threadSplit = new ThreadLocal<Split>();

	/**
	 * Simon manager used to get stopwatches
	 */
	private Manager manager = SimonManager.manager();

	/**
	 * Indicates if Controller/View should be monitored.
	 * Override this method to filter monitored requests.
	 *
	 * @param request HTTP request
	 * @param handler Handler
	 * @param suffix Either ctrl or view
	 * @return Always true.
	 */
	protected boolean isMonitored(HttpServletRequest request, Object handler, ModelAndView modelAndView, Step step) {
		return true;
	}

	/**
	 * Builds Simon name for given controller method and HTTP request
	 * Override this method to customize monitor names.
	 *
	 * @param request HTTP request
	 * @param handler Controller method
	 * @param suffix Either ctrl or view
	 */
	protected String getMonitorName(HttpServletRequest request, Object handler, ModelAndView modelAndView, Step step) {
		StringBuilder stringBuilder = new StringBuilder(prefix).append(".");
		// Append controller type
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			stringBuilder.append(handlerMethod.getBeanType().getSimpleName())
				.append(".").append(handlerMethod.getMethod().getName());
		} else {
			stringBuilder.append(handler.getClass().getSimpleName());
		}
		// Append step
		stringBuilder.append(".");
		switch (step) {
			case CONTROLLER:
				stringBuilder.append(controllerSuffix);
				break;
			case VIEW:
				stringBuilder.append(viewSuffix);
				break;
		}
		return stringBuilder.toString();
	}

	/**
	 * Start stopwatch for given name and thread
	 *
	 * @return Running split
	 */
	private Split startStopwatch(HttpServletRequest request, Object handler, ModelAndView modelAndView, Step step) {
		Split split = null;
		if (isMonitored(request, handler, modelAndView, step)) {
			split = manager.getStopwatch(getMonitorName(request, handler, modelAndView, step)).start();
			threadSplit.set(split);
		}
		return split;
	}

	/**
	 * Stop current thread stopwatch (if any)
	 *
	 * @return Stopped split
	 */
	protected Split stopStopwatch() {
		Split split = threadSplit.get();
		if (split != null) {
			split.stop();
			threadSplit.remove();
		}
		return split;
	}

	/**
	 * Invoked before controller
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		// Start controller stopwatch
		startStopwatch(request, handler, null, Step.CONTROLLER);
		return true;
	}

	/**
	 * Invoked between controller and view
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
		// Stop controller stopwatch
		stopStopwatch();
		// Start view stopwatch
		startStopwatch(request, handler, modelAndView, Step.VIEW);
	}

	/**
	 * Invoked after view
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		// Stop view stopwatch
		stopStopwatch();
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getControllerSuffix() {
		return controllerSuffix;
	}

	public void setControllerSuffix(String controllerSuffix) {
		this.controllerSuffix = controllerSuffix;
	}

	public String getViewSuffix() {
		return viewSuffix;
	}

	public void setViewSuffix(String viewSuffix) {
		this.viewSuffix = viewSuffix;
	}
}
