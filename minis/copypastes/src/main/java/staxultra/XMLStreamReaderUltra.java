package staxultra;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Wraps {@link XMLStreamReader} and provides handy shortcut features.
 * All original methods are not affected at all.
 */
public class XMLStreamReaderUltra implements XMLStreamReader {

	private XMLStreamReader xmlStreamReader;

	public XMLStreamReaderUltra(XMLStreamReader xmlStreamReader) {
		this.xmlStreamReader = xmlStreamReader;
	}

	@Override public void close() throws XMLStreamException {
		xmlStreamReader.close();
	}

	@Override public int getAttributeCount() {
		return xmlStreamReader.getAttributeCount();
	}

	@Override public String getAttributeLocalName(int index) {
		return xmlStreamReader.getAttributeLocalName(index);
	}

	@Override public QName getAttributeName(int index) {
		return xmlStreamReader.getAttributeName(index);
	}

	@Override public String getAttributeNamespace(int index) {
		return xmlStreamReader.getAttributeNamespace(index);
	}

	@Override public String getAttributePrefix(int index) {
		return xmlStreamReader.getAttributePrefix(index);
	}

	@Override public String getAttributeType(int index) {
		return xmlStreamReader.getAttributeType(index);
	}

	@Override public String getAttributeValue(int index) {
		return xmlStreamReader.getAttributeValue(index);
	}

	@Override public String getAttributeValue(String namespaceURI, String localName) {
		return xmlStreamReader.getAttributeValue(namespaceURI, localName);
	}

	@Override public String getCharacterEncodingScheme() {
		return xmlStreamReader.getCharacterEncodingScheme();
	}

	@Override public String getElementText() throws XMLStreamException {
		return xmlStreamReader.getElementText();
	}

	@Override public String getEncoding() {
		return xmlStreamReader.getEncoding();
	}

	@Override public int getEventType() {
		return xmlStreamReader.getEventType();
	}

	@Override public String getLocalName() {
		return xmlStreamReader.getLocalName();
	}

	@Override public Location getLocation() {
		return xmlStreamReader.getLocation();
	}

	@Override public QName getName() {
		return xmlStreamReader.getName();
	}

	@Override public NamespaceContext getNamespaceContext() {
		return xmlStreamReader.getNamespaceContext();
	}

	@Override public int getNamespaceCount() {
		return xmlStreamReader.getNamespaceCount();
	}

	@Override public String getNamespacePrefix(int index) {
		return xmlStreamReader.getNamespacePrefix(index);
	}

	@Override public String getNamespaceURI() {
		return xmlStreamReader.getNamespaceURI();
	}

	@Override public String getNamespaceURI(int index) {
		return xmlStreamReader.getNamespaceURI(index);
	}

	@Override public String getNamespaceURI(String prefix) {
		return xmlStreamReader.getNamespaceURI(prefix);
	}

	@Override public String getPIData() {
		return xmlStreamReader.getPIData();
	}

	@Override public String getPITarget() {
		return xmlStreamReader.getPITarget();
	}

	@Override public String getPrefix() {
		return xmlStreamReader.getPrefix();
	}

	@Override public Object getProperty(String name) throws IllegalArgumentException {
		return xmlStreamReader.getProperty(name);
	}

	@Override public String getText() {
		return xmlStreamReader.getText();
	}

	@Override public char[] getTextCharacters() {
		return xmlStreamReader.getTextCharacters();
	}

	@Override public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
		return xmlStreamReader.getTextCharacters(sourceStart, target, targetStart, length);
	}

	@Override public int getTextLength() {
		return xmlStreamReader.getTextLength();
	}

	@Override public int getTextStart() {
		return xmlStreamReader.getTextStart();
	}

	@Override public String getVersion() {
		return xmlStreamReader.getVersion();
	}

	@Override public boolean hasName() {
		return xmlStreamReader.hasName();
	}

	@Override public boolean hasNext() throws XMLStreamException {
		return xmlStreamReader.hasNext();
	}

	@Override public boolean hasText() {
		return xmlStreamReader.hasText();
	}

	@Override public boolean isAttributeSpecified(int index) {
		return xmlStreamReader.isAttributeSpecified(index);
	}

	@Override public boolean isCharacters() {
		return xmlStreamReader.isCharacters();
	}

	@Override public boolean isEndElement() {
		return xmlStreamReader.isEndElement();
	}

	@Override public boolean isStandalone() {
		return xmlStreamReader.isStandalone();
	}

	@Override public boolean isStartElement() {
		return xmlStreamReader.isStartElement();
	}

	@Override public boolean isWhiteSpace() {
		return xmlStreamReader.isWhiteSpace();
	}

	@Override public int next() throws XMLStreamException {
		return xmlStreamReader.next();
	}

	@Override public int nextTag() throws XMLStreamException {
		return xmlStreamReader.nextTag();
	}

	@Override public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
		xmlStreamReader.require(type, namespaceURI, localName);
	}

	@Override public boolean standaloneSet() {
		return xmlStreamReader.standaloneSet();
	}

	// HERE IS THE PART WITH EXTENSIONS

	/**
	 * Finds the start of any of the specified elements (local names) and returns the matching
	 * element local name. Does not consume the element.
	 */
	public String searchFor(String... possibleElements) throws XMLStreamException {
		while (true) {
			if (isStartElement()) {
				String localName = getLocalName();
				for (String element : possibleElements) {
					if (localName.equals(element)) {
						return element;
					}
				}
			}
			xmlStreamReader.next();
		}
	}

	public Map<String, String> processStartElement(String elementName, String... requiredAttributes) throws XMLStreamException {
		Map<String, String> attrs = processStartElementPrivate(elementName, requiredAttributes);
		xmlStreamReader.next();
		return attrs;
	}

	public String processOptionalTextElement(String elementName) throws XMLStreamException {
		if (!xmlStreamReader.isStartElement() || !xmlStreamReader.getLocalName().equals(elementName)) {
			return null;
		}
		return processTextElement(elementName);
	}

	public Integer processOptionalTextElementAsInteger(String elementName) throws XMLStreamException {
		if (!xmlStreamReader.isStartElement() || !xmlStreamReader.getLocalName().equals(elementName)) {
			return null;
		}
		return processTextElementAsInteger(elementName);
	}

	public String processTextElement(String elementName) throws XMLStreamException {
		return processTextElementPrivate(elementName).text;
	}

	private TextElementResult processTextElementPrivate(String elementName) throws XMLStreamException {
		assertStartTag(elementName);
		xmlStreamReader.next();
		String position = currentPosition();
		String text = getText();
		processEndElement(elementName);
		return new TextElementResult(text, position);
	}

	public <T extends Enum<T>> T processTextElementAsEnum(String elementName, Class<T> enumClass) throws XMLStreamException {
		TextElementResult textElementResult = processTextElementPrivate(elementName);
		try {
			return Enum.valueOf(enumClass, textElementResult.text);
		} catch (IllegalArgumentException e) {
			throw illegalValueException("enum", elementName, textElementResult, e);
		}
	}

	private XMLStreamException illegalValueException(String valueType, String elementName, TextElementResult textElementResult, Throwable e) throws XMLStreamException {
		return new XMLStreamException("Illegal " + valueType + " value '" + textElementResult.text + "' for element '" + elementName + "' at " + textElementResult.position, e);
	}

	public Integer processTextElementAsInteger(String elementName) throws XMLStreamException {
		TextElementResult textElementResult = processTextElementPrivate(elementName);
		try {
			if (!textElementResult.text.isEmpty()) {
				return Integer.valueOf(textElementResult.text);
			}
			return null;
		} catch (NumberFormatException e) {
			throw illegalValueException("integer", elementName, textElementResult, e);
		}
	}

	private Map<String, String> processStartElementPrivate(String elementName, String... requiredAttributes) throws XMLStreamException {
		assertStartTag(elementName);
		Map<String, String> attrs = readAttributes();
		for (String attr : requiredAttributes) {
			if (!attrs.containsKey(attr)) {
				throw new XMLStreamException("Attribute '" + attr + "' MUST be present (element: " + elementName + "). " + currentPosition());
			}
		}
		return attrs;
	}

	public void assertStartTag(String name) throws XMLStreamException {
		String position = currentPosition();
		while (!xmlStreamReader.isStartElement()) {
			if (cannotBeIgnoredDuringSearch(xmlStreamReader)
				&& xmlStreamReader.getEventType() != START_DOCUMENT)
			{
				throw new XMLStreamException("Assert start tag - wrong event type " +
					xmlStreamReader.getEventType() + " (expected name: " + name + ") " + position);
			}
			xmlStreamReader.next();
		}
		assertName("start tag", name);
	}

	/** We ignore any empty characters (after trimming), start/end document and comments. */
	private boolean cannotBeIgnoredDuringSearch(XMLStreamReader xmlStreamReader) {
		if (xmlStreamReader.isCharacters()) {
			String text = xmlStreamReader.getText();
			return text != null && !text.trim().isEmpty();
		}

		return xmlStreamReader.getEventType() != START_DOCUMENT
			&& xmlStreamReader.getEventType() != END_DOCUMENT
			&& xmlStreamReader.getEventType() != COMMENT;
	}

	public Map<String, String> readAttributes() {
		Map<String, String> attributes = new LinkedHashMap<>();
		int attrCount = xmlStreamReader.getAttributeCount();
		for (int i = 0; i < attrCount; i++) {
			attributes.put(xmlStreamReader.getAttributeName(i).toString(), xmlStreamReader.getAttributeValue(i));
		}
		return attributes;
	}

	public void assertName(String operation, String name) throws XMLStreamException {
		if (!xmlStreamReader.getLocalName().equals(name)) {
			throw new XMLStreamException("Assert " + operation + " - wrong element name " +
				xmlStreamReader.getName().toString() + " (expected name: " + name + ") " + currentPosition());
		}
	}

	public String currentPosition() {
		Location location = xmlStreamReader.getLocation();
		return "[row,col]:[" + location.getLineNumber() + ',' + location.getColumnNumber() + ']';
	}

	public void assertEndTag(String name) throws XMLStreamException {
		String position = currentPosition();
		while (!xmlStreamReader.isEndElement()) {
			if (cannotBeIgnoredDuringSearch(xmlStreamReader)) {
				throw new XMLStreamException("Assert end tag - wrong event type " +
					xmlStreamReader.getEventType() + " (expected name: " + name + ") " + position);
			}
			xmlStreamReader.next();
		}
		assertName("end tag", name);
	}

	/** Checks if the next tag is start tag with the specified name. Uses {@link #findTag()} first. */
	public boolean isStartTag(String name) throws XMLStreamException {
		findTag();
		return xmlStreamReader.isStartElement() && xmlStreamReader.getLocalName().equals(name);
	}

	/** Checks if the next tag is end tag with the specified name. Uses {@link #findTag()} first. */
	public boolean isEndTag(String name) throws XMLStreamException {
		findTag();
		return xmlStreamReader.isEndElement() && xmlStreamReader.getLocalName().equals(name);
	}

	public void processEndElement(String name) throws XMLStreamException {
		assertEndTag(name);
		xmlStreamReader.next();
	}

	/**
	 * Finds and consumes the matching end element on this level of the document.
	 * That is if the reader is inside element X, <b>but NOT inside any other element</b>,
	 * it will find matching closing element X.
	 */
	public void findAndProcessEndElement(String name) throws XMLStreamException {
		int level = 0;
		while (true) {
			if (xmlStreamReader.isStartElement()) {
				level += 1;
			}
			if (xmlStreamReader.isEndElement()) {
				if (level == 0) {
					processEndElement(name);
					return;
				}

				level -= 1;
				if (level < 0) {
					throw new IllegalStateException("Got out of level in findAndProcessEndElement" +
						currentPosition());
				}
			}
			xmlStreamReader.next();
		}
	}

	public String getTextTrimmed() throws XMLStreamException {
		StringBuilder sb = new StringBuilder();
		while (xmlStreamReader.isCharacters()) {
			sb.append(xmlStreamReader.getText());
			xmlStreamReader.next();
		}
		return sb.toString().trim();
	}

	/** Finds next tag (end or start) if the reader is not on start or end element already. */
	public XMLStreamReaderUltra findTag() throws XMLStreamException {
		while (!(xmlStreamReader.isStartElement() || xmlStreamReader.isEndElement())) {
			xmlStreamReader.next();
		}
		return this;
	}

	private static class TextElementResult {

		private final String text;
		private final String position;

		private TextElementResult(String text, String position) {
			this.text = text;
			this.position = position;
		}
	}
}
