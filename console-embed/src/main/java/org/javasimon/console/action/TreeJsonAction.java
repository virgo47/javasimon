package org.javasimon.console.action;

import java.io.IOException;

import org.javasimon.Simon;
import org.javasimon.console.ActionContext;
import org.javasimon.console.json.ArrayJS;
import org.javasimon.console.json.ObjectJS;

/**
 * Export Simons as a hierarchical JSON object for display in a tree-table.
 * All attributes of simons are exported.
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

	/** @noinspection WeakerAccess*/
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
	public void execute() throws IOException {
		getContext().setContentType("application/json");
		Simon simon = name == null ? getContext().getManager().getRootSimon() : findSimonByName(name);
		ObjectJS simonRootJS = createObjectJS(simon);
		simonRootJS.write(getContext().getWriter());
	}
}
