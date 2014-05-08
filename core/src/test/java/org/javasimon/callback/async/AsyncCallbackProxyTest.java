package org.javasimon.callback.async;

import org.javasimon.SimonUnitTest;
import org.javasimon.callback.Callback;
import org.javasimon.proxy.Delegating;
import org.javasimon.proxy.DelegatingMethodInvocation;
import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.Callable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertSame;

/**
 * Unit test for {@link org.javasimon.callback.async.AsyncCallbackProxyFactory}.
 *
 * @author gerald
 */
public class AsyncCallbackProxyTest extends SimonUnitTest {

	private Callback callbackMock;
	private Callback callbackProxy;
	private Executor executor;

	@BeforeMethod
	public void setUpMethod() throws Exception {
		callbackMock = mock(Callback.class);
		executor = mock(Executor.class);
		callbackProxy = new AsyncCallbackProxyFactory(callbackMock, executor).newProxy();
	}

	@AfterClass
	public void tearDown() {
		Executors.shutdownAsync();
	}

	/**
	 * Check that calling any method on proxy call the method on the delegate.
	 * We are not checking interaction with actual Executor to avoid possible race conditions.
	 * Instead we just check that proper method invocation was submitted to the specified Executor.
	 */
	@Test
	public void testOnMessage() throws Throwable {
		final String message = "Hello";
		callbackProxy.onManagerMessage(message);
		ArgumentCaptor<Callable> captor = ArgumentCaptor.forClass(Callable.class);
		verify(executor).execute(captor.capture());

		DelegatingMethodInvocation methodInvocation = (DelegatingMethodInvocation) captor.getValue();
		Assert.assertSame(methodInvocation.getDelegate(), callbackMock);
		Assert.assertEquals(methodInvocation.getMethod().getName(), "onManagerMessage");
		Assert.assertEquals(methodInvocation.getArgs(), new Object[] {message});
	}

	/** Checks that calling getDelegate method on proxy returns delegate implementation. */
	@Test
	public void testGetDelegate() throws Exception {
		@SuppressWarnings("unchecked")
		Callback callbackDelegate = ((Delegating<Callback>) callbackProxy).getDelegate();
		assertSame(callbackDelegate, callbackMock);
	}
}
