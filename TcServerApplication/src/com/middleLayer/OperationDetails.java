package com.middleLayer;

import java.util.ArrayList;

import com.properties.OperationProperties;
import com.properties.UserProperties;
import com.teamcenter.soa.exceptions.NotLoadedException;
import com.xmlreaders.XMLReaderOperation;

public class OperationDetails {
	private ArrayList<UserProperties> currentUser = new ArrayList<UserProperties>();
	
	private UserDetails userDetails;
	private int userLevel;
	private int userProfession;
	private int operationProfession;
	
	private ArrayList<OperationProperties> operationList = new ArrayList<OperationProperties>();
	private XMLReaderOperation xmlReaderOperation = new XMLReaderOperation();
	private ArrayList<OperationProperties> currentOperation = new ArrayList<OperationProperties>();
	
	private ArrayList<String> amateurList = new ArrayList<String>();
    private ArrayList<String> expertList = new ArrayList<String>();
    private ArrayList<String> mediumList = new ArrayList<String>();

	//operationID --> operationId which comes from AR client
	public OperationDetails(String operationId) throws NotLoadedException 
	{
		currentUser = userDetails.getCurrentUser();
		userLevel = Integer.parseInt(currentUser.get(0).getLevel());
		operationList = xmlReaderOperation.getOperationPropertiesList();
		userProfession = Integer.parseInt(userDetails.getCurrentUser().get(0).getProfession());
		int operationSize = xmlReaderOperation.getOperationPropertiesList().size();
		
		
		for(int i = 0; i < operationSize; i++) 
		{
			if(operationList.get(i).getId().equals(operationId)) 	
			{
				String filename = operationList.get(i).getFileName();
				String revisionId = operationList.get(i).getRevisionId();
				//Eger Teamcenter baglantisi yoksa bu instruction dosyasini localden al
				GetOperationDataFromTeamcenter godft = new GetOperationDataFromTeamcenter(operationId, filename, revisionId);
				amateurList = godft.getAmateurList();
				expertList = godft.getExpertList();
				mediumList = godft.getMediumList();
				operationProfession = Integer.parseInt(operationList.get(i).getProfession());

			    //Write the code which determines which list will be returned based on user level!!
				if(userLevel == 3 || operationProfession != userProfession) 
			    {
					operationList.get(i).set_instructions(amateurList);
					operationList.get(i).setInstructionsListId("3");
			    }
			    else if(userLevel == 2) 
			    {
			    	operationList.get(i).set_instructions(mediumList);
					operationList.get(i).setInstructionsListId("2");
			    }
			    else if(userLevel == 1 && operationProfession == userProfession)
			    {
			    	operationList.get(i).set_instructions(expertList);
					operationList.get(i).setInstructionsListId("1");
			    }
			    else if(userLevel == 1 && operationProfession != userProfession) 
			    {
			    	operationList.get(i).set_instructions(amateurList);
					operationList.get(i).setInstructionsListId("3");
			    }
			    else 
			    {
			    	System.out.println("There is no available list to display for this kind of user!");
			    }
				
				currentOperation.add(operationList.get(i));
				break;
			}
		}
		
		
	}
	
	//Cotructor to update operation Instructionss
	public OperationDetails(String operationId, String operationInstructionListId) throws NotLoadedException 
	{
		operationList = xmlReaderOperation.getOperationPropertiesList();
		int operationSize = xmlReaderOperation.getOperationPropertiesList().size();
		
		for(int i = 0; i < operationSize; i++) 
		{
			if(operationList.get(i).getId().equals(operationId)) 	
			{
				String filename = operationList.get(i).getFileName();
				String revisionId = operationList.get(i).getRevisionId();
				//Eger Teamcenter baglantisi yoksa bu instruction dosyasini localden al
				GetOperationDataFromTeamcenter godft = new GetOperationDataFromTeamcenter(operationId, filename, revisionId);
				amateurList = godft.getAmateurList();
				expertList = godft.getExpertList();
				mediumList = godft.getMediumList();
				operationProfession = Integer.parseInt(operationList.get(i).getProfession());
				
				if(operationInstructionListId.equals("2")) 
				{
					operationList.get(i).set_instructions(amateurList);
					operationList.get(i).setInstructionsListId("3");
				}
				else if(operationInstructionListId.equals("1")) 
				{
					operationList.get(i).set_instructions(mediumList);
					operationList.get(i).setInstructionsListId("2");
				}
			    else 
			    {
			    	System.out.println("There is no available list to display for this kind of user!");
			    }
				
				currentOperation.add(operationList.get(i));
				break;
			}
		}
		
		
	}
	
	public ArrayList<OperationProperties> getCurrentOperation() 
	{
		return currentOperation;
	}
	
	//Ne zaman lazim olacak?
	//Operation gosterildigi zaman 
	//Bu ayrim C# tarafinda yapilacak ve gerekli olan toolId ile sorgu yapilacak
	public String[] getCurrentOperationTools() 
	{
		String[] tools = currentOperation.get(0).getToolIds().split("-");
		return tools;
	}

}
