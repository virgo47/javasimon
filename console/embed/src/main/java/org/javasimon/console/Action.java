package org.javasimon.console;

import java.io.IOException;
import javax.servlet.ServletException;

/**
 * Base class for actions (controller+model)
 *
 * @author gquintana
 */
public abstract class Action {

	/**
	 * Action context (request, response & more)
	 */
	private final ActionContext context;

	public Action(ActionContext context) {
		this.context = context;
	}

	public ActionContext getContext() {
		return context;
	}

	/**
	 * Parse HTTP Request parameters and store them locally
	 */
	public void readParameters() {
	}

	/**
	 * Execute action
	 */
	public abstract void execute() throws ServletException, IOException, ActionException;
}
