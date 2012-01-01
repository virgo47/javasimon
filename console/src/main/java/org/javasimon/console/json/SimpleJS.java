package org.javasimon.console.json;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import org.javasimon.console.ValueFormatter;

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
	protected final ValueFormatter valueFormatter;
	/**
	 * Hidden constructor use factory methods instead
	 */
	private SimpleJS(T value, ValueFormatter valueFormatter) {
		this.value = value;
		this.valueFormatter = valueFormatter;
	}

	public T getValue() {
		return value;
	}

	public String getFormattedValue() {
		return valueFormatter.formatObject(value);
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write(getFormattedValue());
	}

	public static SimpleJS createNumber(Integer i, ValueFormatter valueFormatter) {
		return new SimpleJS<Integer>(i, valueFormatter) {

			@Override
			public String getFormattedValue() {
				return this.valueFormatter.formatNumber(this.value);
			}
		};
	}

	public static SimpleJS createNumber(Long l, ValueFormatter valueFormatter) {
		return new SimpleJS<Long>(l, valueFormatter) {

			@Override
			public String getFormattedValue() {
				return this.valueFormatter.formatNumber(this.value);
			}
		};
	}

	public static SimpleJS createTime(Long l, ValueFormatter valueFormatter) {
		return new SimpleJS<Long>(l, valueFormatter) {

			@Override
			public String getFormattedValue() {
				return this.valueFormatter.formatTime(this.value);
			}
		};
	}

	public static SimpleJS createTime(Double d, ValueFormatter valueFormatter) {
		return new SimpleJS<Double>(d, valueFormatter) {

			@Override
			public String getFormattedValue() {
				return this.valueFormatter.formatTime(this.value);
			}
		};
	}

	public static SimpleJS createDate(Date d, ValueFormatter valueFormatter) {
		return new SimpleJS<Date>(d, valueFormatter) {

			@Override
			public String getFormattedValue() {
				return this.valueFormatter.formatDate(this.value);
			}
		};
	}

	public static SimpleJS createDate(Long l, ValueFormatter valueFormatter) {
		return new SimpleJS<Long>(l, valueFormatter) {

			@Override
			public String getFormattedValue() {
				return this.valueFormatter.formatDate(this.value);
			}
		};
	}

	public static <E extends Enum<E>> SimpleJS createEnum(E e, ValueFormatter valueFormatter) {
		return new SimpleJS<E>(e, valueFormatter) {

			@Override
			public String getFormattedValue() {
				return this.valueFormatter.formatEnum(this.value);
			}
		};
	}

	public static SimpleJS createString(String value, ValueFormatter valueFormatter) {
		return new SimpleJS<String>(value, valueFormatter) {

			@Override
			public String getFormattedValue() {
				return this.valueFormatter.formatString(this.value);
			}
		};
	}

	public static <T> SimpleJS<T> createObject(T value, ValueFormatter valueFormatter) {
		return new SimpleJS<T>(value, valueFormatter);
	}
}
