package org.javasimon;

/**
 * TopSimon is a special top level Simon.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public final class TopSimon extends SimonStopwatchImpl {
	public TopSimon(String name) {
		super(name);
		enable();
	}

	public Simon inheritState() {
		return this;
	}

	public String toString() {
		return "Top " + super.toString();
	}
}
