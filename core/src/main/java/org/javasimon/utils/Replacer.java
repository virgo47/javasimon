package org.javasimon.utils;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Replacer stores {@code from} regex as pattern and its {@link #process} method than returns
 * string with all {@code from} replaced with {@code to}. Using the replacer should be faster
 * because the regex is compiled only once. {@link java.util.regex.Pattern#matcher(CharSequence)}
 * and {@link java.util.regex.Matcher#replaceAll(String)} is used internally, hence {@code to}
 * parameter is regex as well and can reference matched group from the {@code from} String.
 * <p/>
 * Behavior can be further modified by {@link Modificator}s.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class Replacer {
	private final Pattern from;
	private String to;
	private boolean repeatUntilUnchanged;
	private boolean ignoreCase;
	private Modificator[] modificators;

	/**
	 * Creates the replacer with from->to regex specification.
	 *
	 * @param from replaced regex
	 * @param to replacement string
	 * @param modificators list of modificators of the behavior or pattern treatment
	 */
	public Replacer(final String from, final String to, Modificator... modificators) {
		processModificators(modificators);

		this.from = ignoreCase ? Pattern.compile(from, Pattern.CASE_INSENSITIVE) : Pattern.compile(from);
		this.to = to;
		this.modificators = modificators;
	}

	private void processModificators(Modificator... modificators) {
		for (Modificator modificator : modificators) {
			switch (modificator) {
				case IGNORE_CASE:
					ignoreCase = true;
					break;
				case REPEAT_UNTIL_UNCHANGED:
					repeatUntilUnchanged = true;
					break;
				default:
			}
		}
	}

	/**
	 * Returns replacement string.
	 *
	 * @return replacement string
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Sets replacement string - this can be changed anytime with any subsequent {@link #process(String)} calls
	 * reflecting this change immediatelly.
	 *
	 * @param to new replacement string
	 */
	public void setTo(String to) {
		this.to = to;
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
	 * Flags for modifying the default replacer behavior.
	 */
	public enum Modificator {
		/**
		 * Enforces repetitive replacer application until there is no other change to perform.
		 * Attention should be paid to provide from/to combination that converges to the final
		 * result otherwise the process method may trigger infinite loop and/or excessive
		 * memory consumption.
		 */
		REPEAT_UNTIL_UNCHANGED,

		/**
		 * Applies {@link Pattern#CASE_INSENSITIVE} flag to the pattern.
		 */
		IGNORE_CASE
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
			", mods=" + Arrays.toString(modificators) +
			'}';
	}
}
