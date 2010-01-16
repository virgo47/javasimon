package org.javasimon.javaee;

import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.utils.SimonUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Simon servlet filter.
 *
 * @author Richard Richter
 */
public class SimonFilter implements Filter {
	/**
	 * Name of filter init parameter for Simon name prefix.
	 */
	public static final String INIT_PARAM_PREFIX = "prefix";

	/**
	 * Name of filter init parameter determining the attribute name under which
	 * Simon Manager is to be published in servlet context attributes. If this
	 * parameter is not used the manager is not published.
	 */
	public static final String INIT_PARAM_PUBLISH_MANAGER = "manager-attribute-name";

	private String simonPrefix;

	public void init(FilterConfig filterConfig) throws ServletException {
		simonPrefix = filterConfig.getInitParameter(INIT_PARAM_PREFIX);
		String publishManager = filterConfig.getInitParameter(INIT_PARAM_PUBLISH_MANAGER);
		if (publishManager != null) {
			filterConfig.getServletContext().setAttribute(publishManager, SimonManager.manager());
		}
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String simonName = getSimonName(request);
		Split split = SimonManager.getStopwatch(simonPrefix + Manager.HIERARCHY_DELIMITER + simonName).start();
		filterChain.doFilter(request, response);
		split.stop();
	}

	protected String getSimonName(HttpServletRequest request) {
		StringBuilder name = new StringBuilder(request.getRequestURI().replaceAll("\\.+", "").replace('/', '.'));
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == '?') {
				name.delete(i, name.length());
				break;
			}
			if (SimonUtils.ALLOWED_CHARS.indexOf(name.charAt(i)) == -1) {
				name.deleteCharAt(i);
				i--;
			}
		}
		return name.toString().replaceAll("^\\.+", "").replaceAll("\\.+", ".");
	}

	public void destroy() {
	}
}
