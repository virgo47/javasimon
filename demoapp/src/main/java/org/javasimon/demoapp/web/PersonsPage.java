package org.javasimon.demoapp.web;

import java.util.Random;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.*;
import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;

public class PersonsPage extends WebPage {
	private static final long serialVersionUID = 1L;

	private static final String[] RANDOM_NAMES = ("Deadra Breakell Rod Herrero Genesis Boutilier Cliff Daus Carey Chevas" +
		" Loralee Rizvi Virgen Pahler Un Muscat Elwood Poeppel Jeffry Carlise Kuramoto Bibi Whatcott Lianne Tellefson" +
		" Ruthanne Stipes Elwood Kisselburg Raphael Maxam Pura Abrecht Rod Jernberg Bok Mehrtens Brittanie Palamino" +
		" Jeffry Wansing Delsie Palms Rob Doub Moises Minney Armand Khaleel").split(" ");

	private static final Random RANDOM = new Random();

	@SuppressWarnings("UnusedParameters")
	public PersonsPage(final PageParameters parameters) {
		Split split = null;
		StringValue parameterNew = parameters.get("new");
		if (parameterNew != null && !parameterNew.isEmpty()) {
			split = SimonManager.getStopwatch(generateRandomName(5)).start();
		}
		try {
			SimonManager.getCounter("some.counter").increase(2);
			SimonManager.getCounter("some.counter").decrease();
		} finally {
			if (split != null) {
				split.stop();
			}
		}
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
