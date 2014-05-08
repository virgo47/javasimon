package org.javasimon.callback.logging;

/**
 * Log template that logs something every N invocations of the {@link #isEnabled(Object)} method.
 *
 * @author gquintana
 */
public class CounterLogTemplate<C> extends DelegateLogTemplate<C> {

	/** Counter max value corresponds to N value. */
	private final int counterMax;
	/** Counter value. */
	private int counter;

	/**
	 * Constructor.
	 *
	 * @param delegate Concrete log template
	 * @param counterMax Logging period
	 */
	public CounterLogTemplate(LogTemplate delegate, int counterMax) {
		super(delegate);
		this.counterMax = counterMax;
		this.counter = 1;
	}

	/**
	 * Returns counter max value, corresponding to logging period.
	 *
	 * @return Counter max
	 */
	public int getCounterMax() {
		return counterMax;
	}

	/**
	 * Returns counter value.
	 *
	 * @return counter value
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * Increments counter.
	 *
	 * @return true if counter looped and returned to 0
	 */
	private boolean incrementCounter() {
		boolean loop = (counter >= counterMax);
		counter = loop ? 1 : counter + 1;
		return loop;
	}

	@Override
	protected boolean isEnabled(C context) {
		return super.isEnabled(context) && incrementCounter();
	}

	@Override
	protected void log(String message) {
		super.log(message);
	}
}
