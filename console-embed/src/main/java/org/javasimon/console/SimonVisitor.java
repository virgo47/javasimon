package org.javasimon.console;

import java.io.IOException;
import org.javasimon.Simon;

/**
 * Callback interface used when visiting Simon manager and it Simons.
 *
 * @see SimonVisitors
 * @author gquintana
 */
public interface SimonVisitor {

	void visit(Simon simon) throws IOException;
}
