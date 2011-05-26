package gwimon.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gwimon.client.GwimonService;
import gwimon.client.SimonAggregation;
import gwimon.client.SimonFilter;
import gwimon.client.SimonValue;
import org.javasimon.*;

import java.util.regex.Pattern;

/**
 * Gwimon Servlet providing Simon data for the GWT client.
 *
 * @author Richard "Virgo" Richter (virgo47@gmail.com)
 */
public class GwimonServlet extends RemoteServiceServlet implements GwimonService {
	@Override
	public SimonAggregation listSimons(SimonFilter filter) {
		Pattern simonMask = null;
		if (filter.getMask() != null && !filter.getMask().isEmpty()) {
			// TODO can fail - but maybe we don't need to solve it for now
			simonMask = Pattern.compile(filter.getMask());
		}
		SimonAggregation aggregation = new SimonAggregation();
		for (String name : SimonManager.manager().simonNames()) {
			if (simonMask != null && !simonMask.matcher(name).find()) {
				System.out.println("No match: " + name);
				continue; // required match, but failed
			}
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
			aggregation.add(value);
		}

		return aggregation;
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
