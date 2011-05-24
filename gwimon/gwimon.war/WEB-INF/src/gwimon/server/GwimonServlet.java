package gwimon.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gwimon.client.GwimonService;
import gwimon.client.SimonValue;
import org.javasimon.*;

import java.util.ArrayList;

/**
 * Gwimon Servlet providing Simon data for the GWT client.
 *
 * @author Richard "Virgo" Richter (virgo47@gmail.com)
 */
public class GwimonServlet extends RemoteServiceServlet implements GwimonService {
	@Override
	public ArrayList<SimonValue> listSimons(String mask) {
		ArrayList<SimonValue> values = new ArrayList<SimonValue>();
		for (String name : SimonManager.manager().simonNames()) {
			SimonValue value = new SimonValue();
			if (name.equals(Manager.ROOT_SIMON_NAME)) {
				name = "!ROOT!";
			}
			value.name = name;

			Sample sample = SimonManager.getSimon(name).sample();
			if (sample != null) {
				sampleToValueObject(value, sample);
			}
			values.add(value);
		}

		return values;
	}

	private void sampleToValueObject(SimonValue value, Sample sample) {
		value.note = sample.getNote();
		value.firstUsage = sample.getFirstUsage();
		value.lastUsage = sample.getLastUsage();
		value.lastReset = sample.getLastReset();

		if (sample instanceof StopwatchSample) {
			StopwatchSample sws = (StopwatchSample) sample;
			value.counter = sws.getCounter();
			value.max = sws.getMax();
			value.min = sws.getMin();
			value.mean = sws.getMean();
		}
	}
}
