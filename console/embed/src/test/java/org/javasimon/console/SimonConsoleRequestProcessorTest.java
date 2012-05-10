package org.javasimon.console;

import java.io.IOException;
import javax.servlet.ServletException;
import static org.testng.Assert.*;
import org.testng.annotations.Test;
/**
 * Unit test for {@link SimonConsoleRequestProcessor}.
 * @author gquintana
 */
public class SimonConsoleRequestProcessorTest {
	public static class FooAction extends Action {
		public static boolean executed=false;
		public static String path=null;
		public FooAction(ActionContext context) {
			super(context);
		}
		@Override
		public void execute() throws ServletException, IOException, ActionException {
			executed=true;
			path=getContext().getPath();
		}
	}
	public static class BarAction extends Action {
		public BarAction(ActionContext context) {
			super(context);
		}
		@Override
		public void execute() throws ServletException, IOException, ActionException {
		}
	}
	@Test
	public void testSimple() throws Exception {
		SimonConsoleRequestProcessor requestProcessor=new SimonConsoleRequestProcessor("/prefix");
		requestProcessor.addSimpleActionBinding("/bar.html", BarAction.class);
		requestProcessor.addSimpleActionBinding("/foo.html", FooAction.class);
		// Run BarAction
		FooAction.executed=false;
		requestProcessor.processContext(new TestActionContext("/bar.html"));
		assertFalse(FooAction.executed);
		// Run FooAction
		requestProcessor.processContext(new TestActionContext("/foo.html"));
		assertTrue(FooAction.executed);
		assertEquals(FooAction.path,"/foo.html");
	}
	@Test
	public void testError() throws Exception {
		SimonConsoleRequestProcessor requestProcessor=new SimonConsoleRequestProcessor("/prefix");
		requestProcessor.addSimpleActionBinding("/foo.html", FooAction.class);
		// Run BarAction
		FooAction.executed=false;
		requestProcessor.processContext(new TestActionContext("/error.html"));
		assertFalse(FooAction.executed);
	}
}
