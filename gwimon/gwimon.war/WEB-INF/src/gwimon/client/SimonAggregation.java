package gwimon.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SimonAggregation contains various information returned from Gwimon Servlet.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public class SimonAggregation implements Serializable {
	private List<SimonValue> simonList = new ArrayList<SimonValue>();
	private SimonValue maxSimon = new SimonValue();

	public List<SimonValue> getSimonList() {
		return simonList;
	}

	public SimonValue getMaxSimon() {
		return maxSimon;
	}

	public void add(SimonValue value) {
		updateMaxValues(maxSimon, value);
		simonList.add(value);
	}

	private void updateMaxValues(SimonValue maxValues, SimonValue simonValue) {
		if (simonValue.max > maxValues.max) {
			maxValues.max = simonValue.max;
		}
		if (simonValue.min > maxValues.min) {
			maxValues.min = simonValue.min;
		}
		if (simonValue.total > maxValues.total) {
			maxValues.total = simonValue.total;
		}
		if (simonValue.mean > maxValues.mean) {
			maxValues.mean = simonValue.mean;
		}
	}
}
