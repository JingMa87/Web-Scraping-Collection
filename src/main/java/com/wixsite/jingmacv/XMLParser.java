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

public class XMLParser {

	public static void parseXML() {
		// Builds a document object from an xml file.
		DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
    	try {
    		DocumentBuilder docBuilder = docBuildFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("dataFiles/Data.xml"));
	        doc.getDocumentElement().normalize();
	        // Loops over the xml nodes and prints the children.
	        NodeList DAMSettlementPointPrices = doc.getElementsByTagName("DAMSettlementPointPrice");
	        System.out.println("Size: " + DAMSettlementPointPrices.getLength());
	        for (int i = 0; i < DAMSettlementPointPrices.getLength(); i++) {
	        	Element DAMSettlementPointPrice = (Element) DAMSettlementPointPrices.item(i);
	        	System.out.print(DAMSettlementPointPrice.getElementsByTagName("DeliveryDate").item(0).getTextContent() + ", ");
	        	System.out.print(DAMSettlementPointPrice.getElementsByTagName("HourEnding").item(0).getTextContent() + ", ");
	        	System.out.print(DAMSettlementPointPrice.getElementsByTagName("SettlementPoint").item(0).getTextContent() + ", ");
	        	System.out.print(DAMSettlementPointPrice.getElementsByTagName("SettlementPointPrice").item(0).getTextContent() + ", ");
	        	System.out.print(DAMSettlementPointPrice.getElementsByTagName("DSTFlag").item(0).getTextContent() + "\n");
	        }
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		parseXML();
	}
}
