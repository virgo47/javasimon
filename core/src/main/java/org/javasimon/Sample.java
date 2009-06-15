package org.javasimon;

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
 * @created Jan 7, 2009
 */
public abstract class Sample {
	private String note;

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
}
