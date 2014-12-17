package org.javasimon.console;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javasimon.Manager;
import org.javasimon.utils.SimonUtils;

/**
 * Simon Console filter does the same as {@link SimonConsoleServlet} and provides just an alternative way
 * to include embeddable console in your applications. Putting this filter before the Simon Servlet filter is also
 * the easiest way how to avoid monitoring of the console itself.
 *
 * @author Richard Richter
 * @since 2.3
 */
@SuppressWarnings("UnusedParameters")
public class SimonConsoleFilter implements Filter {
	private SimonConsoleRequestProcessor requestProcessor;

	@Override
	public final void init(FilterConfig config) {
		// Manager
		Manager manager = SimonConsoleServlet.getManager(config.getServletContext());
		// URL Prefix
		String urlPrefix = config.getInitParameter(SimonConsoleServlet.URL_PREFIX_INIT_PARAMETER);
		// Plugin classes
		String pluginClasses = config.getInitParameter(SimonConsoleServlet.PLUGIN_CLASSES_INIT_PARAMETER);
		// Create request processor
		requestProcessor = SimonConsoleRequestProcessor.create(urlPrefix, manager, pluginClasses);

	}

	/**
	 * Wraps the HTTP request with Simon measuring. Separate Simons are created for different URIs (parameters
	 * ignored).
	 *
	 * @param servletRequest HTTP servlet request
	 * @param servletResponse HTTP servlet response
	 * @param filterChain filter chain
	 * @throws java.io.IOException possibly thrown by other filter/serlvet in the chain
	 * @throws javax.servlet.ServletException possibly thrown by other filter/serlvet in the chain
	 */
	public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String localPath = request.getRequestURI().substring(request.getContextPath().length());
		if (localPath.startsWith(requestProcessor.getUrlPrefix())) {
			requestProcessor.processRequest(request, response);
			return;
		}

		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
