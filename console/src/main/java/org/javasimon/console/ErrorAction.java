package org.javasimon.console;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author gquintana
 */
public class ErrorAction extends Action {
	private ActionException error;

	public ErrorAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public ActionException getError() {
		return error;
	}

	public void setError(ActionException error) {
		this.error = error;
	}

	@Override
	public void execute() throws ServletException, IOException, ActionException {
		getResponse().setContentType("text/plain");
		error.printStackTrace(getResponse().getWriter());
		getResponse().getWriter().flush();
	}

}
