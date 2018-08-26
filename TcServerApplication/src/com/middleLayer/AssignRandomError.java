package com.middleLayer;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.properties.MachineProperties;

public class AssignRandomError {

	private ArrayList<MachineProperties> machinesWithErrorList = new ArrayList<MachineProperties>();
	
	public AssignRandomError(ArrayList<MachineProperties> machineList)
	{
		int size = machineList.size();
		//This code gives random error states to machines
		for(int i = 0; i < size; i++) 
		{
			int randomNum = ThreadLocalRandom.current().nextInt(0, 3);
			
			if(randomNum == 0) 
			{
				machineList.get(i).setErrorState("0");		//Green State, Regular Maintenance
			}
			else if(randomNum == 1) 
			{
				machineList.get(i).setErrorState("1");		//Yellow State, There is a problem but not urgent
			}
			else 
			{
				machineList.get(i).setErrorState("2");		//Red State, Needs to be taken care as soon as possible
			}
		}
		
		machinesWithErrorList = machineList;
		
	}
	
	//Send this list to ARClient and ask user to select yellow errors
	public ArrayList<MachineProperties> getMachineErrorList()
	{
		return machinesWithErrorList;
	}
}
