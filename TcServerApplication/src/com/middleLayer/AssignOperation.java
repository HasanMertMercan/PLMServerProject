package com.middleLayer;

import java.util.ArrayList;

import com.properties.MachineProperties;
import com.properties.OperationProperties;
import com.xmlreaders.XMLReaderOperation;

public class AssignOperation {
	
	private ArrayList<OperationProperties> temporaryOperationList = new ArrayList<OperationProperties>();
	private XMLReaderOperation xmlReaderOperation = new XMLReaderOperation();
	private ArrayList<MachineProperties> completedMachineList = new ArrayList<MachineProperties>();
	
	public AssignOperation(ArrayList<MachineProperties> machinesWithErrorList) 
	{
		temporaryOperationList = xmlReaderOperation.getOperationPropertiesList();
		int size = machinesWithErrorList.size();
		int operationSize = temporaryOperationList.size();
		
		//Assign Operations to Every Machine based on their ErrorState (Every error cannot occur on every error state)
		for(int i = 0; i < size; i++) 
		{
			for(int j = 0; j < operationSize; j++) 
			{
				if(temporaryOperationList.get(j).get_state().equals(machinesWithErrorList.get(i).getErrorState()))
				{
					machinesWithErrorList.get(i).setOperationToDo(temporaryOperationList.get(j).getName());
				}
			}
		}
		
		completedMachineList = machinesWithErrorList;
		
	}
	
	public ArrayList<MachineProperties> getCompletedMachineList()
	{
		return completedMachineList;
	}

}
