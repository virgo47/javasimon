package org.javasimon;

/**
 * Class for creating common clocks.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class Clocks {

	private Clocks() {
		throw new AssertionError();
	}

	private static final Clock SYSTEM_CLOCK = new Clock() {
		@Override
		public long timeNanos() {
			return System.nanoTime();
		}

		@Override
		public long timeMillis() {
			return System.currentTimeMillis();
		}
	};

	/**
	 * Get clock that uses {@link java.lang.System} class to get current
	 * time.
	 *
	 * @return instance of Clock that uses <code>java.lang.System</code> to get current time
	 */
	public static Clock getSystemClock() {
		return SYSTEM_CLOCK;
	}
}
