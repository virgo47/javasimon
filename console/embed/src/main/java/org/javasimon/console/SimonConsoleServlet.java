package org.javasimon.console;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.javasimon.console.action.*;

/**
 * Front controller servlet of Simon Web console
 *
 * @author gquintana
 */
public class SimonConsoleServlet extends HttpServlet {
	/**
	 * Serial version UID since class is Serializable
	 */
	public static final long serialVersionUID=1L;
	/**
	 * URL Prefix init parameter name
	 */
	public static final String URL_PREFIX_INIT_PARAMETER = "url-prefix";
	/**
	 * Root page path
	 */
	public static final String ROOT_PATH = "/index.html";
	/**
	 * Tree page path
	 */
	public static final String TREE_PATH = "/tree.html";
	/**
	 * URL Prefix.
	 * Set with init parameters
	 */
	private String urlPrefix;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		urlPrefix = config.getInitParameter(URL_PREFIX_INIT_PARAMETER);
		if (urlPrefix == null) {
			urlPrefix = "";
		} else {
			urlPrefix = urlPrefix.trim();
		}
	}
	/**
	 * Processes requests for both HTTP
	 * <code>GET</code> and
	 * <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		String path = request.getRequestURI().substring(request.getContextPath().length() + urlPrefix.length());
		ActionContext actionContext = new ActionContext(request, response, path);
		Action action = null;
		try {
			// Create action corresponding to request path
			if (path.equals("")) {
				action = new RedirectAction(request.getContextPath() + urlPrefix + ROOT_PATH, actionContext);
			} else if (path.equals("/")||path.equals(ROOT_PATH)) {
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
		ResourceAction resourceAction = new ResourceAction(actionContext);
		return resourceAction;
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP
	 * <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP
	 * <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}
