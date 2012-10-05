package org.javasimon.console.plugin;

import java.io.IOException;
import org.javasimon.Simon;
import org.javasimon.Stopwatch;
import org.javasimon.callback.calltree.CallTree;
import org.javasimon.callback.calltree.CallTreeCallback;
import org.javasimon.callback.calltree.CallTreeNode;
import org.javasimon.console.ActionContext;
import org.javasimon.console.SimonCallbacks;
import org.javasimon.console.action.DetailHtmlBuilder;
import org.javasimon.console.action.DetailPlugin;
import org.javasimon.console.html.HtmlResourceType;
import org.javasimon.console.json.ArrayJS;
import org.javasimon.console.json.ObjectJS;
import org.javasimon.console.text.StringifierFactory;

/**
 * Detail plugin to display call tree.
 * @author gquintana
 */
public class CallTreeDetailPlugin extends DetailPlugin {

	/**
	 * Message: Callback not registered
	 */
	public static final String NO_CALLBACK_MESSAGE = "CallTree callback not registered";
	/**
	 * Message: Data not found in Simon
	 */
	private static final String NO_DATA_MESSAGE = "No call tree available yet";

	public CallTreeDetailPlugin() {
		super("callTree", "Call Tree");
		addResource("js/javasimon-callTreePlugin.js",	HtmlResourceType.JS);
		addResource("js/javasimon-dataTreeTable.js",	HtmlResourceType.JS);
	}

	/**
	 * Indicate that this plugin only applies on Stopwatches.
	 */
	@Override
	public boolean supports(Simon simon) {
		return simon instanceof Stopwatch;
	}

	/**
	 * Indicate whether {@link CallTreeCallback} was registered in manager
	 */
	private boolean isCallTreeCallbackRegistered(ActionContext context) {
		return SimonCallbacks.getCallbackByType(context.getManager(), CallTreeCallback.class) != null;
	}

	/**
	 * Get CallTree data from Simon
	 */
	private CallTree getData(Simon simon) {
		return CallTreeCallback.getLastCallTree((Stopwatch) simon);
	}

	/**
	 * Generate an HTML message row
	 */
	private void htmlMessage(DetailHtmlBuilder htmlBuilder, String message) throws IOException {
		htmlBuilder.beginRow()
			.labelCell("Message").valueCell(" colspan=\"3\"", message)
			.endRow();
	}
	/**
	 * Generate a HTML call tree node list
	 */
	private DetailHtmlBuilder htmlTreeNode(CallTreeNode node, DetailHtmlBuilder htmlBuilder, StringifierFactory htmlStringifierFactory) throws IOException {
		htmlBuilder.begin("li")
			.text(node.getName()).text(":&nbsp;");
		if (node.getParent()!=null) {
			htmlBuilder
				.text(htmlStringifierFactory.toString(node.getPercent())).text("%")
				.text(", ");
		}
		htmlBuilder
			.text("total&nbsp;").text(htmlStringifierFactory.toString(node.getTotal(), "Time"))
			.text(", ")
			.text("count&nbsp;").text(htmlStringifierFactory.toString(node.getSplitCount()));
		if (!node.getChildren().isEmpty()) {
			htmlBuilder.begin("ul");
			for(CallTreeNode childNode:node.getChildren()) {
				htmlTreeNode(childNode, htmlBuilder, htmlStringifierFactory);
			}
			htmlBuilder.end("ul");
		}
		return htmlBuilder.end("li");
	}
	@Override
	public DetailHtmlBuilder executeHtml(ActionContext context, DetailHtmlBuilder htmlBuilder, StringifierFactory htmlStringifierFactory, Simon simon) throws IOException {
		if (isCallTreeCallbackRegistered(context)) {
			CallTree callTree = getData(simon);
			if (callTree == null) {
				htmlMessage(htmlBuilder, NO_DATA_MESSAGE);
			} else {
				htmlBuilder
					.beginRow()
						.labelCell("Threshold")
						.valueCell(htmlStringifierFactory.toString(callTree.getLogThreshold(), "Time"))
					.endRow()
					.beginRow()
						.labelCell("Tree")
						.beginValueCell().begin("ul");
						htmlTreeNode(callTree.getRootNode(), htmlBuilder, htmlStringifierFactory)
						.end("ul").endValueCell()
					.endRow();				
			}
		} else {
			htmlMessage(htmlBuilder, NO_CALLBACK_MESSAGE);
		}
		return htmlBuilder;
	}

	/**
	 * Generate a JSON message object
	 */
	private ObjectJS jsonMessage(String message, StringifierFactory jsonStringifierFactory) {
		ObjectJS callTreeJS = new ObjectJS();
		callTreeJS.setSimpleAttribute("message", message, jsonStringifierFactory.getStringifier(String.class));
		return callTreeJS;
	}
	/**
	 * Generate a JSON call tree node object
	 */
	private ObjectJS jsonTreeNode(CallTreeNode node, StringifierFactory jsonStringifierFactory) {
		final ObjectJS nodeJS = ObjectJS.create(node, jsonStringifierFactory);
		if (!node.getChildren().isEmpty()) {
			final ArrayJS childNodesJS=new ArrayJS(node.getChildren().size());
			for(CallTreeNode childNode:node.getChildren()) {
				childNodesJS.addElement(jsonTreeNode(childNode, jsonStringifierFactory));
			}
			nodeJS.setAttribute("children", childNodesJS);
		}
		return nodeJS;
	}

	/**
	 * Generate a JSON call tree object or an error string if no call tree
	 */
	@Override
	public ObjectJS executeJson(ActionContext context, StringifierFactory jsonStringifierFactory, Simon simon) {
		ObjectJS callTreeJS;
		if (isCallTreeCallbackRegistered(context)) {
			CallTree callTree = getData(simon);
			if (callTree == null) {
				callTreeJS = jsonMessage(NO_DATA_MESSAGE, jsonStringifierFactory);
			} else {
				callTreeJS = ObjectJS.create(callTree, jsonStringifierFactory);
				callTreeJS.setAttribute("rootNode", jsonTreeNode(callTree.getRootNode(), jsonStringifierFactory));
			}
		} else {
			callTreeJS = jsonMessage(NO_CALLBACK_MESSAGE, jsonStringifierFactory);
		}
		return callTreeJS;
	}
}
