package org.javasimon.console;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author gquintana
 */
public class SimonConsoleServlet extends HttpServlet {
	public static final String PREFIX_URL_INIT_PARAMETER = "prefix-url";
	private String prefixUrl;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		prefixUrl = config.getInitParameter(PREFIX_URL_INIT_PARAMETER);
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
		throws ServletException, IOException
	{
		String path = request.getRequestURI().substring(request.getContextPath().length() + prefixUrl.length());
		Action action;
		try {
			if (path.equals("/")) {
				action = createResourceAction(request, response, ResourceAction.PREFIX + "/index.html");
			} else if (path.endsWith("*.html")) {
				action = createResourceAction(request, response, ResourceAction.PREFIX + path);
			} else if (path.startsWith(ResourceAction.PREFIX)) {
				action = createResourceAction(request, response, path);
			} else if (path.equals(DataJsonAction.PATH)) {
				action = new DataJsonAction(request, response);
			} else {
				throw new ActionException("No action for path " + path);
			}
			action.readParameters();
			action.execute();
		} catch (ActionException actionException) {
			try {
				ErrorAction errorAction = new ErrorAction(request, response);
				errorAction.setError(actionException);
				errorAction.execute();
			} catch (ActionException actionException1) {
				throw new ServletException(actionException);
			}
		}
	}

	private ResourceAction createResourceAction(HttpServletRequest request, HttpServletResponse response, String path) {
		ResourceAction resourceAction = new ResourceAction(request, response);
		resourceAction.setPath(path);
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
		throws ServletException, IOException
	{
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
		throws ServletException, IOException
	{
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
