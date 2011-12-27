package org.javasimon.console;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author gquintana
 */
public class ResourceAction extends Action {
	public static final String PREFIX = "/resource";
	private String path;
	private static final Map<String, String> CONTENT_TYPES = new HashMap<String, String>();

	static {
		CONTENT_TYPES.put("gif", "image/gif");
		CONTENT_TYPES.put("png", "image/png");
		CONTENT_TYPES.put("jpg", "image/jpeg");
		CONTENT_TYPES.put("html", "text/html");
		CONTENT_TYPES.put("css", "text/css");
		CONTENT_TYPES.put("js", "text/javascript");
	}

	public ResourceAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public void execute() throws ServletException, IOException, ActionException {
		InputStream resourceIStream = null;
		try {
			String resourceName = getPath().substring(1);
			String resourcePath = "/" + getClass().getPackage().getName().replace('.', '/') + "/" + resourceName;
			resourceIStream = getClass().getResourceAsStream(resourcePath);
			if (resourceIStream == null) {
				throw new ActionException("Resource " + resourceName + " not found");
			}
			String extension = resourceName.substring(resourceName.lastIndexOf('.') + 1).toLowerCase();
			String contentType = CONTENT_TYPES.get(extension);
			if (contentType != null) {
				getResponse().setContentType(contentType);
				if (contentType.startsWith("text")) {
					getResponse().setCharacterEncoding("UTF-8");
				}
			}
			getResponse().setHeader("Cache-Control", "public; max-age=300");
//            forwardTo("/WEB-INF/classes"+resourcePath);
			copyStream(resourceIStream);
		} finally {
			if (resourceIStream != null) {
				resourceIStream.close();
			}
		}
	}

	private void copyStream(InputStream inputStream) throws IOException {
		OutputStream outputStream = null;
		try {
			outputStream = getResponse().getOutputStream();
			byte[] buffer = new byte[65535];
			int bufferLen;
			int totalLen = 0;
			while ((bufferLen = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, bufferLen);
				totalLen += bufferLen;
			}
			getResponse().setContentLength(totalLen);
		} finally {
			if (outputStream != null) {
				outputStream.flush();
			}
		}
	}
}
