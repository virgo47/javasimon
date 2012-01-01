package org.javasimon.console.action;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
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
		getContext().setContentType("text/html");
		PrintWriter writer = getContext().getWriter();
		writer.write(
			"<html>"
			+"<head><title>Redirecting...</title></head>"
			+"<body><a href=\"");
		writer.write(target);
		writer.write(
			"\">Redirecting...</a></body>"
			+"</html>");
		getContext().getResponse().setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		getContext().getResponse().setHeader("Location", target);
	}
}
