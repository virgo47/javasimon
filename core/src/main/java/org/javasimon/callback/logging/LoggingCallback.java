package org.javasimon.callback.logging;

import static org.javasimon.callback.logging.LogTemplates.toSLF4J;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.utils.SimonUtils;

/**
 * Callback which logs stopwatch splits and manager warnings.
 * By default every split is logged, but one can configure this callback to log
 * only:<ul>
 * <li>Splits too long (longer that threshold)</li>
 * <li>One split out of N</li>
 * <li>One split every N milliseconds</li>
 * </li>
 */
public class LoggingCallback extends CallbackSkeleton {

	/** Log template used for Stopwatch splits. */
	private final LogTemplate<Split> stopwatchLogTemplate;

	/** Split to string converter. */
	private final LogMessageSource<Split> stopwatchLogMessageSource = new LogMessageSource<Split>() {
		public String getLogMessage(Split split) {
			return "Split " + SimonUtils.presentNanoTime(split.runningFor()) + " in Stopwatch " + split.getStopwatch();
		}
	};

	/** Log template used for manager. */
	private final LogTemplate<String> managerLogTemplate;

	/** String to string (no-op) converter. */
	private final LogMessageSource<String> managerLogMessageSource = new LogMessageSource<String>() {
		public String getLogMessage(String message) {
			return message;
		}
	};

	/**
	 * Constructor which can be used to customize log templates.
	 *
	 * @param stopwatchLogTemplate Logger used for Stopwatch splits
	 * @param managerLogTemplate Logger used for manager
	 */
	public LoggingCallback(LogTemplate<Split> stopwatchLogTemplate, LogTemplate<String> managerLogTemplate) {
		this.stopwatchLogTemplate = stopwatchLogTemplate;
		this.managerLogTemplate = managerLogTemplate;
	}

	/** Default constructor logging everything to SLF4J. */
	public LoggingCallback() {
		this.stopwatchLogTemplate = toSLF4J(Stopwatch.class.getName(), "debug");
		this.managerLogTemplate = toSLF4J(SimonManager.class.getName(), "info");
	}

	/**
	 * Get log template used  for manage warnings.
	 *
	 * @return Logger
	 */
	public LogTemplate<String> getManagerLogTemplate() {
		return managerLogTemplate;
	}

	/**
	 * Get log template used  for stopwatch splits.
	 *
	 * @return Logger
	 */
	public LogTemplate<Split> getStopwatchLogTemplate() {
		return stopwatchLogTemplate;
	}

	/**
	 * Get log template for stopwatch, defaults to {@link #stopwatchLogTemplate}.
	 * This method can be overridden to get a specific log template per stopwatch.
	 *
	 * @param stopwatch Stopwatch
	 * @return Logger
	 */
	@SuppressWarnings("UnusedParameters")
	protected LogTemplate<Split> getStopwatchLogTemplate(Stopwatch stopwatch) {
		return stopwatchLogTemplate;
	}

	/**
	 * {@inheritDoc}
	 * Split and stopwatch are logger to log template is enabled.
	 *
	 * @param split Split
	 * @param sample Stopwatch sample
	 */
	@Override
	public void onStopwatchStop(Split split, StopwatchSample sample) {
		getStopwatchLogTemplate(split.getStopwatch()).log(split, stopwatchLogMessageSource);
	}

	@Override
	public void onManagerWarning(String warning, Exception cause) {
		managerLogTemplate.log(warning, managerLogMessageSource);
	}
}