package org.javasimon.javaee;

import org.javasimon.Manager;
import org.javasimon.Stopwatch;
import org.javasimon.javaee.reqreporter.DefaultRequestReporter;
import org.javasimon.javaee.reqreporter.RequestReporter;
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
	 * Creates new replacer for unallowed characters in the URL. This inverts character group for name pattern
	 * ({@link SimonUtils#NAME_PATTERN_CHAR_CLASS_CONTENT}) and replaces its dot with slash too (dots are to be
	 * replaced, slashs preserved in this step of URL processing).
	 *
	 * @param replacement replacement string (for every unallowed character)
	 * @return compiled pattern matching characters to remove from the URL
	 */
	public static Replacer createUnallowedCharsReplacer(String replacement) {
		return new Replacer("[^" + SimonUtils.NAME_PATTERN_CHAR_CLASS_CONTENT.replace('.', '/') + "]+", replacement);
	}

	/**
	 * Returns Simon name for the specified request (local name without any configured prefix). By default dots and all non-simon-name
	 * compliant characters are removed first, then all slashes are switched to dots (repeating slashes make one dot).
	 *
	 * @param uri request URI
	 * @param unallowedCharacterReplacer replacer for characters that are not allowed in Simon name
	 * @return local part of the Simon name for the request URI (without prefix)
	 */
	public static String getSimonName(String uri, Replacer unallowedCharacterReplacer) {
		if (uri.startsWith("/")) {
			uri = uri.substring(1);
		}
		String name = unallowedCharacterReplacer.process(uri);
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
