package org.javasimon.jdbc.logging;

import org.javasimon.Callback;
import org.javasimon.utils.SimonUtils;

/**
 * CallbackLogParams is data object and contains parameters from callback that
 * are interesting for logging.
 *
 * @author Radovan Sninsky
 * @version $Revision: $ $Date: $
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
