package org.javasimon.console;

import java.io.IOException;
import javax.servlet.ServletException;
import org.javasimon.console.action.DetailJsonAction;
import org.javasimon.console.action.ResourceAction;
import org.javasimon.console.action.TableJsonAction;
import org.javasimon.console.action.TreeJsonAction;
import static org.testng.Assert.*;
import org.testng.annotations.Test;
/**
 * Unit test for {@link SimonConsoleRequestProcessor}.
 * @author gquintana
 */
public class SimonConsoleRequestProcessorTest {
	private SimonConsoleRequestProcessor requestProcessor=new SimonConsoleRequestProcessor("/prefix");
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
		requestProcessor.addSimpleActionBinding("/foo.html", FooAction.class);
		// Run BarAction
		FooAction.executed=false;
		requestProcessor.processContext(new TestActionContext("/error.html"));
		assertFalse(FooAction.executed);
	}
	private void assertActionBinding(String path, Class<? extends Action> actionClass) {
		TestActionContext actionContext=new TestActionContext(path);
		assertEquals(requestProcessor.findActionBinding(actionContext).create(actionContext).getClass(), actionClass);
	}
	/**
	 * Test default action bindings
	 */
	@Test
	public void testInit() throws Exception {
		requestProcessor.initActionBindings();
		assertActionBinding("/resources/js/foo.js", ResourceAction.class);
		assertActionBinding("/data/table.json", TableJsonAction.class);
		assertActionBinding("/data/detail.json", DetailJsonAction.class);
		assertActionBinding("/data/tree.json", TreeJsonAction.class);
	}
}
