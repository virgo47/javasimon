package org.javasimon.console.action;

import org.javasimon.Simon;
import org.javasimon.console.Action;
import org.javasimon.console.ActionContext;
import org.javasimon.console.ActionException;
import org.javasimon.console.SimonType;
import org.javasimon.console.SimonTypeFactory;
import org.javasimon.console.TimeFormatType;
import org.javasimon.console.text.StringifierFactory;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 * Export single Simon data as static HTML for printing purposes.
 * Path: http://.../data/detail.html?name=o.j...SimonName&timeFormat=MILLISECOND
 *
 * @author gquintana
 */
public class DetailHtmlAction extends Action {

	/** URI for detail action. */
	public static final String PATH = "/data/detail.html";
	/** Value formatter. */
	protected StringifierFactory stringifierFactory;
	/** Simon name. */
	private String name;

	/** Constructor. */
	public DetailHtmlAction(ActionContext context) {
		super(context);
		this.stringifierFactory = new StringifierFactory();
	}

	@Override
	public void readParameters() {
		name = getContext().getParameterAsString("name", null);
		stringifierFactory.init(getContext().getParameterAsEnum("timeFormat", TimeFormatType.class, TimeFormatType.MILLISECOND),
			StringifierFactory.READABLE_DATE_PATTERN,
			StringifierFactory.READABLE_NUMBER_PATTERN);
	}

	@Override
	public void execute() throws ServletException, IOException, ActionException {
		// Check arguments
		if (name == null) {
			throw new ActionException("Null name");
		}
		Simon simon = getContext().getManager().getSimon(name);
		if (simon == null) {
			throw new ActionException("Simon \"" + name + "\" not found");
		}
		getContext().setContentType("text/html");
		SimonType simonType = SimonTypeFactory.getValueFromInstance(simon);
		DetailHtmlBuilder htmlBuilder = new DetailHtmlBuilder(getContext().getWriter(), stringifierFactory);
		// Page header
		htmlBuilder.header("Detail View", DetailPlugin.getResources(getContext(), DetailPlugin.class))
			// Common Simon section
			.beginSection("simonPanel", "Simon")
			.beginRow()
			.simonProperty(simon, "Name", "name", 5)
			.endRow()
			.beginRow()
			.labelCell("Type")
			.beginValueCell().simonTypeImg(simonType, "../../").object(simonType).endValueCell()
			.simonProperty(simon, "State", "state")
			.simonProperty(simon, "Enabled", "enabled")
			.endRow()
			.beginRow()
			.simonProperty(simon, "Note", "note", 5)
			.endRow()
			.beginRow()
			.simonProperty(simon, "First Use", "firstUsage")
			.simonProperty(simon, "Last Use", "lastUsage")
			.endRow()
			.endSection();
		// Specific Stopwatch/Counter section
		switch (simonType) {
			case STOPWATCH:
				htmlBuilder.beginSection("stopwatchPanel", "Stopwatch")
					.beginRow()
					.simonProperty(simon, "Counter", "counter")
					.simonProperty(simon, "Total", "total", 3)
					.endRow()
					.beginRow()
					.simonProperty(simon, "Min", "min", 3)
					.simonProperty(simon, "Min Timestamp", "minTimeStamp")
					.endRow()
					.beginRow()
					.simonProperty(simon, "Mean", "mean")
					.simonProperty(simon, "Standard Deviation", "standardDeviation", 3)
					.endRow()
					.beginRow()
					.simonProperty(simon, "Max", "max", 3)
					.simonProperty(simon, "Max Timestamp", "maxTimeStamp")
					.endRow()
					.beginRow()
					.simonProperty(simon, "Last", "last", 3)
					.simonProperty(simon, "Last Timestamp", "lastUsage")
					.endRow()
					.beginRow()
					.simonProperty(simon, "Active", "active")
					.simonProperty(simon, "Max Active", "maxActive")
					.simonProperty(simon, "Max Active Timestamp", "maxActiveTimestamp")
					.endRow()
					.endSection();
				break;
			case COUNTER:
				htmlBuilder.beginSection("counterPanel", "Counter")
					.beginRow()
					.simonProperty(simon, "Counter", "counter")
					.endRow()
					.beginRow()
					.simonProperty(simon, "Min", "min")
					.simonProperty(simon, "Min Timestamp", "minTimeStamp")
					.endRow()
					.beginRow()
					.simonProperty(simon, "Max", "max")
					.simonProperty(simon, "Max Timestamp", "maxTimeStamp")
					.endRow()
					.beginRow()
					.simonProperty(simon, "Increment Sum", "incrementSum")
					.simonProperty(simon, "Decrement Sum", "decrementSum")
					.endRow()
					.endSection();
				break;
		}
		// Plugins
		for (DetailPlugin plugin : getContext().getPluginManager().getPluginsByType(DetailPlugin.class)) {
			if (plugin.supports(simon)) {
				htmlBuilder.beginSection(plugin.getId() + "Panel", plugin.getLabel());
				plugin.executeHtml(getContext(), htmlBuilder, stringifierFactory, simon);
				htmlBuilder.endSection();
			}
		}
		// Page footer
		htmlBuilder.footer();
	}
}
