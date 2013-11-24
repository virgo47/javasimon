package org.javasimon.examples;

import java.util.Random;

import org.javasimon.Manager;
import org.javasimon.SimonManager;

/**
 * Contains some supportive utils common for more examples.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
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
	public static void waitRandomlySquared(long maxMsRoot) {
		long random = (long) (Math.random() * maxMsRoot);
		try {
			Thread.sleep(random * random);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static final String[] RANDOM_NAMES = ("Deadra Breakell Rod Herrero Genesis Boutilier Cliff Daus Carey Chevas" +
		" Loralee Rizvi Virgen Pahler Un Muscat Elwood Poeppel Jeffry Carlise Kuramoto Bibi Whatcott Lianne Tellefson" +
		" Ruthanne Stipes Elwood Kisselburg Raphael Maxam Pura Abrecht Rod Jernberg Bok Mehrtens Brittanie Palamino" +
		" Jeffry Wansing Delsie Palms Rob Doub Moises Minney Armand Khaleel").split(" ");

	private static final Random RANDOM = new Random();

	/**
	 * Fills the {@link org.javasimon.SimonManager} with specified number of Simons (or slightly more).
	 *
	 * @param roughCount how many Stopwatches to create (or a bit more)
	 */
	public static void fillManagerWithSimons(int roughCount) {
		System.out.print("Filling manager with ~" + roughCount + " Simons...");
		while (SimonManager.getSimonNames().size() < roughCount) {
			SimonManager.getStopwatch(generateRandomName(RANDOM.nextInt(10) + 1));
		}
		System.out.println(" " + SimonManager.getSimonNames().size() + " created.");
	}

	private static String generateRandomName(int depth) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			if (sb.length() > 0) {
				sb.append(Manager.HIERARCHY_DELIMITER);
			}
			sb.append(RANDOM_NAMES[RANDOM.nextInt(RANDOM_NAMES.length)]);
		}
		return sb.toString();
	}
}
