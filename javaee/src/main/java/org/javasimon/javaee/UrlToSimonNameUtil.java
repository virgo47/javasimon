package org.javasimon.javaee;

import org.javasimon.utils.Replacer;
import org.javasimon.utils.SimonUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Default implementation of URL to Simon name conversion for {@link SimonServletFilter}.
 *
 * @author virgo47@gmail.com
 */
public class UrlToSimonNameUtil {
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
	 * Returns Simon name for the specified request (without prefix) - by default dots and all non-simon-name compliant
	 * characters are removed first, then all slashes are switched to dots (repeating slashes make one dot).
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
}
