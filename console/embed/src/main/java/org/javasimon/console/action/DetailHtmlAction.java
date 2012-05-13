package org.javasimon.console.action;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletException;
import org.javasimon.Simon;
import org.javasimon.console.*;
import org.javasimon.console.text.StringifierFactory;

/**
 * Export single Simon data as static HTML for printing purposes.
 * Path: http://.../data/detail.html?name=o.j...SimonName&timeFormat=MILLISECOND
 * @author gquintana
 */
public class DetailHtmlAction extends Action {
	/**
	 * HTTP Request path
	 */
	public static final String PATH="/data/detail.html";
	/**
	 * Value formatter
	 */
	protected StringifierFactory stringifierFactory;

	/**
	 * Simon name
	 */
	private String name;
	
	/**
	 * Constructor.
	 */
	public DetailHtmlAction(ActionContext context) {
		super(context);
		this.stringifierFactory = new StringifierFactory();
	}

	@Override
	public void readParameters() {
		name=getContext().getParameterAsString("name", null);
		stringifierFactory.init(getContext().getParameterAsEnum("timeFormat", TimeFormatType.class, TimeFormatType.MILLISECOND),
			StringifierFactory.READABLE_DATE_PATTERN,
			StringifierFactory.READABLE_NUMBER_PATTERN);
	}
	private static class LocalHtmlBuilder extends HtmlBuilder<LocalHtmlBuilder> {
		public LocalHtmlBuilder(Writer writer, StringifierFactory stringifierFactory) {
			super(writer, stringifierFactory);
		}
		public LocalHtmlBuilder beginSection(String id) throws IOException {
			return begin("div",id,"sectionPanel").begin("table",null,"sectionTable");
		}
		public LocalHtmlBuilder endSection() throws IOException {
			return end("table").end("div");
		}
		public LocalHtmlBuilder beginRow() throws IOException {
			return begin("tr");
		}
		public LocalHtmlBuilder endRow() throws IOException {
			return end("tr");
		}
		public LocalHtmlBuilder labelCell(String propertyLabel) throws IOException {
			return begin("td class=\"label\"").text(propertyLabel).end("td");
		}
		public LocalHtmlBuilder beginValueCell() throws IOException {
			return beginValueCell("");
		}
		public LocalHtmlBuilder beginValueCell(String extraAttrs) throws IOException {
			return begin("td class=\"value\""+extraAttrs);
		}
		public LocalHtmlBuilder endValueCell() throws IOException {
			return end("td");
		}
		public LocalHtmlBuilder simonProperty(Simon simon, String propertyLabel, String propertyName) throws IOException {
			return labelCell(propertyLabel)
				.beginValueCell().simonProperty(simon, propertyName).endValueCell();
		}
		public LocalHtmlBuilder simonProperty(Simon simon, String propertyLabel, String propertyName, Integer colSpan) throws IOException {
			return labelCell(propertyLabel)
				.beginValueCell(" colspan=\""+colSpan+"\"").simonProperty(simon, propertyName).endValueCell();
		}
	}
	@Override
	public void execute() throws ServletException, IOException, ActionException {
		// Check arguments
		if (name==null) {
			throw new ActionException("Null name");
		}
		Simon simon=getContext().getManager().getSimon(name);
		if (simon==null) {
			throw new ActionException("Simon \""+name+"\" not found");
		}
		getContext().setContentType("text/html");
		SimonType simonType=SimonTypeFactory.getValueFromInstance(simon);
		LocalHtmlBuilder htmlBuilder=new LocalHtmlBuilder(getContext().getWriter(), stringifierFactory);
		// Page header
		htmlBuilder.header("Detail View")
		// Common Simon section
		.beginSection("simonPanel")
			.beginRow()
				.simonProperty(simon, "Name", "name", 5)
			.endRow()
			.beginRow()
				.labelCell("Type")
					.beginValueCell().simonTypeImg(simonType,"../../").object(simonType).endValueCell()
				.simonProperty(simon, "State", "state")
				.simonProperty(simon, "Enabled", "enabled")
			.endRow()
			.beginRow()
				.simonProperty(simon, "Note", "note", 5)
			.endRow()
			.beginRow()
				.simonProperty(simon, "First Use", "firstUsage")
				.simonProperty(simon, "Last Reset", "lastReset")
				.simonProperty(simon, "Last Use", "lastUsage")
			.endRow()
		.endSection();
		// Specific Stopwatch/Counter section
		switch(simonType) {
			case STOPWATCH:
				htmlBuilder.beginSection("stopwatchPanel")
					.beginRow()
						.simonProperty(simon, "Counter", "counter")
						.simonProperty(simon, "Total", "total")
					.endRow()
					.beginRow()
						.simonProperty(simon, "Min", "min")
						.simonProperty(simon, "Min Timestamp", "minTimeStamp")
					.endRow()
					.beginRow()
						.simonProperty(simon, "Mean", "mean")
						.simonProperty(simon, "Standard Deviation", "standardDeviation")
					.endRow()
					.beginRow()
						.simonProperty(simon, "Max", "max")
						.simonProperty(simon, "Max Timestamp", "maxTimeStamp")
					.endRow()
					.beginRow()
						.simonProperty(simon, "Last", "last")
						.simonProperty(simon, "Last Timestamp", "lastUsage")
					.endRow()
					.beginRow()
						.simonProperty(simon, "Max Active", "maxActive")
						.simonProperty(simon, "Max Active Timestamp", "maxActiveTimestamp")
					.endRow()
				.endSection();
				break;
			case COUNTER:
				htmlBuilder.beginSection("counterPanel")
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
		// Page footer
		htmlBuilder.footer();
	}

	
}
