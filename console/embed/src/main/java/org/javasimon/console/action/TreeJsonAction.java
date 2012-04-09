package org.javasimon.console.action;

import java.io.IOException;
import javax.servlet.ServletException;
import org.javasimon.Simon;
import org.javasimon.console.ActionContext;
import org.javasimon.console.ActionException;
import org.javasimon.console.json.ArrayJS;
import org.javasimon.console.json.ObjectJS;

/**
 * Export Simons as a hierarchical JSON object for display in a treetable.
 * All attributes of simons are exported
 *
 * @author gquintana
 */
public class TreeJsonAction extends AbstractJsonAction {

	public static final String PATH = "/data/tree.json";
	/**
	 * Name of the simon from where to start.
	 * {@code null} means root.
	 */
	private String name;
	public TreeJsonAction(ActionContext context) {
		super(context);
	}

	@Override
	public void readParameters() {
		super.readParameters();
		name = getContext().getParameterAsString("name", null);
	}

	@Override
	protected ObjectJS createObjectJS(Simon simon) {
		ObjectJS simonJS = super.createObjectJS(simon);
		ArrayJS childrenJS = new ArrayJS();
		for (Simon child : simon.getChildren()) {
			ObjectJS childJS = createObjectJS(child);
			childrenJS.addElement(childJS);
		}
		simonJS.setAttribute("children", childrenJS);
		return simonJS;
	}

	@Override
	public void execute() throws ServletException, IOException, ActionException {
		getContext().setContentType("application/json");
		Simon simon = name == null ? getContext().getManager().getRootSimon() : getContext().getManager().getSimon(name);
		ObjectJS simonRootJS = createObjectJS(simon);
		simonRootJS.write(getContext().getWriter());
	}
}
