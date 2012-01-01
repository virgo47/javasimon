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
 * @author gquintana
 */
public class SimonConsoleServlet extends HttpServlet {

	public static final String PREFIX_URL_INIT_PARAMETER = "prefix-url";
	private String prefixUrl;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		prefixUrl = config.getInitParameter(PREFIX_URL_INIT_PARAMETER);
		if (prefixUrl==null) {
			prefixUrl="";
		} else {
			prefixUrl=prefixUrl.trim();
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
		String path = request.getRequestURI().substring(request.getContextPath().length() + prefixUrl.length());
		ActionContext actionContext = new ActionContext(request, response, path);
		Action action = null;
		try {
			if (path.equals("/")) {
				action = createResourceAction(actionContext, ResourceAction.PREFIX + "/index.html");
			} else if (path.endsWith("*.html")) {
				action = createResourceAction(actionContext, ResourceAction.PREFIX + path);
			} else if (path.startsWith(ResourceAction.PREFIX)) {
				action = createResourceAction(actionContext, path);
			} else if (path.equals(TableJsonAction.PATH)) {
				TableJsonAction dataTableJsonAction = new TableJsonAction(actionContext);
				action = dataTableJsonAction;
			} else if (path.equals(ListJsonAction.PATH)) {
				ListJsonAction dataTableJsonAction = new ListJsonAction(actionContext);
				action = dataTableJsonAction;
			} else if (path.equals(TreeJsonAction.PATH)) {
				TreeJsonAction dataTreeJsonAction = new TreeJsonAction(actionContext);
				action = dataTreeJsonAction;
			} else if (path.equals(TableHtmlAction.PATH)) {
				TableHtmlAction dataTableHtmlAction = new TableHtmlAction(actionContext);
				action = dataTableHtmlAction;
			} else if (path.equals(TableCsvAction.PATH)) {
				TableCsvAction dataTableCsvAction = new TableCsvAction(actionContext);
				action = dataTableCsvAction;
			} else if (path.equals(TreeXmlAction.PATH)) {
				TreeXmlAction treeXmlAction = new TreeXmlAction(actionContext);
				action = treeXmlAction;
			} else {
				throw new ActionException("No action for path " + path);
			}
			action.readParameters();
			action.execute();
		} catch (ActionException actionException) {
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
