package sk.finrisk.marketdata.process.ecb;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.testng.annotations.Test;
import staxultra.XMLStreamReaderUltra;

public class XMLStreamReaderUltraTest {

	@Test
	public void searchForFindsTheElement() throws XMLStreamException {
		XMLStreamReaderUltra xr = newReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<gesmes:Envelope xmlns:gesmes=\"http://www.gesmes.org/xml/2002-08-01\" xmlns=\"http://www.ecb.int/vocabulary/2002-08-01/eurofxref\">\n" +
			"\t<gesmes:subject>Reference rates</gesmes:subject>\n" +
			"\t<gesmes:Sender>\n" +
			"\t\t<gesmes:name>European Central Bank</gesmes:name>\n" +
			"\t</gesmes:Sender>\n" +
			"\t<Cube> </Cube>\n" +
			"</gesmes:Envelope>");

		xr.searchFor("Cube");
		assertTrue(xr.isStartElement());
		assertEquals(xr.getLocalName(), "Cube");
		assertTrue(xr.isStartTag("Cube"));
	}

	@Test
	public void testFindElementAtDocumentEnd() throws XMLStreamException {
		XMLStreamReaderUltra xr = newReader("<Cube>\n" +
			"\t<Cube></Cube>\n" +
			"\t<Cubex>asdfa</Cubex>\n" +
			"\t<NoPair/>\n" +
			"</Cube>");
		xr.searchFor("Cube");
		xr.processStartElement("Cube");

		xr.findAndProcessEndElement("Cube");
		assertTrue(xr.getEventType() == XMLStreamConstants.END_DOCUMENT);
	}

	@Test
	public void properEndElementIsFoundAndProcessed() throws XMLStreamException {
		XMLStreamReaderUltra xr = newReader("<Doc><Cube>\n" +
			"\t<Cube></Cube>\n" +
			"\t<Cubex>asdfa</Cubex>\n" +
			"\t<NoPair/>\n" +
			"</Cube></Doc>");
		xr.searchFor("Cube");
		xr.processStartElement("Cube");

		xr.findAndProcessEndElement("Cube");
		assertTrue(xr.isEndElement(), "Found event type: " + xr.getEventType());
		assertEquals(xr.getLocalName(), "Doc");
	}

	@Test
	public void processStartAndEndElementIgnoresIgnorableCharacters() throws XMLStreamException {
		XMLStreamReaderUltra xr = newReader(" <Doc> <!-- comment --> <Cube>\n" +
			"</Cube> \n </Doc> ");

		xr.processStartElement("Doc");
		xr.processStartElement("Cube");
		xr.processEndElement("Cube");

		assertEquals(xr.currentPosition(), "[row,col]:[3,2]");

		xr.processEndElement("Doc");

		assertEquals(xr.currentPosition(), "[row,col]:[-1,-1]");
		assertTrue(xr.getEventType() == XMLStreamConstants.END_DOCUMENT);
	}

	@Test
	public void nonPairElementsActLikePairedOnes() throws XMLStreamException {
		XMLStreamReaderUltra xr = newReader("<Doc><Cube/><Anything/></Doc>");
		assertTrue(xr.getEventType() == XMLStreamConstants.START_DOCUMENT);

		xr.processStartElement("Doc");
		xr.processStartElement("Cube");
		xr.processEndElement("Cube");
		xr.processStartElement("Anything");
		xr.processEndElement("Anything");
		xr.processEndElement("Doc");

		assertEquals(xr.currentPosition(), "[row,col]:[-1,-1]");
		assertTrue(xr.getEventType() == XMLStreamConstants.END_DOCUMENT);
	}

	private XMLStreamReaderUltra newReader(String xml) throws XMLStreamException {
		return new XMLStreamReaderUltra(XMLInputFactory.newFactory()
			.createXMLStreamReader(new StringReader(xml)));
	}
}
