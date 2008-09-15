package org.javasimon;

/**
 * StopwatchSimpleImpl.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Sep 11, 2008
 */
final class StopwatchSimpleImpl extends AbstractStopwatch {
	private long start;

	StopwatchSimpleImpl(String name) {
		super(name);
	}

	@Override
	public Stopwatch start() {
		if (enabled) {
			start = System.nanoTime();
		}
		return this;
	}

	@Override
	public long stop() {
		if (enabled) {
			if (start != 0) {
				long split = System.nanoTime() - start;
				start = 0;
				return addSplit(split);
			}
		}
		return 0;
	}
}
