package org.javasimon.console;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.ServletException;

import org.javasimon.Simon;

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

	// mainly for IE8
	protected void dontCache() {
		getContext().getResponse().setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
		getContext().getResponse().setHeader("Pragma", "no-cache");
	}

	protected Simon findSimonByName(String name) {
		Simon simon = context.getManager().getSimon(name);
		if (simon == null) {
			try {
				final String nameDecoded = URLDecoder.decode(name, context.getCharacterEncoding());
				if (!name.equals(nameDecoded)) {
					simon = context.getManager().getSimon(nameDecoded);
				}
			} catch (UnsupportedEncodingException e) {
				// pass
			}
		}
		return simon;
	}
}
