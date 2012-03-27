package org.javasimon.javaee;

import org.javasimon.utils.Replacer;
import org.javasimon.utils.SimonUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Default implementation of URL to Simon name conversion for {@link SimonServletFilter}.
 *
 * @author virgo47@gmail.com
 */
public class DefaultUrlToSimonNameUtil {
	/**
	 * Regex replacer for unallowed characters in Simon names.
	 */
	private static final Replacer UNALLOWED_CHARS_PATTERN = new Replacer(createUnallowedCharsPattern(), "");
	/**
	 * Regex replacer for any number of slashes or dots for a single dot.
	 */
	private static final Replacer TO_DOT_PATTERN = new Replacer("[/.]+", ".");

	/**
	 * Initializes the pattern used to remove unallowed characters from the URL.
	 *
	 * @return compiled pattern matching characters to remove from the URL
	 */
	private static String createUnallowedCharsPattern() {
		StringBuilder sb = new StringBuilder(SimonUtils.NAME_PATTERN.pattern());
		sb.insert(1, "^/"); // negates the whole character group ("whatever is not allowed character")
		sb.deleteCharAt(sb.indexOf(".")); // don't spare dots either (not allowed for URL)
		return sb.toString();
	}

	/**
	 * Returns Simon name for the specified request - by default dots and all non-simon-name compliant characters
	 * are removed first, then all slashes are switched to dots (repeating slashes make one dot).
	 *
	 * @param request HTTP servlet request
	 * @return Simon name for the request URI
	 */
	public static String getSimonName(HttpServletRequest request) {
		String name = request.getRequestURI();
		if (name.startsWith("/")) {
			name = name.substring(1);
		}
		name = UNALLOWED_CHARS_PATTERN.process(name);
		name = TO_DOT_PATTERN.process(name);
		return name;
	}
}
