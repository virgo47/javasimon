package org.javasimon.utils;

import org.javasimon.Simon;

/**
 * Interface for specifying logic for filtering simons in hierarchy.
 * Can be used for collecting aggregate statistics only for specific simons.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public interface SimonFilter {
	/**
	 * Check current simon should be used/considered.
	 *
	 * @param simon - simon to check
	 * @return true if current simon should be used, false otherwise
	 */
	boolean accept(Simon simon);
}
