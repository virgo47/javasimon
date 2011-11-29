package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * Sample contains all relevant values of the Simon that are obtained by the
 * {@link org.javasimon.Simon#sample()} and {@link org.javasimon.Simon#sampleAndReset()} methods.
 * Returned object contains consistent set of Simon values as both operations are synchronized.
 * However Sample is a Java Bean and it can be modified afterwards so no consistency is guaranteed
 * when the object is used in an inapropriate context. As a Java Bean object can be
 * used directly as the data transfer object without need to create another DTO with the same data.
 * Sample generally doesn't have any behavior.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public abstract class Sample {
	private String name;
	private String note;
	private long firstUsage;
	private long lastUsage;
	private long lastReset;

	/**
	 * Name of the sampled Simon.
	 *
	 * @return Simon's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the sampled Simon - mostly not used directly.
	 *
	 * @param name Simon's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Note from the sampled Simon.
	 *
	 * @return Simon's note
	 */
	public final String getNote() {
		return note;
	}

	/**
	 * Sets the note for the sample, typically note from the sampled Simon.
	 *
	 * @param note Simon's note
	 */
	public final void setNote(String note) {
		this.note = note;
	}

	/**
	 * Timestamp of the first usage from the sampled Simon.
	 *
	 * @return Simon's first usage timestamp
	 */
	public long getFirstUsage() {
		return firstUsage;
	}

	/**
	 * Timestamp of the first usage from the sampled Simon as a formatted string.
	 *
	 * @return Simon's first usage timestamp as string
	 */
	public String getFirstUsageAsString() {
		return SimonUtils.presentTimestamp(firstUsage);
	}

	/**
	 * Sets the timestamp of the first usage from the sampled Simon.
	 *
	 * @param firstUsage Simon's first usage timestamp
	 */
	public void setFirstUsage(long firstUsage) {
		this.firstUsage = firstUsage;
	}

	/**
	 * Timestamp of the last usage from the sampled Simon.
	 *
	 * @return Simon's last usage timestamp
	 */
	public long getLastUsage() {
		return lastUsage;
	}

	/**
	 * Timestamp of the last usage from the sampled Simon as a formatted string.
	 *
	 * @return Simon's last usage timestamp as string
	 */
	public String getLastUsageAsString() {
		return SimonUtils.presentTimestamp(lastUsage);
	}

	/**
	 * Sets the timestamp of the last usage from the sampled Simon.
	 *
	 * @param lastUsage Simon's last usage timestamp
	 */
	public void setLastUsage(long lastUsage) {
		this.lastUsage = lastUsage;
	}

	/**
	 * Timestamp of the last reset from the sampled Simon.
	 *
	 * @return Simon's last reset timestamp
	 */
	public long getLastReset() {
		return lastReset;
	}

	/**
	 * Timestamp of the last reset from the sampled Simon as a formatted string.
	 *
	 * @return Simon's last reset timestamp as string
	 */
	public String getLastResetAsString() {
		return SimonUtils.presentTimestamp(lastReset);
	}

	/**
	 * Sets the timestamp of the last reset from the sampled Simon.
	 *
	 * @param lastReset Simon's last reset timestamp
	 */
	public void setLastReset(long lastReset) {
		this.lastReset = lastReset;
	}

	// common part of the toString method
	protected void finishWithUsagesAndNote(StringBuilder sb) {
		sb.append(", firstUsage=").append(SimonUtils.presentTimestamp(getFirstUsage()));
		sb.append(", lastUsage=").append(SimonUtils.presentTimestamp(getLastUsage()));
		sb.append(", lastReset=").append(SimonUtils.presentTimestamp(getLastReset()));
		sb.append(", note=").append(getNote());
		sb.append("}");
	}
}
