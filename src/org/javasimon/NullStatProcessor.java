package org.javasimon;

import java.util.Map;
import java.util.Collections;

/**
 * NullStatProcessor.
*
* @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
* @created Aug 12, 2008
*/
class NullStatProcessor implements StatProcessor {
	public static final NullStatProcessor INSTANCE = new NullStatProcessor();

	public StatProcessorType getType() {
		return StatProcessorType.NULL;
	}

	@Override
	public void process(double value) {
	}

	@Override
	public double getSum() {
		return 0;
	}

	@Override
	public double getMean() {
		return 0;
	}

	@Override
	public double getVarianceN() {
		return 0;
	}

	@Override
	public double getVariance() {
		return 0;
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Map<String, String> sample(boolean reset) {
		return Collections.emptyMap();
	}

	@Override
	public void reset() {
	}

	@Override
	public void setInterpreter(ResultInterpreter interpreter) {
	}

	@Override
	public String toString() {
		return "Null Stats";
	}
}
