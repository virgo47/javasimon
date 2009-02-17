package org.javasimon.jdbc;

import org.javasimon.Split;
import org.javasimon.CallbackSkeleton;
import org.javasimon.utils.SimonUtils;

import java.util.Date;
import java.util.logging.*;
import java.text.SimpleDateFormat;
import java.io.IOException;

/**
 * Trieda LoggingCallback.
 * Logging structure:
 * <ul>
 * <li>timestamp</li>
 * <li>exec/life time</li>
 * <li>conn id</li>
 * <li>stmt id</li>
 * <li>action code</li>
 * <li>text (values)</li>
 * <li>javasimon</li>
 * </ul>
 *
 * Actions:
 * <ul>
 * <li>conn - open</li>
 * <li>conn - close</li>
 * <li>stmt - open</li>
 * <li>stmt - close</li>
 * <li>sql - exec</li>
 * <li>resultset - open</li>
 * <li>resultset - close</li>
 * </ul>
 *
 * <pre>
 CompositeFilterCallback filter = new CompositeFilterCallback();
 filter.addRule(FilterCallback.Rule.Type.MUST, null, "org.javasimon.jdbc.*");
 filter.addCallback(new JdbcLogCallback());

 SimonManager.installCallback(filter);
 * </pre>
 * @author Radovan Sninsky
 * @version $Revision: $ $Date: $
 * @created 30.1.2009 15:26:00
 * @since 1.0
 */
public class JdbcLogCallback extends CallbackSkeleton {

	private final Logger logger = Logger.getLogger("simon_jdbc_logger");

	/**
	 * Trieda MyFormatter.
	 *
	 * @author Radovan Sninsky
	 * @version $ Revision $ $ Date $
	 * @created 30.05.2003 14:56:29
	 * @since 2
	 */
	private class HumanFormatter extends Formatter {

		private Date dat = new Date();
		private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
		private String lineSeparator = System.getProperty("line.separator");

		/**
		 * Naformatovanie logovacej spravy.
		 *
		 * @param record logovacia sprava
		 * @return naformatovay retazec
		 */
		public synchronized String format(LogRecord record) {
			dat.setTime(record.getMillis());
			StringBuilder sb = new StringBuilder();
			sb.append(dateTimeFormat.format(dat)).append(' ');
			sb.append(record.getThreadID()).append(' ');

			String ln = SimonUtils.localName(fullName(record));
			sb.append('[').append(ln.equals("conn") || ln.equals("stmt") ? ln : "sql ")
				.append(isStart(record) ? "->" : "-<").append("]").append(' ');
			sb.append(fullName(record)).append(' ');
			sb.append(split(record)).append(' ');
			sb.append(note(record)).append(lineSeparator);
			return sb.toString();
		}

		protected String fullName(LogRecord r) {
			return (String)r.getParameters()[0];
		}

		protected boolean isStart(LogRecord r) {
			return r.getParameters()[1].equals("start");
		}

		protected String split(LogRecord r) {
			Object o = r.getParameters()[2];
			return o != null ? SimonUtils.presentNanoTime((Long)o) : "";
		}

		protected String note(LogRecord r) {
			Object o = r.getParameters()[3];
			return o != null ? "{"+o+"}" : "";
		}
	}

	private class CsvFormatter extends HumanFormatter {

		private String lineSeparator = System.getProperty("line.separator");

		public synchronized String format(LogRecord record) {
			StringBuilder sb = new StringBuilder();
			sb.append(record.getMillis()).append('|');
			sb.append(record.getThreadID()).append('|');

			String ln = SimonUtils.localName(fullName(record));
			sb.append(ln.equals("conn") || ln.equals("stmt") ? ln : "sql")
				.append(isStart(record) ? "|>" : "|<").append('|');
			sb.append(fullName(record)).append('|');
			sb.append(split(record)).append('|');
			sb.append(note(record)).append(lineSeparator);
			return sb.toString();
		}

		protected String split(LogRecord r) {
			Object o = r.getParameters()[2];
			return o != null ? o.toString() : "";
		}

		protected String note(LogRecord r) {
			Object o = r.getParameters()[3];
			return o != null ? o.toString() : "";
		}
	}

	public JdbcLogCallback(String filename) {
		logger.setUseParentHandlers(false);
//		logger.addHandler(new StreamHandler(System.out, new HumanFormatter()));
//		logger.addHandler(new StreamHandler(System.out, new CsvFormatter()));
		try {
			FileHandler fh = new FileHandler(filename);
			fh.setFormatter(new CsvFormatter());
			logger.addHandler(fh);
		} catch (IOException e) {
			// todo do something
		}
	}

	public void stopwatchStart(Split split) {
		String fullName = split.getStopwatch().getName();
		String localName = SimonUtils.localName(fullName);
		if (localName.equals("conn") || localName.equals("stmt")) {
			logger.log(Level.INFO, null, new Object[] {fullName, "start", null, split.getStopwatch().getNote()});
		}
	}

	public void stopwatchStop(Split split) {
		String fullName = split.getStopwatch().getName();
		logger.log(Level.INFO, null, new Object[] {fullName, "stop", split.runningFor(), split.getStopwatch().getNote()});
	}
}
