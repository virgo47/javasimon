package org.javasimon.console.plugin;

import java.io.IOException;
import org.javasimon.Simon;
import org.javasimon.Stopwatch;
import org.javasimon.callback.quantiles.BucketSample;
import org.javasimon.callback.quantiles.BucketsSample;
import org.javasimon.callback.quantiles.QuantilesCallback;
import org.javasimon.console.ActionContext;
import org.javasimon.console.SimonCallbacks;
import org.javasimon.console.action.DetailHtmlBuilder;
import org.javasimon.console.action.DetailPlugin;
import org.javasimon.console.html.HtmlResourceType;
import org.javasimon.console.json.ArrayJS;
import org.javasimon.console.json.ObjectJS;
import org.javasimon.console.text.StringifierFactory;

/**
 * Detail plugin to display {@link QuantilesCallback} information
 */
public class QuantilesDetailPlugin extends DetailPlugin {

	/**
	 * Message: Callback not registered
	 */
	public static final String NO_CALLBACK_MESSAGE = "Quantiles callback not registered";
	/**
	 * Message: Data not found in Simon
	 */
	private static final String NO_DATA_MESSAGE = "No data available";

	public QuantilesDetailPlugin() {
		super("quantiles", "Distribution and Quantiles");
		addResource("js/javasimon-quantilesPlugin.js", HtmlResourceType.JS);
		addResource("css/javasimon-quantilesPlugin.css", HtmlResourceType.CSS);
	}

	/**
	 * Indicate that this plugin only applies on Stopwatches.
	 */
	@Override
	public boolean supports(Simon simon) {
		return simon instanceof Stopwatch;
	}

	/**
	 * Indicate whether {@link QuantilesCallback} was registered in manager
	 */
	private boolean isQuantilesCallbackRegistered(ActionContext context) {
		return SimonCallbacks.getCallbackByType(context.getManager(), QuantilesCallback.class) != null;
	}

	/**
	 * Get quantiles data from Simon
	 */
	private BucketsSample getData(Simon simon) {
		return QuantilesCallback.sampleBuckets((Stopwatch) simon);
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
		if (isQuantilesCallbackRegistered(context)) {
			BucketsSample bucketsSample = getData(simon);
			if (bucketsSample == null) {
				htmlMessage(htmlBuilder, NO_DATA_MESSAGE);
			} else {
				htmlBuilder.beginRow()
					.labelCell("Median")
					.valueCell(htmlStringifierFactory.toString(bucketsSample.getMedian(), "Time"))
					.labelCell("90%")
					.valueCell(htmlStringifierFactory.toString(bucketsSample.getPercentile90(), "Time"))
					.endRow();
				htmlBuilder.beginRow().labelCell("Distribution").beginValueCell();
				htmlBuilder.begin("table")
					.beginRow().labelCell("Min").labelCell("Max").labelCell("Counter").endRow();				
				Integer maxCount = bucketsSample.getMaxCount();
				for(BucketSample bucketSample:bucketsSample.getBuckets()) {
					final int count = bucketSample.getCount();
					final int barSize = count > 0 && maxCount > 0 ? count * 200 / maxCount : 0;
					htmlBuilder.beginRow()
						.beginValueCell().value(bucketSample.getMin(),"Time").endValueCell()
						.beginValueCell().value(bucketSample.getMax(),"Time").endValueCell()
						.beginValueCell().write("<div class=\"bar\" style=\"width:").write(Integer.toString(barSize)).write("px\">&nbsp;").end("div").value(count, null).endValueCell()
					.endRow();				
				}
				htmlBuilder.end("table");
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
		ObjectJS bucketsJS = new ObjectJS();
		bucketsJS.setSimpleAttribute("message", message, jsonStringifierFactory.getStringifier(String.class));
		return bucketsJS;
	}

	@Override
	public ObjectJS executeJson(ActionContext context, StringifierFactory jsonStringifierFactory, Simon simon) {
		ObjectJS bucketsJS;
		if (isQuantilesCallbackRegistered(context)) {
			BucketsSample bucketsSample = getData(simon);
			if (bucketsSample == null) {
				bucketsJS = jsonMessage(NO_DATA_MESSAGE, jsonStringifierFactory);
			} else {
				bucketsJS = ObjectJS.create(bucketsSample, jsonStringifierFactory);
				bucketsJS.setAttribute("buckets", ArrayJS.create(bucketsSample.getBuckets(), jsonStringifierFactory));
			}
		} else {
			bucketsJS = jsonMessage(NO_CALLBACK_MESSAGE, jsonStringifierFactory);
		}
		return bucketsJS;
	}
}
