/*
 * This class reads the UserProperties.xml file in order to
 * take the user information and return value as a list
 */

package com.xmlreaders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.properties.UserProperties;

public class XMLReaderUser {
	

	private List<UserProperties> userProperties = new ArrayList<UserProperties>();

	public XMLReaderUser() {
		try {
            File file = new File("C:\\eclipse\\TcServerApplication\\src\\UserProperties.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
             
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
             
            document.getDocumentElement().normalize();
             
            NodeList nodeList = document.getElementsByTagName("user");
            
            for (int i = 0; i < nodeList.getLength(); i++) {
                userProperties.add(getUserProperties(nodeList.item(i)));
            }
 
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
	}
	
	
	public List<UserProperties> getUserPropertiesList() 
	{
		return userProperties;
	}
	
	private static UserProperties getUserProperties(Node node) {
        //XMLReaderDOM domReader = new XMLReaderDOM();
		UserProperties up = new UserProperties();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            up.setName(getTagValue("name", element));
            up.setProfession(getTagValue("profession", element));
            up.setId(getTagValue("id", element));
            up.setLevel(getTagValue("level", element));
        }

        return up;
    }
	
	private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

}
