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
				value.name = "!ROOT!";
			} else {
				value.name = name;
			}

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
			value.type = SimonValue.TYPE_STOPWATCH;
			value.counter = sws.getCounter();
			value.max = sws.getMax();
			value.maxTimestamp = sws.getMaxTimestamp();
			value.min = sws.getMin();
			value.minTimestamp = sws.getMinTimestamp();
			value.mean = sws.getMean();
			value.total = sws.getTotal();
			value.active = sws.getActive();
			value.last = sws.getLast();
			value.maxActive = sws.getMaxActive();
			value.maxActiveTimestamp = sws.getMaxActiveTimestamp();
			value.standardDeviation = sws.getStandardDeviation();
			value.variance = sws.getVariance();
			value.varianceN = sws.getVarianceN();
		} else if (sample instanceof CounterSample) {
			CounterSample ctrs = (CounterSample) sample;
			value.type = SimonValue.TYPE_COUNTER;
			value.counter = ctrs.getCounter();
			value.max = ctrs.getMax();
			value.maxTimestamp = ctrs.getMaxTimestamp();
			value.min = ctrs.getMin();
			value.minTimestamp = ctrs.getMinTimestamp();
			value.incrementSum = ctrs.getIncrementSum();
			value.decrementSum = ctrs.getDecrementSum();
		}
	}
}
