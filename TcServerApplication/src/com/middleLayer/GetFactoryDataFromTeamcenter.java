package com.middleLayer;

import java.util.ArrayList;

import com.properties.MachineProperties;
import com.teamcenter.clientx.AppXSession;
import com.teamcenter.services.internal.strong.core.DataManagementService;
import com.teamcenter.soa.client.model.strong.Mfg0BvrPlantResource;
import com.teamcenter.soa.exceptions.NotLoadedException;

public class GetFactoryDataFromTeamcenter {

	private ArrayList<MachineProperties> machineIds = new ArrayList<MachineProperties>();
	private String machines;
	private String[] factoryResource, firstMachine, secondMachine, thirdMachine, fourthMachine, fifthMachine, sixthMachine, seventhMachine, eightMachine, ninethMachine, tenthMachine;
	
	//factoryId will come from AR client.
	//This method is for Optimisation algorithm.
	public GetFactoryDataFromTeamcenter(String factoryId) 
	{
		factoryResource = getFactoryInformation(factoryId).split("\\s"); 
		
		//Print Some Debug Information
		for(int i = 0; i < factoryResource.length; i++) 
		{
			System.out.println(factoryResource[i]);
		}
				
		firstMachine = factoryResource[0].split("/");
		secondMachine = factoryResource[1].split("/");
		thirdMachine = factoryResource[2].split("/");
		fourthMachine = factoryResource[3].split("/");
		fifthMachine = factoryResource[4].split("/");
		sixthMachine = factoryResource[5].split("/");
		seventhMachine = factoryResource[6].split("/");
		eightMachine = factoryResource[7].split("/");
		ninethMachine = factoryResource[8].split("/");
		tenthMachine = factoryResource[9].split("/");
		
		machineIds.get(0).setId(firstMachine[0]);
		machineIds.get(1).setId(secondMachine[0]);
		machineIds.get(2).setId(thirdMachine[0]);
		machineIds.get(3).setId(fourthMachine[0]);
		machineIds.get(4).setId(fifthMachine[0]);
		machineIds.get(5).setId(sixthMachine[0]);
		machineIds.get(6).setId(seventhMachine[0]);
		machineIds.get(7).setId(eightMachine[0]);
		machineIds.get(8).setId(ninethMachine[0]);
		machineIds.get(9).setId(tenthMachine[0]);
		
		//Print Some Debug Information		
		for(int i = 0; i < machineIds.size(); i++) 
		{
			System.out.println(machineIds.get(i).getId());
		}
	}
	
	private String getFactoryInformation(String factoryId) 
	{
		//DataManagementService dmService = DataManagementService.getService(AppXSession.getConnection());
		//TODO-simdilik factoryID gelmiyor. Fabrika ID erisilebildiginde bu kodu yeniden duzenle,
		
		Mfg0BvrPlantResource plantResource = new Mfg0BvrPlantResource(null, factoryId); 
		
		try {
			machines = plantResource.get_bl_rev_ps_children();
		} catch (NotLoadedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return machines;
	}
	
	public ArrayList<MachineProperties> getMachineIds() 
	{
		return machineIds;
	}

}
