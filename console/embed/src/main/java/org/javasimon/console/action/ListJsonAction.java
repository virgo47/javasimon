package org.javasimon.console.action;

import java.io.IOException;
import javax.servlet.ServletException;

import org.javasimon.Simon;
import org.javasimon.console.*;
import org.javasimon.console.json.ArrayJS;
import org.javasimon.console.json.ObjectJS;

/**
 * Export Simons as a flat JSON array.
 * All attributes of Simons are exported.
 *
 * @author gquintana
 */
public class ListJsonAction extends AbstractJsonAction {

	public static final String PATH = "/data/list.json";

	/**
	 * Pattern for Simon name filtering.
	 */
	private String pattern;

	/**
	 * Type for Simon type filtering.
	 */
	private SimonType type;

	public ListJsonAction(ActionContext context) {
		super(context);
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public void readParameters() {
		super.readParameters();
		pattern = getContext().getParameterAsString("pattern", null);
		type = getContext().getParameterAsEnum("type", SimonType.class, null);
	}

	@Override
	public void execute() throws ServletException, IOException, ActionException {
		getContext().setContentType("application/json");
		ArrayJS arrayJS = new ArrayJS();
		SimonVisitors.visitList(getContext().getManager(), pattern, type, new SimonVisitorImpl(arrayJS));
		arrayJS.write(getContext().getWriter());
	}

	private class SimonVisitorImpl implements SimonVisitor {

		private final ArrayJS arrayJS;

		public SimonVisitorImpl(ArrayJS arrayJS) {
			this.arrayJS = arrayJS;
		}

		public void visit(Simon simon) throws IOException {
			ObjectJS objectJS = createObjectJS(simon);
			arrayJS.addElement(objectJS);
		}
	}
}
