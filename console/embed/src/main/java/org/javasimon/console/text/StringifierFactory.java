package org.javasimon.console.text;

import java.text.*;
import java.util.Date;
import org.javasimon.SimonState;
import org.javasimon.console.SimonType;
import org.javasimon.console.TimeFormatType;
import org.javasimon.console.html.HtmlResourceType;
import org.javasimon.utils.SimonUtils;

/**
 * Base helper class to format values of attributes of simons.
 * This class is in charge of initializing the dictionnary contained
 * in {@link CompositeStringifier}.
 * This class can be overriden to export differently JSON, CSV formats
 *
 * @author gquintana
 */
public class StringifierFactory {
	/**
	 * Value used to identify Date (=timestamp) sub type.
	 */
	public static final String DATE_SUBTYPE = "Date";
	/**
	 * Value used to identify Time (=duration) sub type.
	 */
	public static final String TIME_SUBTYPE = "Time";
	/**
	 * Value used to identify None (=disabled=hidden) sub type.
	 * @see NoneStringifier
	 */
	public static final String NONE_SUBTYPE = "None";
	/**
	 * Default number pattern of Integers and Longs
	 */
	public static final String INTEGER_NUMBER_PATTERN="0";
	/**
	 * Default number pattern of Doubles and Floats
	 */
	public static final String READABLE_NUMBER_PATTERN="0.000";
	/**
	 * Date+Time pattern used aimed human reading
	 */
	public static final String READABLE_DATE_PATTERN="yyyy-MM-dd HH:mm:ss";
	/**
	 * Date+Time pattern used aimed XML parsers (ISO)
	 */
	public static final String ISO_DATE_PATTERN="yyyy-MM-dd'T'HH:mm:ss";
	/**
	 * Stringifier dictionnary
	 */
	protected final  CompositeStringifier compositeStringifier = new CompositeStringifier();
	/**
	 * Register a stringifier for null values.
	 * Method aimed at being overriden. Should call {@link #registerNullStringifier(java.lang.String) }.
	 */
	protected Stringifier registerNullStringifier() {
		return registerNullStringifier("");
	}
	/**
	 * Register a stringifier for null values.
	 */
	protected final Stringifier registerNullStringifier(final String nullValue) {
		Stringifier nullStringifier = new Stringifier() {
			public String toString(Object object) {
				return nullValue;
			}
		};
		compositeStringifier.setNullStringifier(nullStringifier);
		return nullStringifier;
	}
	/**
	 * Register a stringifier for String values.
	 * Method aimed at being overriden.
	 */
	protected Stringifier<String> registerStringStringifier(Stringifier nullStringifier) {
		Stringifier<String> stringStringifier=new BaseStringifier<String>(nullStringifier) {
			@Override
			protected String doToString(String s) {
				return s;
			}
		};
		compositeStringifier.add(String.class, stringStringifier);
		return stringStringifier;
	}
	/**
	 * Register a stringifier for Long and long  values.
	 */
	protected final Stringifier<Long> registerLongStringifier(String name, Stringifier<Long> longStringifier) {
		compositeStringifier.add(Long.class, name, longStringifier);
		compositeStringifier.add(Long.TYPE, name, longStringifier);
		return longStringifier;
	}
	/**
	 * Register a stringifier for Double and double values.
	 */
	protected final Stringifier<Double> registerDoubleStringifier(String name, Stringifier<Double> doubleStringifier) {
		compositeStringifier.add(Double.class, name, doubleStringifier);
		compositeStringifier.add(Double.TYPE, name, doubleStringifier);
		return doubleStringifier;
	}
	/**
	 * Register a stringifier for various types and subtypes
	 * Method aimed at being overriden.
	 */
	public void init(TimeFormatType timeFormat, String datePattern, String numberPattern) {
		// Null
		final Stringifier nullStringifier=registerNullStringifier();

		// Default
		compositeStringifier.setDefaultStringifier(new BaseStringifier(nullStringifier));

		// String
		final Stringifier<String> stringStringifier = registerStringStringifier(nullStringifier);

		// Disabled String
		compositeStringifier.add(String.class, NONE_SUBTYPE, NoneStringifier.getInstance());

		// Integer
		final Stringifier<Integer> integerStringifier = new BaseStringifier<Integer>(nullStringifier) {

			@Override
			protected boolean isValid(Integer l) {
				return super.isValid(l) && !(l == Integer.MIN_VALUE || l == Integer.MAX_VALUE);
			}
		};
		compositeStringifier.add(Integer.class, integerStringifier);
		compositeStringifier.add(Integer.TYPE, integerStringifier);

		// Long
		final Stringifier<Long> longStringifier = new BaseStringifier<Long>(nullStringifier) {
			@Override
			protected boolean isValid(Long l) {
				return super.isValid(l) && !(l == Long.MIN_VALUE || l == Long.MAX_VALUE);
			}
		};
		registerLongStringifier(null, longStringifier);

		// Float
		final Stringifier<Float> floatStringifier = new NumberStringifier<Float>(nullStringifier, numberPattern) {
			@Override
			protected boolean isValid(Float f) {
				return super.isValid(f) && !(f == Float.MIN_VALUE || f == Float.MAX_VALUE
					|| f == Float.NEGATIVE_INFINITY || f == Float.POSITIVE_INFINITY
					|| f == Float.NaN);
			}
		};
		compositeStringifier.add(Float.class, floatStringifier);
		compositeStringifier.add(Float.TYPE, floatStringifier);

		// Double
		final Stringifier<Double> doubleStringifier = new NumberStringifier<Double>(nullStringifier, numberPattern) {
			@Override
			protected boolean isValid(Double d) {
				return super.isValid(d) && !(d == Double.MIN_VALUE || d == Double.MAX_VALUE
					|| d == Double.NEGATIVE_INFINITY || d == Double.POSITIVE_INFINITY
					|| d == Double.NaN);
			}
		};
		registerDoubleStringifier(null, doubleStringifier);

		// Enum
		final Stringifier<Enum> enumStringifier = new BaseStringifier<Enum>(nullStringifier) {
			@Override
			protected String doToString(Enum e) {
				return stringStringifier.toString(e.name());
			}
		};
		compositeStringifier.add(Enum.class, enumStringifier);
		compositeStringifier.add(SimonType.class, enumStringifier);
		compositeStringifier.add(SimonState.class, enumStringifier);
		compositeStringifier.add(HtmlResourceType.class, enumStringifier);

		// Date
		final Stringifier<java.util.Date> dateStringifier = new DateStringifier(nullStringifier, stringStringifier, datePattern);
		compositeStringifier.add(Date.class, dateStringifier);
		final LongDateStringifier longDateStringifier = new LongDateStringifier(nullStringifier, dateStringifier);
		registerLongStringifier(DATE_SUBTYPE, longDateStringifier);

		// Time
		final LongTimeStringifier longTimeStringifier = new LongTimeStringifier(nullStringifier, longStringifier, stringStringifier, timeFormat);
		registerLongStringifier(TIME_SUBTYPE, longTimeStringifier);
		final DoubleTimeStringifier doubleTimeStringifier = new DoubleTimeStringifier(nullStringifier, doubleStringifier, stringStringifier, timeFormat);
		registerDoubleStringifier(TIME_SUBTYPE, doubleTimeStringifier);

		// Boolean
		final Stringifier<Boolean> booleanStringifier = new BaseStringifier<Boolean>(nullStringifier) {
			@Override
			protected String doToString(Boolean b) {
				return stringStringifier.toString(b.toString());
			}
		};
		compositeStringifier.add(Boolean.class, booleanStringifier);
		compositeStringifier.add(Boolean.TYPE,  booleanStringifier);
	}

	/**
	 * Stringifier implementation for {@link Date} type.
	 */
	protected static class DateStringifier extends BaseStringifier<Date> {
		private final DateFormat dateFormat;
		private final Stringifier<String> stringStringifier;
		public DateStringifier(Stringifier nullStringifier, Stringifier<String> stringStringifier, String datePattern) {
			super(nullStringifier);
			this.dateFormat = new SimpleDateFormat(datePattern);
			this.stringStringifier = stringStringifier;
		}
		@Override
		protected String doToString(Date d) {
			return stringStringifier.toString(dateFormat.format(d));
		}
	}

	/**
	 * Stringifier implementation for {@link Number} type.
	 */
	protected static class NumberStringifier<T extends Number> extends BaseStringifier<T> {
		private final NumberFormat numberFormat;
		public NumberStringifier(Stringifier nullStringifier, String numberPattern) {
			super(nullStringifier);
			DecimalFormat decimalFormat = new DecimalFormat(numberPattern);
			DecimalFormatSymbols decimalFormatSymbols=decimalFormat.getDecimalFormatSymbols();
			decimalFormatSymbols.setDecimalSeparator('.');
			decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
			this.numberFormat=decimalFormat;
		}
		@Override
		protected String doToString(T n) {
			return numberFormat.format(n.doubleValue());
		}
	}

	/**
	 * Stringifier implementation for {@link Long} type reprensenting a timestamp.
	 */
	protected static class LongDateStringifier extends BaseStringifier<Long> {

		private final Stringifier<Date> dateStringifier;

		public LongDateStringifier(Stringifier nullStringifier, Stringifier<Date> dateStringifier) {
			super(nullStringifier);
			this.dateStringifier = dateStringifier;
		}

		@Override
		protected boolean isValid(Long object) {
			return super.isValid(object) && object > 0L && object < Long.MAX_VALUE;
		}

		@Override
		protected String doToString(Long object) {
			return dateStringifier.toString(new Date(object));
		}
	}

	/**
	 * Stringifier implementation for {@link Long} type reprensenting a duration.
	 */
	protected static class LongTimeStringifier extends BaseStringifier<Long> {

		private final TimeFormatType timeFormatType;
		private final Stringifier<Long> longStringifier;
		private final Stringifier<String> stringStringifier;

		public LongTimeStringifier(Stringifier nullStringifier, Stringifier<Long> longStringifier, Stringifier<String> stringStringifier, TimeFormatType timeFormatType) {
			super(nullStringifier);
			this.timeFormatType = timeFormatType;
			this.longStringifier = longStringifier;
			this.stringStringifier = stringStringifier;
		}

		@Override
		protected boolean isValid(Long object) {
			return super.isValid(object) && object >= 0L && object < Long.MAX_VALUE;
		}

		@Override
		protected String doToString(Long l) {
			if (timeFormatType == TimeFormatType.AUTO) {
				return stringStringifier.toString(SimonUtils.presentNanoTime(l));
			} else {
				long l2 = timeFormatType.convert(l);
				return longStringifier.toString(l2);
			}
		}
	}

	/**
	 * Stringifier implementation for {@link Double} type reprensenting a duration.
	 */
	protected static class DoubleTimeStringifier extends BaseStringifier<Double> {

		private final TimeFormatType timeFormatType;
		private final Stringifier<Double> doubleStringifier;
		private final Stringifier<String> stringStringifier;

		public DoubleTimeStringifier(Stringifier nullStringifier, Stringifier<Double> doubleStringifier, Stringifier<String> stringStringifier, TimeFormatType timeFormatType) {
			super(nullStringifier);
			this.timeFormatType = timeFormatType;
			this.doubleStringifier = doubleStringifier;
			this.stringStringifier = stringStringifier;
		}

		@Override
		protected boolean isValid(Double object) {
			return super.isValid(object) && object >= 0D && object < Double.MAX_VALUE && object != Double.NaN;
		}

		@Override
		protected String doToString(Double d) {
			if (timeFormatType == TimeFormatType.AUTO) {
				return stringStringifier.toString(SimonUtils.presentNanoTime(d));
			} else {
				Double d2 = timeFormatType.convert(d);
				return doubleStringifier.toString(d2);
			}
		}
	}

	/**
	 * Get the stringifier for null values.
	 */
	@SuppressWarnings("unchecked")
	public <T> Stringifier<T> getNullStringifier() {
		return compositeStringifier.getNullStringifier();
	}

	/**
	 * Get the stringifier for given type.
	 */
	public <T> Stringifier<T> getStringifier(Class<? extends T> type) {
		return compositeStringifier.getForType(type);
	}

	/**
	 * Get the stringifier for given type and sub type.
	 */
	public <T> Stringifier<T> getStringifier(Class<? extends T> type, String subType) {
		return compositeStringifier.getForType(type, subType);
	}

	/**
	 * Get the stringifier for given instance and use it to format value.
	 */
	public <T> String toString(T value) {
		return compositeStringifier.toString(value);
	}

	/**
	 * Get the stringifier for given instance and subtype and use it to format value.
	 */
	public <T> String toString(T value, String subType) {
		return compositeStringifier.toString(value, subType);
	}
}
