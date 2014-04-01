package org.javasimon;

import org.mockito.ArgumentMatcher;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
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

	/*
	@DataProvider(name = "managersDataProvider")
	public Object[][] managersDataProvider() {
		return new Object[][] {
			{new EnabledManager(), IncrementalSimonsPurger.ENABLED_MANAGER_PURGER},
			{new SwitchingManager(), IncrementalSimonsPurger.SWITCH_MANAGER_PURGER},
			{new DisabledManager(), IncrementalSimonsPurger.DISABLED_MANAGER_PURGER}
		};
	}

	@Test(dataProvider = "managersDataProvider")
	public void testPeriodicalIncrementalSimonsPurge(Manager manager) {
		IncrementalSimonsPurger incrementalSimonsPurger = new IncrementalSimonsPurger(manager, executorService);

		long duration = 1;
		TimeUnit timeUnit = TimeUnit.SECONDS;
		incrementalSimonsPurger.start(duration, timeUnit);

		verify(executorService).scheduleWithFixedDelay(
				argThat(new PurgerRunnableMatcher(manager, expectedManagerPurger)), eq(duration), eq(duration), eq(timeUnit));
	}
	*/

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

	/*
	@Test
	public void testPurgerRunnable() {
		Manager manager = mock(Manager.class);
		IncrementalSimonsPurger.ManagerPurger managerPurger = mock(IncrementalSimonsPurger.ManagerPurger.class);
		IncrementalSimonsPurger.PurgerRunnable runnable = new IncrementalSimonsPurger.PurgerRunnable(manager, managerPurger);

		runnable.run();

		verify(managerPurger).purgeManager(same(manager), anyLong());
	}
	*/

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

	/*
	private class PurgerRunnableMatcher extends ArgumentMatcher<Runnable> {

		private final Manager expectedManager;
		private final IncrementalSimonsPurger.ManagerPurger expectedManagerPurger;

		public PurgerRunnableMatcher(Manager expectedManager, IncrementalSimonsPurger.ManagerPurger managerPurger) {
			this.expectedManager = expectedManager;
			this.expectedManagerPurger = managerPurger;
		}

		@Override
		public boolean matches(Object o) {
			IncrementalSimonsPurger.PurgerRunnable purger = (IncrementalSimonsPurger.PurgerRunnable) o;

			return purger.getManager() == expectedManager && purger.getManagerPurger() == expectedManagerPurger;
		}
	}
	*/
}
