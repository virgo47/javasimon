package org.javasimon;

/**
 * Generic filter useful whenever Simons are filtered for some operation.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public interface SimonFilter {

	/** Filter accepting all Simons. */
	SimonFilter ACCEPT_ALL_FILTER = new SimonFilter() {
		@Override
		public boolean accept(Simon simon) {
			return true;
		}
	};

	/**
	 * Checks whether current Simon should be used/considered.
	 *
	 * @param simon Simon to check
	 * @return true if current Simon should be used, false otherwise
	 */
	boolean accept(Simon simon);
}
