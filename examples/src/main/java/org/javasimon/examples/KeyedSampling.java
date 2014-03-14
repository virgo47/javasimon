package org.javasimon.examples;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.callback.CallbackSkeleton;

public class KeyedSampling {
	public static void main(String[] args) {
		Stopwatch stopwatch = SimonManager.getStopwatch(null);
		SimonManager.callback().addCallback(new CallbackSkeleton() {
			@Override
			public void onStopwatchAdd(Stopwatch stopwatch, Split split, StopwatchSample sample) {
				System.out.println("\nAdded: " + split.runningFor());
			}
		});

		System.out.println(stopwatch.sample());

		stopwatch.addSplit(Split.create(55));
		System.out.println("1: " + stopwatch.sample("1"));

		stopwatch.addSplit(Split.create(47));
		System.out.println("1: " + stopwatch.sample("1"));

		stopwatch.addSplit(Split.create(33));
		System.out.println("1: " + stopwatch.sample("1"));
		System.out.println("2: " + stopwatch.sample("2"));

		stopwatch.addSplit(Split.create(100));
		System.out.println("1: " + stopwatch.sample("1"));
		System.out.println("2: " + stopwatch.sample("2"));
		System.out.println("3: " + stopwatch.sample("3"));

		stopwatch.removeSampleKey("1");
		stopwatch.addSplit(Split.create(12));
		System.out.println("1: " + stopwatch.sample("1"));
		System.out.println("2: " + stopwatch.sample("2"));
		System.out.println("3: " + stopwatch.sample("3"));
	}
}
