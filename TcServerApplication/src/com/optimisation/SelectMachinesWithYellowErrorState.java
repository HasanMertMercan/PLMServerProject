package com.optimisation;

import java.util.ArrayList;

import com.properties.MachineProperties;

public class SelectMachinesWithYellowErrorState {
	
	private ArrayList<MachineProperties> yellowStateMachines = new ArrayList<MachineProperties>();
	private ArrayList<MachineProperties> greenStateMachines = new ArrayList<MachineProperties>();
	private ArrayList<MachineProperties> redStateMachines = new ArrayList<MachineProperties>();
	
	public SelectMachinesWithYellowErrorState(ArrayList<MachineProperties> machinesWithError) 
	{
		for(int i = 0; i < machinesWithError.size(); i++) 
		{
			if(machinesWithError.get(i).getErrorState().equals("1")) 
			{
				yellowStateMachines.add(machinesWithError.get(i));
			}
			else if(machinesWithError.get(i).getErrorState().equals("0")) 
			{
				greenStateMachines.add(machinesWithError.get(i));
				
			}
			else if(machinesWithError.get(i).getErrorState().equals("2")) 
			{
				redStateMachines.add(machinesWithError.get(i));
			}
		}

		System.out.println("Green");
		for(int i = 0; i < greenStateMachines.size(); i++) 
		{
			System.out.println(greenStateMachines.get(i).getId());
		}
		System.out.println();
		System.out.println("Red");
		for(int i = 0; i < redStateMachines.size(); i++) 
		{
			System.out.println(redStateMachines.get(i).getId());
		}
		
	}
	
	public ArrayList<MachineProperties> getYellowStateMachines()
	{
		return yellowStateMachines;
	}

	public ArrayList<MachineProperties> getGreenStateMachines()
	{
		return greenStateMachines;
	}
	
	public ArrayList<MachineProperties> getRedStateMachines()
	{
		return redStateMachines;
	}
}
