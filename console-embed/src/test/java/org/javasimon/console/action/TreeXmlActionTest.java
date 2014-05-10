package org.javasimon.console.action;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.javasimon.console.SimonData;
import org.javasimon.console.TestActionContext;
import org.json.JSONException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Unit test for {@link org.javasimon.console.action.TreeXmlAction}.
 *
 * @author gquintana
 */
public class TreeXmlActionTest {
	@BeforeClass
	public static void setUpClass() {
		SimonData.initialize();
	}

	private void visitElement(Element element, Set<String> names) throws JSONException {
		String name = element.getAttribute("name");
		if (name != null) {
			names.remove(name);
		}
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				visitElement((Element) node, names);
			}
		}
	}

	@Test
	public void testExecute() throws Exception {
		TestActionContext context = new TestActionContext("/data/tree.xml");
		TreeXmlAction action = new TreeXmlAction(context);
		action.readParameters();
		action.execute();
		assertEquals(context.getContentType(), "text/xml");
		byte[] bytes = context.toByteArray();
		// Test JSON format with an external library
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document document = builder.parse(new ByteArrayInputStream(bytes));
		Set<String> names = new HashSet<>();
		names.add("A");
		names.add("B");
		names.add("C");
		visitElement(document.getDocumentElement(), names);
		assertTrue(names.isEmpty());
	}
}
