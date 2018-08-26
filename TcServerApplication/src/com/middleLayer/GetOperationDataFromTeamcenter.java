package com.middleLayer;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

import com.teamcenter.soa.exceptions.NotLoadedException;

public class GetOperationDataFromTeamcenter {
	
	private ArrayList<String> temporaryList = new ArrayList<String>();
	private ArrayList<String> amateurList = new ArrayList<String>();
    private ArrayList<String> expertList = new ArrayList<String>();
    private ArrayList<String> mediumList = new ArrayList<String>();
	
	//Parameters come from QR (ARClient)
	//operationId --> currentOperation.get(0).operationId
	//fileName    --> currentOperation.get(0).filename
	//revisionId  --> currentOperation.get(0).revisionId
	public GetOperationDataFromTeamcenter(String operationId, String fileName, String revisionId) throws NotLoadedException 
	{
		GetFileFromTeamcenter getFileFromTeamcenter = new GetFileFromTeamcenter(operationId, fileName, revisionId);
		
	    try 
	    {
	    	int size = Files.readAllLines(getFileFromTeamcenter.getFile().toPath()).size();
	    	for(int i = 0; i < size; i++) 
	    	{
				temporaryList.add(Files.readAllLines(getFileFromTeamcenter.getFile().toPath()).get(i));
	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    //Separate lists based on the users experience
	    for(int i = 0; i<temporaryList.size(); i++) 
	    {
	    	amateurList.add(temporaryList.get(i).substring(2, temporaryList.get(i).length()));
	    	
	    	if(temporaryList.get(i).substring(0, 1).equals("1"))
	    		expertList.add(temporaryList.get(i).substring(2, temporaryList.get(i).length()));
	    	
	    	if(temporaryList.get(i).substring(0, 1).equals("1") || temporaryList.get(i).substring(0, 1).equals("2"))
	    		mediumList.add(temporaryList.get(i).substring(2, temporaryList.get(i).length()));	
	    }
	}
	
	//Test Constructor
	public GetOperationDataFromTeamcenter() throws NotLoadedException 
	{
		File file = new File("C:\\Teamcenter önemli\\lASTIK dEGISTIRMEK.txt");
		
	    try 
	    {
	    	int size = Files.readAllLines(file.toPath()).size();
	    	for(int i = 0; i < size; i++) 
	    	{
	    		temporaryList.add(Files.readAllLines(file.toPath()).get(i));
	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    //Separate lists based on the users experience
	    for(int i = 0; i<temporaryList.size(); i++) 
	    {
	    	amateurList.add(temporaryList.get(i).substring(2, temporaryList.get(i).length()));
	    	
	    	if(temporaryList.get(i).substring(0, 1).equals("1"))
	    		expertList.add(temporaryList.get(i).substring(2, temporaryList.get(i).length()));
	    	
	    	if(temporaryList.get(i).substring(0, 1).equals("1") || temporaryList.get(i).substring(0, 1).equals("2"))
	    		mediumList.add(temporaryList.get(i).substring(2, temporaryList.get(i).length()));	
	    }
	}
	
	public ArrayList<String> getInstructionList()
	{
		return temporaryList;
	    
	}
	
	public ArrayList<String> getAmateurList()
	{
		return amateurList;
	}
	
	public ArrayList<String> getMediumList()
	{
		return mediumList;
	}
	
	public ArrayList<String> getExpertList()
	{
		return expertList;
	}
}
