package org.javasimon.jdbc.logging;

import org.javasimon.Callback;
import org.javasimon.utils.SimonUtils;

/**
 * CallbackLogParams is data object and contains parameters from callback that
 * are interesting for logging.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 3.3.2009 23:28:48
 * @since 2.0
 * @see org.javasimon.jdbc.logging.SimonFormatter
 */
final class CallbackLogParams {

	private String fullName;
	private String localName;
	private Callback.Event event;
	private long split;
	private String note;

	/**
	 * Class constructor, initialize all properties.
	 *
	 * @param fullName full name of simon
	 * @param event event (see {@link org.javasimon.Callback.Event})
	 * @param split split in ms
	 * @param note provided note
	 */
	CallbackLogParams(String fullName, Callback.Event event, long split, String note) {
		this.fullName = fullName;
		this.localName = SimonUtils.localName(fullName);
		this.event = event;
		this.split = split;
		this.note = note;
	}

	/**
	 * Getter for simon fully qualified name.
	 * @return simon fullname
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Getter for simon local name, just name after last dot.
	 * @return simon local name
	 */
	public String getLocalName() {
		return localName;
	}

	/**
	 * Getter for stopwatch event (start, stop, etc.), see {@link org.javasimon.Callback.Event}.
	 * @return stopwatch event
	 */
	public Callback.Event getEvent() {
		return event;
	}

	/**
	 * Getter for stopwatch split.
	 * @return stopwatch split
	 */
	public long getSplit() {
		return split;
	}

	/**
	 * Getter for simon note.
	 * @return simon note
	 */
	public String getNote() {
		return note;
	}
}
