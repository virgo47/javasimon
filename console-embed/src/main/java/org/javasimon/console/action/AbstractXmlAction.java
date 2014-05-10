package org.javasimon.console.action;

import org.javasimon.Sample;
import org.javasimon.Simon;
import org.javasimon.console.Action;
import org.javasimon.console.ActionContext;
import org.javasimon.console.ActionException;
import org.javasimon.console.SimonType;
import org.javasimon.console.SimonTypeFactory;
import org.javasimon.console.TimeFormatType;
import org.javasimon.console.reflect.Getter;
import org.javasimon.console.reflect.GetterFactory;
import org.javasimon.console.text.Stringifier;
import org.javasimon.console.text.StringifierFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Base for actions returning data in XML format.
 *
 * @author gquintana
 */
public abstract class AbstractXmlAction extends Action {

	protected AbstractXmlAction(ActionContext context) {
		super(context);
	}

	/** Converter Object &rarr; HTML String. */
	protected final StringifierFactory stringifierFactory = new StringifierFactory();

	@Override
	public void readParameters() {
		final TimeFormatType timeFormat = getContext().getParameterAsEnum("timeFormat", TimeFormatType.class, TimeFormatType.MILLISECOND);
		stringifierFactory.init(timeFormat,
			StringifierFactory.ISO_DATE_PATTERN,
			StringifierFactory.READABLE_NUMBER_PATTERN
		);
	}

	/**
	 * Transforms a Simon into a JSON object.
	 *
	 * @param simon Simon
	 * @return JSON object
	 */
	@SuppressWarnings("unchecked")
	protected Element createElement(Document document, Simon simon) {
		// Simon type is used as element name
		Sample sample = simon.sample();
		SimonType lType = SimonTypeFactory.getValueFromInstance(sample);
		Element element = document.createElement(lType.name().toLowerCase());
		// Only to have the name as first attribute
		element.setAttribute("name", sample.getName());
		// Export properties using reflection
		for (Getter getter : GetterFactory.getGetters(sample.getClass())) {
			Object propertyValue = getter.get(sample);
			if (propertyValue != null) {
				Stringifier propertyStringifier = stringifierFactory
					.getStringifier(getter.getType(), getter.getSubType());
				if (propertyStringifier != null) {
					element.setAttribute(getter.getName(), propertyStringifier.toString(propertyValue));
				}
			}
		}
		// Export attribute map
		Iterator<String> attributeNameIter = simon.getAttributeNames();
		while (attributeNameIter.hasNext()) {
			String attributeName = attributeNameIter.next();
			Object attributeValue = simon.getAttribute(attributeName);
			if (attributeValue != null) {
				Stringifier attributeStringifier = stringifierFactory
					.getStringifier(attributeValue.getClass());
				if (attributeStringifier != null) {
					Element attributeElt = document.createElement("attribute");
					attributeElt.setAttribute("name", attributeName);
					attributeElt.setAttribute("value", attributeStringifier.toString(attributeValue));
					element.appendChild(attributeElt);
				}
			}

		}
		return element;

	}

	@Override
	public void execute() throws ServletException, IOException, ActionException {
		dontCache();
		getContext().setContentType("text/xml");
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			fillDocument(document);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.transform(new DOMSource(document),
				new StreamResult(getContext().getOutputStream()));
		} catch (ParserConfigurationException | TransformerException parserConfigurationException) {
			throw new ActionException("XML Parser error", parserConfigurationException);
		} finally {
			getContext().getOutputStream().flush();
		}
	}

	protected abstract void fillDocument(Document document);
}
