package org.javasimon.console.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Base class for all JavaScript things
 *
 * @author gquintana
 */
public abstract class AnyJS {
	/**
	 * Renders the reponse
	 */
	public abstract void write(Writer writer) throws IOException;

	protected final void writeString(Writer writer, String string) throws IOException {
		writer.write("\"");
		writer.write(string);
		writer.write("\"");
	}

	/**
	 * Renders the reponse in a String
	 */
	@Override
	public String toString() {
		try {
			StringWriter writer = new StringWriter();
			write(writer);
			return writer.toString();
		} catch (IOException iOException) {
			return iOException.getMessage();
		}
	}
}
