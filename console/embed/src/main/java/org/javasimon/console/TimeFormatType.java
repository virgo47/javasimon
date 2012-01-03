package org.javasimon.console;

import org.javasimon.utils.SimonUtils;

/**
 * Strategies used to to display times
 *
 * @author gquintana
 */
public enum TimeFormatType {

	NANOSECOND {

		@Override
		public String format(double value) {
			return Long.toString((long) value);
		}

		@Override
		public String format(long value) {
			return Long.toString(value);
		}
	},
	MICROSECOND {

		@Override
		public String format(double value) {
			return Long.toString((long) (value / 1000d));
		}

		@Override
		public String format(long value) {
			return Long.toString(value / 1000);
		}
	},
	MILLISECOND {

		@Override
		public String format(double value) {
			return Long.toString((long) (value / 1000000d));
		}

		@Override
		public String format(long value) {
			return Long.toString(value / 1000000);
		}
	},
	SECOND {

		@Override
		public String format(double value) {
			return Long.toString((long) (value / 1000000000d));
		}

		@Override
		public String format(long value) {
			return Long.toString(value / 1000000000);
		}
	},
	PRESENT {

		@Override
		public String format(double value) {
			return SimonUtils.presentNanoTime((long) value);
		}

		@Override
		public String format(long value) {
			return SimonUtils.presentNanoTime(value);
		}
	};

	public abstract String format(double value);

	public abstract String format(long value);
}
