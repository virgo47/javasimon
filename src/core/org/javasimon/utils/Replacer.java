package org.javasimon.utils;

import java.util.regex.Pattern;

/**
 * Replacer stores {@code from} regex as pattern and process method than returns string with
 * all {@code from} replaced with {@code to}. Using the replacer should be faster
 * because the regex is compiled only once. Object allows to specify replace operation
 * that should be re-applied while the replacement cause any change - use
 * {@link #Replacer(String, String, boolean)} with last parameter {@code repeatUntilUnchanged}
 * set to true.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 28, 2008
 */
public final class Replacer {
	private final Pattern from;
	private final String to;
	private final boolean repeatUntilUnchanged;

	/**
	 * Creates the replacer with from->to regex specification.
	 *
	 * @param from replaced regex
	 * @param to replacement string
	 */
	public Replacer(final String from, final String to) {
		this.from = Pattern.compile(from);
		this.to = to;
		this.repeatUntilUnchanged = false;
	}

	/**
	 * Creates the replacer with from->to regex specification. UntilUnchanged set to true
	 * enforces repetitive replacer application until there is no other change to perform.
	 *
	 * @param from replaced regex
	 * @param to replacement string
	 * @param repeatUntilUnchanged if true, replacement is re-applied while it changes the result
	 */
	public Replacer(final String from, final String to, final boolean repeatUntilUnchanged) {
		this.from = Pattern.compile(from);
		this.to = to;
		this.repeatUntilUnchanged = repeatUntilUnchanged;
	}

	/**
	 * Processes input, replaces all as expected and returns the result.
	 *
	 * @param in input string
	 * @return replaced string
	 */
	public String process(String in) {
		if (repeatUntilUnchanged) {
			String retVal = in;
			String old = "";
			while (!old.equals(retVal)) {
				old = retVal;
				retVal = from.matcher(retVal).replaceAll(to);
			}
			return retVal;
		}
		return from.matcher(in).replaceAll(to);
	}

	@Override
	public String toString() {
		return "Replacer{" +
			"from='" + from.pattern() + '\'' +
			", to='" + to + '\'' +
			", untilUnchanged=" + repeatUntilUnchanged +
			'}';
	}
}
