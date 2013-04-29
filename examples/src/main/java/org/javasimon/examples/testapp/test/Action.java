package org.javasimon.examples.testapp.test;

/**
 * Common interface for various database-based actions.
 *
 * @author Radovan Sninsky
 * @since 2.0
 */
public interface Action {
	/**
	 * Performs the action.
	 *
	 * @param runno sequence number of the run
	 */
	void perform(int runno);
}
