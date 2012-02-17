package org.javasimon.javaee;

import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.utils.SimonUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Simon Servlet filter measuring HTTP request execution times. Non-HTTP usages are not supported.
 * Filter provides these functions:
 * <ul>
 * <li>measures all requests and creates tree of Simons with names derived from URLs</li>
 * <li>checks if the request is not longer then a specified threshold and logs warning (TODO)</li>
 * <li>provides basic "console" function if config parameter {@link #INIT_PARAM_SIMON_CONSOLE_PATH} is used in {@code web.xml}</li>
 * </ul>
 * <p/>
 * All constants are public and fields protected for easy extension of the class.
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
	public static final String INIT_PARAM_REPORT_THRESHOLD = "report-threshold";

	/**
	 * Name of filter init parameter that sets relative ULR path that will provide Simon console page.
	 */
	public static final String INIT_PARAM_SIMON_CONSOLE_PATH = "console-path";

	/**
	 * Simon prefix - protected for subclasses.
	 */
	protected String simonPrefix = DEFAULT_SIMON_PREFIX;

	/**
	 * Threshold in ns - any reqest longer than this will be reported by
	 * {@link #reportRequestOverThreshold(javax.servlet.http.HttpServletRequest, org.javasimon.Split, java.util.List)}.
	 * Specified by {@link #INIT_PARAM_REPORT_THRESHOLD} ({@value #INIT_PARAM_REPORT_THRESHOLD}) in the {@code web.xml} (in ms,
	 * converted to ns during servlet init). This is the default value returned by {@link #getThreshold(javax.servlet.http.HttpServletRequest)}
	 * but it may be completely ignored if method is overrided so. However if the field is {@code null} threshold reporting feature
	 * is disabled.
	 */
	protected Long reportThreshold;

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
	 * Callback that saves all splits in {@link #splitsThreadLocal} if {@link #reportThreshold} is configured.
	 */
	private SplitSaverCallback splitSaverCallback;
	
	/**
	 * Pattern used to replace unallowed characters
	 */
	private final Pattern unallowedCharsPattern=createUnallowedCharsPattern();
	/**
	 * Initializes the pattern used to replace unallowed characters
	 */	
	private static Pattern createUnallowedCharsPattern() {
		String s=SimonUtils.NAME_PATTERN.pattern();
		// Insert negation ^ after [
		s=s.replaceFirst("\\[", "[^/");
		// Remove . (dot) because it will be used for something else
		s=s.replaceAll("\\.", "");
		return Pattern.compile(s);
	}
	/**
	 * Pattern used to replace to dot.
	 * / // /// becomes .
	 */
	private static final Pattern toDotPattern=Pattern.compile("/+");

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
		String reportTreshold = filterConfig.getInitParameter(INIT_PARAM_REPORT_THRESHOLD);
		if (reportTreshold != null) {
			try {
				this.reportThreshold = Long.parseLong(reportTreshold) * SimonUtils.NANOS_IN_MILLIS;
				splitSaverCallback = new SplitSaverCallback();
				manager.callback().addCallback(splitSaverCallback);
			} catch (NumberFormatException e) {
				// ignore
			}
		}
		String consolePath = filterConfig.getInitParameter(INIT_PARAM_SIMON_CONSOLE_PATH);
		if (consolePath != null) {
			this.consolePath = consolePath;
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
	public final void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String localPath = request.getRequestURI().substring(request.getContextPath().length());
		if (consolePath != null && localPath.startsWith(consolePath)) {
			consolePage(request, (HttpServletResponse) response, localPath);
			return;
		}
		if (isMonitored(request)) {
			if (reportThreshold != null) {
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
				if (reportThreshold != null) {
					List<Split> splits = splitsThreadLocal.get();
					splitsThreadLocal.remove(); // better do this before we call potentially overriden method
					if (splitNanoTime > getThreshold(request)) {
						reportRequestOverThreshold(request, split, splits);
					}
				}
			}
		} else {
			filterChain.doFilter(request, response);
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
	 * Returns actual threshold in *nanoseconds* (not ms as configured) which allows to further customize threshold per request - intended for override.
	 * Default behavior returns configured {@link #reportThreshold} (already converted to ns).
	 *
	 * @param request HTTP Request
	 * @return threshold in ns for current request
	 * @since 3.2.0
	 */
	protected long getThreshold(HttpServletRequest request) {
		return reportThreshold;
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
		String subcommand = localPath.substring(consolePath.length());
		if (subcommand.isEmpty()) {
			printSimonTree(response);
		} else if (subcommand.equalsIgnoreCase("/clear")) {
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
		String name=request.getRequestURI();
		// Remove starting /
		if (name.startsWith("/")) {
			name=name.substring(1);
		}
		// Remove unallowed characters and
		name=unallowedCharsPattern.matcher(name).replaceAll("");
		// Replace / and .. by .
		name=toDotPattern.matcher(name).replaceAll(".");
		return name;
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
