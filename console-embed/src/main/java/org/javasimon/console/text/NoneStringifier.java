package org.javasimon.console.text;

/**
 * Stringifier with particular meaning: do not stringify this value.
 * @author gquintana
 */
public class NoneStringifier<T> implements Stringifier<T> {
	private NoneStringifier() {
	}
	public String toString(T value) {
		throw new UnsupportedOperationException("Do not stringify");
	}
	private static final NoneStringifier INSTANCE=new NoneStringifier();
	/**
	 * Get unique instance of the NoneStringifier
	 */
	public static <T> NoneStringifier<T> getInstance() {
		return (NoneStringifier<T>) INSTANCE;
	}
	/**
	 * Check whether stringifier is the NoneStringifier
	 * @param stringifier Stringifier to compare with NoneStringifier
	 * @return null when stringifier==NoneStringifier, else given stringifier
	 */
	public static <T> Stringifier<T> checkInstance(Stringifier<T> stringifier) {
		return INSTANCE==stringifier?null:stringifier;
	}
}
