package com.middleLayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.properties.MachineProperties;
import com.teamcenter.soa.exceptions.NotLoadedException;
import com.xmlreaders.XMLReaderMachine;

public class MachineDetails {
	
	private XMLReaderMachine xmlReaderMachine = new XMLReaderMachine();
	private ArrayList<MachineProperties> NonOptimisedMachines = new ArrayList<MachineProperties>();
	private ArrayList<MachineProperties> currentMachine = new ArrayList<MachineProperties>();

	//Constructor for basic operations, Used for testing purposes 
	public MachineDetails(String machineId) throws IOException 
	{
		NonOptimisedMachines = xmlReaderMachine.getMachinePropertiesList();
		int size = NonOptimisedMachines.size();
		for(int j = 0; j < size; j++) 
		{	
			if(machineId.equals(NonOptimisedMachines.get(j).getId())) 
			{
				currentMachine.add(NonOptimisedMachines.get(j));
				int randomNum = ThreadLocalRandom.current().nextInt(0, 3);
				currentMachine.get(0).setErrorState(Integer.toString(randomNum));
				//System.out.println(currentMachine.get(j).getId());
				
				String fileName = currentMachine.get(0).getFileName();
				String revisionId = currentMachine.get(0).getRevisionId();
				//If there is no Session, use alternative constructor of the GetMachineDataFromTeamcenter
				GetMachineDataFromTeamcenter getMachineDataFromTeamcenter = new GetMachineDataFromTeamcenter(machineId, fileName, revisionId);
				currentMachine.get(0).setMachineCADFile(getMachineDataFromTeamcenter.getMachineData().get(0).getMachineCADFile());
				
				break;
			}
			
		}
	}
	
	//Constructor to get MachineCADData
	//machineId --> ARClient, completedMachineList --> The list which returned from AssignOperation
	public MachineDetails(String machineId, ArrayList<MachineProperties> completedMachineList) throws NotLoadedException, IOException
	{
		int size = completedMachineList.size();
		
		for(int j = 0; j < size; j++) 
		{
			if(completedMachineList.get(j).getId().equals(machineId)) 
			{
				String fileName = completedMachineList.get(j).getFileName();
				String revisionId = completedMachineList.get(j).getRevisionId();
				currentMachine.add(completedMachineList.get(j));
				//If there is no Session, use alternative constructor of the GetMachineDataFromTeamcenter
				GetMachineDataFromTeamcenter getMachineDataFromTeamcenter = new GetMachineDataFromTeamcenter(machineId, fileName, revisionId);
				currentMachine.get(0).setMachineCADFile(getMachineDataFromTeamcenter.getMachineData().get(0).getMachineCADFile());
				
				OperationDetails operationDetails = new OperationDetails(currentMachine.get(0).getOperationToDo());
				currentMachine.get(0).setOperationInstructionList(operationDetails.getCurrentOperation().get(0).get_instructions());
				currentMachine.get(0).setOperationInstructionListId(operationDetails.getCurrentOperation().get(0).getInstructionsListId());
				break;
			}
		}
	}
	
	public ArrayList<MachineProperties> getCurrentMachine() 
	{
		return currentMachine;
	}

}
