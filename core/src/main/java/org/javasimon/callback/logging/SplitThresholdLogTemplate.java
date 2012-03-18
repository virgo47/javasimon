package org.javasimon.callback.logging;

import org.javasimon.Split;

/**
 * Log template which awakes only when split is longer than given threshold.
 * @author gquintana
 */
public class SplitThresholdLogTemplate extends DelegateLogTemplate<Split> {
	/**
	 * Split duration theshold
	 */
	private final long threshold;
	/**
	 * Constructor
	 * @param delegate Concreate log template
	 * @param threshold Theshold
	 */
	public SplitThresholdLogTemplate(LogTemplate delegate, long threshold) {
		super(delegate);
		this.threshold = threshold;
	}
	
	@Override
	public boolean isEnabled(Split split) {
		return split.runningFor()>threshold&&super.isEnabled(split);
	}

	public long getThreshold() {
		return threshold;
	}
	
}
