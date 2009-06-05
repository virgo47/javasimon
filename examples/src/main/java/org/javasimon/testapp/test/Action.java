package org.javasimon.testapp.test;

/**
 * Common interface for various database-based actions.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 18.3.2009 12:22:07
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
