package org.javasimon.jmx;

import org.javasimon.*;
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
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 *
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
		setSimonState(name, SimonState.ENABLED);
	}

	private void setSimonState(String name, SimonState newState) {
		Simon simon = manager.getSimon(name);
		if (simon == null) {
			throw new SimonException("Unknown Simon: " + name);
		}
		simon.setState(newState, false);
	}

	@Override
	public final void disableSimon(String name) {
		setSimonState(name, SimonState.DISABLED);
	}

	@Override
	public final void inheritState(String name) {
		setSimonState(name, SimonState.INHERIT);
	}

	/**
	 * Create a JMX Counter Sample from a Sample
	 *
	 * @param counter Counter
	 */
	private org.javasimon.jmx.CounterSample sampleCounter(Simon counter) {
		return new CounterSample((org.javasimon.CounterSample) counter.sample());
	}

	@Override
	public final CounterSample getCounterSample(String name) {
		Counter counter = getSimonOfType(name, Counter.class);
		return sampleCounter(counter);
	}

	private <T extends Simon> T getSimonOfType(String name, Class<T> simonType) {
		Simon simon = manager.getSimon(name);
		if (simon == null) {
			throw new SimonException("Unknown Simon: " + name);
		}

		if (!simonType.isInstance(simon)) {
			throw new SimonException("Wrong Simon type");
		}

		return simonType.cast(simon);
	}

	@Override
	public CounterSample getIncrementCounterSample(String name, String key) {
		Counter counter = getSimonOfType(name, Counter.class);
		return new CounterSample(counter.sampleIncrement(key));
	}

	/**
	 * Sample all Counters whose name matches given pattern
	 *
	 * @param namePattern Name pattern, null means all Counters
	 * @return One Sample for each Counter
	 */
	@Override
	public List<CounterSample> getCounterSamples(String namePattern) {
		List<CounterSample> counterSamples = new ArrayList<>();
		for (Simon simon : manager.getSimons(SimonPattern.createForCounter(namePattern))) {
			counterSamples.add(sampleCounter(simon));
		}
		return counterSamples;
	}

	@Override
	public List<CounterSample> getIncrementCounterSamples(String namePattern, String key) {
		List<CounterSample> counterSamples = new ArrayList<>();
		for (Simon simon : manager.getSimons(SimonPattern.createForCounter(namePattern))) {
			Counter counter = (Counter) simon;
			counterSamples.add(new CounterSample(counter.sampleIncrement(key)));
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
		Stopwatch stopwatch = getSimonOfType(name, Stopwatch.class);
		return sampleStopwatch(stopwatch);
	}

	@Override
	public StopwatchSample getIncrementStopwatchSample(String name, String key) {
		Stopwatch stopwatch = getSimonOfType(name, Stopwatch.class);
		return new StopwatchSample(stopwatch.sampleIncrement(key));
	}

	@Override
	public List<StopwatchSample> getStopwatchSamples(String namePattern) {
		List<StopwatchSample> stopwatchSamples = new ArrayList<>();
		for (Simon simon : manager.getSimons(SimonPattern.createForStopwatch(namePattern))) {
			stopwatchSamples.add(sampleStopwatch(simon));
		}
		return stopwatchSamples;
	}

	@Override
	public List<StopwatchSample> getIncrementStopwatchSamples(String namePattern, String key) {
		List<StopwatchSample> stopwatchSamples = new ArrayList<>();
		for (Simon simon : manager.getSimons(SimonPattern.createForStopwatch(namePattern))) {
			Stopwatch stopwatch = (Stopwatch) simon;
			stopwatchSamples.add(new StopwatchSample(stopwatch.sampleIncrement(key)));
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

	Manager getManager() {
		return manager;
	}
}
