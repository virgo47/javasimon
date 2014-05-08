package org.javasimon.spring.webmvc;

import org.javasimon.Manager;
import org.javasimon.source.AbstractStopwatchSource;

import org.springframework.web.method.HandlerMethod;

/**
 * Stopwatch source for Spring MVC handlers.
 *
 * @author gquintana
 */
public class HandlerStopwatchSource extends AbstractStopwatchSource<HandlerLocation> {
	/**
	 * Prefix used for Simon names.
	 */
	private static final String PREFIX = "org.javasimon.mvc";

	/**
	 * Suffix used for Simon names of Controllers.
	 */
	private static final String CONTROLLER_SUFFIX = "ctrl";

	/**
	 * Suffix used for Simon names of Views.
	 */
	private static final String VIEW_SUFFIX = "view";

	public HandlerStopwatchSource(Manager manager) {
		super(manager);
	}

	@Override
	protected String getMonitorName(HandlerLocation t) {
		StringBuilder stringBuilder = new StringBuilder(PREFIX).append(".");
		// Append controller type
		if (t.getHandler() instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) t.getHandler();
			stringBuilder.append(handlerMethod.getBeanType().getSimpleName())
				.append(".").append(handlerMethod.getMethod().getName());
		} else {
			stringBuilder.append(t.getHandler().getClass().getSimpleName());
		}
		// Append step
		stringBuilder.append(".");
		switch (t.getStep()) {
			case CONTROLLER:
				stringBuilder.append(CONTROLLER_SUFFIX);
				break;
			case VIEW:
				stringBuilder.append(VIEW_SUFFIX);
				break;
		}
		return stringBuilder.toString();
	}
}
