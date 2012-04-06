package org.javasimon.javaee;

import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.utils.Replacer;
import org.javasimon.utils.SimonUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.javasimon.source.DisabledMonitorSource;
import org.javasimon.source.MonitorSource;
import org.javasimon.source.StopwatchTemplate;

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
 * are provided to override the default function:
 * <ul>
 * <li>{@link #shouldBeReported} - compares actual request nano time with {@link #getThreshold(javax.servlet.http.HttpServletRequest)}
 * (which may become unused if this method is overriden)</li>
 * <li>{@link #getThreshold(javax.servlet.http.HttpServletRequest)} - returns threshold configured in {@code web.xml}</li>
 * <li>{@link #reportRequestOverThreshold(javax.servlet.http.HttpServletRequest, org.javasimon.Split, java.util.List)}</li>
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
	 * FQN of the Stopwatch source class implementing {@link org.javasimon.source.MonitorSource}.
	 * One can use {@link DisabledMonitorSource} to disabled monitoring.
	 * Defaults to {@link HttpStopwatchSource}.
	 */
	public static final String INIT_PARAM_STOPWATCH_SOURCE_CLASS = "stopwatch-source-class";

	/**
	 * Name of filter init parameter for Simon name prefix.
	 */
	public static final String INIT_PARAM_STOPWATCH_SOURCE_PREFIX = "prefix";

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
	 * URL path that displays Simon tree - it is console-path without the ending slash.
	 */
	protected String printTreePath;

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
	 * Stopwatch template is invoked before/after each request to start/stop.
	 */
	private StopwatchTemplate<HttpServletRequest> stopwatchTemplate;

	/**
	 * Create and initialize the stopwatch source depending on
	 * filter init parameter
	 *
	 * @param filterConfig Filter configuration
	 * @return Stopwatch source
	 */
	@SuppressWarnings("unchecked")
	protected MonitorSource<HttpServletRequest, Stopwatch> initStopwatchSource(FilterConfig filterConfig) {
		MonitorSource<HttpServletRequest, Stopwatch> stopwatchSource;
		String stopwatchSourceClass = filterConfig.getInitParameter(INIT_PARAM_STOPWATCH_SOURCE_CLASS);

		if (stopwatchSourceClass == null) {
			stopwatchSource = new HttpStopwatchSource();
		} else {
			try {
				stopwatchSource = (MonitorSource<HttpServletRequest, Stopwatch>)
					Class.forName(stopwatchSourceClass).newInstance();
			} catch (ClassNotFoundException classNotFoundException) {
				throw new IllegalArgumentException("Invalid Stopwatch source class name", classNotFoundException);
			} catch (InstantiationException instantiationException) {
				throw new IllegalArgumentException("Invalid Stopwatch source class name", instantiationException);
			} catch (IllegalAccessException illegalAccessException) {
				throw new IllegalArgumentException("Invalid Stopwatch source class name", illegalAccessException);
			}
		}

		// Inject prefix into HTTP Stopwatch source
		String simonPrefix = filterConfig.getInitParameter(INIT_PARAM_STOPWATCH_SOURCE_PREFIX);
		if (simonPrefix != null) {
			if (stopwatchSource instanceof HttpStopwatchSource) {
				HttpStopwatchSource httpStopwatchSource = (HttpStopwatchSource) stopwatchSource;
				httpStopwatchSource.setPrefix(simonPrefix);
			} else {
				throw new IllegalArgumentException("Prefix init param is only compatible with HttpStopwatchSource");
			}

		}

		// Wrap stopwatch source in a cache
		String cache = filterConfig.getInitParameter(INIT_PARAM_STOPWATCH_SOURCE_CACHE);
		if (cache != null && Boolean.parseBoolean(cache)) {
			stopwatchSource = HttpStopwatchSource.newCacheStopwatchSource(stopwatchSource);
		}
		return stopwatchSource;
	}

	/**
	 * Initialization method that processes {@link #INIT_PARAM_PREFIX} and {@link #INIT_PARAM_PUBLISH_MANAGER}
	 * parameters from {@literal web.xml}.
	 *
	 * @param filterConfig filter config object
	 */
	public final void init(FilterConfig filterConfig) {
		stopwatchTemplate = new StopwatchTemplate<HttpServletRequest>(initStopwatchSource(filterConfig));

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
			this.printTreePath = FINAL_SLASH_REMOVE.process(consolePath);
			this.consolePath = printTreePath + "/";
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
		if (consolePath != null && (localPath.equals(printTreePath) || localPath.startsWith(consolePath))) {
			consolePage(request, response, localPath);
			return;
		}
		Split split = stopwatchTemplate.start(request);
		if (split != null && reportThresholdNanos != null) {
			splitsThreadLocal.set(new ArrayList<Split>());
		}

		try {
			filterChain.doFilter(request, response);
		} finally {
			if (split != null) {
				long splitNanoTime = split.stop().runningFor();
				if (reportThresholdNanos != null) {
					List<Split> splits = splitsThreadLocal.get();
					splitsThreadLocal.remove(); // better do this before we call potentially overriden method
					if (shouldBeReported(request, splitNanoTime, splits)) {
						reportRequestOverThreshold(request, split, splits);
					}
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
		StringBuilder messageBuilder = new StringBuilder(
			"Web request is too long (" + SimonUtils.presentNanoTime(requestSplit.runningFor()) +
				") [" + requestSplit.getStopwatch().getNote() + "]");
		for (Split split : splits) {
			messageBuilder.append("\n\t").append(split.getStopwatch().getName()).append(": ").
				append(SimonUtils.presentNanoTime(split.runningFor()));
		}
		manager.message(messageBuilder.toString());
	}

	private void consolePage(HttpServletRequest request, HttpServletResponse response, String localPath) throws IOException {
		response.setContentType("text/plain");
		response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
		response.setHeader("Pragma", "no-cache");

		if (localPath.equals(printTreePath)) {
			printSimonTree(response);
			return;
		}

		String subcommand = SLASH_TRIM.process(localPath.substring(consolePath.length()));
		if (subcommand.isEmpty()) {
			printSimonTree(response);
		} else if (subcommand.equalsIgnoreCase("clear")) {
			manager.clear();
			response.getOutputStream().println("Simon Manager was cleared");
		} else if (subcommand.equalsIgnoreCase("help")) {
			simonHelp(response);
		} else {
			response.getOutputStream().println("Invalid command\n");
			simonHelp(response);
		}
	}

	private void simonHelp(ServletResponse response) throws IOException {
		response.getOutputStream().println("Simon Console help - available commands:");
		response.getOutputStream().println("- clear - clears the manager (removes all Simons)");
		response.getOutputStream().println("- help - shows this help");
	}

	private void printSimonTree(ServletResponse response) throws IOException {
		response.getOutputStream().println(SimonUtils.simonTreeString(manager.getRootSimon()));
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
		public void onStopwatchStart(Split split, StopwatchSample sample) {
			List<Split> splits = splitsThreadLocal.get();
			if (splits != null) {
				splits.add(split);
			}
		}
	}
}
