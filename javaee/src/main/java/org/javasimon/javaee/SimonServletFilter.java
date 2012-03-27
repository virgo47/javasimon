package org.javasimon.javaee;

import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.utils.Replacer;
import org.javasimon.utils.SimonUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Simon Servlet filter measuring HTTP request execution times. Non-HTTP usages are not supported.
 * Filter provides these functions:
 * <ul>
 * <li>measures all requests and creates tree of Simons with names derived from URLs</li>
 * <li>checks if the request is not longer then a specified threshold and logs warning (TODO)</li>
 * <li>provides basic "console" function if config parameter {@link #INIT_PARAM_SIMON_CONSOLE_PATH} is used in {@code web.xml}</li>
 * </ul>
 * <p/>
 * All constants are public and fields protected for easy extension of the class. Following protected methods
 * are provided to override the default function:
 * <ul>
 * <li>{@link #isMonitored(javax.servlet.http.HttpServletRequest)} - by default always true</li>
 * <li>{@link #getSimonName(javax.servlet.http.HttpServletRequest)}</li>
 * <li>{@link #isOverThreshold(javax.servlet.http.HttpServletRequest, long, java.util.List)} - compares actual request
 * nano time with {@link #getThreshold(javax.servlet.http.HttpServletRequest)} (which may become unused if this method
 * is overriden)</li>
 * <li>{@link #getThreshold(javax.servlet.http.HttpServletRequest)} - returns threshold configured in {@code web.xml}</li>
 * <li>{@link #reportRequestOverThreshold(javax.servlet.http.HttpServletRequest, org.javasimon.Split, java.util.List)}</li>
 * </ul>
 *
 * @author Richard Richter
 * @since 2.3
 */
@SuppressWarnings("UnusedParameters")
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
	 * Name of filter init parameter that sets the value of threshold in milliseconds for maximal
	 * request duration beyond which all splits will be dumped to log. The actual threshold can be
	 * further customized overriding {@link #getThreshold(javax.servlet.http.HttpServletRequest)} method,
	 * but this parameter has to be set to non-null value to enable threshold reporting feature (0 for instance).
	 */
	public static final String INIT_PARAM_REPORT_THRESHOLD_MS = "report-threshold-ms";

	/**
	 * Name of filter init parameter that sets relative ULR path that will provide Simon console page.
	 */
	public static final String INIT_PARAM_SIMON_CONSOLE_PATH = "console-path";

	private static Replacer FINAL_SLASH_REMOVE = new Replacer("/*$", "");

	private static Replacer SLASH_TRIM = new Replacer("^/*(.*?)/*$", "$1");

	/**
	 * Simon prefix - protected for subclasses.
	 */
	protected String simonPrefix = DEFAULT_SIMON_PREFIX;

	/**
	 * Threshold in ns - any reqest longer than this will be reported by
	 * {@link #reportRequestOverThreshold(javax.servlet.http.HttpServletRequest, org.javasimon.Split, java.util.List)}.
	 * Specified by {@link #INIT_PARAM_REPORT_THRESHOLD_MS} ({@value #INIT_PARAM_REPORT_THRESHOLD_MS}) in the {@code web.xml} (in ms,
	 * converted to ns during servlet init). This is the default value returned by {@link #getThreshold(javax.servlet.http.HttpServletRequest)}
	 * but it may be completely ignored if method is overrided so. However if the field is {@code null} threshold reporting feature
	 * is disabled.
	 */
	protected Long reportThresholdNanos;

	/**
	 * URL path that displays Simon web console (or null if no console is required).
	 */
	protected String consolePath;

	/**
	 * Simon Manager used by the filter - protected for the subclass.
	 * <p/>
	 * TODO: not configurable yet, but at least class uses this field and not the SM class.
	 */
	protected Manager manager = SimonManager.manager();

	/**
	 * Thread local list of splits used to cummulate all splits for the request.
	 * Every instance of the Servlet has its own thread-local to bind its lifecycle to
	 * the callback that servlet is registering. Then even more callbacks registered from various
	 * servlets in the same manager do not interfere.
	 */
	private final ThreadLocal<List<Split>> splitsThreadLocal = new ThreadLocal<List<Split>>();

	/**
	 * Callback that saves all splits in {@link #splitsThreadLocal} if {@link #reportThresholdNanos} is configured.
	 */
	private SplitSaverCallback splitSaverCallback;

	/**
	 * Initialization method that processes {@link #INIT_PARAM_PREFIX} and {@link #INIT_PARAM_PUBLISH_MANAGER}
	 * parameters from {@literal web.xml}.
	 *
	 * @param filterConfig filter config object
	 */
	public final void init(FilterConfig filterConfig) {
		if (filterConfig.getInitParameter(INIT_PARAM_PREFIX) != null) {
			simonPrefix = filterConfig.getInitParameter(INIT_PARAM_PREFIX);
		}

		String publishManager = filterConfig.getInitParameter(INIT_PARAM_PUBLISH_MANAGER);
		if (publishManager != null) {
			filterConfig.getServletContext().setAttribute(publishManager, manager);
		}

		String reportTreshold = filterConfig.getInitParameter(INIT_PARAM_REPORT_THRESHOLD_MS);
		if (reportTreshold != null) {
			try {
				this.reportThresholdNanos = Long.parseLong(reportTreshold) * SimonUtils.NANOS_IN_MILLIS;
				splitSaverCallback = new SplitSaverCallback();
				manager.callback().addCallback(splitSaverCallback);
			} catch (NumberFormatException e) {
				// ignore
			}
		}

		String consolePath = filterConfig.getInitParameter(INIT_PARAM_SIMON_CONSOLE_PATH);
		if (consolePath != null) {
			this.consolePath = FINAL_SLASH_REMOVE.process(consolePath);
		}
	}

	/**
	 * Wraps the HTTP request with Simon measuring. Separate Simons are created for different URIs (parameters
	 * ignored).
	 *
	 * @param servletRequest HTTP servlet request
	 * @param servletResponse HTTP servlet response
	 * @param filterChain filter chain
	 * @throws IOException possibly thrown by other filter/serlvet in the chain
	 * @throws ServletException possibly thrown by other filter/serlvet in the chain
	 */
	public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String localPath = request.getRequestURI().substring(request.getContextPath().length());
		if (consolePath != null && localPath.startsWith(consolePath)) {
			consolePage(request, response, localPath);
			return;
		}

		if (isMonitored(request)) {
			doFilterWithMonitoring(request, response, filterChain);
		} else {
			filterChain.doFilter(request, response);
		}
	}

	private void doFilterWithMonitoring(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		if (reportThresholdNanos != null) {
			splitsThreadLocal.set(new ArrayList<Split>());
		}

		String simonName = getSimonName(request);
		Stopwatch stopwatch = manager.getStopwatch(simonPrefix + Manager.HIERARCHY_DELIMITER + simonName);
		if (stopwatch.getNote() == null) {
			stopwatch.setNote(request.getRequestURI());
		}
		Split split = stopwatch.start();
		try {
			filterChain.doFilter(request, response);
		} finally {
			long splitNanoTime = split.stop().runningFor();
			if (reportThresholdNanos != null) {
				List<Split> splits = splitsThreadLocal.get();
				splitsThreadLocal.remove(); // better do this before we call potentially overriden method
				if (isOverThreshold(request, splitNanoTime, splits)) {
					reportRequestOverThreshold(request, split, splits);
				}
			}
		}
	}

	/**
	 * Indicates whether the HTTP Request should be monitored - method is intended for override.
	 * Default behavior always returns true.
	 * <p/>
	 * Example of overriding code:
	 * <code>
	 * String uri = request.getRequestURI().toLowerCase();
	 * return !(uri.endsWith(".css") || uri.endsWith(".png") || uri.endsWith(".gif") || uri.endsWith(".jpg") || uri.endsWith(".js"));
	 * </code>
	 *
	 * @param request HTTP Request
	 * @return true to enable Simon, false either
	 */
	protected boolean isMonitored(HttpServletRequest request) {
		return true;
	}

	/**
	 * Determines whether the request is over the threshold - with all incoming parameters this method can be
	 * very flexible. Default implementation just compares the actual requestNanoTime with
	 * {@link #getThreshold(javax.servlet.http.HttpServletRequest)} (which by default returns value configured
	 * in {@code web.xml})
	 *
	 * @param request HTTP servlet request
	 * @param requestNanoTime actual HTTP request nano time
	 * @param splits all splits started for the request
	 * @return {@code true}, if request should be reported as over threshold
	 */
	protected boolean isOverThreshold(HttpServletRequest request, long requestNanoTime, List<Split> splits) {
		return requestNanoTime > getThreshold(request);
	}

	/**
	 * Returns actual threshold in *nanoseconds* (not ms as configured) which allows to further customize threshold per request - intended for override.
	 * Default behavior returns configured {@link #reportThresholdNanos} (already converted to ns).
	 *
	 * @param request HTTP Request
	 * @return threshold in ns for current request
	 * @since 3.2.0
	 */
	protected long getThreshold(HttpServletRequest request) {
		return reportThresholdNanos;
	}

	/**
	 * Reports request that exceeds the threshold - method is intended for override.
	 * Default behavior reports over {@link Manager#message(String)}.
	 *
	 * @param request offending HTTP request
	 * @param requestSplit split measuring the offending request
	 * @param splits list of all splits started for this request
	 */
	protected void reportRequestOverThreshold(HttpServletRequest request, Split requestSplit, List<Split> splits) {
		StringBuilder messageBuilder = new StringBuilder("Web request is too long (" + SimonUtils.presentNanoTime(requestSplit.runningFor()) +
			") [" + requestSplit.getStopwatch().getNote() + "]");
		for (Split split : splits) {
			messageBuilder.append("\n\t" + split.getStopwatch().getName() + ": " + SimonUtils.presentNanoTime(split.runningFor()));
		}
		manager.message(messageBuilder.toString());
	}

	private void consolePage(HttpServletRequest request, HttpServletResponse response, String localPath) throws IOException {
		response.setContentType("text/plain");
		response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
		response.setHeader("Pragma", "no-cache");

		String subcommand = SLASH_TRIM.process(localPath.substring(consolePath.length()));
		if (subcommand.isEmpty()) {
			printSimonTree(response);
		} else if (subcommand.equalsIgnoreCase("clear")) {
			manager.clear();
			response.getOutputStream().println("Simon Manager was cleared");
		} else {
			response.getOutputStream().println("Invalid command\n");
			simonHelp(response);
		}
	}

	private void simonHelp(ServletResponse response) throws IOException {
		response.getOutputStream().println("Simon Console help - available commands:");
		response.getOutputStream().println("- clear - clears the manager (removes all Simons)");
	}

	private void printSimonTree(ServletResponse response) throws IOException {
		response.getOutputStream().println(SimonUtils.simonTreeString(manager.getRootSimon()));
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
		return DefaultUrlToSimonNameUtil.getSimonName(request);
	}

	/**
	 * Removes the splitSaverCallback if initialized.
	 */
	public void destroy() {
		if (splitSaverCallback != null) {
			manager.callback().removeCallback(splitSaverCallback);
		}
	}

	private class SplitSaverCallback extends CallbackSkeleton {
		@Override
		public void onStopwatchStart(Split split) {
			List<Split> splits = splitsThreadLocal.get();
			if (splits != null) {
				splits.add(split);
			}
		}
	}
}
