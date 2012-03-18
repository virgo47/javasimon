package org.javasimon.callback.logging;

/**
 * Logger which logs something at most every N milliseconds.
 * The {@link #isLoggingEnabled} is only true once per N milliseconds.
 * @author gquintana
 */
public class PeriodicLogTemplate<C> extends DelegateLogTemplate<C> {
	/**
	 * Maximum time between two calls to log method
	 */
	private final long period;
	/**
	 * Timestamp of next invocation
	 */
	private long nextTime;
	/**
	 * Constructor
	 * @param delegate Concrete log template
	 * @param period Logging period in milliseconds
	 */
	public PeriodicLogTemplate(LogTemplate delegate, long period) {
		super(delegate);
		this.period = period;
		initNextTime();
	}
	/**
	 * Get next invocation time time
	 * @return  Next time
	 */
	public long getNextTime() {
		return nextTime;
	}
	
	/**
	 * Get current timestamp
	 * @return Current timestamp
	 */
	private long getCurrentTime() {
		return System.currentTimeMillis();
	}
	/**
	 * Compute next timestamp
	 */
	private synchronized void initNextTime() {
		nextTime=getCurrentTime()+period;
	}
	/**
	 * Indicates whether next timestamp is in past
	 */
	public synchronized boolean isNextTimePassed() {
		return nextTime<getCurrentTime();
	}
	/**
	 * {@inheritDoc }
	 * @return True if delegate is true and enough time passed since last log
	 */
	@Override
	public boolean isEnabled(C context) {
		return super.isEnabled(context)&&isNextTimePassed();
	}
	/**
	 * {@inheritDoc }
	 * Next time is updated after delegate log is called
	 */
	@Override
	public void log(String message) {
		super.log(message);
		initNextTime();
	}
}
