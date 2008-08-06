package org.javasimon;

/**
 * FactoryVsStopwatchComparison.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 6, 2008
 */
public final class SimonDepthEffect {
	private static final int LOOP = 20000000;
	private static String name;

	public static void main(String[] args) {
		SimonFactory.disbleOverheadSimon();
		name = "something";
		// warmup
		for (int i = 0; i < LOOP; i++) {
			SimonFactory.getCounter(name);
		}

		long ns = System.nanoTime();
		for (int i = 0; i < LOOP; i++) {
			SimonFactory.getCounter(name);
		}
		System.out.println("Get '" + name + "': " + SimonUtils.presentTime(System.nanoTime() - ns));

		name = "a.b.c.d.e.f.g.h";
		// warmup
		theMethod();

		ns = System.nanoTime();
		theMethod();
		System.out.println("Get '" + name + "': " + SimonUtils.presentTime(System.nanoTime() - ns));

		name = "a.b.c.d.e.f.g.h.i.j.k.l.m.n.o.p.q.r.s.t.u.v.w.x.y.z";
		// warmup
		theMethod();

		ns = System.nanoTime();
		theMethod();
		System.out.println("Get '" + name + "': " + SimonUtils.presentTime(System.nanoTime() - ns));

		SimonFactory.getStopwatch(name).enable(false);
		// warmup
		theMethod();

		ns = System.nanoTime();
		theMethod();
		System.out.println("Get enabled '" + name + "': " + SimonUtils.presentTime(System.nanoTime() - ns));

		SimonFactory.getStopwatch(name).disable(false);
		// warmup
		theMethod();

		ns = System.nanoTime();
		theMethod();
		System.out.println("Get disabled '" + name + "': " + SimonUtils.presentTime(System.nanoTime() - ns));
		System.out.println("SimonFactory.getOverheadSimon() = " + SimonFactory.getOverheadSimon());
	}

	private static void theMethod() {
		for (int i = 0; i < LOOP; i++) {
			SimonFactory.getStopwatch(name);
		}
	}
}