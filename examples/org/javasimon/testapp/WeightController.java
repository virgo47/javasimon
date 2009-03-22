package org.javasimon.testapp;

import org.javasimon.testapp.test.Action;
import org.javasimon.testapp.test.Controller;

import java.util.*;

/**
 * Class WeightController.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $ Date: $
 * @created 19.3.2009 13:18:29
 * @since 2.0
 */
public class WeightController implements Controller {

	private final Random random = new Random();

	private SortedMap<Integer, Action> actions = new TreeMap<Integer, Action>();

	public WeightController() {
	}

	public void addAction(Action action, int weight) {
		int last = 0;
		if (actions.size() > 0) {
			for (int x : actions.keySet()) {
				last = x;
			}
		}
		actions.put(last+weight, action);
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
