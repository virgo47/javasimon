package org.javasimon.spring.webmvc;

import org.javasimon.Split;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Location used by stopwatch source for MVC Handler interceptor.
 * Basically, represents a Controller method invocation.
 * @author gquintana
 */
public class HandlerLocation {
	/**
	 * HTTP Servlet Request
	 */
	private final HttpServletRequest request;
	/**
	 * Handler (controller method invocation)
	 */
	private final Object handler;
	/**
	 * Request processing step: controller processing org view rendering
	 */
	private HandlerStep step;
	/**
	 * View (and Model, null when step is not VIEW
	 */
	private ModelAndView modelAndView;
	/**
	 * Currently running split
	 */
	private Split split;
	/**
	 * Constructor
	 * @param request
	 * @param handler
	 * @param step
	 */
	public HandlerLocation(HttpServletRequest request, Object handler, HandlerStep step) {
		this.request = request;
		this.handler = handler;
		this.step = step;
	}

	public Object getHandler() {
		return handler;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public ModelAndView getModelAndView() {
		return modelAndView;
	}

	public void setModelAndView(ModelAndView modelAndView) {
		this.modelAndView = modelAndView;
	}

	public HandlerStep getStep() {
		return step;
	}

	public void setStep(HandlerStep step) {
		this.step = step;
	}

	public Split getSplit() {
		return split;
	}

	public void setSplit(Split split) {
		this.split = split;
	}

}
