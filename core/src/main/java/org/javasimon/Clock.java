package org.javasimon;

/**
 * Interface for getting current time. Can be used instead of {@link System#currentTimeMillis()}
 * and {@link System#nanoTime()} to avoid slow tests.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public interface Clock {
    /**
     * Gets current time in nanoseconds.
     *
     * @return current time in nanoseconds
     */
	long timeNanos();

    /**
     * Gets current time in milliseconds.
     *
     * @return current time in milliseconds
     */
	long timeMillis();
}
