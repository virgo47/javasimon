package org.javasimon.console.action;

import org.javasimon.Simon;
import org.javasimon.SimonManager;
import org.javasimon.console.ActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Export Simons as a hierarchical XML document.
 * All attributes of simons are exported
 *
 * @author gquintana
 */
public class TreeXmlAction extends AbstractXmlAction {

	public static final String PATH = "/data/tree.xml";

	public TreeXmlAction(ActionContext context) {
		super(context);
	}

	@Override
	protected Element createElement(Document document, Simon simon) {
		Element element = super.createElement(document, simon);
		for (Simon child : simon.getChildren()) {
			Element childElement = createElement(document, child);
			element.appendChild(childElement);
		}
		return element;
	}

	@Override
	protected void fillDocument(Document document) {
		Element rootElement = createElement(document, SimonManager.manager().getRootSimon());
		document.appendChild(rootElement);
	}
}
