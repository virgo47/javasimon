package org.javasimon;

import java.util.Map;
import java.util.Collections;

/**
 * UnknownSimon represents Simon node in the hierarchy without known type. It may be replaced
 * in the hierarchy for real Simon in the future.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public final class UnknownSimon extends AbstractSimon {
	public UnknownSimon(String name) {
		super(name);
	}

	public Simon reset() {
		return this;
	}

	public Map<String, String> sample(boolean reset) {
		return Collections.emptyMap();
	}

	public String toString() {
		return "Unknown Simon: " + super.toString();
	}
}
