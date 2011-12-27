package org.javasimon.console;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author gquintana
 */
public abstract class Action {
	private final HttpServletRequest request;
	private final HttpServletResponse response;

	public Action(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	protected String getParameterAsString(String name, String defaultValue) {
		String value = getRequest().getParameter(name);
		if (value != null) {
			value = value.trim();
			if (value.equals("")) {
				value = null;
			}
		}
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	protected <T extends Enum<T>> T getParameterAsEnum(String name, Class<T> type, T defaultValue) {
		String value = getParameterAsString(name, null);
		return (value == null) ? defaultValue : Enum.valueOf(type, value.toUpperCase());
	}

	public void readParameters() {

	}

	public abstract void execute() throws ServletException, IOException, ActionException;

	protected void forwardTo(String path) throws ServletException, IOException {
		getRequest().getRequestDispatcher(path).forward(getRequest(), getResponse());
	}
}
