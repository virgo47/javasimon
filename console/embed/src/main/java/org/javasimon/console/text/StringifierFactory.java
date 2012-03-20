package org.javasimon.console.text;

import java.text.*;
import java.util.Date;
import org.javasimon.SimonState;
import org.javasimon.console.SimonType;
import org.javasimon.console.TimeFormatType;
import org.javasimon.utils.SimonUtils;

/**
 * Base helper class to format values of attributes of simons. This class is
 * overriden to export differently JSON, CSV formats
 *
 * @author gquintana
 */
public class StringifierFactory {
	public static final String DATE_SUBTYPE = "Date";
	public static final String TIME_SUBTYPE = "Time";
	public static final String INTEGER_NUMBER_PATTERN="0";
	public static final String READABLE_NUMBER_PATTERN="0.000";
	public static final String READABLE_DATE_PATTERN="yyyy-MM-dd HH:mm:ss";
	public static final String ISO_DATE_PATTERN="yyyy-MM-dd'T'HH:mm:ss";
	protected final  CompositeStringifier compositeStringifier = new CompositeStringifier();
	protected Stringifier registerNullStringifier() {
		return registerNullStringifier("");
	}
	protected final Stringifier registerNullStringifier(final String nullValue) {
		Stringifier nullStringifier = new Stringifier() {
			public String toString(Object object) {
				return nullValue;
			}
		};
		compositeStringifier.setNullStringifier(nullStringifier);
		return nullStringifier;
	}
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
	private Stringifier<Long> registerLongStringifier(String name, Stringifier<Long> longStringifier) {
		compositeStringifier.add(Long.class, name, longStringifier);
		compositeStringifier.add(Long.TYPE, name, longStringifier);
		return longStringifier;
	}
	protected final Stringifier<Double> registerDoubleStringifier(String name, Stringifier<Double> doubleStringifier) {
		compositeStringifier.add(Double.class, name, doubleStringifier);
		compositeStringifier.add(Double.TYPE, name, doubleStringifier);
		return doubleStringifier;
	}
	// "yyyy-MM-dd HH:mm:ss"
	public void init(TimeFormatType timeFormat, String datePattern, String numberPattern) {
		// Null
		final Stringifier nullStringifier=registerNullStringifier();
		
		// Default
		compositeStringifier.setDefaultStringifier(new BaseStringifier(nullStringifier));
		
		// String
		final Stringifier<String> stringStringifier = registerStringStringifier(nullStringifier);
		
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
	}

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
	};
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
	};

	protected static class LongDateStringifier extends BaseStringifier<Long> {

		private final Stringifier<Date> dateStringifier;

		public LongDateStringifier(Stringifier nullStringifier, Stringifier<Date> dateStringifier) {
			super(nullStringifier);
			this.dateStringifier = dateStringifier;
		}

		@Override
		protected boolean isValid(Long object) {
			return super.isValid(object) && object > 0L;
		}

		@Override
		protected String doToString(Long object) {
			return dateStringifier.toString(new Date(object));
		}
	}

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
			return super.isValid(object) && object >= 0L;
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
	};

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
			return super.isValid(object) && object >= 0D;
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
	};
	@SuppressWarnings("unchecked")
	public <T> Stringifier<T> getNullStringifier() {
		return compositeStringifier.getNullStringifier();
	}
	public <T> Stringifier<T> getStringifier(Class<? extends T> type) {
		return compositeStringifier.get(type);
	}
	public <T> Stringifier<T> getStringifier(Class<? extends T> type, String subType) {
		return compositeStringifier.get(type, subType);
	}
	public <T> String toString(T value) {
		return compositeStringifier.toString(value);
	}
	public <T> String toString(T value, String subType) {
		return compositeStringifier.toString(value, subType);
	}
}
