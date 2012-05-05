package org.javasimon.console;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.console.action.*;

/**
 * Common part processing the request for {@link SimonConsoleServlet} or {@link SimonConsoleFilter}.
 *
 * @author virgo47@gmail.com
 */
class SimonConsoleRequestProcessor {
	/**
	 * Root page path.
	 */
	public static final String ROOT_PATH = "/index.html";

	/**
	 * Tree page path.
	 */
	public static final String TREE_PATH = "/tree.html";

	/**
	 * URL Prefix - set by init parameters in {@code web.xml} (for filter or servlet).
	 */
	private String urlPrefix;

	/**
	 * Simon manager to use.
	 */
	private Manager manager = SimonManager.manager();

	SimonConsoleRequestProcessor(String urlPrefix) {
		if (urlPrefix == null) {
			this.urlPrefix = "";
		} else {
			this.urlPrefix = urlPrefix.trim();
		}
	}

	/**
	 * Processes requests for both HTTP {@code GET} and {@code POST} methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws javax.servlet.ServletException if a servlet-specific error occurs
	 * @throws java.io.IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		String path = request.getRequestURI().substring(request.getContextPath().length() + urlPrefix.length());
		ActionContext actionContext = new ActionContext(request, response, path);
		actionContext.setManager(manager);
		Action action;
		try {
			// Create action corresponding to request path
			if (path.equals("")) {
				action = new RedirectAction(request.getContextPath() + urlPrefix + ROOT_PATH, actionContext);
			} else if (path.equals("/") || path.equals(ROOT_PATH)) {
				action = createResourceAction(actionContext, ResourceAction.PREFIX + ROOT_PATH);
			} else if (path.equals(TREE_PATH)) {
				action = createResourceAction(actionContext, ResourceAction.PREFIX + TREE_PATH);
			} else if (path.startsWith(ResourceAction.PREFIX)) {
				action = createResourceAction(actionContext, path);
			} else if (path.equals(TableJsonAction.PATH)) {
				action = new TableJsonAction(actionContext);
			} else if (path.equals(ListJsonAction.PATH)) {
				action = new ListJsonAction(actionContext);
			} else if (path.equals(TreeJsonAction.PATH)) {
				action = new TreeJsonAction(actionContext);
			} else if (path.equals(TableHtmlAction.PATH)) {
				action = new TableHtmlAction(actionContext);
			} else if (path.equals(TableCsvAction.PATH)) {
				action = new TableCsvAction(actionContext);
			} else if (path.equals(TreeXmlAction.PATH)) {
				action = new TreeXmlAction(actionContext);
			} else if (path.equals(ResetAction.PATH)) {
				action = new ResetAction(actionContext);
			} else if (path.equals(ClearAction.PATH)) {
				action = new ClearAction(actionContext);
			} else {
				throw new ActionException("No action for path " + path);
			}
			// Read request parameters
			action.readParameters();
			// Execute action (generate response)
			action.execute();
		} catch (ActionException actionException) {
			// Handle action errors
			try {
				ErrorAction errorAction = new ErrorAction(actionContext);
				errorAction.setError(actionException);
				errorAction.execute();
			} catch (ActionException actionException1) {
				throw new ServletException(actionException);
			}
		}
	}

	private ResourceAction createResourceAction(ActionContext actionContext, String path) {
		actionContext.setPath(path);
		return new ResourceAction(actionContext);
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}
}
