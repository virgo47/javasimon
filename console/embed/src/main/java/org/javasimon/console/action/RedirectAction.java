package org.javasimon.console.action;

import java.io.IOException;
import javax.servlet.ServletException;
import org.javasimon.console.Action;
import org.javasimon.console.ActionContext;
import org.javasimon.console.ActionException;

/**
 * Action to send a redirect instruction to the browser
 * @author gquintana
 */
public class RedirectAction extends Action{
	/**
	 * Target URL
	 */
	private String target;
	public RedirectAction(ActionContext context) {
		super(context);
	}
	public RedirectAction(String target, ActionContext context) {
		super(context);
		this.target = target;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	
	@Override
	public void execute() throws ServletException, IOException, ActionException {
		getContext().getResponse().sendRedirect(target);
	}
}
