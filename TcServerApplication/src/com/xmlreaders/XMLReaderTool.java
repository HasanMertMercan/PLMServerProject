package com.xmlreaders;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.properties.ToolProperties;

public class XMLReaderTool {
	
	private ArrayList<ToolProperties> toolProperties = new ArrayList<ToolProperties>();
	
	public XMLReaderTool() 
	{
		try {
            File file = new File("C:\\eclipse\\TcServerApplication\\src\\OperationProperties.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
             
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
             
            document.getDocumentElement().normalize();
             
            NodeList nodeList = document.getElementsByTagName("operation");
            
            for (int i = 0; i < nodeList.getLength(); i++) {
            	toolProperties.add(getToolProperties(nodeList.item(i)));
            }
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
	}
	
	public ArrayList<ToolProperties> getToolPropertiesList()
	{
		return toolProperties;
	}
	
	private static ToolProperties getToolProperties(Node node) {
        //XMLReaderDOM domReader = new XMLReaderDOM();
		ToolProperties tp = new ToolProperties();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            tp.setToolId(getTagValue("id", element));
            tp.setToolName(getTagValue("name", element));
            tp.set_toolFileName(getTagValue("filename", element));
            tp.set_revisionId(getTagValue("revisionid", element));
        }

        return tp;
    }
	
	private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

}
