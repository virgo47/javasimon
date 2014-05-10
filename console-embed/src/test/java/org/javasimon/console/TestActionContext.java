package org.javasimon.console;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;

/**
 * Action context for unit testing.
 *
 *
 * @author gquintana
 */
public class TestActionContext extends ActionContext {
	private StringWriter stringWriter;
	private PrintWriter printWriter;
	private ByteArrayOutputStream byteArrayOutputStream;
	private Map<String, Object> parameters = new HashMap<>();
	private String contentType;
	public TestActionContext(String path) {
		super(mock(HttpServletRequest.class), mock(HttpServletResponse.class), path);
		setPluginManager(new SimonConsolePluginManager());
	}

	@Override
	public String getParameter(String name) {
		return (String) parameters.get(name);
	}

	public void setParameter(String name, String value) {
		parameters.put(name, value);
	}

	@Override
	protected String[] getParameters(String name) {
		return (String[]) parameters.get(name);
	}

	public void setParameters(String name, String... values) {
		parameters.put(name, values);
	}

	private void initWriter() {
		if (stringWriter == null) {
			stringWriter = new StringWriter();
		}
		if (printWriter == null) {
			printWriter = new PrintWriter(stringWriter);
		}
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		initWriter();
		return printWriter;
	}

	private void initOutputStream() {
		if (byteArrayOutputStream == null) {
			byteArrayOutputStream = new ByteArrayOutputStream();
		}
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		initOutputStream();
		return byteArrayOutputStream;
	}

	@Override
	public String toString() {
		return stringWriter.toString();
	}

	public byte[] toByteArray() {
		return byteArrayOutputStream.toByteArray();
	}

	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return this.contentType;
	}
}
