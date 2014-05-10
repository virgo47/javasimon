package org.javasimon.console.action;

import org.javasimon.console.Action;
import org.javasimon.console.ActionContext;
import org.javasimon.console.ActionException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 * Action used to send resources (images, JS, CSS) to the browser.
 *
 * @author gquintana
 */
public class ResourceAction extends Action {

	public static final String PREFIX = "/resource";

	private static final Map<String, String> CONTENT_TYPES = new HashMap<>();

	static {
		CONTENT_TYPES.put("gif", "image/gif");
		CONTENT_TYPES.put("png", "image/png");
		CONTENT_TYPES.put("jpg", "image/jpeg");
		CONTENT_TYPES.put("html", "text/html");
		CONTENT_TYPES.put("css", "text/css");
		CONTENT_TYPES.put("js", "text/javascript");
	}

	/**
	 * Relative path of the resource to return.
	 */
	private final String resourcePath;

	/**
	 * Constructor.
	 *
	 * @param context Action context
	 * @param resourcePath Local path of the resource
	 */
	public ResourceAction(ActionContext context, String resourcePath) {
		super(context);
		if (resourcePath.startsWith("/")) {
			this.resourcePath = resourcePath;
		} else {
			this.resourcePath = "/" + resourcePath;
		}
	}

	@Override
	public void execute() throws ServletException, IOException, ActionException {
		InputStream resourceIStream = null;
		try {
			resourceIStream = getClass().getResourceAsStream("/org/javasimon/console/resource" + resourcePath);
			if (resourceIStream == null) {
				getContext().getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
				throw new ActionException("Resource " + resourcePath + " not found");
			}
			String extension = resourcePath.substring(resourcePath.lastIndexOf('.') + 1).toLowerCase();
			String contentType = CONTENT_TYPES.get(extension);
			if (contentType != null) {
				getContext().setContentType(contentType);
				if (contentType.startsWith("text")) {
					getContext().getResponse().setCharacterEncoding("UTF-8");
				}
			}
			getContext().getResponse().setHeader("Cache-Control", "public; max-age=300");
			copyStream(resourceIStream);
		} finally {
			if (resourceIStream != null) {
				resourceIStream.close();
			}
		}
	}

	/**
	 * Copy resource input stream to HTTP response output stream using
	 * a 64kib buffer
	 */
	private void copyStream(InputStream inputStream) throws IOException {
		OutputStream outputStream = null;
		try {
			outputStream = getContext().getOutputStream();
			byte[] buffer = new byte[65535];
			int bufferLen;
			int totalLen = 0;
			while ((bufferLen = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, bufferLen);
				totalLen += bufferLen;
			}
			getContext().getResponse().setContentLength(totalLen);
		} finally {
			if (outputStream != null) {
				outputStream.flush();
			}
		}
	}
}
