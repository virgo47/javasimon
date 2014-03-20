package org.javasimon.jmx;

import org.javasimon.Counter;
import org.javasimon.Manager;
import org.javasimon.Simon;
import org.javasimon.SimonPattern;
import org.javasimon.SimonState;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Simon MXBean implementation. See {@link SimonManagerMXBean} for interface
 * documentation.
 * Most methods are implemented by calling the {@link Manager}.
 *
 * @author Radovan Sninsky
 * @see Manager
 * @see Simon
 * @see SimonUtils
 * @since 2.0
 */
public class SimonManagerMXBeanImpl implements SimonManagerMXBean {

	private Manager manager;

	/**
	 * MXBean constructor.
	 *
	 * @param manager instance of {@link Manager}, typically {@code SimonManager.manager()}.
	 */
	public SimonManagerMXBeanImpl(Manager manager) {
		this.manager = manager;
	}

	@Override
	public final void enable() {
		manager.enable();
	}

	@Override
	public final void disable() {
		manager.disable();
	}

	@Override
	public final boolean isEnabled() {
		return manager.isEnabled();
	}

	@Override
	public final String[] getSimonNames() {
		Collection<String> simonNames = manager.getSimonNames();
		return simonNames.toArray(new String[simonNames.size()]);
	}

	@Override
	public final String[] getSimonNamesOrdered() {
		String[] simonNames = getSimonNames();
		Arrays.sort(simonNames);
		return simonNames;
	}

	@Override
	public final String getType(String name) {
		Simon s = manager.getSimon(name);
		return s != null ? s instanceof Stopwatch ? SimonInfo.STOPWATCH : SimonInfo.COUNTER : null;
	}

	@Override
	public final SimonInfo[] getSimonInfos() {
		String[] simonNames = getSimonNamesOrdered();
		SimonInfo[] simonInfo = new SimonInfo[simonNames.length];
		int i = 0;
		for (String name : simonNames) {
			Simon s = manager.getSimon(name);
			simonInfo[i++] = new SimonInfo(name, s instanceof Stopwatch ? SimonInfo.STOPWATCH :
				s instanceof Counter ? SimonInfo.COUNTER : SimonInfo.UNKNOWN);
		}
		return simonInfo;
	}

	@Override
	public final void clearManager() {
		manager.clear();
	}

	@Override
	public final void enableSimon(String name) {
		manager.getSimon(name).setState(SimonState.ENABLED, false);
	}

	@Override
	public final void disableSimon(String name) {
		manager.getSimon(name).setState(SimonState.DISABLED, false);
	}

	@Override
	public final void inheritState(String name) {
		manager.getSimon(name).setState(SimonState.INHERIT, false);
	}

	/**
	 * Create a JMX Counter Sample from a Sample
	 *
	 * @param s Counter
	 */
	private org.javasimon.jmx.CounterSample sampleCounter(Simon s) {
		return new CounterSample((org.javasimon.CounterSample) s.sample());
	}

	@Override
	public final CounterSample getCounterSample(String name) {
		Simon s = manager.getSimon(name);
		if (s != null && s instanceof Counter) {
			return sampleCounter(s);
		}
		return null;
	}

	@Override
	public final CounterSample getCounterSampleAndReset(String name) {
		Simon s = manager.getSimon(name);
		if (s != null && s instanceof Counter) {
			return new CounterSample((org.javasimon.CounterSample) s.sampleAndReset());
		}
		return null;
	}

	@Override
	public CounterSample getIncrementCounterSample(String name, String key) {
		Simon simon = manager.getSimon(name);

		if (simon instanceof Counter) {
			Counter counter = (Counter) simon;
			return new CounterSample(counter.sampleIncrement(key));
		}

		return null;
	}

	/**
	 * Sample all Counters whose name matches given pattern
	 *
	 * @param namePattern Name pattern, null means all Counters
	 * @return One Sample for each Counter
	 */
	@Override
	public List<CounterSample> getCounterSamples(String namePattern) {
		List<CounterSample> counterSamples = new ArrayList<CounterSample>();
		for (Simon simon : manager.getSimons(SimonPattern.create(namePattern))) {
			if (simon instanceof Counter) {
				counterSamples.add(sampleCounter(simon));
			}
		}
		return counterSamples;
	}

	@Override
	public List<CounterSample> getIncrementCounterSamples(String namePattern, String key) {
		List<CounterSample> counterSamples = new ArrayList<CounterSample>();
		for (Simon simon : manager.getSimons(SimonPattern.create(namePattern))) {
			if (simon instanceof Counter) {
				Counter counter = (Counter) simon;
				counterSamples.add(new CounterSample(counter.sampleIncrement(key)));
			}
		}

		return counterSamples;
	}

	/**
	 * Sample all Counters
	 *
	 * @return One Sample for each Counter
	 */
	@Override
	public List<CounterSample> getCounterSamples() {
		return getCounterSamples(null);
	}

	@Override
	public List<CounterSample> getIncrementCounterSamples(String key) {
		return getIncrementCounterSamples(null, key);
	}

	/**
	 * Create a JMX Stopwatch Sample from a Stopwatch
	 *
	 * @param s Stopwatch
	 */
	private org.javasimon.jmx.StopwatchSample sampleStopwatch(Simon s) {
		return new StopwatchSample((org.javasimon.StopwatchSample) s.sample());
	}

	@Override
	public final StopwatchSample getStopwatchSample(String name) {
		Simon s = manager.getSimon(name);
		if (s != null && s instanceof Stopwatch) {
			return sampleStopwatch(s);
		}
		return null;
	}

	@Override
	public final StopwatchSample getStopwatchSampleAndReset(String name) {
		Simon s = manager.getSimon(name);
		if (s != null && s instanceof Stopwatch) {
			return new StopwatchSample((org.javasimon.StopwatchSample) s.sampleAndReset());
		}
		return null;
	}

	@Override
	public StopwatchSample getIncrementStopwatchSample(String name, String key) {
		Simon simon = manager.getSimon(name);
		if (simon instanceof Stopwatch) {
			Stopwatch stopwatch = (Stopwatch) simon;
			return new StopwatchSample(stopwatch.sampleIncrement(key));
		}

		return null;
	}

	@Override
	public List<StopwatchSample> getStopwatchSamples(String namePattern) {
		List<StopwatchSample> stopwatchSamples = new ArrayList<StopwatchSample>();
		for (Simon simon : manager.getSimons(SimonPattern.create(namePattern))) {
			if (simon instanceof Stopwatch) {
				stopwatchSamples.add(sampleStopwatch(simon));
			}
		}
		return stopwatchSamples;
	}

	@Override
	public List<StopwatchSample> getIncrementStopwatchSamples(String namePattern, String key) {
		List<StopwatchSample> stopwatchSamples = new ArrayList<StopwatchSample>();
		for (Simon simon : manager.getSimons(SimonPattern.create(namePattern))) {
			if (simon instanceof Stopwatch) {
				Stopwatch stopwatch = (Stopwatch) simon;
				stopwatchSamples.add(new StopwatchSample(stopwatch.sampleIncrement(key)));
			}
		}

		return stopwatchSamples;
	}

	@Override
	public List<StopwatchSample> getStopwatchSamples() {
		return getStopwatchSamples(null);
	}

	@Override
	public List<StopwatchSample> getIncrementStopwatchSamples(String key) {
		return getIncrementStopwatchSamples(null, key);
	}

	@Override
	public final void printSimonTree() {
		System.out.println(SimonUtils.simonTreeString(manager.getRootSimon()));
	}

	@Override
	public final void reset(String name) {
		manager.getSimon(name).reset();
	}
}
