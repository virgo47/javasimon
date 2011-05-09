package org.javasimon.testapp;

import org.javasimon.testapp.test.DataProvider;

import java.util.Random;

/**
 * Class InsertDataProvider.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $ Date: $
 * @since 2.0
 */
public class RandomNumberDataProvider implements DataProvider {

	private Random random = new Random();
	private int max;

	public RandomNumberDataProvider(int max) {
		this.max = max;
	}

	public int no() {
		return random.nextInt(max);
	}
}
