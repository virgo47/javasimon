package org.javasimon;

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

	public void reset() {
	}

	public String toString() {
		return "Unknown Simon: " + super.toString();
	}

	public Simon getDisabledDecorator() {
		return new DisabledDecorator(this) {
		};
	}
}
