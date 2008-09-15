package org.javasimon;

/**
 * Implements common ground for various stat processors.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Sep 13, 2008
 */
abstract class AbstractStatProcessor implements StatProcessor {
	private ResultInterpreter resultInterpreter = DefaultInterpreter.INSTANCE;

	@Override
	public void setInterpreter(ResultInterpreter interpreter) {
		resultInterpreter = interpreter;
	}

	protected String interpret(double value) {
		return resultInterpreter.interpret(value);
	}
}
