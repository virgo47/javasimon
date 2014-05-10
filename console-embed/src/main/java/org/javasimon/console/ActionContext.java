package org.javasimon.console;

import org.javasimon.Manager;
import org.javasimon.SimonManager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action context wraps both HTTP request and response to make unit testing easier.
 *
 * @author gquintana
 */
public class ActionContext {

	/** HTTP Request. */
	private final HttpServletRequest request;
	/** HTTP Response. */
	private final HttpServletResponse response;
	/** Request Path. */
	private String path;
	/** Simon manager to use. */
	private Manager manager = SimonManager.manager();
	/** Simon console plugin manager. */
	private SimonConsolePluginManager pluginManager;

	public ActionContext(HttpServletRequest request, HttpServletResponse response, String path) {
		this.request = request;
		this.response = response;
		this.path = path;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getCharacterEncoding() {
		String encoding = getRequest().getCharacterEncoding();
		return encoding == null ? Charset.defaultCharset().name() : encoding;
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

	/** Set content type of response. */
	public void setContentType(String contentType) {
		getResponse().setContentType(contentType);
	}

	/** Get response output stream. */
	public OutputStream getOutputStream() throws IOException {
		return getResponse().getOutputStream();
	}

	/** Get response writer. */
	public PrintWriter getWriter() throws IOException {
		return getResponse().getWriter();
	}

	/** Get Simon {@link org.javasimon.Manager}. */
	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	protected String getParameter(String name) {
		String value = getRequest().getParameter(name);
		if (value != null) {
			try {
				value = URLDecoder.decode(value, getCharacterEncoding());
			} catch (UnsupportedEncodingException unsupportedEncodingException) {
				// Leave value as is
			}
		}
		return value;
	}

	protected String[] getParameters(String name) {
		return getRequest().getParameterValues(name);
	}

	/**
	 * Transform empty string (only white spaces) into null, handles null.
	 */
	private static String blankToNull(String value) {
		if (value != null) {
			value = value.trim();
			if (value.equals("")) {
				value = null;
			}
		}
		return value;
	}

	/** Returns default value when value is null. */
	private static <T> T defaultValue(T value, T defaultValue) {
		return value == null ? defaultValue : value;
	}

	/**
	 * Get request parameter as a String.
	 *
	 * @param name Parameter name
	 * @param defaultValue Parameter default value (can be null)
	 * @return Parameter value
	 */
	public String getParameterAsString(String name, String defaultValue) {
		return defaultValue(blankToNull(getParameter(name)), defaultValue);
	}

	/** Transform a string into an boolean. */
	private static Boolean stringToBoolean(String value) {
		final String s = blankToNull(value);
		return s == null ? null : Boolean.valueOf(s);
	}

	/**
	 * Get request parameter as a Boolean.
	 *
	 * @param name Parameter name
	 * @param defaultValue Parameter default value
	 */
	public boolean getParameterAsBoolean(String name, Boolean defaultValue) {
		return defaultValue(stringToBoolean(getParameter(name)), defaultValue);
	}

	/**
	 * Transform a string into an enum (using its name which is supposed to be
	 * uppercase, handles null values.
	 */
	private static <T extends Enum<T>> T stringToEnum(String value, Class<T> type) {
		final String s = blankToNull(value);
		return value == null ? null : Enum.valueOf(type, s.toUpperCase());
	}

	/**
	 * Get request parameter as a Enum.
	 *
	 * @param name Parameter name
	 * @param type Enum type
	 * @param defaultValue Parameter default value (can be null)
	 * @return Parameter value
	 */
	public <T extends Enum<T>> T getParameterAsEnum(String name, Class<T> type, T defaultValue) {
		return defaultValue(stringToEnum(getParameter(name), type), defaultValue);
	}

	/**
	 * Get multiple request parameters as Enums.
	 *
	 * @param name Parameter name
	 * @return Parameter values as an Enum Set.
	 */
	public <T extends Enum<T>> EnumSet<T> getParametersAsEnums(String name, Class<T> type, EnumSet<T> defaultValue) {
		String[] enumNames = getParameters(name);
		if (enumNames == null) {
			return defaultValue;
		} else {
			Collection<T> enums = new ArrayList<>();
			for (String enumName : enumNames) {
				T enumValue = stringToEnum(blankToNull(enumName), type);
				if (enumValue != null) {
					enums.add(enumValue);
				}
			}
			return enums.isEmpty() ? defaultValue : EnumSet.copyOf(enums);
		}
	}

	public SimonConsolePluginManager getPluginManager() {
		return pluginManager;
	}

	public void setPluginManager(SimonConsolePluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}
}
