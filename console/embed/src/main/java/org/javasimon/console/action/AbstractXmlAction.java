package org.javasimon.console.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.javasimon.Counter;
import org.javasimon.Simon;
import org.javasimon.Stopwatch;
import org.javasimon.console.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Base for actions returing data in XML format
 *
 * @author gquintana
 */
public abstract class AbstractXmlAction extends Action {

	protected AbstractXmlAction(ActionContext context) {
		super(context);
	}
	protected final ValueFormatter valueFormatter = new ValueFormatter();

	@Override
	public void readParameters() {
		valueFormatter.setTimeFormat(getContext().getParameterAsEnum("timeFormat", TimeFormatType.class, TimeFormatType.MILLISECOND));
		valueFormatter.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
	}

	/**
	 * Transforms a Simon into a JSON object
	 *
	 * @param simon Simon
	 * @return JSON object
	 */
	protected Element createElement(Document document, Simon simon) {
		SimonType lType = SimonType.getValueFromInstance(simon);
		Element element = document.createElement(lType.name().toLowerCase());
		element.setAttribute("name", simon.getName()); // Only to have the name as first attribute
		for (Getter getter : Getter.getGetters(simon.getClass())) {
			String propertyName = getter.getName();
			Class propertyType = getter.getType();
			Object propertyValue = getter.get(simon);
			String formattedPropertyValue = null;
			if (propertyType.equals(Integer.class) || propertyType.equals(Integer.TYPE)) {
				formattedPropertyValue = valueFormatter.formatNumber((Integer) propertyValue);
			} else if (propertyType.equals(Long.class) || propertyType.equals(Long.TYPE)) {
				formattedPropertyValue = valueFormatter.formatNumber((Long) propertyValue);
			} else if (propertyType.equals(String.class)) {
				formattedPropertyValue = valueFormatter.formatString((String) propertyValue);
			} else if (Enum.class.isAssignableFrom(propertyType)) {
				formattedPropertyValue = valueFormatter.formatEnum((Enum) propertyValue);
			}
			if (formattedPropertyValue != null) {
				element.setAttribute(propertyName, formattedPropertyValue);
			}
		}
		switch (lType) {
			case STOPWATCH:
				Stopwatch stopwatch = (Stopwatch) simon;
				element.setAttribute("total", valueFormatter.formatTime(stopwatch.getTotal()));
				element.setAttribute("min", valueFormatter.formatTime(stopwatch.getMin()));
				element.setAttribute("mean", valueFormatter.formatTime(stopwatch.getMean()));
				element.setAttribute("max", valueFormatter.formatTime(stopwatch.getMax()));
				element.setAttribute("standardDeviation", valueFormatter.formatTime(stopwatch.getStandardDeviation()));
				element.setAttribute("last", valueFormatter.formatTime(stopwatch.getLast()));
				element.setAttribute("maxActiveTimestamp", valueFormatter.formatDate(stopwatch.getMaxActiveTimestamp()));
				element.setAttribute("minTimestamp", valueFormatter.formatDate(stopwatch.getMinTimestamp()));
				element.setAttribute("maxTimestamp", valueFormatter.formatDate(stopwatch.getMaxTimestamp()));
				break;
			case COUNTER:
				Counter counter = (Counter) simon;
				element.setAttribute("minTimestamp", valueFormatter.formatDate(counter.getMinTimestamp()));
				element.setAttribute("maxTimestamp", valueFormatter.formatDate(counter.getMaxTimestamp()));
				break;
		}
		element.setAttribute("firstUsage", valueFormatter.formatDate(simon.getFirstUsage()));
		element.setAttribute("lastUsage", valueFormatter.formatDate(simon.getLastUsage()));
		return element;

	}

	@Override
	public void execute() throws ServletException, IOException, ActionException {
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
		} catch (ParserConfigurationException parserConfigurationException) {
			throw new ActionException("XML Parser error", parserConfigurationException);
		} catch (TransformerException transformerException) {
			throw new ActionException("XML Parser error", transformerException);
		} finally {
			getContext().getOutputStream().flush();
		}
	}

	protected abstract void fillDocument(Document document);
}
