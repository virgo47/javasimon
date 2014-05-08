package org.javasimon.console.plugin;

import java.io.IOException;

import org.javasimon.Simon;
import org.javasimon.Stopwatch;
import org.javasimon.callback.timeline.StopwatchTimeRange;
import org.javasimon.callback.timeline.TimeRange;
import org.javasimon.callback.timeline.Timeline;
import org.javasimon.callback.timeline.TimelineCallback;
import org.javasimon.callback.timeline.TimelineSample;
import org.javasimon.clock.ClockUtils;
import org.javasimon.console.ActionContext;
import org.javasimon.console.SimonCallbacks;
import org.javasimon.console.action.DetailHtmlBuilder;
import org.javasimon.console.action.DetailPlugin;
import org.javasimon.console.html.HtmlResourceType;
import org.javasimon.console.json.ArrayJS;
import org.javasimon.console.json.ObjectJS;
import org.javasimon.console.text.StringifierFactory;

/**
 * Detail plugin to display {@link TimelineCallback} information
 */
public class TimelineDetailPlugin extends DetailPlugin {

	/**
	 * Message: Callback not registered
	 */
	public static final String NO_CALLBACK_MESSAGE = "Timeline callback not registered";
	/**
	 * Message: Data not found in Simon
	 */
	private static final String NO_DATA_MESSAGE = "No data available";
	/**
	 * Simon attribute name used to retrieved timeline information
	 */
	private final String attributeName;
	public TimelineDetailPlugin(String pluginId, String pluginTitle, String attributeName) {
		super(pluginId, pluginTitle);
		this.attributeName = attributeName;
		// Google Chart
		addResource("https://www.google.com/jsapi", HtmlResourceType.JS);
		addResource("js/jquery-dataTables.js", HtmlResourceType.JS);
		addResource("js/javasimon-timelinePlugin.js", HtmlResourceType.JS);
		addResource("css/javasimon-timelinePlugin.css", HtmlResourceType.CSS);
	}
	public TimelineDetailPlugin() {
		this("timeline", "Timeline", TimelineCallback.TIMELINE_ATTRIBUTE_NAME);
	}
	/**
	 * Indicate that this plugin only applies on Stopwatches.
	 */
	@Override
	public boolean supports(Simon simon) {
		return simon instanceof Stopwatch;
	}

	/**
	 * Indicate whether {@link TimelineCallback} was registered in manager
	 */
	private boolean isTimelineCallbackRegistered(ActionContext context) {
		return SimonCallbacks.getCallbackByType(context.getManager(), TimelineCallback.class) != null;
	}
	private TimelineSample getData(Simon simon) {
		return ((Timeline) simon.getAttribute(attributeName)).sample();
	}

	/**
	 * Generate an HTML message row
	 */
	private void htmlMessage(DetailHtmlBuilder htmlBuilder, String message) throws IOException {
		htmlBuilder.beginRow()
			.labelCell("Message").valueCell(" colspan=\"3\"", message)
			.endRow();
	}
	@Override
	public DetailHtmlBuilder executeHtml(ActionContext context, DetailHtmlBuilder htmlBuilder, StringifierFactory htmlStringifierFactory, Simon simon) throws IOException {
		if (isTimelineCallbackRegistered(context)) {
			TimelineSample timelineSample = getData(simon);
			if (timelineSample == null) {
				htmlMessage(htmlBuilder, NO_DATA_MESSAGE);
			} else {
				htmlBuilder.beginRow()
					.labelCell("Capacity")
					.valueCell(htmlStringifierFactory.toString(timelineSample.getCapacity()))
					.labelCell("Width")
					.valueCell(htmlStringifierFactory.toString(timelineSample.getWidth()* ClockUtils.NANOS_IN_MILLIS,"Time"))
					.endRow();
				htmlBuilder.beginRow().labelCell("Evolution").beginValueCell(" colspan=\"3\"");
				htmlBuilder.begin("table").begin("thead")
					.beginRow().labelCell("Start").labelCell("End");
				if (simon instanceof Stopwatch) {
					htmlBuilder.labelCell("Counter").labelCell("Total").labelCell("Min").labelCell("Mean").labelCell("Last").labelCell("Max").labelCell("Std. Dev.");
				}
				htmlBuilder.endRow().end("thead").begin("tbody");
				for(TimeRange timeRange:timelineSample.getTimeRanges()) {
					htmlBuilder.beginRow()
						.valueCell(htmlStringifierFactory.toString(timeRange.getStartTimestamp(),"Date"))
						.valueCell(htmlStringifierFactory.toString(timeRange.getEndTimestamp(),"Date"));
					if (timeRange instanceof StopwatchTimeRange) {
						StopwatchTimeRange sTimeRange=(StopwatchTimeRange) timeRange;
						htmlBuilder
							.valueCell(htmlStringifierFactory.toString(sTimeRange.getCounter()))
							.valueCell(htmlStringifierFactory.toString(sTimeRange.getTotal(),"Time"))
							.valueCell(htmlStringifierFactory.toString(sTimeRange.getMin(),"Time"))
							.valueCell(htmlStringifierFactory.toString(sTimeRange.getMean(),"Time"))
							.valueCell(htmlStringifierFactory.toString(sTimeRange.getLast(),"Time"))
							.valueCell(htmlStringifierFactory.toString(sTimeRange.getMax(),"Time"))
							.valueCell(htmlStringifierFactory.toString(sTimeRange.getStandardDeviation(),"Time"));
					}
					htmlBuilder.endRow();				
				}
				htmlBuilder.end("tbody").end("table");
				htmlBuilder.endValueCell().endRow();
			}
		} else {
			htmlMessage(htmlBuilder, NO_CALLBACK_MESSAGE);
		}
		return htmlBuilder;
	}

	/**
	 * Generate a JSON message attribute
	 */
	private ObjectJS jsonMessage(String message, StringifierFactory jsonStringifierFactory) {
		ObjectJS timelineJS = new ObjectJS();
		timelineJS.setSimpleAttribute("message", message, jsonStringifierFactory.getStringifier(String.class));
		return timelineJS;
	}

	@Override
	public ObjectJS executeJson(ActionContext context, StringifierFactory jsonStringifierFactory, Simon simon) {
		ObjectJS timelineJS;
		if (isTimelineCallbackRegistered(context)) {
			TimelineSample timelineSample = getData(simon);
			if (timelineSample == null) {
				timelineJS = jsonMessage(NO_DATA_MESSAGE, jsonStringifierFactory);
			} else {
				timelineJS = ObjectJS.create(timelineSample, jsonStringifierFactory);
				timelineJS.setAttribute("timeRanges", ArrayJS.create(timelineSample.getTimeRanges(), jsonStringifierFactory));
			}
		} else {
			timelineJS = jsonMessage(NO_CALLBACK_MESSAGE, jsonStringifierFactory);
		}
		return timelineJS;
	}

}
