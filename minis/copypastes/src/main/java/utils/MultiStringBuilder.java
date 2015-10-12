package utils;

import java.io.Serializable;
import java.util.stream.IntStream;

/**
 * Works like {@link StringBuilder} but provides joining out of the box. It is alternative to
 * {@link java.util.StringJoiner} preserving the advantages of StringBuilder when necessary.
 * It introduces {@link #newEntry()} method which returns this and can be used in chains, just
 * like appends, etc. This method appends the separator {@link CharSequence} which is mandatory
 * in both constructors.
 * <p>
 * Optionally, it can be constructed with prefix/postfix {@link CharSequence} which will be
 * prepended/appended to the result. Prefix is appended during the construction, postfix is
 * appended when {@link #toString()} is called. Because of this feature it is forbidden to
 * modify the content after {@link #toString()} is called and any mutating method will throw
 * {@link IllegalStateException}. However non-mutating methods and {@link #toString()} can
 * still be called.
 * <p>
 * Typical usage may look like this:
 * <pre>{@code
MultiStringBuilder sb = new MultiStringBuilder("[", "]", ",");
IntStream.rangeClosed(1, 5)
	.forEach(i -> sb.newEntry().append(i));}</pre>
 * <p>
 * This will produce the string: [1,2,3,4,5]
 */
public class MultiStringBuilder implements Appendable, CharSequence, Serializable {

	private final StringBuilder delegate = new StringBuilder();
	private final CharSequence postfix;
	private final CharSequence separator;

	private boolean noEntryYet = true;
	private boolean finished = false;

	public MultiStringBuilder(CharSequence prefix, CharSequence postfix, CharSequence separator) {
		delegate.append(prefix);
		this.separator = separator;
		this.postfix = postfix;
	}

	public MultiStringBuilder(CharSequence separator) {
		this.separator = separator;
		postfix = "";
	}

	/**
	 * This call must proceed each logical entry and ensures that {@link #separator} string is
	 * appended except before the first entry.
	 */
	public MultiStringBuilder newEntry() {
		notFinishedCheck();
		if (noEntryYet) {
			noEntryYet = false;
			return this;
		}

		delegate.append(separator);
		return this;
	}

	public MultiStringBuilder append(String str) {
		notFinishedCheck();
		delegate.append(str);
		return this;
	}

	@Override
	public MultiStringBuilder append(CharSequence s) {
		notFinishedCheck();
		delegate.append(s);
		return this;
	}

	@Override
	public MultiStringBuilder append(CharSequence s, int start, int end) {
		notFinishedCheck();
		delegate.append(s, start, end);
		return this;
	}

	public MultiStringBuilder append(char[] str) {
		notFinishedCheck();
		delegate.append(str);
		return this;
	}

	public MultiStringBuilder append(char[] str, int offset, int len) {
		notFinishedCheck();
		delegate.append(str, offset, len);
		return this;
	}

	public MultiStringBuilder append(Object obj) {
		notFinishedCheck();
		delegate.append(obj);
		return this;
	}

	public MultiStringBuilder append(boolean b) {
		notFinishedCheck();
		delegate.append(b);
		return this;
	}

	@Override
	public MultiStringBuilder append(char c) {
		notFinishedCheck();
		delegate.append(c);
		return this;
	}

	public MultiStringBuilder append(double d) {
		notFinishedCheck();
		delegate.append(d);
		return this;
	}

	public MultiStringBuilder append(float f) {
		notFinishedCheck();
		delegate.append(f);
		return this;
	}

	public MultiStringBuilder append(int i) {
		notFinishedCheck();
		delegate.append(i);
		return this;
	}

	public MultiStringBuilder append(long lng) {
		notFinishedCheck();
		delegate.append(lng);
		return this;
	}

	public MultiStringBuilder appendCodePoint(int codePoint) {
		notFinishedCheck();
		delegate.appendCodePoint(codePoint);
		return this;
	}

	public MultiStringBuilder insert(int offset, String str) {
		notFinishedCheck();
		delegate.insert(offset, str);
		return this;
	}

	public MultiStringBuilder insert(int dstOffset, CharSequence s) {
		notFinishedCheck();
		delegate.insert(dstOffset, s);
		return this;
	}

	public MultiStringBuilder insert(int dstOffset, CharSequence s, int start, int end) {
		notFinishedCheck();
		delegate.insert(dstOffset, s, start, end);
		return this;
	}

	public MultiStringBuilder insert(int offset, char[] str) {
		notFinishedCheck();
		delegate.insert(offset, str);
		return this;
	}

	public MultiStringBuilder insert(int index, char[] str, int offset, int len) {
		notFinishedCheck();
		delegate.insert(index, str, offset, len);
		return this;
	}

	public MultiStringBuilder insert(int offset, Object obj) {
		notFinishedCheck();
		delegate.insert(offset, obj);
		return this;
	}

	public MultiStringBuilder insert(int offset, boolean b) {
		notFinishedCheck();
		delegate.insert(offset, b);
		return this;
	}

	public MultiStringBuilder insert(int offset, char c) {
		notFinishedCheck();
		delegate.insert(offset, c);
		return this;
	}

	public MultiStringBuilder insert(int offset, double d) {
		notFinishedCheck();
		delegate.insert(offset, d);
		return this;
	}

	public MultiStringBuilder insert(int offset, float f) {
		notFinishedCheck();
		delegate.insert(offset, f);
		return this;
	}

	public MultiStringBuilder insert(int offset, int i) {
		notFinishedCheck();
		delegate.insert(offset, i);
		return this;
	}

	public MultiStringBuilder insert(int offset, long l) {
		notFinishedCheck();
		delegate.insert(offset, l);
		return this;
	}

	public MultiStringBuilder delete(int start, int end) {
		notFinishedCheck();
		delegate.delete(start, end);
		return this;
	}

	public MultiStringBuilder deleteCharAt(int index) {
		notFinishedCheck();
		delegate.deleteCharAt(index);
		return this;
	}

	public MultiStringBuilder replace(int start, int end, String str) {
		notFinishedCheck();
		delegate.replace(start, end, str);
		return this;
	}

	public MultiStringBuilder reverse() {
		notFinishedCheck();
		delegate.reverse();
		return this;
	}

	public void setCharAt(int index, char ch) {
		notFinishedCheck();
		delegate.setCharAt(index, ch);
	}

	public void setLength(int newLength) {
		notFinishedCheck();
		delegate.setLength(newLength);
	}

	// doesn't make sense if we don't want to change it, so the finished check is here as well
	public void ensureCapacity(int minimumCapacity) {
		notFinishedCheck();
		delegate.ensureCapacity(minimumCapacity);
	}

	// non-mutating methods
	public int capacity() {
		return delegate.capacity();
	}

	@Override
	public char charAt(int index) {
		return delegate.charAt(index);
	}

	@Override
	public IntStream chars() {
		return delegate.chars();
	}

	@Override
	public IntStream codePoints() {
		return delegate.codePoints();
	}

	public int codePointAt(int index) {
		return delegate.codePointAt(index);
	}

	public int codePointBefore(int index) {
		return delegate.codePointBefore(index);
	}

	/** @see StringBuilder#codePointCount(int, int) */
	public int codePointCount(int beginIndex, int endIndex) {
		return delegate.codePointCount(beginIndex, endIndex);
	}

	/** @see StringBuilder#getChars(int, int, char[], int) */
	public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
		delegate.getChars(srcBegin, srcEnd, dst, dstBegin);
	}

	/** @see StringBuilder#indexOf(String) */
	public int indexOf(String str) {
		return delegate.indexOf(str);
	}

	/** @see StringBuilder#indexOf(String, int) */
	public int indexOf(String str, int fromIndex) {
		return delegate.indexOf(str, fromIndex);
	}

	/** @see StringBuilder#lastIndexOf(String) */
	public int lastIndexOf(String str) {
		return delegate.lastIndexOf(str);
	}

	/** @see StringBuilder#lastIndexOf(String, int) */
	public int lastIndexOf(String str, int fromIndex) {
		return delegate.lastIndexOf(str, fromIndex);
	}

	@Override
	public int length() {
		return delegate.length();
	}

	public int offsetByCodePoints(int index, int codePointOffset) {
		return delegate.offsetByCodePoints(index, codePointOffset);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return delegate.subSequence(start, end);
	}

	public String substring(int start) {
		return delegate.substring(start);
	}

	public String substring(int start, int end) {
		return delegate.substring(start, end);
	}

	public void trimToSize() {
		delegate.trimToSize();
	}

	private void notFinishedCheck() {
		if (finished) {
			throw new IllegalStateException("MultiStringBuilder was finished already (toString was called)");
		}
	}

	/** @noinspection NullableProblems */
	@Override
	public String toString() {
		if (!finished) {
			finished = true;
			delegate.append(postfix);
		}
		return delegate.toString();
	}
}
