package org.javasimon.testapp;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.javasimon.testapp.test.Action;
import org.javasimon.testapp.test.Controller;

/**
 * Class WeightController.
 *
 * @author Radovan Sninsky
 * @since 2.0
 */
public class WeightController implements Controller {

	private final Random random = new Random();

	private Map<Integer, Action> actions = new TreeMap<Integer, Action>();

	public WeightController() {
	}

	public void addAction(Action action, int weight) {
		int last = 0;
		if (actions.size() > 0) {
			for (int x : actions.keySet()) {
				last = x;
			}
		}
		actions.put(last + weight, action);
	}

	public Action next() {
		int x = random.nextInt(100);
		for (int i : actions.keySet()) {
			if (x < i) {
				return actions.get(i);
			}
		}
		return null;
	}
}
