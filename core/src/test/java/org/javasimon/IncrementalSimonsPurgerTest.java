package org.javasimon;

import org.mockito.ArgumentMatcher;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class IncrementalSimonsPurgerTest {

	private ScheduledExecutorService executorService;
	private ScheduledFuture scheduledFuture;

	@BeforeMethod
	public void beforeMethod() {
		executorService = mock(ScheduledExecutorService.class);

		scheduledFuture = mock(ScheduledFuture.class);
		when(executorService.scheduleWithFixedDelay(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class)))
				.thenReturn(scheduledFuture);
	}

	@Test(dataProvider = "managersDataProvider")
	public void testPeriodicalIncrementalSimonsPurge(Manager manager) {
		IncrementalSimonsPurger incrementalSimonsPurger = new IncrementalSimonsPurger(manager, executorService);

		long duration = 1;
		TimeUnit timeUnit = TimeUnit.SECONDS;
		incrementalSimonsPurger.start(duration, timeUnit);

		verify(executorService).scheduleWithFixedDelay(
				argThat(new PurgerRunnableMatcher(manager)), eq(duration), eq(duration), eq(timeUnit));
	}

	private class PurgerRunnableMatcher extends ArgumentMatcher<Runnable> {

		private final Manager expectedManager;

		public PurgerRunnableMatcher(Manager expectedManager) {
			this.expectedManager = expectedManager;
		}

		@Override
		public boolean matches(Object o) {
			IncrementalSimonsPurger.PurgerRunnable purger = (IncrementalSimonsPurger.PurgerRunnable) o;

			return purger.getManager() == expectedManager;
		}
	}

	@Test
	public void testCancel() {
		EnabledManager manager = new EnabledManager();
		IncrementalSimonsPurger incrementalSimonsPurger = new IncrementalSimonsPurger(manager, executorService);

		long duration = 1;
		TimeUnit timeUnit = TimeUnit.SECONDS;

		incrementalSimonsPurger.start(duration, timeUnit);
		incrementalSimonsPurger.cancel();

		verify(scheduledFuture).cancel(false);
	}

	@Test(expectedExceptions = IllegalStateException.class)
	public void testCancelWithoutStart() {
		EnabledManager manager = new EnabledManager();
		IncrementalSimonsPurger incrementalSimonsPurger = new IncrementalSimonsPurger(manager, executorService);

		incrementalSimonsPurger.cancel();
	}

	@Test(expectedExceptions = IllegalStateException.class)
	public void testStartWithoutCancel() {
		EnabledManager manager = new EnabledManager();
		IncrementalSimonsPurger incrementalSimonsPurger = new IncrementalSimonsPurger(manager, executorService);

		incrementalSimonsPurger.start(1, TimeUnit.SECONDS);
		incrementalSimonsPurger.start(1, TimeUnit.SECONDS);
	}

	@Test(expectedExceptions = IllegalStateException.class)
	public void testDoubleCancel() {
		EnabledManager manager = new EnabledManager();
		IncrementalSimonsPurger incrementalSimonsPurger = new IncrementalSimonsPurger(manager, executorService);

		incrementalSimonsPurger.start(1, TimeUnit.SECONDS);
		incrementalSimonsPurger.cancel();
		incrementalSimonsPurger.cancel();
	}

	@DataProvider(name = "managersProvider")
	public Object[][] managersDataProvider() {

		return new Object[][] {
				{new EnabledManager()},
				{new SwitchingManager()}
		};
	}

	@Test(dataProvider = "managersProvider")
	public void testPurging(Manager manager) {
		Stopwatch stopwatch = manager.getStopwatch("stopwatch");
		stopwatch.sampleIncrement("key");
		stopwatch.start().stop();

		long timeInFuture = manager.milliTime() + 1000;

		IncrementalSimonsPurger.PurgerRunnable runnable = new IncrementalSimonsPurger.PurgerRunnable(manager, timeInFuture);
		runnable.run();

		// should return false if it was removed by purger runnable
		Assert.assertFalse(stopwatch.stopIncrementalSampling("key"));
	}

	@Test
	public void testDaemonThreadFactoryCreatesDaemonThread() {
		IncrementalSimonsPurger.DaemonThreadFactory threadFactory = new IncrementalSimonsPurger.DaemonThreadFactory();
		Runnable runnable = mock(Runnable.class);
		Thread thread = threadFactory.newThread(runnable);
		Assert.assertTrue(thread.isDaemon());
	}

	@Test
	public void testDaemonThreadFactorySetName() {
		IncrementalSimonsPurger.DaemonThreadFactory threadFactory = new IncrementalSimonsPurger.DaemonThreadFactory();
		Runnable runnable = mock(Runnable.class);
		Thread thread = threadFactory.newThread(runnable);
		Assert.assertEquals(thread.getName(), "javasimon-simonsPurger-1");
	}


	@Test
	public void testDaemonThreadFactoryThreadNameChanges() {
		IncrementalSimonsPurger.DaemonThreadFactory threadFactory = new IncrementalSimonsPurger.DaemonThreadFactory();
		Runnable runnable = mock(Runnable.class);

		Thread thread1 = threadFactory.newThread(runnable);
		Thread thread2 = threadFactory.newThread(runnable);

		Assert.assertEquals(thread1.getName(), "javasimon-simonsPurger-1");
		Assert.assertEquals(thread2.getName(), "javasimon-simonsPurger-2");
	}
}
