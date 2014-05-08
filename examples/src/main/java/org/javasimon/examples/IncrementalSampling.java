package org.javasimon.examples;

import org.javasimon.Counter;
import org.javasimon.CounterSample;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.callback.CallbackSkeleton;

public class IncrementalSampling {

	public static void main(String[] args) {
		SimonManager.callback().addCallback(new CallbackSkeleton() {
			@Override
			public void onStopwatchAdd(Stopwatch stopwatch, Split split, StopwatchSample sample) {
				System.out.println("\nAdded: " + split.runningFor());
			}

			@Override
			public void onCounterIncrease(Counter counter, long inc, CounterSample sample) {
				System.out.println("\nCounter increase: " + counter);
			}

			@Override
			public void onCounterSet(Counter counter, long val, CounterSample sample) {
				System.out.println("\nCounter set: " + counter);
			}
		});

		stopwatchKeyedSampling();
		counterKeyedSampling();
	}

	private static void stopwatchKeyedSampling() {
		System.out.println("\nSTOPWATCH incremental sampling demo");
		Stopwatch stopwatch = SimonManager.getStopwatch(null);
		System.out.println(stopwatch.sample());

		stopwatch.addSplit(Split.create(55));
		System.out.println("sample=" + stopwatch.sample());
		System.out.println("1: " + stopwatch.sampleIncrement("1"));

		stopwatch.addSplit(Split.create(47));
		System.out.println("sample=" + stopwatch.sample());
		System.out.println("1: " + stopwatch.sampleIncrement("1"));

		stopwatch.addSplit(Split.create(33));
		System.out.println("sample=" + stopwatch.sample());
		System.out.println("1: " + stopwatch.sampleIncrement("1"));
		System.out.println("2: " + stopwatch.sampleIncrement("2"));

		stopwatch.addSplit(Split.create(100));
		System.out.println("sample=" + stopwatch.sample());
		System.out.println("1: " + stopwatch.sampleIncrement("1"));
		System.out.println("2: " + stopwatch.sampleIncrement("2"));
		System.out.println("3: " + stopwatch.sampleIncrement("3"));

		stopwatch.stopIncrementalSampling("1");
		stopwatch.addSplit(Split.create(12));
		System.out.println("sample=" + stopwatch.sample());
		System.out.println("1: " + stopwatch.sampleIncrement("1"));
		System.out.println("2: " + stopwatch.sampleIncrement("2"));
		System.out.println("3: " + stopwatch.sampleIncrement("3"));
		System.out.println("3 after no change: " + stopwatch.sampleIncrement("3"));
	}

	private static void counterKeyedSampling() {
		System.out.println("\nCOUNTER incremental sampling demo");
		Counter counter = SimonManager.getCounter(null);
		System.out.println(counter.sample());

		counter.increase();
		System.out.println("sample=" + counter.sample());
		System.out.println("1: " + counter.sampleIncrement("1"));

		counter.increase(2);
		System.out.println("sample=" + counter.sample());
		System.out.println("1: " + counter.sampleIncrement("1"));

		counter.increase(3);
		System.out.println("sample=" + counter.sample());
		System.out.println("1: " + counter.sampleIncrement("1"));
		System.out.println("2: " + counter.sampleIncrement("2"));

		counter.set(4);
		System.out.println("sample=" + counter.sample());
		System.out.println("1: " + counter.sampleIncrement("1"));
		System.out.println("2: " + counter.sampleIncrement("2"));
		System.out.println("3: " + counter.sampleIncrement("3"));

		counter.stopIncrementalSampling("1");
		counter.increase(-5);
		System.out.println("sample=" + counter.sample());
		System.out.println("1: " + counter.sampleIncrement("1"));
		System.out.println("2: " + counter.sampleIncrement("2"));
		System.out.println("3: " + counter.sampleIncrement("3"));
	}
}
