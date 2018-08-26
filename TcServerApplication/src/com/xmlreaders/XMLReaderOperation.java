package com.xmlreaders;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.properties.OperationProperties;

public class XMLReaderOperation {
	
	private ArrayList<OperationProperties> operationProperties = new ArrayList<OperationProperties>();
	
	public XMLReaderOperation() 
	{
		try {
            File file = new File("C:\\eclipse\\TcServerApplication\\src\\OperationProperties.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
             
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
             
            document.getDocumentElement().normalize();
             
            NodeList nodeList = document.getElementsByTagName("operation");
            
            for (int i = 0; i < nodeList.getLength(); i++) {
            	operationProperties.add(getOperationProperties(nodeList.item(i)));
            }
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
	}
	
	public ArrayList<OperationProperties> getOperationPropertiesList() 
	{
		return operationProperties;
	}
	
	private static OperationProperties getOperationProperties(Node node) {
        //XMLReaderDOM domReader = new XMLReaderDOM();
		OperationProperties op = new OperationProperties();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            op.setName(getTagValue("name", element));
            op.setProfession(getTagValue("profession", element));
            op.setId(getTagValue("id", element));
            op.setToolIds(getTagValue("associatedtools", element));
            op.set_state(getTagValue("state", element));
        }

        return op;
    }
	
	private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }


}
