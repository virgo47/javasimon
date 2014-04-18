package org.javasimon.aggregation.metricsDao;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class DaoException extends Exception {
	public DaoException(String msg) {
		super(msg);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

	public DaoException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
