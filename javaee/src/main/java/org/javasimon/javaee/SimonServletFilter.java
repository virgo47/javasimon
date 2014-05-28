package org.javasimon.javaee;

import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.clock.ClockUtils;
import org.javasimon.javaee.reqreporter.RequestReporter;
import org.javasimon.source.DisabledMonitorSource;
import org.javasimon.source.StopwatchSource;
import org.javasimon.utils.Replacer;
import org.javasimon.utils.SimonUtils;
import org.javasimon.utils.bean.SimonBeanUtils;
import org.javasimon.utils.bean.ToEnumConverter;

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
 * <li>checks if the request is not longer then a specified threshold and logs warning</li>
 * <li>provides basic "console" function if config parameter {@link #INIT_PARAM_SIMON_CONSOLE_PATH} is used in {@code web.xml}</li>
 * </ul>
 * <p/>
 * All constants are public and fields protected for easy extension of the class. Following protected methods
 * and classes are provided to override the default function:
 * <ul>
 * <li>{@link #shouldBeReported} - compares actual request nano time with {@link #getThreshold(javax.servlet.http.HttpServletRequest)}
 * (which may become unused if this method is overridden)</li>
 * <li>{@link #getThreshold(javax.servlet.http.HttpServletRequest)} - returns threshold configured in {@code web.xml}</li>
 * <li>{@link org.javasimon.javaee.reqreporter.RequestReporter} can be implemented and specified using init parameter {@link #INIT_PARAM_REQUEST_REPORTER_CLASS}</li>
 * <li>{@link HttpStopwatchSource} can be subclassed and specified using init parameter {@link #INIT_PARAM_STOPWATCH_SOURCE_CLASS}, specifically
 * following methods are intended for override:
 * <ul>
 * <li>{@link HttpStopwatchSource#isMonitored(javax.servlet.http.HttpServletRequest)} - true except for request with typical resource suffixes
 * ({@code .gif}, {@code .jpg}, {@code .css}, etc.)</li>
 * <li>{@link HttpStopwatchSource#getMonitorName(javax.servlet.http.HttpServletRequest)}</li>
 * </ul></li>
 * </ul>
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 2.3
 */
@SuppressWarnings("UnusedParameters")
public class SimonServletFilter implements Filter {
	/**
	 * Name of filter init parameter for Simon name prefix.
	 */
	public static final String INIT_PARAM_PREFIX = "prefix";

	/**
	 * Name of filter init parameter that sets the value of threshold in milliseconds for maximal
	 * request duration beyond which all splits will be dumped to log. The actual threshold can be
	 * further customized overriding {@link #getThreshold(javax.servlet.http.HttpServletRequest)} method,
	 * but this parameter has to be set to non-null value to enable threshold reporting feature (0 for instance).
	 */
	public static final String INIT_PARAM_REPORT_THRESHOLD_MS = "report-threshold-ms";

	/**
	 * Name of filter init parameter that sets relative ULR path that will provide Simon console page.
	 * If the parameter is not used, basic plain text console will be disabled.
	 */
	public static final String INIT_PARAM_SIMON_CONSOLE_PATH = "console-path";

	/**
	 * FQN of the Stopwatch source class implementing {@link org.javasimon.source.MonitorSource}.
	 * One can use {@link DisabledMonitorSource} to disabled monitoring.
	 * Defaults to {@link HttpStopwatchSource}.
	 */
	public static final String INIT_PARAM_STOPWATCH_SOURCE_CLASS = "stopwatch-source-class";

	/**
	 * Enable/disable caching on Stopwatch resolution.
	 * <em>Warning: as the cache key is the {@link HttpServletRequest#getRequestURI()},
	 * this is incompatible with application passing data in their
	 * request URI, this is often the case of RESTful services.
	 * For instance "/car/1023/driver" and "/car/3624/driver"
	 * may point to the same page but with different URLs.</em>
	 * Defaults to {@code false}.
	 */
	public static final String INIT_PARAM_STOPWATCH_SOURCE_CACHE = "stopwatch-source-cache";

	/**
	 * FQN of the {@link org.javasimon.javaee.reqreporter.RequestReporter} implementation that is used to report requests
	 * that {@link #shouldBeReported(javax.servlet.http.HttpServletRequest, long, java.util.List)}.
	 * Default is {@link org.javasimon.javaee.reqreporter.DefaultRequestReporter}.
	 */
	public static final String INIT_PARAM_REQUEST_REPORTER_CLASS = "request-reporter-class";

	/**
	 * Properties for a StopwatchSource class. Has the following format: prop1=val1;prop2=val2
	 * Properties are assumed to be correct Java bean properties and should exist in a class specified by
	 * {@link org.javasimon.javaee.SimonServletFilter#INIT_PARAM_STOPWATCH_SOURCE_CLASS}
	 */
	public static final String INIT_PARAM_STOPWATCH_SOURCE_PROPS = "stopwatch-source-props";

	private static Replacer FINAL_SLASH_REMOVE = new Replacer("/*$", "");

	private static Replacer SLASH_TRIM = new Replacer("^/*(.*?)/*$", "$1");

	/**
	 * Threshold in ns - any request longer than this will be reported by current {@link #requestReporter} instance.
	 * Specified by {@link #INIT_PARAM_REPORT_THRESHOLD_MS} ({@value #INIT_PARAM_REPORT_THRESHOLD_MS}) in the {@code web.xml} (in ms,
	 * converted to ns during servlet init). This is the default value returned by {@link #getThreshold(javax.servlet.http.HttpServletRequest)}
	 * but it may be completely ignored if method is overridden so. However if the field is {@code null} threshold reporting feature
	 * is disabled.
	 */
	protected Long reportThresholdNanos;

	/**
	 * URL path that displays Simon tree - it is console-path without the ending slash.
	 */
	protected String printTreePath;

	/**
	 * URL path that displays Simon web console (or null if no console is required).
	 */
	protected String consolePath;

	/**
	 * Simon Manager used by the filter.
	 */
	private Manager manager = SimonManager.manager();

	/**
	 * Thread local list of splits used to cumulate all splits for the request.
	 * Every instance of the Servlet has its own thread-local to bind its lifecycle to
	 * the callback that servlet is registering. Then even more callbacks registered from various
	 * servlets in the same manager do not interfere.
	 */
	private final ThreadLocal<List<Split>> splitsThreadLocal = new ThreadLocal<>();

	/**
	 * Callback that saves all splits in {@link #splitsThreadLocal} if {@link #reportThresholdNanos} is configured.
	 */
	private SplitSaverCallback splitSaverCallback;

	/**
	 * Stopwatch source is used before/after each request to start/stop a stopwatch.
	 */
	private StopwatchSource<HttpServletRequest> stopwatchSource;

	/**
	 * Object responsible for reporting the request over threshold (if {@link #shouldBeReported(javax.servlet.http.HttpServletRequest, long, java.util.List)}
	 * returns true).
	 */
	private RequestReporter requestReporter;

	/**
	 * Initialization method that processes various init parameters from {@literal web.xml} and sets manager, if
	 * {@link org.javasimon.utils.SimonUtils#MANAGER_SERVLET_CTX_ATTRIBUTE} servlet context attribute is not {@code null}.
	 *
	 * @param filterConfig filter config object
	 */
	public final void init(FilterConfig filterConfig) {
		pickUpSharedManagerIfExists(filterConfig);
		stopwatchSource = SimonServletFilterUtils.initStopwatchSource(filterConfig, manager);
		setStopwatchSourceProperties(filterConfig, stopwatchSource);

		requestReporter = SimonServletFilterUtils.initRequestReporter(filterConfig);
		requestReporter.setSimonServletFilter(this);

		String reportThreshold = filterConfig.getInitParameter(INIT_PARAM_REPORT_THRESHOLD_MS);
		if (reportThreshold != null) {
			try {
				this.reportThresholdNanos = Long.parseLong(reportThreshold) * ClockUtils.NANOS_IN_MILLIS;
				splitSaverCallback = new SplitSaverCallback();
				manager.callback().addCallback(splitSaverCallback);
			} catch (NumberFormatException e) {
				// ignore
			}
		}

		String consolePath = filterConfig.getInitParameter(INIT_PARAM_SIMON_CONSOLE_PATH);
		if (consolePath != null) {
			this.printTreePath = FINAL_SLASH_REMOVE.process(consolePath);
			this.consolePath = printTreePath + "/";
		}
	}

	private void setStopwatchSourceProperties(FilterConfig filterConfig, StopwatchSource<HttpServletRequest> stopwatchSource) {
		String properties = filterConfig.getInitParameter(INIT_PARAM_STOPWATCH_SOURCE_PROPS);
		if (properties == null) {
			return;
		}

		registerEnumConverter();
		for (String keyValStr : properties.split(";")) {
			String[] keyVal = keyValStr.split("=");
			String key = keyVal[0];
			String val = keyVal[1];

			SimonBeanUtils.getInstance().setProperty(stopwatchSource, key, val);
		}
	}

	private void registerEnumConverter() {
		SimonBeanUtils.getInstance().registerConverter(HttpStopwatchSource.IncludeHttpMethodName.class, new ToEnumConverter());
	}

	private void pickUpSharedManagerIfExists(FilterConfig filterConfig) {
		Object managerObject = filterConfig.getServletContext().getAttribute(SimonUtils.MANAGER_SERVLET_CTX_ATTRIBUTE);
		if (managerObject != null && managerObject instanceof Manager) {
			manager = (Manager) managerObject;
		}
	}

	/**
	 * Wraps the HTTP request with Simon measuring. Separate Simons are created for different URIs (parameters
	 * ignored).
	 *
	 * @param servletRequest HTTP servlet request
	 * @param servletResponse HTTP servlet response
	 * @param filterChain filter chain
	 * @throws IOException possibly thrown by other filter/servlet in the chain
	 * @throws ServletException possibly thrown by other filter/servlet in the chain
	 */
	public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String localPath = request.getRequestURI().substring(request.getContextPath().length());
		if (consolePath != null && (localPath.equals(printTreePath) || localPath.startsWith(consolePath))) {
			consolePage(request, response, localPath);
			return;
		}

		doFilterWithMonitoring(filterChain, request, response);
	}

	private void doFilterWithMonitoring(FilterChain filterChain, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Split split = stopwatchSource.start(request);
		if (split.isEnabled() && reportThresholdNanos != null) {
			splitsThreadLocal.set(new ArrayList<Split>());
		}

		try {
			filterChain.doFilter(request, response);
			// TODO: is it sensible to catch exceptions here and stop split with tags?
			// for instance Wicket does not let the exception go to here anyway
		} finally {
			stopSplitForRequest(request, split);
		}
	}

	private void stopSplitForRequest(HttpServletRequest request, Split split) {
		if (split.isEnabled()) {
			split.stop();
			long splitNanoTime = split.runningFor();
			if (reportThresholdNanos != null) {
				List<Split> splits = splitsThreadLocal.get();
				splitsThreadLocal.remove(); // better do this before we call potentially overridden method
				if (shouldBeReported(request, splitNanoTime, splits)) {
					requestReporter.reportRequest(request, split, splits);
				}
			}
		}
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
	protected boolean shouldBeReported(HttpServletRequest request, long requestNanoTime, List<Split> splits) {
		return requestNanoTime > getThreshold(request);
	}

	/**
	 * Returns actual threshold in *nanoseconds* (not ms as configured) which allows to further customize threshold per request - intended for override.
	 * Default behavior returns configured {@link #reportThresholdNanos} (already converted to ns).
	 *
	 * @param request HTTP Request
	 * @return threshold in ns for current request
	 * @since 3.2
	 */
	protected long getThreshold(HttpServletRequest request) {
		return reportThresholdNanos;
	}

	private void consolePage(HttpServletRequest request, HttpServletResponse response, String localPath) throws IOException {
		response.setContentType("text/plain");
		response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
		response.setHeader("Pragma", "no-cache");

		if (localPath.equals(printTreePath)) {
			printSimonTree(response);
			return;
		}

		String subCommand = SLASH_TRIM.process(localPath.substring(consolePath.length()));
		if (subCommand.isEmpty()) {
			printSimonTree(response);
		} else if (subCommand.equalsIgnoreCase("clearManager")) {
			manager.clear();
			response.getOutputStream().println("Simon Manager was cleared");
		} else if (subCommand.equalsIgnoreCase("help")) {
			simonHelp(response);
		} else {
			response.getOutputStream().println("Invalid command\n");
			simonHelp(response);
		}
	}

	private void simonHelp(ServletResponse response) throws IOException {
		response.getOutputStream().println("Simon Console help - available commands:");
		response.getOutputStream().println("- clearManager - clears the manager (removes all Simons)");
		response.getOutputStream().println("- help - shows this help");
	}

	private void printSimonTree(ServletResponse response) throws IOException {
		response.getOutputStream().println(SimonUtils.simonTreeString(manager.getRootSimon()));
	}

	public Manager getManager() {
		return manager;
	}

	/**
	 * Returns stopwatch source used by the filter.
	 *
	 * @return stopwatch source
	 */
	StopwatchSource<HttpServletRequest> getStopwatchSource() {
		return stopwatchSource;
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
