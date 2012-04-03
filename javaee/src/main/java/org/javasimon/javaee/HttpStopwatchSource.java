package org.javasimon.javaee;

import org.javasimon.Manager;
import org.javasimon.Stopwatch;
import org.javasimon.source.AbstractStopwatchSource;
import org.javasimon.source.CacheMonitorSource;
import org.javasimon.source.MonitorSource;
import org.javasimon.utils.Replacer;

import javax.servlet.http.HttpServletRequest;

/**
 * Provide stopwatch source for HTTP Servlet request.
 * Used by {@link SimonServletFilter} as default stopwatch source.
 * Can be overriden to customize monitored HTTP Requests and their
 * related Simon name.
 *
 * To select which HTTP Request should be monitored (by default everything is
 * monitored), can override {@link #isMonitored} method:
 * <code>
 * String uri = request.getRequestURI().toLowerCase();
 * return !(uri.endsWith(".css") || uri.endsWith(".png") || uri.endsWith(".gif") || uri.endsWith(".jpg") || uri.endsWith(".js"));
 * </code>
 *
 * @author gquintana
 */
public class HttpStopwatchSource extends AbstractStopwatchSource<HttpServletRequest> {
	/**
	 * Default prefix for web filter Simons if no "prefix" init parameter is used.
	 */
	public static final String DEFAULT_SIMON_PREFIX = "org.javasimon.web";

	/**
	 * Simon prefix (protected for subclass usage) - can be {@code null}.
	 */
	private String prefix = DEFAULT_SIMON_PREFIX;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * String used as replacement for unallowed characters - defaults to '_'.
	 */
	private Replacer unallowedCharacterReplacer = UrlToSimonNameUtil.createUnallowedCharsReplacer("_");

	public String getReplaceUnallowed() {
		return unallowedCharacterReplacer.getTo();
	}

	public void setReplaceUnallowed(String replaceUnallowed) {
		unallowedCharacterReplacer.setTo(replaceUnallowed);
	}

	/**
	 * Returns Simon name for the specified HTTP request with the specified prefix. By default it contains URI without parameters with
	 * all slashes replaced for dots (slashes then determines position in Simon hierarchy). Method can be
	 * overriden.
	 *
	 * @param request HTTP request
	 * @return fully qualified name of the Simon
	 */
	protected String getMonitorName(HttpServletRequest request) {
		String localName = UrlToSimonNameUtil.getSimonName(request, unallowedCharacterReplacer);
		if (prefix == null || prefix.isEmpty()) {
			return localName;
		}
		return prefix + Manager.HIERARCHY_DELIMITER + localName;
	}

	/**
	 * Get a stopwatch for given HTTP request.
	 *
	 * @param request Method HTTP request
	 * @return Stopwatch for the HTTP request
	 */
	@Override
	public Stopwatch getMonitor(HttpServletRequest request) {
		final Stopwatch stopwatch = super.getMonitor(request);
		if (stopwatch.getNote() == null) {
			stopwatch.setNote(request.getRequestURI());
		}
		return stopwatch;
	}

	/**
	 * Wraps given stop watch source in a cache.
	 *
	 * @param stopwatchSource Stopwatch source
	 * @return Cached stopwatch source
	 */
	public static MonitorSource<HttpServletRequest, Stopwatch> newCacheStopwatchSource(MonitorSource<HttpServletRequest, Stopwatch> stopwatchSource) {
		return new CacheMonitorSource<HttpServletRequest, Stopwatch, String>(stopwatchSource) {
			@Override
			protected String getLocationKey(HttpServletRequest l) {
				return l.getRequestURI();
			}
		};
	}
}
