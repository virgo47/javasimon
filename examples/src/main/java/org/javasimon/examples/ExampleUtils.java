package org.javasimon.examples;

/**
 * Contains some supportive utils common for more examples.
 *
 * @author <a href="mailto:richard.richter@posam.sk">Richard "Virgo" Richter</a>
 * @since 3.1
 */
public final class ExampleUtils {
	private ExampleUtils() {
	}

	/**
	 * Method that lasts randomly from ~0 to the square of the specified amount of maxMsRoot.
	 * This is just to avoid linear randomness.
	 *
	 * @param maxMsRoot square root of the maximal waiting time
	 */
	public static void waitRandomly(long maxMsRoot) {
		long random = (long) (Math.random() * maxMsRoot);
		try {
			Thread.sleep(random * random);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
