package org.javasimon;

/**
 * TopSimon is a special top level Simon that can't be set to inherit state.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public final class RootSimon extends SimonStopwatchImpl {
	public RootSimon(String name) {
		super(name);
		enable(false);
	}

	public void inheritState(boolean resetSubtree) {
		if (resetSubtree) {
			resetSubtree();
		}
	}
}
