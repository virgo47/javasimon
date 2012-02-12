package org.javasimon.console.json;

import java.io.IOException;
import java.io.Writer;
import org.javasimon.console.text.Stringifier;

/**
 * JavaScript simple type
 *
 * @author gquintana
 */
public class SimpleJS<T> extends AnyJS {
	/**
	 * Simple value
	 */
	protected final T value;
	/**
	 * Value formatter
	 */
	protected final Stringifier<T> valueStringifier;
	/**
	 * Hidden constructor use factory methods instead
	 */
	public SimpleJS(T value, Stringifier<T> valueStringifier) {
		this.value = value;
		this.valueStringifier = valueStringifier;
	}

	public T getValue() {
		return value;
	}

	public String getFormattedValue() {
		return valueStringifier.toString(value);
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write(getFormattedValue());
	}

}
