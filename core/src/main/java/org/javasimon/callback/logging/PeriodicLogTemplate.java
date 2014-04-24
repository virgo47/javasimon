package org.javasimon.callback.logging;

import org.javasimon.clock.Clock;

/**
 * Log template that logs something after every N milliseconds.
 * The {@link #isEnabled(Object)} is only true after N milliseconds from the last log.
 *
 * @author gquintana
 */
public class PeriodicLogTemplate<C> extends DelegateLogTemplate<C> {

	/** Maximum time between two calls to log method. */
	private final long period;

	/** Clock object used to get current time */
	private final Clock clock;

	/** Timestamp of next invocation. */
	private long nextTime;

	/**
	 * Constructor with other template and the required period in ms.
	 *
	 * @param delegate concrete log template
	 * @param period logging period in milliseconds
	 */
	public PeriodicLogTemplate(LogTemplate<C> delegate, long period) {
		this(delegate, period, Clock.SYSTEM);
	}

	public PeriodicLogTemplate(LogTemplate<C> delegate, long period, Clock clock) {
		super(delegate);
		this.period = period;
		this.clock = clock;
		initNextTime();
	}


	/**
	 * Get next invocation time time.
	 *
	 * @return next time
	 */
	public long getNextTime() {
		return nextTime;
	}

	/**
	 * Get current timestamp.
	 *
	 * @return current timestamp
	 */
	long getCurrentTime() {
		return clock.milliTime();
	}

	/** Computes the next timestamp. */
	private synchronized void initNextTime() {
		nextTime = getCurrentTime() + period;
	}

	/** Indicates whether next timestamp is in past. */
	public synchronized boolean isNextTimePassed() {
		return nextTime < getCurrentTime();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return true if delegate is true and enough time passed since last log
	 */
	@Override
	protected boolean isEnabled(C context) {
		return super.isEnabled(context) && isNextTimePassed();
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Next time is updated after delegate log is called.
	 */
	@Override
	protected void log(String message) {
		super.log(message);
		initNextTime();
	}
}
