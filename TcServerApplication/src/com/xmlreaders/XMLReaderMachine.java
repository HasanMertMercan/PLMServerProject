package com.xmlreaders;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.properties.MachineProperties;

public class XMLReaderMachine {
	
private ArrayList<MachineProperties> machineProperties = new ArrayList<MachineProperties>();
	
	public XMLReaderMachine() 
	{
		try {
            File file = new File("C:\\eclipse\\TcServerApplication\\src\\MachineProperty.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
             
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
             
            document.getDocumentElement().normalize();
             
            NodeList nodeList = document.getElementsByTagName("machine");
            
            for (int i = 0; i < nodeList.getLength(); i++) {
            	machineProperties.add(getMachineProperties(nodeList.item(i)));
            }
            
            /*for(int i = 0; i < machineProperties.size(); i++) 
            {
                System.out.println(machineProperties.get(i).getId());
                System.out.println(machineProperties.get(i).getErrorState());
                System.out.println(machineProperties.get(i).getxAxis() +" "+ machineProperties.get(i).getyAxis());
            }*/
 
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
	}
	
	public ArrayList<MachineProperties> getMachinePropertiesList() 
	{
		return machineProperties;
	}
	
	private static MachineProperties getMachineProperties(Node node) {
        //XMLReaderDOM domReader = new XMLReaderDOM();
		MachineProperties mp = new MachineProperties();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            mp.setxAxis(getTagValue("x-axis", element));
            mp.setyAxis(getTagValue("y-axis", element));
            mp.setId(getTagValue("id", element));
            mp.setErrorState(getTagValue("errorstate", element));
            mp.setFileName(getTagValue("filename", element));
            mp.setRevisionId(getTagValue("revisionId", element));
        }

        return mp;
    }
	
	private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
	

}
