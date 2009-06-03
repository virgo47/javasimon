package org.javasimon;

/**
 * Implements common ground for various stat processors.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Sep 13, 2008
 */
abstract class AbstractStatProcessor implements StatProcessor {
	private ResultInterpreter resultInterpreter = DefaultInterpreter.INSTANCE;

	/**
	 * {@inheritDoc}
	 */
	public void setInterpreter(ResultInterpreter interpreter) {
		resultInterpreter = interpreter;
	}

	/**
	 * Returns String for a double value (interpreted value) according to specified result interpreter.
	 *
	 * @param value double value of a particular stat
	 * @return String representation according to chosen result interpreter
	 */
	protected String interpret(double value) {
		return resultInterpreter.interpret(value);
	}
}
