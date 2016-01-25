package utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.stream.IntStream;

import org.testng.annotations.Test;

public class MultiStringBuilderTest {

	@Test
	public void canActLikeStringBuilder() {
		MultiStringBuilder sb = new MultiStringBuilder("any, will not be used");
		sb.append("Anything")
			.append("Something");
		assertEquals(sb.toString(), "AnythingSomething");
	}

	@Test
	public void firstEntryDoesNotInsertJoinerAllNextDo() {
		MultiStringBuilder sb = new MultiStringBuilder(",");
		sb.append("Anything")
			.newEntry()
			.append("Something")
			.newEntry()
			.append("AndMore");
		assertEquals(sb.toString(), "AnythingSomething,AndMore");
	}

	@Test
	public void prefixAndSuffixAreProperlyIncluded() {
		MultiStringBuilder sb = new MultiStringBuilder("[", "]", ",");
		IntStream.rangeClosed(1, 5)
			.forEach(i -> sb.newEntry().append(i));
		assertEquals(sb.toString(), "[1,2,3,4,5]");
	}

	@Test
	public void subsequentToStringsReturnTheSameResult() {
		MultiStringBuilder sb = new MultiStringBuilder("[", "]", ",")
			.newEntry().append(1)
			.newEntry().append(2);
		assertEquals(sb.toString(), "[1,2]");
		assertEquals(sb.toString(), "[1,2]");
		assertEquals(sb.toString(), "[1,2]");
	}

	private static final CharSequence CHAR_SEQUENCE = new StringBuilder();
	private static final char[] CHAR_ARRAY = {};

	/** @noinspection Convert2MethodRef */
	@Test
	public void mutateAfterToStringWorkAsBeforeCallingToString() {
		MultiStringBuilder sb = newBuilderForToStringTest();
		assertEquals(sb.newEntry().toString(), "[1,]");
		assertEquals(sb.append("").toString(), "[1,]");
		assertEquals(sb.append(CHAR_SEQUENCE).toString(), "[1,]");
		assertEquals(sb.append("", 0, 0).toString(), "[1,]");
		assertEquals(sb.append(CHAR_ARRAY).toString(), "[1,]");
		assertEquals(sb.append(CHAR_ARRAY, 0, 0).toString(), "[1,]");
		assertTrue(sb.append(new Object()).toString().startsWith("[1,java.lang.Object@"));

		sb = newBuilderForToStringTest(); // reset needed after appending object
		assertEquals(sb.append(true).toString(), "[1true]");
		assertEquals(sb.append('c').toString(), "[1truec]");
		assertEquals(sb.append(1d).toString(), "[1truec1.0]");
		assertEquals(sb.append(1f).toString(), "[1truec1.01.0]");
		assertEquals(sb.append(1).toString(), "[1truec1.01.01]");
		assertEquals(sb.append(1L).toString(), "[1truec1.01.011]");
		assertNotEquals(sb.appendCodePoint(0).toString(), "[1truec1.01.011]");

		sb = newBuilderForToStringTest();
		assertEquals(sb.insert(1, "x").toString(), "[x1]");
		assertEquals(sb.insert(1, CHAR_SEQUENCE).toString(), "[x1]");
		assertEquals(sb.insert(1, "", 0, 0).toString(), "[x1]");
		assertEquals(sb.insert(1, CHAR_ARRAY).toString(), "[x1]");
		assertEquals(sb.insert(1, CHAR_ARRAY, 0, 0).toString(), "[x1]");
		assertTrue(sb.insert(1, new Object()).toString().startsWith("[java.lang.Object"));

		sb = newBuilderForToStringTest();
		assertEquals(sb.insert(1, true).toString(), "[true1]");
		assertEquals(sb.insert(2, 'c').toString(), "[tcrue1]");
		assertEquals(sb.insert(3, 1d).toString(), "[tc1.0rue1]");
		assertEquals(sb.insert(0, 1f).toString(), "1.0[tc1.0rue1]");
		assertEquals(sb.insert(0, 1).toString(), "11.0[tc1.0rue1]");
		assertEquals(sb.insert(3, 1L).toString(), "11.10[tc1.0rue1]");
		assertEquals(sb.delete(0, 3).toString(), "10[tc1.0rue1]");
		assertEquals(sb.deleteCharAt(1).toString(), "1[tc1.0rue1]");
		assertEquals(sb.replace(2, 4, "any").toString(), "1[any1.0rue1]");
		assertEquals(sb.reverse().toString(), "]1eur0.1yna[1");
		sb.setCharAt(0, 'c');
		assertEquals(sb.toString(), "c1eur0.1yna[1");
		sb.setLength(1);
		assertEquals(sb.toString(), "c");
		sb.ensureCapacity(3); // causes nothing visible, just should not fail after toString
	}

	/** Creates builder with prefix and postfix and calls toString on it already. */
	private MultiStringBuilder newBuilderForToStringTest() {
		MultiStringBuilder sb = new MultiStringBuilder("[", "]", ",")
			.newEntry().append(1);
		assertEquals(sb.toString(), "[1]");
		return sb;
	}

	@Test
	public void nonMutateAfterToStringWorkOk() {
		MultiStringBuilder sb = newBuilderForToStringTest();
		assertThat(sb.capacity()).isGreaterThanOrEqualTo(sb.length()); // probably greater with typical default of 16
		sb.trimToSize(); // must pass OK
		assertThat(sb.capacity()).isEqualTo(sb.length());
		assertThat(sb.codePointAt(0)).isEqualTo('[');
		assertThat(sb.codePointBefore(2)).isEqualTo('1');
		assertThat(sb.codePointCount(0, 2)).isEqualTo(2);
		char[] chars = new char[1];
		sb.getChars(1, 2, chars, 0);
		assertThat(chars[0]).isEqualTo('1');
		assertThat(sb.indexOf("]")).isEqualTo(2);
		assertThat(sb.indexOf("[", 1)).isEqualTo(-1);
		assertThat(sb.lastIndexOf("[")).isEqualTo(0);
		assertThat(sb.lastIndexOf("[", 1)).isEqualTo(0);
		assertThat(sb.offsetByCodePoints(0, 0)).isEqualTo(0);
		assertThat(sb.subSequence(0, 2).toString()).isEqualTo("[1");
		assertThat(sb.substring(0, 2)).isEqualTo("[1");
		assertThat(sb.substring(1)).isEqualTo("1]");
	}
}
