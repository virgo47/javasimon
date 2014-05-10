package org.javasimon.console;

import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.console.action.ClearAction;
import org.javasimon.console.action.DetailHtmlAction;
import org.javasimon.console.action.DetailJsonAction;
import org.javasimon.console.action.ErrorAction;
import org.javasimon.console.action.ListJsonAction;
import org.javasimon.console.action.PluginsJsonAction;
import org.javasimon.console.action.RedirectAction;
import org.javasimon.console.action.ResourceAction;
import org.javasimon.console.action.TableCsvAction;
import org.javasimon.console.action.TableHtmlAction;
import org.javasimon.console.action.TableJsonAction;
import org.javasimon.console.action.TreeJsonAction;
import org.javasimon.console.action.TreeXmlAction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Common part processing the request for {@link SimonConsoleServlet} or {@link SimonConsoleFilter}.
 *
 * @author virgo47@gmail.com
 * @author gquintana
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
	 * Tree page path.
	 */
	public static final String DETAIL_PATH = "/detail.html";

	/**
	 * URL Prefix - set by init parameters in {@code web.xml} (for filter or servlet).
	 */
	private String urlPrefix;

	/**
	 * Simon manager to use.
	 */
	private Manager manager = SimonManager.manager();

	/**
	 * List of action bindings.
	 */
	private final List<ActionBinding> actionBindings = new ArrayList<>();

	/**
	 * Plugin manager.
	 */
	private final SimonConsolePluginManager pluginManager = new SimonConsolePluginManager();

	public SimonConsoleRequestProcessor(String urlPrefix) {
		if (urlPrefix == null) {
			this.urlPrefix = "";
		} else {
			this.urlPrefix = urlPrefix.trim();
		}
	}

	/**
	 * Add an action binding to the {@link #actionBindings} list.
	 *
	 * @param actionBinding Action binding to add
	 */
	public final void addActionBinding(ActionBinding actionBinding) {
		this.actionBindings.add(actionBinding);
	}

	/**
	 * Add a simple action binding to the {@link #actionBindings} list.
	 *
	 * @param actionPath Path of the action
	 * @param actionClass Class of the action
	 */
	public final <T extends Action> void addSimpleActionBinding(String actionPath, Class<T> actionClass) {
		this.addActionBinding(new SimpleActionBinding<>(actionPath, actionClass));
	}

	/**
	 * Add a resource action binding to the {@link #actionBindings} list.
	 *
	 * @param actionPath Path of the action
	 * @param resourcePath Path of a resource located under
	 */
	public void addResourceActionBinding(final String actionPath, final String resourcePath) {
		this.addActionBinding(new ActionBinding() {
			public boolean supports(ActionContext actionContext) {
				return actionContext.getPath().equals(actionPath);
			}

			public Action create(ActionContext actionContext) {
				return new ResourceAction(actionContext, resourcePath);
			}
		});
	}

	/**
	 * Find an acttion binding for the given action context
	 *
	 * @return Found Action binding , null if any.
	 */
	protected final ActionBinding findActionBinding(ActionContext actionContext) {
		for (ActionBinding actionBinding : this.actionBindings) {
			if (actionBinding.supports(actionContext)) {
				return actionBinding;
			}
		}
		return null;
	}

	protected void initActionBindings() {
		// /console is redirected to /console/index.html
		addActionBinding(new ActionBinding<Action>() {
			public boolean supports(ActionContext actionContext) {
				return actionContext.getPath().isEmpty();
			}

			public Action create(ActionContext actionContext) {
				return new RedirectAction(actionContext.getRequest().getContextPath() + urlPrefix + ROOT_PATH, actionContext);
			}
		});
		// /console/ and /console/index.html load resource/index.html
		addResourceActionBinding("/", ROOT_PATH);
		addResourceActionBinding(ROOT_PATH, ROOT_PATH);
		// /console/tree.html loads resource/tree.html
		addResourceActionBinding(TREE_PATH, TREE_PATH);
		addResourceActionBinding(DETAIL_PATH, DETAIL_PATH);
		// /resource/* loads resource/*
		addActionBinding(new ActionBinding<Action>() {
			private final String pathPrefix = "/resource";

			public boolean supports(ActionContext actionContext) {
				return actionContext.getPath().startsWith(pathPrefix);
			}

			public Action create(ActionContext actionContext) {
				return new ResourceAction(actionContext, actionContext.getPath().substring(pathPrefix.length()));
			}
		});
		addSimpleActionBinding(TableJsonAction.PATH, TableJsonAction.class);
		addSimpleActionBinding(ListJsonAction.PATH, ListJsonAction.class);
		addSimpleActionBinding(TreeJsonAction.PATH, TreeJsonAction.class);
		addSimpleActionBinding(TableJsonAction.PATH, TableJsonAction.class);
		addSimpleActionBinding(TableHtmlAction.PATH, TableHtmlAction.class);
		addSimpleActionBinding(TableCsvAction.PATH, TableCsvAction.class);
		addSimpleActionBinding(TreeXmlAction.PATH, TreeXmlAction.class);
		addSimpleActionBinding(ClearAction.PATH, ClearAction.class);
		addSimpleActionBinding(DetailHtmlAction.PATH, DetailHtmlAction.class);
		addSimpleActionBinding(DetailJsonAction.PATH, DetailJsonAction.class);
		addSimpleActionBinding(PluginsJsonAction.PATH, PluginsJsonAction.class);
		for (ActionBinding actionBinding : pluginManager.getActionBindings()) {
			addActionBinding(actionBinding);
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
		throws ServletException, IOException
	{
		String path = request.getRequestURI().substring(request.getContextPath().length() + urlPrefix.length());
		ActionContext actionContext = new ActionContext(request, response, path);
		actionContext.setManager(manager);
		actionContext.setPluginManager(pluginManager);
		processContext(actionContext);
	}

	/**
	 * Process an HTTP request.
	 *
	 * @param actionContext Action context (wrapping HTTP request and response)
	 */
	protected void processContext(ActionContext actionContext) throws ServletException, IOException {
		Action action = null;
		try {
			// Find action binding
			ActionBinding actionBinding = findActionBinding(actionContext);
			// Create action
			if (actionBinding != null) {
				action = actionBinding.create(actionContext);
			}
			if (action == null) {
				throw new ActionException("No action bound to path " + actionContext.getPath());
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

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	public SimonConsolePluginManager getPluginManager() {
		return pluginManager;
	}

}
