package org.javasimon.javaee;

import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.utils.SimonUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Simon Servlet filter measuring HTTP request execution times. Non-HTTP usages are not supported.
 *
 * @author Richard Richter
 * @version $Revision$ $Date$
 * @since 2.3
 */
public class SimonServletFilter implements Filter {
	/**
	 * Default prefix for web filter Simons if no "prefix" init parameter is used.
	 */
	public static final String DEFAULT_SIMON_PREFIX = "org.javasimon.web";

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

	/**
	 * Name of filter init parameter that sets the value of threshold in milliseconds
	 * for maximal request duration beyond which all splits will be dumped to log.
	 */
	public static final String INIT_PARAM_REPORT_THRESHOLD = "report-threshold";

	/**
	 * Name of filter init parameter that sets relative ULR path that will provide
	 * Simon report page.
	 */
	public static final String INIT_PARAM_REPORT_PATH = "report-path";

	/**
	 * Public thread local list of splits used to cummulate all splits for the request.
	 */
	public static final ThreadLocal<List<Split>> SPLITS = new ThreadLocal<List<Split>>();

	private String simonPrefix = DEFAULT_SIMON_PREFIX;

	private Long reportThreshold;

	/**
	 * URL path that displays report (or null if no report is required).
	 */
	private String reportPath;

	/**
	 * Initialization method that processes {@link #INIT_PARAM_PREFIX} and {@link #INIT_PARAM_PUBLISH_MANAGER}
	 * parameters from {@literal web.xml}.
	 *
	 * @param filterConfig filter config object
	 */
	public void init(FilterConfig filterConfig) {
		if (filterConfig.getInitParameter(INIT_PARAM_PREFIX) != null) {
			simonPrefix = filterConfig.getInitParameter(INIT_PARAM_PREFIX);
		}
		String publishManager = filterConfig.getInitParameter(INIT_PARAM_PUBLISH_MANAGER);
		if (publishManager != null) {
			filterConfig.getServletContext().setAttribute(publishManager, SimonManager.manager());
		}
		String reportTreshold = filterConfig.getInitParameter(INIT_PARAM_REPORT_THRESHOLD);
		if (reportTreshold != null) {
			try {
				this.reportThreshold = Long.parseLong(reportTreshold);
			} catch (NumberFormatException e) {
				// ignore
			}
		}
		String reportPath = filterConfig.getInitParameter(INIT_PARAM_REPORT_PATH);
		if (reportPath != null) {
			this.reportPath = reportPath;
		}
	}

	/**
	 * Wraps the HTTP request with Simon measuring. Separate Simons are created for different URIs (parameters
	 * ignored).
	 *
	 * @param servletRequest HTTP servlet request
	 * @param response HTTP servlet response
	 * @param filterChain filter chain
	 * @throws IOException possibly thrown by other filter/serlvet in the chain
	 * @throws ServletException possibly thrown by other filter/serlvet in the chain
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		if (reportPath != null && request.getRequestURI().startsWith(reportPath)) {
			response.getOutputStream().println(SimonUtils.simonTreeString(SimonManager.getRootSimon()));
			return;
		}
		String simonName = getSimonName(request);
		SPLITS.set(new ArrayList<Split>());
		Split split = SimonManager.getStopwatch(simonPrefix + Manager.HIERARCHY_DELIMITER + simonName).start();
		try {
			filterChain.doFilter(request, response);
		} finally {
			split.stop();
			if (reportThreshold != null && split.runningFor() > reportThreshold) {
//				logSomehow(SPLITS.get()); TODO + callback
			}
			SPLITS.remove();
		}
	}

	/**
	 * Returns Simon name for the specified HTTP request. By default it contains URI without parameters with
	 * all slashes replaced for dots (slashes then determines position in Simon hierarchy). Method can be
	 * overriden.
	 *
	 * @param request HTTP request
	 * @return fully qualified name of the Simon
	 */
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

	/**
	 * Does nothing - just implements the contract.
	 */
	public void destroy() {
	}
}
