package org.javasimon.javaee;

import org.javasimon.Manager;
import org.javasimon.Stopwatch;
import org.javasimon.source.MonitorSource;
import org.javasimon.utils.Replacer;
import org.javasimon.utils.SimonUtils;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;

/**
 * Various supporting utility methods for {@link SimonServletFilter}.
 *
 * @author virgo47@gmail.com
 */
public class SimonServletFilterUtils {
	/**
	 * Regex replacer for any number of slashes or dots for a single dot.
	 */
	private static final Replacer TO_DOT_PATTERN = new Replacer("[/.]+", ".");

	/**
	 * Creates new replacer for unallowed characters in the URL.
	 *
	 *
	 * @param replacement replacement string (for every unallowed character)
	 * @return compiled pattern matching characters to remove from the URL
	 */
	public static Replacer createUnallowedCharsReplacer(String replacement) {
		StringBuilder sb = new StringBuilder(SimonUtils.NAME_PATTERN.pattern());
		sb.insert(1, "^/"); // negates the whole character group ("whatever is not allowed character")
		sb.deleteCharAt(sb.indexOf(".")); // don't spare dots either (not allowed for URL)
		return new Replacer(sb.toString(), replacement);
	}

	/**
	 * Returns Simon name for the specified request (local name without any configured prefix). By default dots and all non-simon-name
	 * compliant characters are removed first, then all slashes are switched to dots (repeating slashes make one dot).
	 *
	 * @param request HTTP servlet request
	 * @param unallowedCharacterReplacer replacer for characters that are not allowed in Simon name
	 * @return local part of the Simon name for the request URI (without prefix)
	 */
	public static String getSimonName(HttpServletRequest request, Replacer unallowedCharacterReplacer) {
		String name = request.getRequestURI();
		if (name.startsWith("/")) {
			name = name.substring(1);
		}
		name = unallowedCharacterReplacer.process(name);
		name = TO_DOT_PATTERN.process(name);
		return name;
	}

	/**
	 * Create and initialize the stopwatch source depending on
	 * filter init parameter
	 *
	 * @param filterConfig Filter configuration
	 * @return Stopwatch source
	 */
	@SuppressWarnings("unchecked")
	protected static MonitorSource<HttpServletRequest, Stopwatch> initStopwatchSource(FilterConfig filterConfig, Manager manager) {
		MonitorSource<HttpServletRequest, Stopwatch> stopwatchSource;
		String stopwatchSourceClass = filterConfig.getInitParameter(SimonServletFilter.INIT_PARAM_STOPWATCH_SOURCE_CLASS);

		if (stopwatchSourceClass == null) {
			stopwatchSource = new HttpStopwatchSource(manager);
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
		String simonPrefix = filterConfig.getInitParameter(SimonServletFilter.INIT_PARAM_PREFIX);
		if (simonPrefix != null) {
			if (stopwatchSource instanceof HttpStopwatchSource) {
				HttpStopwatchSource httpStopwatchSource = (HttpStopwatchSource) stopwatchSource;
				httpStopwatchSource.setPrefix(simonPrefix);
			} else {
				throw new IllegalArgumentException("Prefix init param is only compatible with HttpStopwatchSource");
			}
		}

		// Wrap stopwatch source in a cache
		String cache = filterConfig.getInitParameter(SimonServletFilter.INIT_PARAM_STOPWATCH_SOURCE_CACHE);
		if (cache != null && Boolean.parseBoolean(cache)) {
			stopwatchSource = HttpStopwatchSource.newCacheStopwatchSource(stopwatchSource);
		}
		return stopwatchSource;
	}

	/**
	 * Returns RequestReporter for the class specified for context parameter {@link SimonServletFilter#INIT_PARAM_REQUEST_REPORTER_CLASS}.
	 */
	public static RequestReporter initRequestReporter(FilterConfig filterConfig) {
		RequestReporter reporter;

		String className = filterConfig.getInitParameter(SimonServletFilter.INIT_PARAM_REQUEST_REPORTER_CLASS);

		if (className == null) {
			reporter = new DefaultRequestReporter();
		} else {
			try {
				reporter = (RequestReporter) Class.forName(className).newInstance();
			} catch (ClassNotFoundException classNotFoundException) {
				throw new IllegalArgumentException("Invalid Request reporter class name", classNotFoundException);
			} catch (InstantiationException instantiationException) {
				throw new IllegalArgumentException("Invalid Request reporter class name", instantiationException);
			} catch (IllegalAccessException illegalAccessException) {
				throw new IllegalArgumentException("Invalid Request reporter class name", illegalAccessException);
			}
		}

		return reporter;
	}
}
