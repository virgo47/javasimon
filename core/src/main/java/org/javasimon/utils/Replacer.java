package org.javasimon.utils;

import java.util.regex.Pattern;

/**
 * Replacer stores {@code from} regex as pattern and its {@link #process} method than returns
 * string with all {@code from} replaced with {@code to}. Using the replacer should be faster
 * because the regex is compiled only once. {@link java.util.regex.Pattern#matcher(CharSequence)}
 * and {@link java.util.regex.Matcher#replaceAll(String)} is used internally, hence {@code to}
 * parameter is not a plain string and can reference matched group from the {@code from} String.
 * Object allows to specify replace operation that should be re-applied while the replacement
 * causes any change - use {@link #Replacer(String, String, boolean)} with last parameter
 * {@code repeatUntilUnchanged} set to {@code true}.
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
		this(from, to, false);
	}

	/**
	 * Creates the replacer with from->to regex specification. UntilUnchanged set to true
	 * enforces repetitive replacer application until there is no other change to perform.
	 * Attention should be paid to provide from/to combination that converges to the final
	 * result otherwise the process method may trigger infinite loop and/or excessive
	 * memory consumption.
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
	public String process(final String in) {
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

	/**
	 * Returns from, to and untilUnchanged fields as a human readable string.
	 *
	 * @return internal details of the replacer as a string
	 */
	@Override
	public String toString() {
		return "Replacer{" +
			"from='" + from.pattern() + '\'' +
			", to='" + to + '\'' +
			", untilUnchanged=" + repeatUntilUnchanged +
			'}';
	}
}
