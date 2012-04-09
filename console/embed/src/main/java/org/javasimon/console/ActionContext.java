package org.javasimon.console;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.javasimon.Manager;
import org.javasimon.SimonManager;

/**
 * Action context wraps both HTTP request and response to make unit testing
 * easier
 *
 * @author gquintana
 */
public class ActionContext {

	/**
	 * HTTP Request
	 */
	private final HttpServletRequest request;
	/**
	 * HTTP Response
	 */
	private final HttpServletResponse response;
	/**
	 * Request Path
	 */
	private String path;
	/**
	 * Simon manager to use.
	 */
	private Manager manager = SimonManager.manager();
	/**
	 * Constructor
	 *
	 * @param request
	 * @param response
	 * @param path
	 */
	public ActionContext(HttpServletRequest request, HttpServletResponse response, String path) {
		this.request = request;
		this.response = response;
		this.path = path;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Set content type of response
	 */
	public void setContentType(String contentType) {
		getResponse().setContentType(contentType);
	}

	/**
	 * Get response outputstream
	 */
	public OutputStream getOutputStream() throws IOException {
		return getResponse().getOutputStream();
	}

	/**
	 * Get response writer
	 */
	public PrintWriter getWriter() throws IOException {
		return getResponse().getWriter();
	}
	/**
	 * Get Simon manager
	 */
	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	protected String getParameter(String name) {
		return getRequest().getParameter(name);
	}

	/**
	 * Get request parameter as a String
	 *
	 * @param name Parameter name
	 * @param defaultValue Parameter default value (can be null)
	 * @return Parameter value
	 */
	public String getParameterAsString(String name, String defaultValue) {
		String value = getParameter(name);
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

	/**
	 * Get request parameter as a Enum
	 *
	 * @param name Parameter name
	 * @param type Enum type
	 * @param defaultValue Parameter default value (can be null)
	 * @return Parameter value
	 */
	public <T extends Enum<T>> T getParameterAsEnum(String name, Class<T> type, T defaultValue) {
		String value = getParameterAsString(name, null);
		return (value == null) ? defaultValue : Enum.valueOf(type, value.toUpperCase());
	}
}
