package org.javasimon.demoapp.dao;

/**
 * @author Ivan Mushketyk (ivan.mushketyk@gmail.com)
 */
public class DaoException extends RuntimeException {
    public DaoException() {
        super();
    }

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
