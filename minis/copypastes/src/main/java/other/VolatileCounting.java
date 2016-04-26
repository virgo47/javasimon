package other;

/** @noinspection Duplicates */
public class VolatileCounting {

	private static final int MAX = 20_000_000;
	private static final int BOUND = 1;

	public static void main(String[] args) throws InterruptedException {
		SourceThread sourceThread = new SourceThread();
		TargetThread targetThread = new TargetThread();
		sourceThread.start();

		Thread.sleep(50);

		// source/target are placed in different places to hopefully not meet in a single cache-line
		System.out.println("source = " + SourceThread.source);
		System.out.println("target = " + TargetThread.target);
		long ms = System.currentTimeMillis();
		targetThread.start();
		targetThread.join();
		sourceThread.join();
		ms = System.currentTimeMillis() - ms;
		System.out.println("ms = " + ms);
	}

	static class SourceThread extends Thread {
		static volatile int source;

		@Override public void run() {
			while (true) {
				if (TargetThread.target >= MAX) return;
				if (source - TargetThread.target < BOUND) source++;
			}
		}
	}

	static class TargetThread extends Thread {
		static volatile int target;

		@Override public void run() {
			while (true) {
				if (target >= MAX) return;
				if (target < SourceThread.source) target++;
			}
		}
	}
}
