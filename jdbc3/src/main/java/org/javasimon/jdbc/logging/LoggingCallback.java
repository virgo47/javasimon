package org.javasimon.jdbc.logging;

import org.javasimon.*;
import org.javasimon.utils.SimonUtils;

import java.util.Date;
import java.util.logging.*;
import java.text.SimpleDateFormat;
import java.io.IOException;

/**
 * LoggingCallback implements jdbc logging logic.
 * <p/>
 * LoggingCallback extends from empty Callback implementation ({@link org.javasimon.CallbackSkeleton}) and
 * implements two events: stopwatch start and stopwatch stop. By implementing start end stop event focusing
 * on jdbc callback produces those jdbc events:
 * <ul>
 * <li>connection open</li>
 * <li>connection close</li>
 * <li>statement open</li>
 * <li>statement close</li>
 * <li>sql execution</li>
 * <li>resultset open</li>
 * <li>resultset close</li>
 * </ul>
 * <p/>
 * Class also contains two build-in formmaters: {@link org.javasimon.jdbc.logging.LoggingCallback.HumanFormatter}
 * and {@link org.javasimon.jdbc.logging.LoggingCallback.CsvFormatter}.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 30.1.2009 15:26:00
 * @since 2
 */
public final class LoggingCallback extends CallbackSkeleton {

	/**
	 * HumanFormatter formats log messages to human readable form.
	 */
	private static final class HumanFormatter extends SimonFormatter {

		private static final String ID = "human";

		private final Date dat = new Date();
		private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
		private final String lineSeparator = System.getProperty("line.separator");

		protected String formatRecord(LogRecord record, CallbackLogParams params) {
			dat.setTime(record.getMillis());
			StringBuilder sb = new StringBuilder();
			sb.append(dateTimeFormat.format(dat)).append(' ');
			sb.append(record.getThreadID()).append(' ');

			String ln = params.getLocalName();
			sb.append('[').append(ln.equals("conn") || ln.equals("stmt") ? ln : "sql ")
				.append(isStart(params) ? "->" : "-<").append("]").append(' ');
			sb.append(params.getFullName()).append(' ');
			sb.append(SimonUtils.presentNanoTime(params.getSplit())).append(' ');
			sb.append(note(params)).append(lineSeparator);
			return sb.toString();
		}
	}

	/**
	 * CsvFormatter formats log messages to comma separated value form.
	 */
	private static final class CsvFormatter extends SimonFormatter {

		private static final String ID = "csv";

		private final String lineSeparator = System.getProperty("line.separator");

		protected String formatRecord(LogRecord record, CallbackLogParams params) {
			StringBuilder sb = new StringBuilder();
			sb.append(record.getMillis()).append('|');
			sb.append(record.getThreadID()).append('|');

			String ln = params.getLocalName();
			sb.append(ln.equals("conn") || ln.equals("stmt") ? ln : "sql")
				.append(isStart(params) ? "|>" : "|<").append('|');
			sb.append(params.getFullName()).append('|');
			sb.append(params.getSplit()).append('|');
			sb.append(params.getNote()).append(lineSeparator);
			return sb.toString();
		}
	}

	private String prefix;
	private String logFilename;
	private String loggerName;
	private boolean logToConsole;
	private String logFormat;

	private Logger logger;

	/**
	 * Default class constructor.
	 */
	public LoggingCallback() {
	}

	/**
	 * Return jdbc prefix that identifies jdbc driver that has enabled logging.
	 *
	 * @return jdbc prefix
	 * @see org.javasimon.jdbc.Driver
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Sets jdbc prefix that identifies jdbc driver that has enabled logging.
	 *
	 * @param prefix new prexif value
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Sets filename where log messages will be written.
	 *
	 * @param logFilename filename value
	 */
	public void setLogFilename(String logFilename) {
		this.logFilename = logFilename;
	}

	/**
	 * Sets logger name that will be used for log jdbc events.
	 *
	 * @param loggerName new logger name value
	 */
	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	/**
	 * Sets logging to console.
	 */
	public void setLogToConsole() {
		this.logToConsole = true;
	}

	/**
	 * Sets log formatter for formating log messages. Formatter could be identified
	 * by identifier, if build-in formatter (HUMAN, CSV) or classname.
	 *
	 * @param logFormat new formatter value
	 */
	public void setLogFormat(String logFormat) {
		this.logFormat = logFormat;
	}

	/**
	 * Based on parameters (could be set through setters) initialize logger, handler and formatter.
	 */
	public void initialize() {
		logger = Logger.getLogger(loggerName != null && loggerName.length() > 0 ? loggerName : "simon_jdbc_logger");

		if (logFilename != null && logFilename.length() > 0) {
			logger.setUseParentHandlers(false);
			try {
				FileHandler fh = new FileHandler(logFilename, true);
				SimonFormatter f = formatter();
				if (f != null) {
					fh.setFormatter(f);
				}
				logger.addHandler(fh);
			} catch (IOException e) {
				// todo do something
			}
		}

		if (logToConsole) {
			ConsoleHandler ch = new ConsoleHandler();
			SimonFormatter f = formatter();
			if (f != null) {
				ch.setFormatter(f);
			}
			logger.addHandler(ch);
		}
	}

	/**
	 * Determine and returns formatter. If no formatter is set or initialization of formatter instance
	 * failed {@link org.javasimon.jdbc.logging.LoggingCallback.HumanFormatter} is returned.
	 *
	 * @return choosed formatter or {@link HumanFormatter}
	 */
	private SimonFormatter formatter() {
		if (logFormat != null) {
			if (logFormat.equalsIgnoreCase(HumanFormatter.ID)) {
				return new HumanFormatter();
			} else if (logFormat.equalsIgnoreCase(CsvFormatter.ID)) {
				return new CsvFormatter();
			} else {
				try {
					Object o = Class.forName(logFormat).newInstance();
					if (o instanceof SimonFormatter) {
						return (SimonFormatter) o;
					}
				} catch (Exception e) {
					return new HumanFormatter();
				}
			}
		}
		return new HumanFormatter();
	}

	/**
	 * Custom start event handler for jdbc logging.
	 * Checks if stopwatch simon is one of jdbc stopwatch and if it is connection or statement
	 * stopwatch, if yes, logs event.
	 *
	 * @param split split from stopwatch from start moment
	 */
	public void stopwatchStart(Split split) {
		String fullName = split.getStopwatch().getName();
		if (fullName != null && fullName.startsWith(prefix)) {
			String localName = SimonUtils.localName(fullName);
			if (localName.equals("conn") || localName.equals("stmt")) {
				logger.log(Level.INFO, "Start: {0}", new CallbackLogParams(fullName, Event.STOPWATCH_START, 0, split.getStopwatch().getNote()));
			}
		}
	}

	/**
	 * Custom stop event handler for jdbc logging.
	 * Checks if stopwatch simon is one of jdbc stopwatch, if yes, logs event.
	 *
	 * @param split split from stopwatch from stop moment
	 */
	public void stopwatchStop(Split split) {
		String fullName = split.getStopwatch().getName();
		if (fullName != null && fullName.startsWith(prefix)) {
			logger.log(Level.INFO, "Stop: {0}", new CallbackLogParams(fullName, Event.STOPWATCH_STOP, split.runningFor(), split.getStopwatch().getNote()));
		}
	}
}
