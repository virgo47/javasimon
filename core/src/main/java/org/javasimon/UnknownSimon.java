package org.javasimon;

/**
 * UnknownSimon represents Simon node in the hierarchy without known type. It may be replaced
 * in the hierarchy for real Simon in the future.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
final class UnknownSimon extends AbstractSimon {
	/**
	 * Construts unknown Simon with a specified name and for the specified manager.
	 *
	 * @param name Simon's name
	 * @param manager owning manager
	 */
	UnknownSimon(String name, Manager manager) {
		super(name, manager);
	}

	/**
	 * {@inheritDoc}
	 */
	public Simon reset() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public Sample sample() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Sample sampleAndReset() {
		return null;
	}

	/**
	 * Returns the label {@code Unknown Simon} and basic information for the Simon as a human readable string.
	 *
	 * @return basic information about unknown Simon
	 * @see AbstractSimon#toString()
	 */
	@Override
	public String toString() {
		return "Unknown Simon: " + super.toString();
	}
}
