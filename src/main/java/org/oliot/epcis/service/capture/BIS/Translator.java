package org.oliot.epcis.service.capture.BIS;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;

public abstract class Translator {
	
	protected DocumentBuilderFactory factory;
	protected DocumentBuilder builder;
	protected Document document;
	
	public Translator() {
		
		factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
	}
	
	//public abstract Object translate(String serviceType, String infoType, String data, String extension);

}