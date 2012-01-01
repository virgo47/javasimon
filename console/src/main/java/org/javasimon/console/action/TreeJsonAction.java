package org.javasimon.console.action;

import java.io.IOException;
import javax.servlet.ServletException;
import org.javasimon.Simon;
import org.javasimon.SimonManager;
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

	public TreeJsonAction(ActionContext context) {
		super(context);
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
		ObjectJS simonRootJS = createObjectJS(SimonManager.manager().getRootSimon());
		simonRootJS.write(getContext().getWriter());
	}
}
