package utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testng.Assert.assertEquals;

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
	public void mutateAfterToStringThrowsException() {
		MultiStringBuilder sb = new MultiStringBuilder("[", "]", ",")
			.newEntry().append(1);
		assertEquals(sb.toString(), "[1]");
		assertThatThrownBy(() -> sb.newEntry()).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.append("")).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.append(CHAR_SEQUENCE)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.append("", 0, 0)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.append(CHAR_ARRAY)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.append(CHAR_ARRAY, 0, 0)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.append(new Object())).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.append(true)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.append('c')).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.append(1d)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.append(1f)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.append(1)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.append(1L)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.appendCodePoint(0)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.insert(10, "")).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.insert(10, CHAR_SEQUENCE)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.insert(10, "", 0, 0)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.insert(10, CHAR_ARRAY)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.insert(10, CHAR_ARRAY, 0, 0)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.insert(10, new Object())).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.insert(10, true)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.insert(10, 'c')).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.insert(10, 1d)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.insert(10, 1f)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.insert(10, 1)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.insert(10, 1L)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.delete(0, 0)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.deleteCharAt(0)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.replace(0, 0, "any")).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.reverse()).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.setCharAt(0, 'c')).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.setLength(0)).isInstanceOf(IllegalStateException.class);
		assertThatThrownBy(() -> sb.ensureCapacity(5)).isInstanceOf(IllegalStateException.class);
	}

	@Test
	public void nonMutateAfterToStringWorkOk() {
		MultiStringBuilder sb = new MultiStringBuilder("[", "]", ",")
			.newEntry().append(1);
		assertEquals(sb.toString(), "[1]");
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
