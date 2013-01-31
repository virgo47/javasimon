package org.javasimon.console;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javasimon.Manager;
import org.javasimon.utils.SimonUtils;

/**
 * Front controller servlet of Simon Web console.
 *
 * @author gquintana
 */
@SuppressWarnings("UnusedDeclaration")
public class SimonConsoleServlet extends HttpServlet {
	/**
	 * Serial version UID since class is Serializable.
	 */
	public static final long serialVersionUID = 1L;

	/**
	 * URL Prefix init parameter name.
	 */
	public static final String URL_PREFIX_INIT_PARAMETER = "url-prefix";
	/**
	 * Plugin classes init parameter name.
	 */
	public static final String PLUGIN_CLASSES_INIT_PARAMETER = "plugin-classes";
	private SimonConsoleRequestProcessor requestProcessor;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		pickUpSharedManagerIfExists(config);

		String urlPrefix = config.getInitParameter(URL_PREFIX_INIT_PARAMETER);
		initRequestProcessor(urlPrefix);

		String pluginClasses = config.getInitParameter(PLUGIN_CLASSES_INIT_PARAMETER);
		if (pluginClasses != null) {
			requestProcessor.getPluginManager().addPlugins(pluginClasses);
		}
	}

	public void initRequestProcessor(String urlPrefix) {
		if (urlPrefix == null) {
			urlPrefix = "";
		} else {
			urlPrefix = urlPrefix.trim();
		}
		requestProcessor = new SimonConsoleRequestProcessor(urlPrefix);
		requestProcessor.initActionBindings();
	}

	public SimonConsoleRequestProcessor getRequestProcessor() {
		return requestProcessor;
	}

	private void pickUpSharedManagerIfExists(ServletConfig config) {
		Object managerObject = config.getServletContext().getAttribute(SimonUtils.MANAGER_SERVLET_CTX_ATTRIBUTE);
		if (managerObject != null && managerObject instanceof Manager) {
			requestProcessor.setManager((Manager) managerObject);
		}
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
		requestProcessor.processRequest(request, response);
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
		requestProcessor.processRequest(request, response);
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
