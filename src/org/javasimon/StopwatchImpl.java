package org.javasimon;

/**
 * Stopwatch default implementation - it is thread-safe, more split times can be
 * measured in parallel - but start and stop must be called within the same thread.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
final class StopwatchImpl extends AbstractStopwatch {
	private ThreadLocal<Long> start = new ThreadLocal<Long>();

	StopwatchImpl(String name) {
		super(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Stopwatch start() {
		if (enabled) {
			updateUsages();
			start.set(System.nanoTime());
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public long stop() {
		if (enabled) {
			Long start = this.start.get();
			if (start != null) {
				return addSplit(System.nanoTime() - start);
			}
		}
		return 0;
	}

	@Override
	protected void enabledObserver() {
		start = new ThreadLocal<Long>();
	}
}
