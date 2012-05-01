package org.javasimon.console.action;

import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import org.javasimon.Simon;
import org.javasimon.SimonManager;
import org.javasimon.console.*;

/**
 * Action to reset one or many simons
 *
 * @author gquintana
 */
public class ResetAction extends Action {
	/**
	 * URI Path
	 */
	public static final String PATH="/data/reset";
	/**
	 * Pattern for Simon name filtering
	 */
	private String pattern;
	/**
	 * Types for Simon type filtering
	 */
	private Set<SimonType> types;
	/**
	 * Name for Simon type filtering
	 */
	private String name;

	public ResetAction(ActionContext context) {
		super(context);
	}

	@Override
	public void readParameters() {
		pattern = getContext().getParameterAsString("pattern", null);
		name = getContext().getParameterAsString("name", null);
		types = getContext().getParametersAsEnums("type", SimonType.class, null);
	}

	@Override
	public void execute() throws ServletException, IOException, ActionException {
		SimonVisitorImpl visitor=new SimonVisitorImpl();
		if (name != null) {
			Simon simon = SimonManager.getSimon(name);
			if (simon == null) {
				throw new ActionException("Simon \"" + name + "\" not found");
			} else {
				visitor.visit(simon);
			}
		} else {
			SimonVisitors.visitList(getContext().getManager(), pattern, types, visitor);
		}
		getContext().getWriter().print("{count:"+visitor.getCount()+"}");
	}

	private static class SimonVisitorImpl implements SimonVisitor {
		private int count;
		@Override
		public void visit(Simon simon) throws IOException {
			simon.reset();
			count++;
		}
		public int getCount() {
			return count;
		}
	}
}
