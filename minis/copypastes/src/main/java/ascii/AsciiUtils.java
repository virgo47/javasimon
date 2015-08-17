package ascii;

import java.text.Normalizer;

import org.apache.commons.lang3.StringUtils;
import org.javasimon.Split;

/**
 * Tests various diacritics strippers (7-bit ASCII normalization).
 * First important question is what is expected? Is it to convert characters to ASCII in
 * best-effort mode and leave other characters as-is? Or is it to produce truly ASCII string
 * without any exceptions?
 * <ul>
 * <li><b>There is no universal easy method to do ASCII conversion without loss of information.</b>
 * Notice that often one charcter is represented with two ASCII characters when people write without
 * diacritics/non-ASCII (think sharp-S replaced by ss, u with : replaced by ue, etc.).</li>
 * <li>Two strict-ASCII solutions are presented - one using latin1+2 translation table (represented
 * as String) and one using Normalizer with NFD, followed by cycle.</li>
 * <li>In both cases tests/ifs ensure, that no non-ASCII character will get into output.</li>
 * <li>Table solution works for Java < 1.6 (not a real problem mostly).</li>
 * <li>Table solution is the fastest one (50x than regex, 6x than NFD+cycle).</li>
 * <li>Table solution works only for Latin 1+2 (rest is replaced with substitute). NFD-based
 * solutions work for characters where the first character after decomposition is ASCII.</li>
 * <li>Regular expression replacing all diacritical marks does NOT ensure pure ASCII result.</li>
 * <li>Apache's solution is the same internally, but has a junction to internal pre-Java 6 implementation.</li>
 * </ul>
 * Add deps manually in IDE:
 * <ul>
 * <li>org.apache.commons:commons-lang3:3.1 (Apache's stripAccents)</li>
 * <li>org.javasimon:javasimon-core:4.1.0 (perf test)</li>
 * </ul>
 */
public class AsciiUtils {

	public static final String TEST_STRING =
		"Ĺúbime normálne písmenká s diakritkou (mäkčene a DĹŽNE): ľšťčšľžýžéí, æøåℌð";
	// "æøåáℌð";

	public static final char SUBSTITUTE = '\u001a';
	public static final char ASCII_MAX = '\u007F';

	private static final int TEST_TIMES = 100_000;

	public static void main(String[] args) {
		String normalized = Normalizer.normalize(TEST_STRING, Normalizer.Form.NFD);
		String strippedWithRegex = stripWithRegex(TEST_STRING);
		String strippedNfdAndCycle = stripWithNdfAndCycle(TEST_STRING);
		String strippedWithTable = stripWithTable(TEST_STRING);
		String strippedApache = StringUtils.stripAccents(TEST_STRING);

		System.out.println("TEST_STRING = (" + TEST_STRING.length() + ")\n" + TEST_STRING);
		System.out.println("normalized = (" + normalized.length() + ")\n" + normalized);
		System.out.println("strippedWithRegex = (" + strippedWithRegex.length() + ")\n" + strippedWithRegex);
		System.out.println("strippedNfdAndCycle = (" + strippedNfdAndCycle.length() + ")\n" + strippedNfdAndCycle);
		System.out.println("strippedWithTable = (" + strippedWithTable.length() + ")\n" + strippedWithTable);
		System.out.println("strippedApache = (" + strippedApache.length() + ")\n" + strippedApache);
		System.out.println();
		System.out.println("isAscii(strippedWithRegex) = " + isAscii(strippedWithRegex));
		System.out.println("isAscii(strippedNfdAndCycle) = " + isAscii(strippedNfdAndCycle));
		System.out.println("isAscii(strippedWithTable) = " + isAscii(strippedWithTable));
		System.out.println("isAscii(strippedApache) = " + isAscii(strippedApache));

		for (int times = 0; times < 3; times++) {
			testPerf();
		}
	}

	public static String stripWithRegex(String s) {
		return Normalizer.normalize(s, Normalizer.Form.NFD)
			.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

	public static String stripWithNdfAndCycle(String string) {
		char[] out = new char[string.length()];
		string = Normalizer.normalize(string, Normalizer.Form.NFD);
		int j = 0;
		for (int i = 0, n = string.length(); i < n; ++i) {
			char c = string.charAt(i);
			if (c <= ASCII_MAX) {
				out[j++] = c;
			} else if (Character.isLetter(c)) {
				// replacing unconvertable to something else than NULL char
				out[j++] = SUBSTITUTE; // ASCII substitute character
				/*
				In any case at least that j++ MUST be here. Character.isLetter may not be the
				best condition for this branch, but is the best I know of. Alternative may be
				something like "if the character is NOT combining character".
				*/
			}
		}
		return new String(out);
	}

	/** Mirror of the unicode table from 00c0 to 017f without diacritics. */
	private static final String TAB_00C0 = "AAAAAAACEEEEIIII" +
		"DNOOOOO*OUUUUYIs" +
		"aaaaaaaceeeeiiii" +
		"?nooooo/ouuuuy?y" +
		"AaAaAaCcCcCcCcDd" +
		"DdEeEeEeEeEeGgGg" +
		"GgGgHhHhIiIiIiIi" +
		"IiJjJjKkkLlLlLlL" +
		"lLlNnNnNnnNnOoOo" +
		"OoOoRrRrRrSsSsSs" +
		"SsTtTtTtUuUuUuUu" +
		"UuUuWwYyYZzZzZzF".replace('?', SUBSTITUTE);

	/**
	 * Returns string without diacritics - 7 bit approximation.
	 *
	 * @param source string to convert
	 * @return corresponding string without diacritics
	 */
	public static String stripWithTable(String source) {
		char[] result = new char[source.length()];
		char c;
		for (int i = 0; i < source.length(); i++) {
			c = source.charAt(i);
			if (c >= '\u00c0' && c <= '\u017f') {
				c = TAB_00C0.charAt((int) c - '\u00c0');
			} else if (c > ASCII_MAX) {
				c = SUBSTITUTE;
			}
			result[i] = c;
		}
		return new String(result);
	}

	public static boolean isAscii(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) > ASCII_MAX) return false;
		}
		return true;
	}

	/**
	 * On my machine in this order from slowest to fastest:
	 * stripWithRegex: 1.71 s
	 * StringUtils.stripAccents (Apache): 1.75 s
	 * stripWithNdfAndCycle: 241 ms
	 * stripWithTable: 31.6 ms
	 */
	private static void testPerf() {
		System.out.println();
		String s = null;

		Split split = Split.start();
		for (int i = 0; i < TEST_TIMES; i++) {
			s = stripWithRegex(TEST_STRING);
		}
		System.out.println("stripWithRegex: " + split.presentRunningFor());
		System.out.println("s = " + s);

		split = Split.start();
		for (int i = 0; i < TEST_TIMES; i++) {
			s = StringUtils.stripAccents(TEST_STRING);
		}
		System.out.println("StringUtils.stripAccents (Apache): " + split.presentRunningFor());
		System.out.println("s = " + s);

		split = Split.start();
		for (int i = 0; i < TEST_TIMES; i++) {
			s = stripWithNdfAndCycle(TEST_STRING);
		}
		System.out.println("stripWithNdfAndCycle: " + split.presentRunningFor());
		System.out.println("s = " + s);

		split = Split.start();
		for (int i = 0; i < TEST_TIMES; i++) {
			s = stripWithTable(TEST_STRING);
		}
		System.out.println("stripWithTable: " + split.presentRunningFor());
		System.out.println("s = " + s);
	}
}
