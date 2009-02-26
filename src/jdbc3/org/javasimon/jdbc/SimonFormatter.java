package org.javasimon.jdbc;

import org.javasimon.Callback;
import org.javasimon.utils.SimonUtils;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Trieda AbstractFormatter.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 18.2.2009 21:10:59
 * @since 2
 */
public abstract class SimonFormatter extends Formatter {

	class LogParams {
		private String fullName;
		private String localName;
		private Callback.Event event;
		private long split;
		private String note;

		LogParams(String fullName, Callback.Event event, long split, String note) {
			this.fullName = fullName;
			this.localName = SimonUtils.localName(fullName);
			this.event = event;
			this.split = split;
			this.note = note;
		}

		public String getFullName() {
			return fullName;
		}

		public String getLocalName() {
			return localName;
		}

		public Callback.Event getEvent() {
			return event;
		}

		public long getSplit() {
			return split;
		}

		public String getNote() {
			return note;
		}
	}

	/**
	 * Naformatovanie logovacej spravy.
	 *
	 * @param record logovacia sprava
	 * @return naformatovay retazec
	 */
	public synchronized String format(LogRecord record) {
		return formatRecord(record, (LogParams)record.getParameters()[0]);
	}

	protected abstract String formatRecord(LogRecord record, LogParams params);

	protected boolean isStart(LogParams p) {
		return p.getEvent() == Callback.Event.STOPWATCH_START;
	}

	protected String note(LogParams p) {
		return p.getNote() != null ? "{"+p.getNote()+"}" : "";
	}
}
