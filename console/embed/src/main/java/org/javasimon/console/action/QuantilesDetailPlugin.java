package org.javasimon.console.action;

import java.io.IOException;
import org.javasimon.Simon;
import org.javasimon.Stopwatch;
import org.javasimon.callback.quantiles.BucketsSample;
import org.javasimon.callback.quantiles.QuantilesCallback;
import org.javasimon.console.ActionContext;
import org.javasimon.console.html.HtmlResource;
import org.javasimon.console.html.HtmlResourceType;
import org.javasimon.console.json.ObjectJS;
import org.javasimon.console.text.StringifierFactory;

/**
 *
 * @author gquintana
 */
public class QuantilesDetailPlugin extends DetailPlugin {

	public QuantilesDetailPlugin() {
		super("quantiles", "Distribution and Quantiles");
		addResource("js/javasimon-quantiles.js", HtmlResourceType.JS);
		addResource("css/javasimon-quantiles.css", HtmlResourceType.CSS);
	}
	private Object[] getData(Simon simon) {
		String message=null;
		BucketsSample bucketsSample=null;
		if (simon instanceof Stopwatch) {
			 bucketsSample=QuantilesCallback.sampleBuckets((Stopwatch) simon);
			 if (bucketsSample==null) {
				 message="No data available yet";
			 }
		} else {
			message="Only available on Stopwatches";
		}
		return new Object[]{message, bucketsSample};
	}
	@Override
	public DetailHtmlBuilder executeHtml(ActionContext context, DetailHtmlBuilder htmlBuilder, StringifierFactory htmlStringifierFactory, Simon simon) throws IOException {
		Object[] data=getData(simon);
		String message=(String) data[0];
		BucketsSample bucketsSample=(BucketsSample) data[1];
		if (message!=null) {
			htmlBuilder.beginRow()
				.labelCell("Message").valueCell(" colspan=\"3\"", message)
				.endRow();
		}
		if (bucketsSample!=null) {
			htmlBuilder.beginRow()
				.labelCell("Median").valueCell(htmlStringifierFactory.toString(bucketsSample.getMedian(), "Time"))
				.labelCell("90%").valueCell(htmlStringifierFactory.toString(bucketsSample.getPercentile90(), "Time"))
				.endRow();
		}
		return htmlBuilder;
	}
	@Override
	public ObjectJS executeJson(ActionContext context, StringifierFactory jsonStringifierFactory, Simon simon) {
		Object[] data=getData(simon);
		String message=(String) data[0];
		BucketsSample bucketsSample=(BucketsSample) data[1];
		ObjectJS bucketsJS=new ObjectJS();
		if (message!=null) {
			bucketsJS.setSimpleAttribute("message", message, jsonStringifierFactory.getStringifier(String.class));
		}
		if (bucketsSample!=null) {
			bucketsJS.setSimpleAttribute("median", bucketsSample.getMedian(), jsonStringifierFactory.getStringifier(Double.class, "Time"));
			bucketsJS.setSimpleAttribute("percentile90", bucketsSample.getPercentile90(), jsonStringifierFactory.getStringifier(Double.class, "Time"));
		}
		return bucketsJS;
	}
	
}
