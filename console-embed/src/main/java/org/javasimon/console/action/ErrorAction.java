package org.javasimon.console.action;

import org.javasimon.console.Action;
import org.javasimon.console.ActionContext;
import org.javasimon.console.ActionException;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

/**
 * Action used when an exception occurs: exception stack trace is dumped into the response.
 *
 * @author gquintana
 */
public class ErrorAction extends Action {

	/** Exception to render. */
	private ActionException error;

	public ErrorAction(ActionContext context) {
		super(context);
	}

	public ActionException getError() {
		return error;
	}

	public void setError(ActionException error) {
		this.error = error;
	}

	@Override
	public void execute() throws ServletException, IOException, ActionException {
		PrintWriter writer = null;
		try {
			getContext().setContentType("text/plain");
			writer = getContext().getWriter();
			error.printStackTrace(getContext().getWriter());
		} finally {
			if (writer != null) {
				writer.flush();
			}
		}
	}
}
