package org.javasimon.examples.testapp.test;

import org.javasimon.SimonManager;
import org.javasimon.Split;

/**
 * Class Runner.
 *
 * @author Radovan Sninsky
 * @since 2.0
 */
public class Runner {

	private Controller controller;
	private Timer timer;

	private boolean run = true;

	public Runner(Controller controller, Timer timer) {
		this.controller = controller;
		this.timer = timer;
	}

	public void run() {
		int runno = 0;
		try {
			while (run) {
				Split s = SimonManager.getStopwatch("org.javasimon.examples.testapp.action").start();
				controller.next().perform(++runno);
				s.stop();

				long d = timer.delay();
				System.out.println(" -- Delay: " + d + " ms\n");
				s = SimonManager.getStopwatch("org.javasimon.examples.testapp.delay").start();
				Thread.sleep(d);
				s.stop();
			}
		} catch (InterruptedException e) {
			// do nothing
		}
	}

	public void stop() {
		run = false;
		Thread.currentThread().interrupt();
	}
}
