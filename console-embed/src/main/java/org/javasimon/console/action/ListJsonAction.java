package org.javasimon.console.action;

import org.javasimon.Simon;
import org.javasimon.console.ActionContext;
import org.javasimon.console.ActionException;
import org.javasimon.console.SimonType;
import org.javasimon.console.SimonVisitor;
import org.javasimon.console.SimonVisitors;
import org.javasimon.console.json.ArrayJS;
import org.javasimon.console.json.ObjectJS;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;

/**
 * Export Simons as a flat JSON array.
 * All attributes of Simons are exported.
 *
 * @author gquintana
 */
public class ListJsonAction extends AbstractJsonAction {

	public static final String PATH = "/data/list.json";

	/** Pattern for Simon name filtering. */
	private String pattern;

	/** Types for Simon type filtering. */
	private Set<SimonType> types;

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
		types = getContext().getParametersAsEnums("type", SimonType.class, null);
	}

	@Override
	public void execute() throws ServletException, IOException, ActionException {
		getContext().setContentType("application/json");
		ArrayJS arrayJS = new ArrayJS();
		SimonVisitors.visitList(getContext().getManager(), pattern, types, new SimonVisitorImpl(arrayJS));
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
