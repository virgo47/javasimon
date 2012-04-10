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
 * To select which HTTP Request should be monitored method {@link #isMonitored} can be overriden. Default implementation monitors everything except for
 * typical resource-like requests (images, JS/CSS, ...).
 *
 * @author gquintana
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
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

	/**
	 * String used as replacement for unallowed characters - defaults to '_'.
	 */
	private Replacer unallowedCharacterReplacer = SimonServletFilterUtils.createUnallowedCharsReplacer("_");

	public HttpStopwatchSource(Manager manager) {
		super(manager);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

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
		String localName = SimonServletFilterUtils.getSimonName(request, unallowedCharacterReplacer);
		if (prefix == null || prefix.isEmpty()) {
			return localName;
		}
		return prefix + Manager.HIERARCHY_DELIMITER + localName;
	}

	/**
	 * Indicates whether the HTTP Request should be monitored - method is intended for override.
	 * Default behavior ignores URIs ending with .css, .png, .gif, .jpg and .js (ignores casing).
	 *
	 * @param httpServletRequest HTTP Request
	 * @return true to enable request monitoring, false either
	 */
	@Override
	public boolean isMonitored(HttpServletRequest httpServletRequest) {
		String uri = httpServletRequest.getRequestURI().toLowerCase();
		return !(uri.endsWith(".css") || uri.endsWith(".png") || uri.endsWith(".gif") || uri.endsWith(".jpg") || uri.endsWith(".js"));
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
