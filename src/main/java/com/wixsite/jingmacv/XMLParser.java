package com.wixsite.jingmacv;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*
 * This class parses a XML file for data and prints it.
 */
public class XMLParser {

	/*
	 * The only public method in this class. Parses an XML file for data and prints it.
	 */
	public static void parseXML() {
		DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
    	try {
    		// Builds a document object from an XML file.
    		DocumentBuilder docBuilder = docBuildFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("dataFiles/Data.xml"));
	        doc.getDocumentElement().normalize();
	        // Loops over the XML nodes and prints the children.
	        NodeList nodes = doc.getElementsByTagName("DAMSettlementPointPrice");
	        System.out.println("Size: " + nodes.getLength());
	        for (int i = 0; i < nodes.getLength(); i++) {
	        	Element node = (Element) nodes.item(i);
	        	System.out.print(node.getElementsByTagName("DeliveryDate").item(0).getTextContent() + ", ");
	        	System.out.print(node.getElementsByTagName("HourEnding").item(0).getTextContent() + ", ");
	        	System.out.print(node.getElementsByTagName("SettlementPoint").item(0).getTextContent() + ", ");
	        	System.out.print(node.getElementsByTagName("SettlementPointPrice").item(0).getTextContent() + ", ");
	        	System.out.print(node.getElementsByTagName("DSTFlag").item(0).getTextContent() + "\n");
	        }
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Main method.
	 */
	public static void main(String[] args) {
		parseXML();
	}
}
