package org.javasimon.examples.testapp;

import java.util.Random;

import org.javasimon.examples.testapp.test.DataProvider;

/**
 * Class InsertDataProvider.
 *
 * @author Radovan Sninsky
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
