package other;

import java.util.concurrent.atomic.AtomicInteger;

/** @noinspection Duplicates */
public class AtomicIntCounting {

	private static final int MAX = 200_000_000;
	private static final int BOUND = 1000;

	private static AtomicInteger source = new AtomicInteger();
	private static AtomicInteger target = new AtomicInteger();

	public static void main(String[] args) throws InterruptedException {
		SourceThread sourceThread = new SourceThread();
		TargetThread targetThread = new TargetThread();
		sourceThread.start();

		Thread.sleep(50);

		System.out.println("source = " + source);
		System.out.println("target = " + target);
		long ms = System.currentTimeMillis();
		targetThread.start();
		targetThread.join();
		sourceThread.join();
		ms = System.currentTimeMillis() - ms;
		System.out.println("ms = " + ms);
	}

	private static class SourceThread extends Thread {
		@Override public void run() {
			while (true) {
				if (target.get() >= MAX) return;
				if (source.get() - target.get() < BOUND) source.incrementAndGet();
			}
		}
	}

	private static class TargetThread extends Thread {
		@Override public void run() {
			while (true) {
				if (target.get() >= MAX) return;
				if (target.get() < source.get()) target.incrementAndGet();
			}
		}
	}
}
