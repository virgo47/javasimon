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
 * prepended/appended to the result. It is still possible to combine {@link #toString()} with
 * mutating methods, postfix will be placed properly after the end.
 * <p>
 * Insert methods index from the start of the buffer with prefix included. It is possible to
 * insert something before prefix with index 0. TODO: maybe this should index from the current item?
 * Generally all methods using indexes and all "low-level" methods (like {@link #setLength(int)}
 * or {@link #reverse()}) have not very clear semantics for multi-string builder. (yet)
 * <p>
 * Typical usage may look like this:
 * <pre>{@code
 * MultiStringBuilder sb = new MultiStringBuilder("[", "]", ",");
 * IntStream.rangeClosed(1, 5)
 * .forEach(i -> sb.newEntry().append(i));}</pre>
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
		unfinish();
		if (noEntryYet) {
			noEntryYet = false;
			return this;
		}

		delegate.append(separator);
		return this;
	}

	public MultiStringBuilder append(String str) {
		unfinish();
		delegate.append(str);
		return this;
	}

	@Override
	public MultiStringBuilder append(CharSequence s) {
		unfinish();
		delegate.append(s);
		return this;
	}

	@Override
	public MultiStringBuilder append(CharSequence s, int start, int end) {
		unfinish();
		delegate.append(s, start, end);
		return this;
	}

	public MultiStringBuilder append(char[] str) {
		unfinish();
		delegate.append(str);
		return this;
	}

	public MultiStringBuilder append(char[] str, int offset, int len) {
		unfinish();
		delegate.append(str, offset, len);
		return this;
	}

	public MultiStringBuilder append(Object obj) {
		unfinish();
		delegate.append(obj);
		return this;
	}

	public MultiStringBuilder append(boolean b) {
		unfinish();
		delegate.append(b);
		return this;
	}

	@Override
	public MultiStringBuilder append(char c) {
		unfinish();
		delegate.append(c);
		return this;
	}

	public MultiStringBuilder append(double d) {
		unfinish();
		delegate.append(d);
		return this;
	}

	public MultiStringBuilder append(float f) {
		unfinish();
		delegate.append(f);
		return this;
	}

	public MultiStringBuilder append(int i) {
		unfinish();
		delegate.append(i);
		return this;
	}

	public MultiStringBuilder append(long lng) {
		unfinish();
		delegate.append(lng);
		return this;
	}

	public MultiStringBuilder appendCodePoint(int codePoint) {
		unfinish();
		delegate.appendCodePoint(codePoint);
		return this;
	}

	public MultiStringBuilder insert(int offset, String str) {
		unfinish();
		delegate.insert(offset, str);
		return this;
	}

	public MultiStringBuilder insert(int dstOffset, CharSequence s) {
		unfinish();
		delegate.insert(dstOffset, s);
		return this;
	}

	public MultiStringBuilder insert(int dstOffset, CharSequence s, int start, int end) {
		unfinish();
		delegate.insert(dstOffset, s, start, end);
		return this;
	}

	public MultiStringBuilder insert(int offset, char[] str) {
		unfinish();
		delegate.insert(offset, str);
		return this;
	}

	public MultiStringBuilder insert(int index, char[] str, int offset, int len) {
		unfinish();
		delegate.insert(index, str, offset, len);
		return this;
	}

	public MultiStringBuilder insert(int offset, Object obj) {
		unfinish();
		delegate.insert(offset, obj);
		return this;
	}

	public MultiStringBuilder insert(int offset, boolean b) {
		unfinish();
		delegate.insert(offset, b);
		return this;
	}

	public MultiStringBuilder insert(int offset, char c) {
		unfinish();
		delegate.insert(offset, c);
		return this;
	}

	public MultiStringBuilder insert(int offset, double d) {
		unfinish();
		delegate.insert(offset, d);
		return this;
	}

	public MultiStringBuilder insert(int offset, float f) {
		unfinish();
		delegate.insert(offset, f);
		return this;
	}

	public MultiStringBuilder insert(int offset, int i) {
		unfinish();
		delegate.insert(offset, i);
		return this;
	}

	public MultiStringBuilder insert(int offset, long l) {
		unfinish();
		delegate.insert(offset, l);
		return this;
	}

	public MultiStringBuilder delete(int start, int end) {
		unfinish();
		delegate.delete(start, end);
		return this;
	}

	public MultiStringBuilder deleteCharAt(int index) {
		unfinish();
		delegate.deleteCharAt(index);
		return this;
	}

	public MultiStringBuilder replace(int start, int end, String str) {
		unfinish();
		delegate.replace(start, end, str);
		return this;
	}

	/**
	 * Currently reverses underlying string completely - prefix/postfix included.
	 * TODO: Do we want different semantics? Only for item? Unsupported? Only within prefix/suffix?
	 */
	public MultiStringBuilder reverse() {
		delegate.reverse();
		return this;
	}

	/** Works on complete buffer. What should be the semantics here? */
	public void setCharAt(int index, char ch) {
		delegate.setCharAt(index, ch);
	}

	/** Works on complete buffer. What should be the semantics here? */
	public void setLength(int newLength) {
		delegate.setLength(newLength);
	}

	// doesn't make sense if we don't want to change it, so the finished check is here as well
	public void ensureCapacity(int minimumCapacity) {
		unfinish();
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

	/** Used internally by all mutating methods. */
	private void unfinish() {
		if (finished && postfix.length() > 0) {
			delegate.setLength(delegate.length() - postfix.length());
		}
		finished = false;
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

	/** Returns original delegate - kind of backdoor to the content, use with caution. */
	public StringBuilder toStringBuilder() {
		return delegate;
	}
}
